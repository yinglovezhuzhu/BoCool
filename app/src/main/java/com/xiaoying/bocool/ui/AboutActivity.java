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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaoying.bocool.R;
import com.xiaoying.bocool.ui.base.BaseActivity;
import com.xiaoying.bocool.widget.Titlebar;

/**
 * 关于界面
 * Create by yinglovezhuzhu@gmail.com in 2014年12月13日
 */
public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about);
		
		initView();
	}
	
	@Override
	public void onBackPressed() {
		finishRight();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ibtn_tb_left1: // 返回
			finishRight();
			break;
		case R.id.ibtn_tb_right1: // 分享

			break;

		default:
			break;
		}
	}
	
	private void initView() {
		initTitlebar();
	}
	
	/**
	 * 初始化标题栏
	 */
	private void initTitlebar() {
		Titlebar titlebar= (Titlebar) findViewById(R.id.tb_about);
		titlebar.setLeftButton1(R.drawable.ic_title_back, this);
		titlebar.setRightButton1(R.drawable.ic_title_share, this);
		titlebar.setTitle(R.string.about);
	}
}
