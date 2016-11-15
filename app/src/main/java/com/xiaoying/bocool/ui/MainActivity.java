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

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.xiaoying.bocool.R;
import com.xiaoying.bocool.adapter.MenuAdapter;
import com.xiaoying.bocool.config.Config;
import com.xiaoying.bocool.config.SharedPrefConfig;
import com.xiaoying.bocool.data.SharedPrefHelper;
import com.xiaoying.bocool.observer.JPushObserver;
import com.xiaoying.bocool.observer.JPushObserverManager;
import com.xiaoying.bocool.tts.BDTtsManager;
import com.xiaoying.bocool.ui.fragment.SettingFragment;
import com.xiaoying.bocool.ui.fragment.StorageFragment;
import com.xiaoying.bocool.utils.BoCoolManager;
import com.xiaoying.bocool.utils.FragmentUtil;
import com.xiaoying.bocool.utils.NetworkUtils;
import com.xiaoying.bocool.utils.StringUtils;
import com.xiaoying.bocool.utils.Utils;
import com.xiaoying.bocool.widget.Titlebar;
import com.xiaoying.bocool.widget.dialog.CustomDialog;
import com.xiaoying.bocool.widget.slidingmenu.SlidingMenu;
import com.xiaoying.bocool.widget.slidingmenu.base.BaseSlidingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * 首页
 * 
 * Create by yinglovezhuzhu@gmail.com in 2014年12月5日
 */
public class MainActivity extends BaseSlidingActivity {
	
	private SlidingMenu mSlidingMenu;
	
	private Titlebar mTitlebar;
	
	private ListView mLvMenu;
	
	private MenuAdapter mMenuAdapter;
	
	private FragmentUtil mFragmentUtil;
	
	private SharedPrefHelper mSharedPrefHelper;

    private boolean mRealodStorage = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initSlidingMenu();
		
		setContentView(R.layout.activity_main);

		initView();
		
		mFragmentUtil = FragmentUtil.newInstance(this, R.id.fl_main_body);
		
		// 默认打开知识库
		mLvMenu.setItemChecked(0, true);
		openStorage();
		
		mSharedPrefHelper = SharedPrefHelper.getInstance(this);
		
		initUmengUpdate();

        JPushObserverManager.getInstance().registerJPushObserver(mJPushObserver);
		
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        JPushObserverManager.getInstance().unregisterJPushObserver(mJPushObserver);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode) {
            if(mSlidingMenu.isMenuShowing()) {
                mSlidingMenu.toggle(true);
                exitApp();
            } else {
                mSlidingMenu.toggle(true);
            }
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_menu_exit:
			// 退出程序
			if(mSlidingMenu.isMenuShowing()) {
				mSlidingMenu.toggle(true);
			}
			exitApp();
			break;

		default:
			break;
		}
	}

    public boolean reloadStorage() {
        return mRealodStorage;
    }

    public void setReloadStorage(boolean reload) {
        this.mRealodStorage = reload;
    }
	
	/**
	 * 初始化侧边栏菜单
	 */
	private void initSlidingMenu() {
		setBehindContentView(R.layout.layout_menu);
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setShadowWidth(0);
		DisplayMetrics dm = getResources().getDisplayMetrics();
//		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setBehindOffset(dm.widthPixels / 3);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mSlidingMenu.setShadowDrawable(R.drawable.shape_shadow_slidingmenu);
		mSlidingMenu.setBehindScrollScale(0);
		mSlidingMenu.setMode(SlidingMenu.LEFT);
		mSlidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {

            @Override
            public void onOpen() {
                // TODO 滑出菜单的时候做的事情

            }
        });
		mSlidingMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {

            @Override
            public void onClosed() {
                // TODO 滑出菜单关闭后做的事情

            }
        });
	}
	
	private void initView() {
		
		initBehidView();
		
		initTitlebar();
	}
	
	private void initTitlebar() {
		mTitlebar = (Titlebar) findViewById(R.id.tb_main);
		mTitlebar.setLeftButton1(R.drawable.ic_title_menu, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showMenu();
            }
        });
		mTitlebar.setTitle(R.string.storage);
	}
	
	private void initBehidView() {
		mLvMenu = (ListView) findViewById(R.id.lv_menu);
		mLvMenu.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		mMenuAdapter = new MenuAdapter(this);
		mLvMenu.setAdapter(mMenuAdapter);
		mLvMenu.setOnItemClickListener(mMenuListener);
		findViewById(R.id.btn_menu_exit).setOnClickListener(this);
	}
	
	private AdapterView.OnItemClickListener mMenuListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
			case 0: //知识库
				openStorage();
				break;
