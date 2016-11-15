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
package com.xiaoying.bocool.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xiaoying.bocool.R;
import com.xiaoying.bocool.config.SharedPrefConfig;
import com.xiaoying.bocool.data.SharedPrefHelper;
import com.xiaoying.bocool.ui.base.BaseActivity;
import com.xiaoying.bocool.widget.Titlebar;

import cn.jpush.android.api.JPushInterface;

/**
 * 用户偏好设置
 * 
 * Create by yinglovezhuzhu@gmail.com in 2014年12月20日
 */
public class UserSettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final int AUTO_PLAY_DELAY_MAX = 1000 * 5;
    public static final int AUTO_PLAY_DELAY_DEFAULT = 1000 * 2; // 默认延时2秒,进度为0表示1秒

	private Titlebar mTitlebar;
	
	private CheckBox mAutoCheck;
	private CheckBox mAutoDownload;
	private CheckBox mAutoPlayVoice;
	private CheckBox mReceivePush;
    private CheckBox mAutoPlayRecycle;

    private SeekBar mAutoPlayDelay;
    private TextView mTvDelayValue;

	private SharedPrefHelper mSharedPrefHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_user_setting);
		
		mSharedPrefHelper = SharedPrefHelper.getInstance(this);
		
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
		case R.id.ibtn_tb_left1:
			finishRight();
			break;

		default:
			break;
		}
	}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_user_setting_auto_check_update:
                mSharedPrefHelper.saveBoolean(SharedPrefConfig.KEY_AUTO_CHECK_UPDATE, isChecked);
                mAutoDownload.setEnabled(mAutoCheck.isChecked());
                break;
            case R.id.cb_user_setting_auto_download_wifi:
                mSharedPrefHelper.saveBoolean(SharedPrefConfig.KEY_AUTO_DOWNLOAD_APK_WIFI, isChecked);
                break;
            case R.id.cb_user_setting_auto_play_voice:
                mSharedPrefHelper.saveBoolean(SharedPrefConfig.KEY_AUTO_PLAY_VOICE, isChecked);
                break;
            case R.id.cb_user_setting_receive_push_message:
                mSharedPrefHelper.saveBoolean(SharedPrefConfig.KEY_RECEIVE_PUSH_MESSAGE, isChecked);
                if(isChecked) {
                    JPushInterface.resumePush(getApplicationContext());
                } else {
                    JPushInterface.stopPush(getApplicationContext());
                }
                break;
            case R.id.cb_user_setting_auto_play_recycle:
                mSharedPrefHelper.saveBoolean(SharedPrefConfig.KEY_AUTO_PLAY_RECYCLE, isChecked);
                break;
            default:
                break;
        }
    }

    private void initView() {
		
		initTitlebar();
		
		mAutoCheck = (CheckBox) findViewById(R.id.cb_user_setting_auto_check_update);
		mAutoDownload = (CheckBox) findViewById(R.id.cb_user_setting_auto_download_wifi);
		mAutoPlayVoice = (CheckBox) findViewById(R.id.cb_user_setting_auto_play_voice);
		mReceivePush = (CheckBox) findViewById(R.id.cb_user_setting_receive_push_message);
        mAutoPlayRecycle = (CheckBox) findViewById(R.id.cb_user_setting_auto_play_recycle);
        mAutoCheck.setOnCheckedChangeListener(this);
		mAutoDownload.setOnCheckedChangeListener(this);
		mAutoPlayVoice.setOnCheckedChangeListener(this);
		mReceivePush.setOnCheckedChangeListener(this);
        mAutoPlayRecycle.setOnCheckedChangeListener(this);
		mAutoCheck.setChecked(mSharedPrefHelper.getBoolean(SharedPrefConfig.KEY_AUTO_CHECK_UPDATE, false));
		mAutoDownload.setChecked(mSharedPrefHelper.getBoolean(SharedPrefConfig.KEY_AUTO_DOWNLOAD_APK_WIFI, false));
		mAutoDownload.setEnabled(mAutoCheck.isChecked());
		mAutoPlayVoice.setChecked(mSharedPrefHelper.getBoolean(SharedPrefConfig.KEY_AUTO_PLAY_VOICE, false));
        mReceivePush.setChecked(mSharedPrefHelper.getBoolean(SharedPrefConfig.KEY_RECEIVE_PUSH_MESSAGE, true));
        mAutoPlayRecycle.setChecked(mSharedPrefHelper.getBoolean(SharedPrefConfig.KEY_AUTO_PLAY_RECYCLE, true));

        mAutoPlayDelay = (SeekBar) findViewById(R.id.sbar_user_setting_auto_play_delay);
        mTvDelayValue = (TextView) findViewById(R.id.tv_user_setting_auto_play_delay_value);
        mAutoPlayDelay.setMax(AUTO_PLAY_DELAY_MAX - 1000);
        int delay = mSharedPrefHelper.getInt(SharedPrefConfig.KEY_AUTO_PLAY_DELAY, AUTO_PLAY_DELAY_DEFAULT);
        mAutoPlayDelay.setProgress(delay - 1000);
        mAutoPlayDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int time = AUTO_PLAY_DELAY_DEFAULT;
                if (progress < 500) {
                    time = 1000;
                } else {
                    int remainder = progress % 1000;
                    if (remainder > 500) {
                        time = 1000 * (progress / 1000 + 2);
                    } else if (remainder > 0) {
                        time = 1000 * (progress / 1000 + 1);
                    } else {
                        time = 1000 * progress / 1000;
                    }
                }
                mAutoPlayDelay.setProgress(time - 1000);
                mTvDelayValue.setText(String.format(getString(R.string.time_second), time / 1000));
                mSharedPrefHelper.saveInt(SharedPrefConfig.KEY_AUTO_PLAY_DELAY, time);
            }
        });
        mTvDelayValue.setText(String.format(getString(R.string.time_second), delay / 1000));

    }
	
	private void initTitlebar() {
		mTitlebar = (Titlebar) findViewById(R.id.tb_user_setting);
		mTitlebar.setLeftButton1(R.drawable.ic_title_back, this);
		mTitlebar.setTitle(R.string.user_setting);
	}
}
