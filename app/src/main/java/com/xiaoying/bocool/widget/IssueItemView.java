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
package com.xiaoying.bocool.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoying.bocool.R;

/**
 * 专辑Item视图控件
 * 
 * Create by yinglovezhuzhu@gmail.com in 2015年2月16日
 */
public class IssueItemView extends LinearLayout {
	
	private ImageView mIvImage;
	private View mNewView;
//	private View mStateView;
//	private ImageView mIvStateIcon;
//	private TextView mTvProgress;
	private TextView mTvName;
//	private CheckBox mCbCollect;

	public IssueItemView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		View.inflate(context, R.layout.item_issue, this);
		
		mIvImage = (ImageView) findViewById(R.id.iv_issue_image);
		mNewView = findViewById(R.id.iv_issue_new);
//		mStateView = findViewById(R.id.fl_issue_download_state_content);
//		mIvStateIcon = (ImageView) findViewById(R.id.iv_issue_download_state);
//		mTvProgress = (TextView) findViewById(R.id.tv_issue_download_progress);
		mTvName = (TextView) findViewById(R.id.tv_issue_name);
//		mCbCollect = (CheckBox) findViewById(R.id.cb_issue_collect);
		
//		mCbCollect.setVisibility(View.GONE);
	}
	
	
	public ImageView getImageView() {
		return mIvImage;
	}
	
	public void setNew(boolean isNew) {
		mNewView.setVisibility(isNew ? View.VISIBLE : View.INVISIBLE);
	}
	
	public void setNameText(CharSequence name) {
		mTvName.setText(name);
	}
	
}
