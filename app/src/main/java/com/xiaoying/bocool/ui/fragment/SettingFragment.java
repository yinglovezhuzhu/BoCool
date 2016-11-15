/*
 * Copyright (C) 2015. The BoCool Project.
 *
 *            yinglovezhuzhu@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.xiaoying.bocool.ui.fragment;

import java.io.File;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.xiaoying.bocool.R;
import com.xiaoying.bocool.config.Config;
import com.xiaoying.bocool.task.CustomAsyncTask;
import com.xiaoying.bocool.ui.AboutActivity;
import com.xiaoying.bocool.ui.FeedbackActivity;
import com.xiaoying.bocool.ui.UserSettingActivity;
import com.xiaoying.bocool.ui.base.BaseFragment;
import com.xiaoying.bocool.utils.FileUtil;
import com.xiaoying.bocool.utils.StringUtils;
import com.xiaoying.bocool.widget.dialog.CustomDialog;

/**
 * 设置界面
 * Create by yinglovezhuzhu@gmail.com in 2014年12月11日
 */
public class SettingFragment extends BaseFragment {
	
	private TextView mTvVersion;
	private TextView mTvCacheSize;
	
	private GetCacheSizeTask mGetCacheSizeTask;
	private DeleteCacheTask mDeleteCacheTask;
	
