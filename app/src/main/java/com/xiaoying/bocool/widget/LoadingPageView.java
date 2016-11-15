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
 * 加载页，显示整个页面的加载状态
 * Created by XIAOYING on 2015/6/7.
 */
public class LoadingPageView extends LinearLayout {

    private ProgressBar mPbProgress;
    private TextView mTvMessage;
    private ErrorPageView mErrorPage;

    private boolean mLoading = false;

    public LoadingPageView(Context context) {
        super(context);

        initView(context);
    }

    public LoadingPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingPageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void loading(CharSequence text) {
        this.setVisibility(View.VISIBLE);
        mPbProgress.setVisibility(View.VISIBLE);
        if(null != text) {
            mTvMessage.setVisibility(View.VISIBLE);
        }
        mErrorPage.setVisibility(View.INVISIBLE);
        mTvMessage.setText(text);
        mLoading = true;
    }

    public void loading(int resid) {
        this.setVisibility(View.VISIBLE);
        mPbProgress.setVisibility(View.VISIBLE);
        if(0 != resid) {
            mTvMessage.setVisibility(View.VISIBLE);
        }
        mErrorPage.setVisibility(View.INVISIBLE);
        mTvMessage.setText(resid);
        mTvMessage.setClickable(false);
        mLoading = true;
    }

    public void onError(CharSequence errorMsg, CharSequence reload, View.OnClickListener l) {
        this.setVisibility(View.VISIBLE);
        mPbProgress.setVisibility(View.INVISIBLE);
        mTvMessage.setVisibility(View.INVISIBLE);
        mErrorPage.setVisibility(View.VISIBLE);
        mErrorPage.setErrorMessage(errorMsg);
        if(null == reload || reload.length() == 0) {
            mErrorPage.hideReloadButton();
        } else {
            mErrorPage.setReloadButton(reload, l);
        }
        mLoading = false;
    }

    public void onError(int errorMsg, int reload, View.OnClickListener l) {
        this.setVisibility(View.VISIBLE);
        mPbProgress.setVisibility(View.INVISIBLE);
        mTvMessage.setVisibility(View.INVISIBLE);
        mErrorPage.setVisibility(View.VISIBLE);
        mErrorPage.setErrorMessage(errorMsg);
        if(reload == 0) {
            mErrorPage.hideReloadButton();
        } else {
            mErrorPage.setReloadButton(reload, l);
        }
        mLoading = false;
    }


    public void dismiss() {
        this.setVisibility(View.GONE);
        mLoading = false;
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.layout_loading_page, this);
        mPbProgress = (ProgressBar) findViewById(R.id.pb_loading_page_progress);
        mErrorPage = (ErrorPageView) findViewById(R.id.error_page_loading_page);
        mTvMessage = (TextView) findViewById(R.id.tv_loading_page_msg);
    }
}
