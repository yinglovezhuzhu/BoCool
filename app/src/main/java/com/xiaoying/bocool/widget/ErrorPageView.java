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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoying.bocool.R;

/**
 * 错误页
 * Created by XIAOYING on 2015/6/10.
 */
public class ErrorPageView extends LinearLayout {

    private ImageView mIvIcon;
    private TextView mTvError;
    private Button mBtnRealod;

    public ErrorPageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public ErrorPageView(Context context) {
        super(context);
        initView(context);
    }

    public ErrorPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setIconResource(int resId) {
        mIvIcon.setImageResource(resId);
    }

    public void setErrorMessage(CharSequence text) {
        mTvError.setText(text);
    }

    public void setErrorMessage(int resid) {
        mTvError.setText(resid);
    }

    public void setReloadButton(CharSequence text, View.OnClickListener l) {
        mBtnRealod.setVisibility(View.VISIBLE);
        mBtnRealod.setText(text);
        mBtnRealod.setOnClickListener(l);
    }

    public void setReloadButton(int resid, View.OnClickListener l) {
        mBtnRealod.setVisibility(View.VISIBLE);
        mBtnRealod.setText(resid);
        mBtnRealod.setOnClickListener(l);
    }

    public void setReloadButton(View.OnClickListener l) {
        mBtnRealod.setVisibility(View.VISIBLE);
        mBtnRealod.setOnClickListener(l);
    }

    public void hideReloadButton() {
        mBtnRealod.setVisibility(View.INVISIBLE);
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    public void dismiss() {
        this.setVisibility(View.GONE);
    }

    public boolean isShowing() {
        return View.VISIBLE == this.getVisibility();
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.layout_error_page, this);
        mIvIcon = (ImageView) findViewById(R.id.iv_error_page_icon);
        mTvError = (TextView) findViewById(R.id.tv_error_page_message);
        mBtnRealod = (Button) findViewById(R.id.btn_error_page_reload);
    }
}
