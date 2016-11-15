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
package com.xiaoying.bocool.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.UserInfo;
import com.xiaoying.bocool.R;
import com.xiaoying.bocool.ui.base.BaseActivity;
import com.xiaoying.bocool.utils.NetworkUtils;
import com.xiaoying.bocool.utils.StringUtils;
import com.xiaoying.bocool.widget.Titlebar;
import com.xiaoying.bocool.widget.dialog.CustomProgressDialog;

/**
 * 意见反馈
 * Create by yinglovezhuzhu@gmail.com in 2014年12月18日
 */
public class FeedbackActivity extends BaseActivity {


	private Titlebar mTitlebar;
	
	private EditText mEtContent;
	
	private EditText mEtContact;
	
	private CustomProgressDialog mProgressDialog;
	
	private FeedbackAgent mFeedbackAgent;
	
	private Conversation mConversation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feedback);
		
		initView();
		
		mFeedbackAgent = new FeedbackAgent(this);
		mConversation = Conversation.newInstance(this);
	}
	
	@Override
	public void onBackPressed() {
		finishRight();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_feedback_submit:
			if(NetworkUtils.isNetworkAwailable(FeedbackActivity.this)) {
				if(checkInput()) {
					mProgressDialog = CustomProgressDialog.show(FeedbackActivity.this, 
							null, getString(R.string.submiting_data), true, 
							getResources().getDrawable(R.drawable.progress_drawable_round_white_unclip),
							true);
					mProgressDialog.setCanceledOnTouchOutside(false);
					mConversation.sync(mSyncListener);
				}
			} else {
				showShortToast(R.string.network_unavailable);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		
		initTitlebar();
		
		mEtContent = (EditText) findViewById(R.id.et_feedback_content);
		mEtContact = (EditText) findViewById(R.id.et_feedback_contact);
		
		findViewById(R.id.btn_feedback_submit).setOnClickListener(this);
		
	}
	
	/**
	 * 初始化标题栏
	 */
	private void initTitlebar() {
		mTitlebar = (Titlebar) findViewById(R.id.tb_feedback);
		mTitlebar.setLeftButton1(R.drawable.ic_title_back, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishRight();
			}
		});
		mTitlebar.setTitle(R.string.feedback);
	}
	
	/**
	 * 检查输入<br>
	 * 检查用户的输入，内容为必填，联系方式选填
	 * @return
	 */
	private boolean checkInput() {
		String content = mEtContent.getText().toString().trim();
		String contact = mEtContact.getText().toString().trim();
		if(StringUtils.isEmpty(content)) {
			showShortToast(R.string.feedback_content_not_null);
			return false;
		}
		UserInfo info = mFeedbackAgent.getUserInfo();
		Map<String, String> contacts = new HashMap<String, String>();
		contacts.put(getString(R.string.feedback_contact), contact);
		info.setContact(contacts);
		mFeedbackAgent.setUserInfo(info);
		mFeedbackAgent.updateUserInfo();
		
		mConversation.addUserReply(content);
		
		return true;
	}
	
	private SyncListener mSyncListener = new SyncListener() {
		
		@Override
		public void onSendUserReply(List<Reply> arg0) {
			showShortToast(R.string.feedback_thanks_word);
			if(null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.cancel();
				mProgressDialog = null;
			}
			finishRight();
		}
		
		@Override
		public void onReceiveDevReply(List<Reply> arg0) {
			// 不做任何事情，这里没有用对话模式
		}
	};
}
