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
package com.xiaoying.bocool.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.opensource.bitmaploader.ImageFetcher;
import com.opensource.bitmaploader.SimpleLoadListener;
import com.xiaoying.bocool.R;
import com.xiaoying.bocool.bean.Issue;
import com.xiaoying.bocool.bean.Storage;
import com.xiaoying.bocool.config.Config;
import com.xiaoying.bocool.ui.IssueActivity;
import com.xiaoying.bocool.utils.StringUtils;
import com.xiaoying.bocool.voice.SimpleFetchVoiceListener;
import com.xiaoying.bocool.voice.VoiceFetcher;
import com.xiaoying.bocool.widget.LoadingPageView;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;

/**
 * 专辑详情的项页面
 * Create by yinglovezhuzhu@gmail.com in 2015年04月23日
 */
public class IssueFragment extends Fragment {

	private ImageView mIvImage;
	private LoadingPageView mLoadingView;

    private int mPosition = Config.INVALID_POSITION;

	private SimpleLoadListener mLoadingListener = new SimpleLoadListener() {

		@Override
		public void onStart(ImageView imageview, Object data) {
			mLoadingView.loading(null);
		}

		@Override
		public void onSet(ImageView imageView, Bitmap bitmap) {
			mLoadingView.dismiss();
            final IssueActivity activity;
            if(getActivity() instanceof IssueActivity) {
                activity = (IssueActivity) getActivity();
                activity.onImageLoaded(mPosition, null != bitmap);
            }
		}

		@Override
		public void onCanceld(ImageView imageView,
							  Object data) {
			super.onCanceld(imageView, data);
			mLoadingView.dismiss();
		}

		@Override
		public void onError(Object data, Object errorMsg) {
			super.onError(data, errorMsg);
			mLoadingView.onError(R.string.loading_image_failed,
                    R.string.click_to_reload, mReloadListener);
		}

	};

	private View.OnClickListener mReloadListener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
            Issue issue = (Issue) mLoadingView.getTag();
            if(null == issue) {
                mLoadingView.onError(R.string.loading_image_failed, 0, null);
                return;
            }
            loadImage(issue);
		}
	};
	
	public static IssueFragment newInstance(Issue issue, int position) {
		IssueFragment fragment = new IssueFragment();
		Bundle args = new Bundle();
		args.putSerializable(Config.EXTRA_DATA, issue);
        args.putInt(Config.EXTRA_POSITION, position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_issue, container, false);
		initView(contentView);
		return contentView;
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final IssueActivity activity;
        if(getActivity() instanceof IssueActivity) {
            activity = (IssueActivity) getActivity();
            activity.onImageLoaded(mPosition, false);
        }
    }

    private void initView(View contentView) {
		mIvImage = (ImageView) contentView.findViewById(R.id.iv_issue_image);
		mLoadingView = (LoadingPageView) contentView.findViewById(R.id.loading_view_issue);

		Bundle args = getArguments();
		Issue issue = (Issue) args.getSerializable(Config.EXTRA_DATA);
        mPosition = args.getInt(Config.EXTRA_POSITION, Config.INVALID_POSITION);
		if(null == issue) {
			mLoadingView.onError(R.string.loading_image_failed, 0, null);
		} else {
			mLoadingView.setTag(issue);
            loadImage(issue);
            loadVoice(issue);
		}
	}

    private void loadImage(Issue issue) {
        Activity activity = getActivity();
        if(activity instanceof IssueActivity) {
            switch (issue.getStatus()) {
                case Storage.STATUS_ONLINE:
                    ImageFetcher imageFetcher = ((IssueActivity) activity).getImageFetcher();
                    if(null == imageFetcher) {
                        mLoadingView.onError(R.string.loading_image_failed, 0, null);
                    } else {
                        BmobFile imgFile = issue.getImg();
                        if(null != imgFile) {
                            imageFetcher.loadImage(imgFile.getFileUrl(getActivity()),
                                    mIvImage, mLoadingListener);
                        }
                    }
                    break;
                case Storage.STATUS_OUTLINE:
                    break;
                default:
                    break;
            }
        }
    }

	private void loadVoice(Issue issue) {
        final IssueActivity activity;
        if(getActivity() instanceof IssueActivity) {
            activity = (IssueActivity) getActivity();
        } else {
            return;
        }
        BmobFile voiceFile = issue.getVoice();
        final String voiceUrl;
        if(null != voiceFile && !StringUtils.isEmpty((voiceUrl = voiceFile.getFileUrl(activity)))) {
            // 下载音频文件播放
            if(!activity.isVoiceLoaded(voiceUrl)) {
                activity.onVoiceLoading(voiceUrl, mPosition);
                VoiceFetcher voiceFetcher = activity.getVoiceFetcher();
                if(null == voiceFetcher) {
                    return;
                }
                voiceFetcher.loadVoice(voiceUrl, new SimpleFetchVoiceListener() {

                    @Override
                    public void onFinished(String url, File file) {
                        if (null == file || !file.exists()) {
                            return;
                        }
                        activity.onVoiceDownloaded(mPosition, voiceUrl, file);
                    }
                });
            }
        }
	}
}
