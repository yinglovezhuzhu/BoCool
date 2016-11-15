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
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.xiaoying.bocool.R;

/**
 * Use：标题栏
 * 
 * @author yinglovezhuzhu@gmail.com
 */
public class Titlebar extends FrameLayout {

	public static final int ID_LEFT_BUTTON1 = R.id.ibtn_tb_left1;
	public static final int ID_LEFT_BUTTON2 = R.id.ibtn_tb_left2;
	public static final int ID_RIGHT_BUTTON1 = R.id.ibtn_tb_right1;
	public static final int ID_RIGHT_BUTTON2 = R.id.ibtn_tb_right2;
	public static final int ID_TITLE_BUTTON = R.id.btn_tb_title;

	private View mContentView;
	private ImageButton mIbtnLeft1;
	private ImageButton mIbtnLeft2;
	private ImageButton mIbtnRight1;
	private ImageButton mIbtnRight2;
	private Button mBtnTitle;

	public Titlebar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public Titlebar(Context context) {
		super(context);
		initView(context);
	}
	
	/**
	 * 设置左1按钮<br>
	 * 说明：最左的为左1
	 * @param iconRes
	 * @param l
	 */
	public void setLeftButton1(int iconRes, View.OnClickListener l) {
		mIbtnLeft1.setImageResource(iconRes);
		mIbtnLeft1.setOnClickListener(l);
		mIbtnLeft1.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置左2按钮<br>
	 * 说明：最左的为左1
	 * @param iconRes
	 * @param l
	 */
	public void setLeftButton2(int iconRes, View.OnClickListener l) {
		mIbtnLeft2.setImageResource(iconRes);
		mIbtnLeft2.setOnClickListener(l);
		mIbtnLeft2.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置右1按钮<br>
	 * 说明：最右的按钮为右1
	 * @param iconRes
	 * @param l
	 */
	public void setRightButton1(int iconRes, View.OnClickListener l) {
		mIbtnRight1.setImageResource(iconRes);
		mIbtnRight1.setOnClickListener(l);
		mIbtnRight1.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置右2按钮<br>
	 * 说明：最右的为右1
	 * @param iconRes
	 * @param l
	 */
	public void setRightButton2(int iconRes, View.OnClickListener l) {
		mIbtnRight2.setImageResource(iconRes);
		mIbtnRight2.setOnClickListener(l);
		mIbtnRight2.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 重设按钮<br>
	 * <p>让所有的按钮均不可见
	 */
	public void resetButtons() {
		mIbtnLeft1.setVisibility(View.INVISIBLE);
		mIbtnLeft2.setVisibility(View.INVISIBLE);
		mIbtnRight1.setVisibility(View.INVISIBLE);
		mIbtnRight2.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * 设置标题文字<br>
	 * <p>单独设置标题的文字，不改变按钮
	 * @param textId
	 */
	public void setTitle(int textId) {
		mBtnTitle.setText(textId);
		mBtnTitle.setVisibility(View.VISIBLE);
		mBtnTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		mBtnTitle.setOnClickListener(null);
	}
	
	/**
	 * 设置标题文字<br>
	 * <p>单独设置标题的文字，不改变按钮
	 * @param text
	 */
	public void setTitle(CharSequence text) {
		mBtnTitle.setText(text);
		mBtnTitle.setVisibility(View.VISIBLE);
		mBtnTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		mBtnTitle.setOnClickListener(null);
	}
	
	/**
	 * 设置标题文字和
	 * @param textId
	 * @param arrowId
	 * @param l
	 */
	public void setTitle(int textId, int arrowId, View.OnClickListener l) {
		mBtnTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowId, 0);
		mBtnTitle.setOnClickListener(l);
		mBtnTitle.setText(textId);
		mBtnTitle.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置标题中间部分的文字和监听
	 * @param text
	 * @param arrowId
	 * @param l
	 */
	public void setTitle(CharSequence text, int arrowId, View.OnClickListener l) {
		mBtnTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, arrowId, 0);
		mBtnTitle.setOnClickListener(l);
		mBtnTitle.setText(text);
		mBtnTitle.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置标题的背景
	 * @param resid
	 */
	public void setTitleBackgroud(int resid) {
		mContentView.setBackgroundResource(resid);
	}
	
	/**
	 * 设置各个按钮的可见性
	 * @param left1
	 * @param left2
	 * @param right1
	 * @param right2
	 */
	public void setButtonsVisibility(int left1, int left2, int right1, int right2) {
		mIbtnLeft1.setVisibility(left1);
		mIbtnLeft2.setVisibility(left2);
		mIbtnRight1.setVisibility(right1);
		mIbtnRight2.setVisibility(right2);
	}
	
	/**
	 * 设置各个按钮是否可点击
	 * @param left1
	 * @param left2
	 * @param right1
	 * @param right2
	 */
	public void setButtonsClickable(boolean left1, boolean left2, boolean right1, boolean right2) {
		mIbtnLeft1.setClickable(left1);
		mIbtnLeft2.setClickable(left2);
		mIbtnRight1.setClickable(right1);
		mIbtnRight2.setClickable(right2);
	}

	public void setLeft1Button1Clickable(boolean clickable, int resId) {
		mIbtnLeft1.setClickable(clickable);
		mIbtnLeft1.setImageResource(resId);
	}
	
	public void setLeft2Button2Clickable(boolean clickable, int resId) {
		mIbtnLeft2.setClickable(clickable);
		mIbtnLeft2.setImageResource(resId);
	}
	
	public void setRight1Button1Clickable(boolean clickable, int resId) {
		mIbtnRight1.setClickable(clickable);
		mIbtnRight1.setImageResource(resId);
	}
	
	public void setRight2Button1Clickable(boolean clickable, int resId) {
		mIbtnRight2.setClickable(clickable);
		mIbtnRight2.setImageResource(resId);
	}
	
	private void initView(Context context) {
		LinearLayout ll = new LinearLayout(context);
		ll.setBackgroundColor(getResources().getColor(R.color.black));
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 
				FrameLayout.LayoutParams.MATCH_PARENT);
		lp.bottomMargin = 10;
		addView(ll, lp);
		mContentView = View.inflate(context, R.layout.layout_titlebar, this);
		mIbtnLeft1 = (ImageButton) findViewById(ID_LEFT_BUTTON1);
		mIbtnLeft2 = (ImageButton) findViewById(ID_LEFT_BUTTON2);
		mIbtnRight1 = (ImageButton) findViewById(ID_RIGHT_BUTTON1);
		mIbtnRight2 = (ImageButton) findViewById(ID_RIGHT_BUTTON2);
		mBtnTitle = (Button) findViewById(ID_TITLE_BUTTON);
	}
	
}