	private boolean mCalculatingCacheSize = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_setting, container, false);
		
		initView(contentView);
		return contentView;
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(isHidden()) {
			cancelTask(mGetCacheSizeTask);
		} else {
			getCacheSize();
		}
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		PackageManager pm = getActivity().getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_ACTIVITIES);
			mTvVersion.setText("V" + pi.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			mTvVersion.setText("Unknow");
		}
		getCacheSize();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		cancelTask(mGetCacheSizeTask);
		cancelTask(mDeleteCacheTask);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_setting_user_setting:
			startActivityLeft(new Intent(getActivity(), UserSettingActivity.class));
			break;
		case R.id.ll_setting_clean_cache:
			if(mCalculatingCacheSize) {
				return;
			}
			deleteCache();
			break;
		case R.id.ll_setting_update:
			showProgressDialog(null, getString(R.string.checking_update), false, false, null);
			UmengUpdateAgent.setDefault();
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			UmengUpdateAgent.setRichNotification(false);
			UmengUpdateAgent.setDeltaUpdate(false);
			UmengUpdateAgent.setUpdateListener(mUpdateListener);
			UmengUpdateAgent.setDownloadListener(mDownloadListener);
			UmengUpdateAgent.forceUpdate(getActivity());
			break;
		case R.id.ll_setting_feddback:
			startActivityLeft(new Intent(getActivity(), FeedbackActivity.class));
			break;
		case R.id.ll_setting_about:
			startActivityLeft(new Intent(getActivity(), AboutActivity.class));
			break;
		default:
			break;
		}
	}
	
	private void initView(View contentView) {
		mTvVersion = (TextView) contentView.findViewById(R.id.tv_setting_version);
		mTvCacheSize = (TextView) contentView.findViewById(R.id.tv_setting_cache);
		contentView.findViewById(R.id.ll_setting_user_setting).setOnClickListener(this);
		contentView.findViewById(R.id.ll_setting_clean_cache).setOnClickListener(this);
		contentView.findViewById(R.id.ll_setting_update).setOnClickListener(this);
		contentView.findViewById(R.id.ll_setting_feddback).setOnClickListener(this);
		contentView.findViewById(R.id.ll_setting_about).setOnClickListener(this);
	}
	
	/**
	 * Umen检查更新监听
	 */
	private UmengUpdateListener mUpdateListener = new UmengUpdateListener() {
		
		@Override
		public void onUpdateReturned(int updateStatus, final UpdateResponse updateInfo) {
			CustomDialog dialog = new CustomDialog(getActivity());
			dialog.setTitleBackgroundResource(R.drawable.bg_dialog_title_shape);
			dialog.setCanceledOnTouchOutside(false);
			switch (updateStatus) {
			case UpdateStatus.Yes: // has update
				if(updateInfo.hasUpdate) {
					// 有更新
					dialog.setTitle(R.string.has_new_version);
					dialog.setMessage(getString(R.string.version_name) + updateInfo.version 
							+ "\n" + getString(R.string.update_log) + "\n" + updateInfo.updateLog);
					dialog.setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 开始下载更新
							File downloadedFile = UmengUpdateAgent.downloadedFile(getActivity(), updateInfo);
							if(null != downloadedFile && !StringUtils.isEmpty(downloadedFile.getPath())) {
								// 该版本（最新版本）的更新包已经下载，直接安装
								UmengUpdateAgent.startInstall(getActivity(), downloadedFile);
							} else {
								// 该版本（最新版本）的更新包没有下载，开始下载
								UmengUpdateAgent.startDownload(getActivity(), updateInfo);
							}
							dialog.cancel();
						}
					});
					dialog.setNegativeButton(R.string.update_later, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
				} else {
					dialog.setTitle(R.string.no_new_version);
					dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
				}
				break;
			case UpdateStatus.No: // 没有更新
				dialog.setTitle(R.string.no_new_version);
				dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				break;
			case UpdateStatus.NoneWifi: // 没有wifi，在设置了只有wifi下更新的时候才会有
			case UpdateStatus.Timeout: // 连接超时
				dialog.setTitle(R.string.network_error);
				dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				break;
			default:
				break;
			}
			cancelProgressDialog();
			dialog.show();
		}
	};
	
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
	
	/**
	 * 计算缓存大小
	 */
	private void getCacheSize() {
		mGetCacheSizeTask = new GetCacheSizeTask();
		mGetCacheSizeTask.execute();
	}
	
	/**
	 * 删除缓存
	 */
	private void deleteCache() {
		CustomDialog dialog = new CustomDialog(getActivity());
		dialog.setTitleBackgroundResource(R.drawable.bg_dialog_title_shape);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle(R.string.clean_cache);
		dialog.setMessage(getString(R.string.clean_chache_warming));
		dialog.setPositiveButton(R.string.clean_cache, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDeleteCacheTask = new DeleteCacheTask();
				mDeleteCacheTask.execute();
				dialog.cancel();
			}
		});
		dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialog.show();
	}
	
	/**
	 * 查询缓存大小
	 *
	 * @author zyy_owen@ivg.com
	 */
	private class GetCacheSizeTask extends CustomAsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mTvCacheSize.setText(R.string.calculating);
			mCalculatingCacheSize = true;
		}
		
		@Override
		protected String doInBackground(Void... params) {
			File cacheFile = FileUtil.createDirs(getActivity(), Config.APP_FOLDER, Config.CACHE_FOLDER);
			long totalSize = 0;
			if(null != cacheFile && cacheFile.exists()) {
				totalSize += FileUtil.getFileSize(cacheFile);
			}
			File appCacheDir = getActivity().getApplication().getCacheDir();
            if(null != appCacheDir && appCacheDir.exists()) {
                totalSize += FileUtil.getFileSize(appCacheDir);
            }
			return FileUtil.formatByte(getActivity(), totalSize);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mCalculatingCacheSize = false;
			if(isCancelled() || StringUtils.isEmpty(result)) {
				mTvCacheSize.setText("0B");
				return;
			}
			mTvCacheSize.setText(result);
		}
	}
	
	/**
	 * 删除缓存的异步线程类
	 *
	 * @author zyy_owen@ivg.com
	 */
	private class DeleteCacheTask extends CustomAsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(null, getText(R.string.cleaning_cache), false, false, null);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			File cacheFile = FileUtil.createDirs(getActivity(), Config.APP_FOLDER, Config.CACHE_FOLDER);
			FileUtil.clearDir(cacheFile);
			File appCacheDir = getActivity().getApplication().getCacheDir();
			FileUtil.clearDir(appCacheDir);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			cancelProgressDialog();
			showShortTost(R.string.cache_has_been_cleaned);
			getCacheSize();
		}
	}
}