//			case 1: //收藏夹
//				openFavorite();
//				break;
//			case 2: //设置
			case 1: //设置
				openSetting();
				break;
			default:
				break;
			}
			toggle();
		}
	};
	
	/**
	 * 友盟更新初始化
	 */
	private void initUmengUpdate() {
		if(mSharedPrefHelper.getBoolean(SharedPrefConfig.KEY_AUTO_CHECK_UPDATE, false)) {
			UmengUpdateAgent.setDefault();
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			UmengUpdateAgent.setRichNotification(false);
			UmengUpdateAgent.setDeltaUpdate(false);
			UmengUpdateAgent.setUpdateListener(mUpdateListener);
			UmengUpdateAgent.setDownloadListener(mDownloadListener);
			UmengUpdateAgent.forceUpdate(this);
		}
	}
	
	/**
	 * Umen检查更新监听
	 */
	private UmengUpdateListener mUpdateListener = new UmengUpdateListener() {
		
		@Override
		public void onUpdateReturned(int updateStatus, final UpdateResponse updateInfo) {
			switch (updateStatus) {
			case UpdateStatus.Yes: // 有更新
				if(updateInfo.hasUpdate) { // 有更新
					if(NetworkUtils.isWifiConnected(MainActivity.this) 
							&& mSharedPrefHelper.getBoolean(SharedPrefConfig.KEY_AUTO_DOWNLOAD_APK_WIFI, false)) {
						// 设置在WIFI下自动下载APK
						downloadApk(updateInfo);
					} else {
						// 弹出对话框，手动下载
						CustomDialog dialog = new CustomDialog(MainActivity.this);
						dialog.setTitleBackgroundResource(R.drawable.bg_dialog_title_shape);
						dialog.setCanceledOnTouchOutside(false);
						dialog.setTitle(R.string.has_new_version);
						dialog.setMessage(getString(R.string.version_name) + updateInfo.version 
								+ "\n" + getString(R.string.update_log) + "\n" + updateInfo.updateLog);
						dialog.setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 开始下载更新
								downloadApk(updateInfo);
								dialog.cancel();
							}
						});
						dialog.setNegativeButton(R.string.update_later, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						dialog.show();
					}
				}
				break;
			case UpdateStatus.No: // 没有更新
				break;
			case UpdateStatus.NoneWifi: // 没有wifi，在设置了只有wifi下更新的时候才会有
				break;
			case UpdateStatus.Timeout: // 连接超时
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 下载APK安装包<br>
	 * <p>如果符合版本信息的APK安装包已经下载完成，则不会再次下载，直接安装已经下载好的APK
	 * @param updateInfo 版本更新信息
	 */
	private void downloadApk(UpdateResponse updateInfo) {
		// 开始下载更新
		File downloadedFile = UmengUpdateAgent.downloadedFile(this, updateInfo);
		if(null != downloadedFile && !StringUtils.isEmpty(downloadedFile.getPath())) {
			// 该版本（最新版本）的更新包已经下载，直接安装
			UmengUpdateAgent.startInstall(this, downloadedFile);
		} else {
			// 该版本（最新版本）的更新包没有下载，开始下载
			UmengUpdateAgent.startDownload(this, updateInfo);
		}
	}
	
	/**
	 * Umen更新下载监听
	 */
	private UmengDownloadListener mDownloadListener = new UmengDownloadListener() {
		
		@Override
		public void OnDownloadUpdate(int progress) {
			
		}
		
		@Override
		public void OnDownloadStart() {
			
		}
		
		@Override
		public void OnDownloadEnd(int result, String file) {
			switch (result) {
			case UpdateStatus.DOWNLOAD_COMPLETE_FAIL: //下载失败
				break;	
			case UpdateStatus.DOWNLOAD_COMPLETE_SUCCESS: //下载成功
				break;
			case UpdateStatus.DOWNLOAD_NEED_RESTART: // 增量更新请求全包更新（请勿处理返回3的情况）
				break;
			default:
				break;
			}
		}
	};

	private JPushObserver mJPushObserver = new JPushObserver() {
        @Override
        public void onNotificationOpened(Bundle bundle) {
            String extrasString = bundle.getString(JPushInterface.EXTRA_EXTRA);
            switch (Utils.getNotificationType(extrasString)) {
                case Config.TYPE_NOTIFICATION_NEW_CONTENT:
                    // 推送通知的类型为新的内容
                    if(BoCoolManager.getInstance(MainActivity.this).isActivityFront(MainActivity.this)) {
                        // 当前Activity在最前端
                        setReloadStorage(true);
                        mLvMenu.setItemChecked(0, true);
                        openStorage();
                    } else {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtras(bundle);
                        startActivityRight(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if(null == bundle) {
            return;
        }
        String extrasString = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if(Config.TYPE_NOTIFICATION_NEW_CONTENT == Utils.getNotificationType(extrasString)) {
            setReloadStorage(true);
            openStorage();
            mLvMenu.setItemChecked(0, true);
        }
    }

    /**
	 * 打开知识库界面
	 */
	private void openStorage() {
		mTitlebar.setTitle(R.string.storage);
		mFragmentUtil.openFragment(StorageFragment.class, null);
	}
	
	/**
	 * 打开收藏
	 */
//	private void openFavorite() {
//		mTitlebar.setTitle(R.string.favorite);
//		mFragmentUtil.openFragment(FavoriteFragment.class, null);
//	}
	
	/**
	 * 打开设置界面
	 */
	private void openSetting() {
		mTitlebar.setTitle(R.string.setting);
		mFragmentUtil.openFragment(SettingFragment.class, null);
	}

	private void exitApp() {
		finish();
		BDTtsManager.getInstance().releaseBDTts(); // 释放百度TTS资源
	}
}