/*
 * Copyright (C) 2015. The BoCool Project.
 *
 *          yinglovezhuzhu@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiaoying.bocool.ui;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.baidu.speechsynthesizer.publicutility.SpeechPlayer;
import com.baidu.speechsynthesizer.publicutility.SpeechPlayerListener;
import com.opensource.bitmaploader.ImageCache;
import com.opensource.bitmaploader.ImageFetcher;
import com.xiaoying.bocool.R;
import com.xiaoying.bocool.bean.Issue;
import com.xiaoying.bocool.bean.Storage;
import com.xiaoying.bocool.config.Config;
import com.xiaoying.bocool.config.SharedPrefConfig;
import com.xiaoying.bocool.data.SharedPrefHelper;
import com.xiaoying.bocool.tts.BDTtsManager;
import com.xiaoying.bocool.tts.TtsCache;
import com.xiaoying.bocool.tts.TtsWorker;
import com.xiaoying.bocool.ui.base.BaseActivity;
import com.xiaoying.bocool.ui.fragment.IssueFragment;
import com.xiaoying.bocool.utils.CacheManager;
import com.xiaoying.bocool.utils.FileUtil;
import com.xiaoying.bocool.utils.LogUtils;
import com.xiaoying.bocool.utils.StringUtils;
import com.xiaoying.bocool.voice.VoiceCache;
import com.xiaoying.bocool.voice.VoiceFetcher;
import com.xiaoying.bocool.widget.LoadingPageView;
import com.xiaoying.bocool.widget.Titlebar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

/**
 * 专辑详情页面
 * Create by yinglovezhuzhu@gmail.com in 2015年04月23日
 */
public class IssueActivity extends BaseActivity {

    private static final int AUTO_PLAY_DELAY = 500;

    private Titlebar mTitlebar;
	private ViewPager mViewPager;
	private View mFlowContent;
	private ImageButton mIbtnVoice;
	private View mExtraContent;
	private TextView mTvDetailText;
	private ImageButton mIbtnExtraToggle;
	private TextView mTvExtraText;
    private LoadingPageView mLoadingPage;
	
	private IssuePagerAdapter mAdapter;
	
	private ImageFetcher mImageFetcher;
	private ImageCache mImageCache;

	private VoiceFetcher mVoiceFetcher;
	private VoiceCache mVoiceCache;

    private TtsWorker mTtsWorker;
    private TtsCache mTtsCache;
	
	private Storage mStorage;

    private boolean mCanceled = false;

	private SoundPool mSoundPool;

	private Map<String, Integer> mSampleIds = new HashMap<String, Integer>();
    private Map<String, File> mVoiceFiles = new HashMap<String, File>();
    private Map<Integer, Boolean> mImageState = new HashMap<Integer, Boolean>();

	private boolean mAutoPlayVoice = false;
    private boolean mPaused = false; // 页面时Pause状态

    private byte [] mAudioData = null;

    private BDTtsManager mBDTtsManager;

    private SimpleSpeechSynthesizerListener mSpeechSynthesizerListener = new SimpleSpeechSynthesizerListener();

    private IssueHandler mHandler = new IssueHandler();

