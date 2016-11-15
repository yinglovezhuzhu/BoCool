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

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xiaoying.bocool.R;
import com.xiaoying.bocool.config.Config;
import com.xiaoying.bocool.config.SharedPrefConfig;
import com.xiaoying.bocool.data.SharedPrefHelper;
import com.xiaoying.bocool.observer.NetworkObserver;
import com.xiaoying.bocool.observer.NetworkObserverManager;
import com.xiaoying.bocool.tts.BDTtsManager;
import com.xiaoying.bocool.ui.base.BaseActivity;
import com.xiaoying.bocool.utils.BoCoolManager;
import com.xiaoying.bocool.utils.FileUtil;
import com.xiaoying.bocool.utils.LogUtils;
import com.xiaoying.bocool.utils.NetworkUtils;
import com.xiaoying.bocool.utils.StringUtils;
import com.xiaoying.bocool.utils.Utils;
import com.xiaoying.bocool.widget.dialog.CustomDialog;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * 启动页
 * Create by yinglovezhuzhu@gmail.com in 2014年12月5日
 */
public class SplashActivity extends BaseActivity {
	
	private static final int SPLASH_DELAY = 2000;
	
	private ProgressBar mProgressBar;
	
	private SharedPrefHelper mSharedPrefHelper;
	
	private SplashHandler mHandler = new SplashHandler();
	
    private boolean mInitialized = false;
    private boolean mTimeout = false;

    private CustomDialog mDialog = null;

    private NetworkObserver mNetworkObserver = new NetworkObserver() {
        @Override
        public void onNetworkStateChaged(boolean noConnectivity, NetworkInfo currentNetwok,
                                         NetworkInfo lastNetwork) {
            if(!noConnectivity) {
                if(null != mDialog && mDialog.isShowing()) {
                    mDialog.cancel();
                }
                startInitialize();
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_splash);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_splash_progress);
        mProgressBar.setVisibility(View.INVISIBLE);

        mSharedPrefHelper = SharedPrefHelper.getInstance(this);

        NetworkObserverManager.getInstance().registerNetworkObserver(mNetworkObserver);

        if(!NetworkUtils.isNetworkConnected(this)) {
            mDialog = new CustomDialog(this)
                    .setTitleBackgroundResource(R.drawable.bg_dialog_title_shape)
                    .setTitleText(R.string.net_error)
                    .setMessage(R.string.no_usable_network)
                    .setPositiveButton(R.string.checking_network, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            return;
        }

        startInitialize();
	}

    @Override
    public void onDestroy() {
        NetworkObserverManager.getInstance().unregisterNetworkObserver(mNetworkObserver);
        super.onDestroy();
    }

    private void startInitialize() {
        mProgressBar.setVisibility(View.VISIBLE);
		mHandler.removeMessages(SplashHandler.MSG_TIMEOUT);
		mHandler.removeMessages(SplashHandler.MSG_INITIALIZED);

		initialize();

		mHandler.sendEmptyMessageDelayed(SplashHandler.MSG_TIMEOUT, SPLASH_DELAY);


	}

    private void initialize() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                // 初始化Bmob后台数据库管理系统SDK
                String bmobAppId = Utils.getMetaDataAsString(SplashActivity.this,
                        Config.META_KEY_BMOB_APPID);
                if(StringUtils.isEmpty(bmobAppId)) {
                    mHandler.sendEmptyMessage(SplashHandler.MSG_ERROR);
                    return;
                }
                Bmob.initialize(SplashActivity.this, bmobAppId);

                // 第一次使用、卸载后重新安装、应用数据被清除（清除数据而不是清除缓存）
                if (mSharedPrefHelper.getBoolean(SharedPrefConfig.KEY_FIRST_USE, true)) {
                    initData();
                }

                // 初始化百度tts语音系统
                initBDTts();

                initJPush();

                mHandler.sendEmptyMessage(SplashHandler.MSG_INITIALIZED);
            }
        }.start();
    }
	
	private void initData() {
        mSharedPrefHelper.saveBoolean(SharedPrefConfig.KEY_AUTO_CHECK_UPDATE, true);
        mSharedPrefHelper.saveBoolean(SharedPrefConfig.KEY_AUTO_DOWNLOAD_APK_WIFI, false);

        mSharedPrefHelper.saveBoolean(SharedPrefConfig.KEY_FIRST_USE, false);

	}

    private void initBDTts() {
        File licenceFolder = FileUtil.createDirs(this, Config.APP_FOLDER, Config.TTS_FOLDER);
        if(null == licenceFolder) {
            // Error
            mHandler.sendEmptyMessage(SplashHandler.MSG_ERROR);
            return;
        }
        // 部分版本不需要BDSpeechDecoder_V1
        try {
            System.loadLibrary("BDSpeechDecoder_V1");
        } catch (UnsatisfiedLinkError e) {
            LogUtils.d(tag, "load BDSpeechDecoder_V1 failed, ignore");
        }
        System.loadLibrary("bd_etts");
        System.loadLibrary("bds");

        // 复制license到指定路径
        File licenceFile = new File(licenceFolder, "baidu_tts_licence.dat");
        if (!licenceFile.exists()) {
            FileUtil.copyAssets2Dir(this, "tts/baidu_tts_licence", licenceFile);
        }
        // 保存license的路径
        SharedPrefHelper.getInstance(this).saveString(SharedPrefConfig.KEY_TTS_LICENSE_PATH,
                licenceFile.getPath());

        BDTtsManager.getInstance().initBDTts(getApplicationContext(), true);
    }

    private void initJPush() {
        boolean receivePushMessage = SharedPrefHelper.getInstance(this)
                .getBoolean(SharedPrefConfig.KEY_RECEIVE_PUSH_MESSAGE, true);
        if(receivePushMessage && JPushInterface.isPushStopped(getApplicationContext())) {
            JPushInterface.resumePush(getApplicationContext());
        } else if(!receivePushMessage && !JPushInterface.isPushStopped(getApplicationContext())) {
            JPushInterface.stopPush(getApplicationContext());
        }
    }

    private void gotoMain() {
        Intent intent = getIntent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivityLeft(intent);
        finish();
    }

    @SuppressLint("HandlerLeak")
    private class SplashHandler extends Handler {

        public static final int MSG_TIMEOUT = 0x0;

        public static final int MSG_INITIALIZED = 0x1;

        public static final int MSG_ERROR = 0x2;


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_TIMEOUT:
                    mTimeout = true;
                    if (mInitialized) {
                        gotoMain();
                    }
                    break;
                case MSG_INITIALIZED:
                    mInitialized = true;
                    if(mTimeout) {
                        gotoMain();
                    }
                    break;
                case MSG_ERROR:
                    mInitialized = false;
                    mTimeout = false;
                    mHandler.removeMessages(MSG_INITIALIZED);
                    mHandler.removeMessages(MSG_TIMEOUT);
                    Toast.makeText(SplashActivity.this, getString(R.string.app_error),
                            Toast.LENGTH_LONG).show();
                    finish();
                default:
                    break;
            }
        }
    }
}
