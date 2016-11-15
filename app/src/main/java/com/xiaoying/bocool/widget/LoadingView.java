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

package com.xiaoying.bocool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaoying.bocool.R;

/**
 * 加载提示控件
 * Created by XIAOYING on 2015/6/7.
 */
public class LoadingView extends LinearLayout {

    private ProgressBar mPbProgress;
    private TextView mTvMessage;

    public LoadingView(Context context) {
        super(context);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void setOnClickListener(View.OnClickListener l) {
        this.setOnClickListener(l);
    }

    public void setMessage(CharSequence text) {
        this.mTvMessage.setText(text);
    }

    public void setMessage(int resId) {
        this.mTvMessage.setText(resId);
    }

    public void setProgressVisibility(int visibility) {
        mPbProgress.setVisibility(visibility);
    }

    /**
     * 显示
     * @param msg
     */
    public void show(CharSequence msg) {
        mTvMessage.setText(msg);
        this.setVisibility(View.VISIBLE);
    }

    /**
     * 显示
     * @param resid
     */
    public void show(int resid) {
        mTvMessage.setText(resid);
        this.setVisibility(View.VISIBLE);
    }

    /**
     * 显示
     */
    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        this.setVisibility(View.GONE);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.layout_loading_view, this);
        mPbProgress = (ProgressBar) findViewById(R.id.pb_loading_progress);
        mTvMessage = (TextView) findViewById(R.id.tv_loading_msg);
    }
}