    private boolean mAutoPlaying = false;
    private boolean mAutoPlayRecycle = false;
    private int mAutoPlayDelay = UserSettingActivity.AUTO_PLAY_DELAY_DEFAULT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_issue);
		
		if(!initData()) {
			showShortToast(R.string.app_error);
			finishRight();
			return;
		}
		
		initImageWorker();

        initVoiceFetcher();

        initTtsWorker();
		
		initView();
		
		mAutoPlayVoice = SharedPrefHelper.getInstance(this).getBoolean(SharedPrefConfig.KEY_AUTO_PLAY_VOICE, false);
        mAutoPlayDelay = SharedPrefHelper.getInstance(this).
                getInt(SharedPrefConfig.KEY_AUTO_PLAY_DELAY, UserSettingActivity.AUTO_PLAY_DELAY_DEFAULT);
        mAutoPlayRecycle = SharedPrefHelper.getInstance(this).getBoolean(SharedPrefConfig.KEY_AUTO_PLAY_RECYCLE, true);
		
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mSoundPool.setOnLoadCompleteListener(mLoadCompleteListener);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mBDTtsManager = BDTtsManager.getInstance();
        mBDTtsManager.initBDTts(getApplicationContext(), false);
        mBDTtsManager.setSpeechSynthesizerListener(mSpeechSynthesizerListener);
        mBDTtsManager.setSpeechPlayerListener(mSpeechPlayerListener);

        queryIsses(mStorage);
	}

    /**
     * 本地音频文件加载完成监听
     */
    private OnLoadCompleteListener mLoadCompleteListener = new OnLoadCompleteListener() {

        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            if (0 == status) { // 加载成功
                Issue issue = (Issue) mIbtnVoice.getTag();
                if (null == issue) {
                    return;
                }
                BmobFile voiceFile = issue.getVoice();
                if (null == voiceFile) {
                    return;
                }
                String voiceUrl = voiceFile.getFileUrl(IssueActivity.this);
                mSampleIds.put(voiceUrl, sampleId);
                int currentItem = mViewPager.getCurrentItem();
                if (mAutoPlayVoice && mImageState.containsKey(currentItem) && mImageState.get(currentItem)) {
                    // 自动播放&&语音数据load完成&&图片加载完成并显示
                    mHandler.sendEmptyMessageDelayed(IssueHandler.MSG_PLAY_VOICE, AUTO_PLAY_DELAY);
                    mIbtnVoice.setEnabled(false);
                } else {
                    mIbtnVoice.setEnabled(true);
                }
            } else {
                mIbtnVoice.setEnabled(false);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(mPaused && View.VISIBLE == mIbtnVoice.getVisibility()) {
            Issue issue = (Issue) mIbtnVoice.getTag();
            if(null == issue) {
                return;
            }
            if(StringUtils.isEmpty(issue.getVoiceText())) {
                BmobFile voiceFile = issue.getVoice();
                if(null == voiceFile) {
                    return;
                }
                String voiceUrl = voiceFile.getFileUrl(IssueActivity.this);
                if(null != voiceUrl && mSampleIds.containsKey(voiceUrl)) {
                    mIbtnVoice.setEnabled(true);
                }
            } else {
                if(null != mAudioData) {
                    mIbtnVoice.setEnabled(true);
                }
            }
        }
        mPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAutoPlaying) {
            pauseAutoPlay(); // 停止自动播放
        }
        mPaused = true;
        mHandler.removeMessages(IssueHandler.MSG_PLAY_VOICE);
        mHandler.removeMessages(IssueHandler.MSG_PLAY_AUDIO_DATA);
        if(null != mBDTtsManager) {
            mBDTtsManager.onPause();
        }
        if(null != mSoundPool) {
            Issue issue = (Issue) mIbtnVoice.getTag();
            if(null == issue) {
                return;
            }
            BmobFile voiceFile = issue.getVoice();
            if(null == voiceFile) {
                return;
            }
            String voiceUrl = voiceFile.getFileUrl(IssueActivity.this);
            if(null != voiceUrl && mSampleIds.containsKey(voiceUrl)) {
                int sampleId = mSampleIds.get(voiceUrl);
                mSoundPool.stop(sampleId);
            }
        }
    }

    @Override
	public void onDestroy() {
        if(null != mBDTtsManager) {
            mBDTtsManager.onDestroy();
        }
        if(null != mSpeechSynthesizerListener) {
            mSpeechSynthesizerListener.closeStream();
        }
		if(null != mSoundPool) {
			mSoundPool.release();
		}
		if(null != mImageFetcher) {
			mImageFetcher.setExitTasksEarly(true);
		}
        if(null != mVoiceFetcher) {
            mVoiceFetcher.setExitTasksEarly(true);
        }
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finishRight();
	}
	
	public ImageFetcher getImageFetcher() {
		return mImageFetcher;
	}

    public VoiceFetcher getVoiceFetcher() {
        return mVoiceFetcher;
    }

	public boolean isVoiceLoaded(String key) {
		return mSampleIds.containsKey(key);
	}

    public void onVoiceLoading(String url, int position) {
        if(position == mViewPager.getCurrentItem()) {
            mIbtnVoice.setEnabled(false);
        }
    }

	/**
	 * 声音加载完成，将它load到SoundPool中
     * @param position 下标位置
     * @param url 音频的下载地址
	 * @param file
	 */
	public void onVoiceDownloaded(int position, String url, File file) {
        mVoiceFiles.put(url, file);
        if(position == mViewPager.getCurrentItem()) {
            mSoundPool.load(file.getPath(), 1);
        }
	}

    public void onImageLoaded(int position, boolean loaded) {
        mImageState.put(position, loaded);
        if(loaded && position == mViewPager.getCurrentItem() && mAutoPlayVoice) {
            Issue issue = mAdapter.getItemData(position);
            BmobFile file = issue.getVoice();
            // 如果自动播放，图片加载成功将会自动播放音频
            if(null != file && mSampleIds.containsKey(file.getFileUrl(this))) {
                // 声音文件的情况下，要声音load完毕才自动播放，否则不播放
                mHandler.sendEmptyMessageDelayed(IssueHandler.MSG_PLAY_VOICE, AUTO_PLAY_DELAY);
            } else if(!StringUtils.isEmpty(issue.getVoiceText()) && null != mAudioData) {
                // 语音合成的情况下，要声音数据不为空才播放，否则不播放
                mHandler.sendEmptyMessageDelayed(IssueHandler.MSG_PLAY_AUDIO_DATA, AUTO_PLAY_DELAY);
            }
        }
    }

	private void initView() {
		initTitlebar();
		
		mViewPager = (ViewPager) findViewById(R.id.vp_detail);
		mFlowContent = findViewById(R.id.ll_issue_detail_flow_content);
		mIbtnVoice = (ImageButton) findViewById(R.id.ibtn_issue_detail_voice);
		mExtraContent = findViewById(R.id.ll_issue_detail_extra_content);
		mTvDetailText = (TextView) findViewById(R.id.tv_issue_detail_text);
		mIbtnExtraToggle = (ImageButton) findViewById(R.id.ibtn_issue_detail_open_extra);
		mTvExtraText = (TextView) findViewById(R.id.tv_issue_detail_extra);
        mLoadingPage = (LoadingPageView) findViewById(R.id.loading_issue);

        mTvExtraText.setVisibility(View.GONE);
		mIbtnVoice.setOnClickListener(this);
		mIbtnExtraToggle.setOnClickListener(this);
		
		mAdapter = new IssuePagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(mOnPageChangeListener);
	}

    /**
     * ViewPage页面切换监听
     */
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            mHandler.removeMessages(IssueHandler.MSG_PLAY_VOICE);
            mHandler.removeMessages(IssueHandler.MSG_PLAY_AUDIO_DATA);
            if(null != mAudioData) {
                mBDTtsManager.stopPlayer(); //停止播放器播放
                mAudioData = null; // 将语音数据清空
            }
            Issue issue = mAdapter.getItemData(position);
            changeViewByCardData(issue);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
	
	private void initTitlebar() {
        mTitlebar = (Titlebar) findViewById(R.id.tb_issue_detail);
        mTitlebar.setLeftButton1(R.drawable.ic_title_back, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finishRight();
            }
        });
        mTitlebar.setRightButton1(R.drawable.ic_title_play, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItemPosition = mViewPager.getCurrentItem();
                if(currentItemPosition == mAdapter.getCount() - 1 && !mAutoPlayRecycle) {
                    mViewPager.setCurrentItem(0, false);
                }
                autoPlay();
            }
        });
        mTitlebar.setTitle(mStorage.getTitle());
	}
	
	private boolean initData() {
		Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(null == extras) {
            LogUtils.e(tag, "Extras is null");
            return false;
        }
		if(extras.containsKey(Config.EXTRA_DATA)) {
            mStorage = (Storage) extras.getSerializable(Config.EXTRA_DATA);
			if(null == mStorage) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * 初始化图片加载工具
	 */
	private void initImageWorker() {
		if(null == mImageFetcher) {
			DisplayMetrics dm = getResources().getDisplayMetrics();
			mImageFetcher = new ImageFetcher(this, dm.widthPixels, dm.heightPixels);
			mImageFetcher.setLoadingImage(R.color.transparent);
			mImageFetcher.setLoadFailedImage(R.color.transparent);
		}
		if(null == mImageCache) {
			mImageCache = CacheManager.getInstance().getImageCache(this,
                    FileUtil.createDirs(this, Config.APP_FOLDER, Config.CACHE_FOLDER), Config.IMAGE_FOLDER, true);
		}
		mImageFetcher.setImageCache(mImageCache);
	}

	/**
	 * 初始化音频加载工具
	 */
	private void initVoiceFetcher() {
		if(null == mVoiceFetcher) {
            mVoiceFetcher = new VoiceFetcher(this);
		}
		if(null == mVoiceCache) {
            mVoiceCache = CacheManager.getInstance().getVoiceCache(this,
                    FileUtil.createDirs(this, Config.APP_FOLDER, Config.CACHE_FOLDER), Config.VOICE_FOLDER);
		}
		mVoiceFetcher.setVoiceCache(mVoiceCache);
	}

	/**
	 * 初始化音频合成数据（缓存）加载工具
	 */
	private void initTtsWorker() {
		if(null == mTtsWorker) {
            mTtsWorker = new TtsWorker(this);
		}
		if(null == mTtsCache) {
            mTtsCache = CacheManager.getInstance().getTtsCache(this,
                    FileUtil.createDirs(this, Config.APP_FOLDER, Config.CACHE_FOLDER), Config.TTS_FOLDER);
        }
        mTtsWorker.setTtsCache(mTtsCache);
	}

    /**
     * 语音合成播放器的播放监听
     */
    private SpeechPlayerListener mSpeechPlayerListener = new SpeechPlayerListener() {
        @Override
        public void onFinished(SpeechPlayer speechPlayer) {
//            mIbtnVoice.setEnabled(true);
            mHandler.sendEmptyMessage(IssueHandler.MSG_VOICE_FINISH);
        }

        @Override
        public void onError(SpeechPlayer speechPlayer, SpeechError speechError) {
            if(null != speechError) {
                switch (speechError.code) {
                    case SpeechSynthesizer.SPEECH_PLAYER_ERROR_PLAYER_DIED:
                    case SpeechSynthesizer.SPEECH_PLAYER_ERROR_RELEASED:
                        mBDTtsManager.initSpeechPlayer(IssueActivity.this);
                        if(null != mAudioData) {
                            mBDTtsManager.playAudioData(mAudioData);
                        }
                        break;
                    default:
                        break;
                }
            }
            mIbtnVoice.setEnabled(true);
        }
    };
	
	/**
	 * 根据card数据来改变视图
	 * @param issue
	 */
	private void changeViewByCardData(Issue issue) {
		if(null == issue) {
			mFlowContent.setVisibility(View.GONE);
			return;
		}
        mIbtnVoice.setTag(issue);
		mFlowContent.setVisibility(View.VISIBLE);
        BmobFile voiceFile = issue.getVoice();
        String voiceUrl;
        int currentItem = mViewPager.getCurrentItem();
		if(null != voiceFile && !StringUtils.isEmpty((voiceUrl = voiceFile.getFileUrl(this)))) {
			mIbtnVoice.setVisibility(View.VISIBLE);
            // 下载音频文件播放
            if(mSampleIds.containsKey(voiceUrl)) {
				// 音频文件已经加载
				if(mAutoPlayVoice && mImageState.containsKey(currentItem) && mImageState.get(currentItem)) {
                    // 自动播放&&声音文件load完毕&&图片加载完成并显示
                    mHandler.sendEmptyMessageDelayed(IssueHandler.MSG_PLAY_VOICE, AUTO_PLAY_DELAY);
				    mIbtnVoice.setEnabled(false);
				} else {
				    mIbtnVoice.setEnabled(true);
                }
            } else {
                // 音频文件未加载，加载音频文件
                mIbtnVoice.setEnabled(false);
                // 如果音频文件已经下载
                if(mVoiceFiles.containsKey(voiceUrl)) {
                    File file = mVoiceFiles.get(voiceUrl);
                    if(null == file || !file.exists()) {
                        return;
                    }
                    mSoundPool.load(file.getPath(), 1);
                }
                // 音频文件下载在Fragment中实现了，等到下载完成后会通知到Activity，做相应的操作，请看onVoiceLoaded()
			}
		} else if(!StringUtils.isEmpty(issue.getVoiceText())){
            mBDTtsManager.cancelSpeechSynthesizer(); // 停止语音合成和播放
			mIbtnVoice.setVisibility(View.VISIBLE);
            mAudioData = mTtsWorker.loadCacheData(issue.getVoiceText());
            if(null == mAudioData) {
                mIbtnVoice.setEnabled(false);
                mBDTtsManager.synthesize(issue.getVoiceText());
            } else {
                if(mAutoPlayVoice && mImageState.containsKey(currentItem) && mImageState.get(currentItem)) {
                    // 自动播放&&合成数据不为空&&图片加载完成并显示
                    mIbtnVoice.setEnabled(false);
                    mHandler.sendEmptyMessageDelayed(IssueHandler.MSG_PLAY_AUDIO_DATA, AUTO_PLAY_DELAY);
                } else {
                    mIbtnVoice.setEnabled(true);
                }
            }
		} else {
            mIbtnVoice.setVisibility(View.INVISIBLE);
            // 没有声音，直接当作声音播放完毕的情况处理
            mHandler.sendEmptyMessage(IssueHandler.MSG_VOICE_FINISH);
        }
//        mIbtnVoice.setTag(issue);

		if(StringUtils.isEmpty(issue.getContent()) && StringUtils.isEmpty(issue.getExtra())) {
            mExtraContent.setVisibility(View.GONE);
		} else {
            mExtraContent.setVisibility(View.VISIBLE);
            mTvDetailText.setText(issue.getContent());
            mTvExtraText.setVisibility(View.GONE);
            mIbtnExtraToggle.setImageResource(R.drawable.ic_arrow_up_circle);
            mTvExtraText.setText(issue.getExtra());
			if(StringUtils.isEmpty(issue.getExtra())) {
                mIbtnExtraToggle.setVisibility(View.GONE);
			} else {
                mIbtnExtraToggle.setVisibility(View.VISIBLE);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ibtn_issue_detail_voice:
            Issue issue = (Issue) v.getTag();
            if(null == issue) {
                return;
            }
            BmobFile voiceFile = issue.getVoice();
            if(null != voiceFile) {
                if(mAutoPlaying) {
                    // 如果正在自动播放，那么取消掉下一页的请求，等待播放完毕后继续下一页
                    mHandler.removeMessages(IssueHandler.MSG_PLAY_NEXT);
                }
                // 播放下载的音频文件
                String voiceUrl = voiceFile.getFileUrl(this);
                if(mSampleIds.containsKey(voiceUrl)) {
                    int sampleId = mSampleIds.get(voiceUrl);
                    mSoundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
                    mIbtnVoice.setEnabled(false);
                    mHandler.sendEmptyMessageDelayed(IssueHandler.MSG_VOICE_FINISH, 1000 * issue.getVoiceTime());
                }
            } else if(!StringUtils.isEmpty(issue.getVoiceText())) {
                if(mAutoPlaying) {
                    // 如果正在自动播放，那么取消掉下一页的请求，等待播放完毕后继续下一页
                    mHandler.removeMessages(IssueHandler.MSG_PLAY_NEXT);
                }
                if(null == mAudioData) {
//                    mBDTtsManager.speak(issue.getVoiceText());
                    mBDTtsManager.synthesize(issue.getVoiceText());
                } else {
                    mBDTtsManager.playAudioData(mAudioData);
                }
                mIbtnVoice.setEnabled(false);
            }
			break;
		case R.id.ibtn_issue_detail_open_extra:
			if(View.VISIBLE == mTvExtraText.getVisibility()) {
				mTvExtraText.setVisibility(View.GONE);
				mIbtnExtraToggle.setImageResource(R.drawable.ic_arrow_up_circle);
			} else {
				mTvExtraText.setVisibility(View.VISIBLE);
				mIbtnExtraToggle.setImageResource(R.drawable.ic_arrow_down_circle);
			}
			break;

		default:
			break;
		}
	}


    private void queryIsses(Storage storage) {
        BmobQuery<Issue> query = new BmobQuery<Issue>();
        query.addWhereEqualTo("storage", storage);
        query.addWhereEqualTo("status", Storage.STATUS_ONLINE);
        query.setLimit(Config.MAX_DATA_COUNT);
        query.order("sort");
        mLoadingPage.loading(R.string.loading_data);
        query.findObjects(this, new FindListener<Issue>() {
            @Override
            public void onSuccess(List<Issue> list) {
                if (mCanceled) {
                    mLoadingPage.dismiss();
                    return;
                }

                if (null == list || list.isEmpty()) {
                    mLoadingPage.onError("没有数据", "点击刷新",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    queryIsses(mStorage);
                                }
                            });
                    return;
                }
                mAdapter.addAll(list);
                int position = mViewPager.getCurrentItem();
                if (position >= 0) {
                    Issue issue = mAdapter.getItemData(position);
                    changeViewByCardData(issue);
                }
                mLoadingPage.dismiss();
            }

            @Override
            public void onError(int code, String msg) {

                mLoadingPage.onError(R.string.net_error, R.string.click_to_reload,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                queryIsses(mStorage);
                            }
                        });
            }
        });
    }

    /**
     * 开始自动播放
     */
    private void autoPlay() {

        mTitlebar.setRightButton1(R.drawable.ic_title_pause, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseAutoPlay();
            }
        });
        mAutoPlayVoice = true;
        mAutoPlaying = true;
        changeViewByCardData(mAdapter.getItemData(mViewPager.getCurrentItem()));
        mViewPager.setKeepScreenOn(true);
    }

    /**
     * 停止自动播放
     */
    private void pauseAutoPlay() {
        mTitlebar.setRightButton1(R.drawable.ic_title_play, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItemPosition = mViewPager.getCurrentItem();
                if(currentItemPosition == mAdapter.getCount() - 1 && !mAutoPlayRecycle) {
                    mViewPager.setCurrentItem(0, false);
                }
                autoPlay();
            }
        });
        // 恢复原来的声音播放设置
        mAutoPlayVoice = SharedPrefHelper.getInstance(this).getBoolean(SharedPrefConfig.KEY_AUTO_PLAY_VOICE, false);
        mAutoPlaying = false;
        mHandler.removeMessages(IssueHandler.MSG_VOICE_FINISH);
        mHandler.removeMessages(IssueHandler.MSG_PLAY_NEXT);
        mViewPager.setKeepScreenOn(false);
        mSoundPool.autoPause();
        mBDTtsManager.stopPlayer();
        mHandler.sendEmptyMessage(IssueHandler.MSG_VOICE_FINISH);
    }

    private class IssueHandler extends Handler {

        public static final int MSG_PLAY_VOICE = 0x00000001;

        public static final int MSG_PLAY_AUDIO_DATA = 0x00000002;

        public static final int MSG_PLAY_NEXT = 0x00000003; // 播放下一个图片

        public static final int MSG_VOICE_FINISH = 0x00000004; // 声音播放完毕了

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PLAY_VOICE:
                    Issue issue = (Issue) mIbtnVoice.getTag();
                    if(null == issue) {
                        return;
                    }
                    BmobFile file = issue.getVoice();
                    if(null == file) {
                        return;
                    }
                    String voiceUrl = file.getFileUrl(IssueActivity.this);
                    int sampleId = mSampleIds.get(voiceUrl);
                    mSoundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
                    mHandler.sendEmptyMessageDelayed(MSG_VOICE_FINISH, 1000 * issue.getVoiceTime());
                    break;
                case MSG_PLAY_AUDIO_DATA:
                    mBDTtsManager.playAudioData(mAudioData);
                    break;
                case MSG_VOICE_FINISH:
                    mIbtnVoice.setEnabled(true);
                    if(mAutoPlaying) {
                        mHandler.sendEmptyMessageDelayed(MSG_PLAY_NEXT, mAutoPlayDelay);
                    }
                    break;
                case MSG_PLAY_NEXT:
                    int currentItemPosition = mViewPager.getCurrentItem();
                    if(currentItemPosition == mAdapter.getCount() - 1) {
                        if(mAutoPlayRecycle) {
                            mViewPager.setCurrentItem(0);
                        } else {
                            pauseAutoPlay();
                        }
                        return;
                    }
                    mViewPager.setCurrentItem(currentItemPosition + 1, true);
                    break;
                default:
                    break;
            }
        }
    }
	
	/**
	 * ViewPager的适配器
	 *
	 * @author zyy_owen@ivg.com
	 */
	public static class IssuePagerAdapter extends FragmentStatePagerAdapter {
		
		private List<Issue> mmDatas = new ArrayList<Issue>();
		
		public IssuePagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		public Issue getItemData(int position) {
			return mmDatas.get(position);
		}
		
		public void addAll(Collection<Issue> datas) {
			if(null == datas || datas.isEmpty()) {
				return;
			}
			mmDatas.addAll(datas);
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int position) {
			return IssueFragment.newInstance(mmDatas.get(position), position);
		}

		@Override
		public int getCount() {
			return mmDatas.size();
		}
		
	}

    /**
     * 语音很成监听
     */
    private class SimpleSpeechSynthesizerListener implements SpeechSynthesizerListener {

        private final ByteArrayOutputStream mmByteArrayOutStream = new ByteArrayOutputStream(1);

        public void closeStream() {
            try {
                mmByteArrayOutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStartWorking(SpeechSynthesizer speechSynthesizer) {
            mIbtnVoice.setEnabled(false);
        }

        @Override
        public void onSpeechStart(SpeechSynthesizer speechSynthesizer) {
            mIbtnVoice.setEnabled(false);
        }

        @Override
        public void onNewDataArrive(SpeechSynthesizer speechSynthesizer,
                                    byte[] audioData, boolean isLastData) {
            try {
                // 接收到数据后写到字节流中，直到收取到最后的字节数据
                mmByteArrayOutStream.write(audioData);
                if(isLastData) {
                    // 收到的是最后的字节数据
                    Issue issue = (Issue) mIbtnVoice.getTag();
                    String voiceText = mBDTtsManager.getCurrentVoiceText();
                    mmByteArrayOutStream.flush();
                    mAudioData = mmByteArrayOutStream.toByteArray();
                    if(null != issue && !StringUtils.isEmpty(voiceText) && mBDTtsManager.isSynthesizerWorking()) {
                        // 将字节数据保存到文件缓存中
                        mTtsWorker.saveDataToCache(voiceText, mAudioData);
                    }
//                        mmByteArrayOutStream.reset();
                    int currentItem = mViewPager.getCurrentItem();
                    if(mAutoPlayVoice && mImageState.containsKey(currentItem) && mImageState.get(currentItem)) {
                        // 自动播放&&合成数据不为空&&图片加载完成并显示
                        mHandler.sendEmptyMessageDelayed(IssueHandler.MSG_PLAY_AUDIO_DATA, AUTO_PLAY_DELAY);
                        mIbtnVoice.setEnabled(false);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(isLastData) {
                    mmByteArrayOutStream.reset();
                }
            }
        }

        @Override
        public void onBufferProgressChanged(SpeechSynthesizer speechSynthesizer, int i) {

        }

        @Override
        public void onSpeechProgressChanged(SpeechSynthesizer speechSynthesizer, int i) {

        }

        @Override
        public void onSpeechPause(SpeechSynthesizer speechSynthesizer) {
        }

        @Override
        public void onSpeechResume(SpeechSynthesizer speechSynthesizer) {

        }

        @Override
        public void onCancel(SpeechSynthesizer speechSynthesizer) {
            mIbtnVoice.setEnabled(!mAutoPlayVoice);
            mmByteArrayOutStream.reset();
        }

        @Override
        public void onSynthesizeFinish(SpeechSynthesizer speechSynthesizer) {
            int currentItem = mViewPager.getCurrentItem();
            if(mAutoPlayVoice && null != mAudioData && mImageState.containsKey(currentItem) && mImageState.get(currentItem)) {
                // 自动播放&&合成数据不为空&&图片加载完成并显示
                mIbtnVoice.setEnabled(false);
                mHandler.sendEmptyMessageDelayed(IssueHandler.MSG_PLAY_AUDIO_DATA, AUTO_PLAY_DELAY);
            } else {
                mIbtnVoice.setEnabled(!mAutoPlayVoice);
            }
        }

        @Override
        public void onSpeechFinish(SpeechSynthesizer speechSynthesizer) {
            mIbtnVoice.setEnabled(!mAutoPlayVoice);
        }

        @Override
        public void onError(SpeechSynthesizer speechSynthesizer, SpeechError speechError) {
            if(null != speechError) {
                switch (speechError.code) {
                    case SpeechSynthesizer.SYNTHESIZER_ENGINE_NOT_INITIALIZED:
                    case SpeechSynthesizer.SYNTHESIZER_ERROR_UNINITIALIZED_ERROR:
                        mBDTtsManager.initBDTts(IssueActivity.this, true);
                        mBDTtsManager.reSynthesize();
                    break;
                    default:
                        break;
                }
            }
            mIbtnVoice.setEnabled(true);
        }
    }
}
