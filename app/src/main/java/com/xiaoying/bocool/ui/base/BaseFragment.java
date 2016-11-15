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
package com.xiaoying.bocool.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xiaoying.bocool.R;
import com.xiaoying.bocool.task.CustomAsyncTask;
import com.xiaoying.bocool.utils.LogUtils;
import com.xiaoying.bocool.widget.dialog.CustomProgressDialog;

/**
 * 功能：
 * @author xiaoying
 *
 */
public class BaseFragment extends Fragment implements View.OnClickListener{
	
	protected final String tag = this.getClass().getSimpleName();
	
	protected CustomProgressDialog mProgressDialog = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		MobclickAgent.onError(getActivity());
		LogUtils.w(tag, tag + "==>>onCreate()");
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils.w(tag, tag + "==>>onActivityCreated()");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(tag);
		MobclickAgent.onResume(getActivity());
		LogUtils.d(tag, tag + "==>>onResume()");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		LogUtils.d(tag, tag + "==>>onStart()");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(tag);
		MobclickAgent.onPause(getActivity());
		LogUtils.d(tag, tag + "==>>onPause()");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		LogUtils.d(tag, tag + "==>>onStop()");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		cancelProgressDialog();
		LogUtils.d(tag, tag + "==>>onDestroy()");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils.d(tag, tag + "==>>onCreateView()");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		LogUtils.d(tag, tag + "==>>onViewCreated()");
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtils.d(tag, tag + "==>>onDestroyView()");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtils.d(tag, tag + "==>>onAttach()=>>>" + activity.getClass().getSimpleName());
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		LogUtils.d(tag, tag + "==>>onDetach()");
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		LogUtils.d(tag, tag + "==>>onSaveInstanceState()");
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	public void startActivityLeft(Intent intent) {
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	public void startActivityRight(Intent intent) {
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
	

	protected void startActivityTop(Intent intent) {
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
	}
	
	public void showShortTost(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}
	
	public void showShortTost(int resId) {
		Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
	}
	
	public void showLongToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}

	public void showLongToast(int resId) {
		Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示加载对话框
	 * @param title
	 * @param message
	 * @param isCanceledOnTouchOutside
	 * @param isCancelable
	 * @param listener
	 */
	protected void showProgressDialog(CharSequence title, CharSequence message, boolean isCanceledOnTouchOutside, 
			boolean isCancelable, DialogInterface.OnCancelListener listener) {
		mProgressDialog = CustomProgressDialog.show(getActivity(), title, message, true, getResources().getDrawable(R.drawable.progress_drawable_round_white_unclip));
		mProgressDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
		mProgressDialog.setCancelable(isCancelable);
		if(listener != null) {
			mProgressDialog.setOnCancelListener(listener);
		}
	}
	
	/**
	 * 取消加载对话框
	 */
	protected void cancelProgressDialog() {
		if(mProgressDialog != null) {
			mProgressDialog.cancel();
			mProgressDialog = null;
		}
	}
	
	protected void cancelTask(CustomAsyncTask<?, ?, ?> task) {
		if(task != null && !task.isCancelled()) {
			task.cancel();
			task = null;
		}
	}
}
