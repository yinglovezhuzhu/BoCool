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

package com.xiaoying.bocool.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xiaoying.bocool.config.SharedPrefConfig;
import com.xiaoying.bocool.data.SharedPrefHelper;
import com.xiaoying.bocool.observer.JPushObserver;
import com.xiaoying.bocool.observer.JPushObserverManager;
import com.xiaoying.bocool.ui.SplashActivity;
import com.xiaoying.bocool.utils.BoCoolManager;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * JPush广播接收器
 * Created by yinglovezhuzhu@gmail.com on 2015/6/11.
 */
public class JPushReciver extends BroadcastReceiver {

    private static final String TAG = "JPushReciver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(null == intent) {
            return;
        }
        Bundle bundle = intent.getExtras();
        if(null == bundle || bundle.isEmpty()) {
            return;
        }
        String action = intent.getAction();
        Log.d(TAG, "[JPushReceiver] onReceive - " + action + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) { // JPush注册的id
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            if(null != regId) {
                SharedPrefHelper.getInstance(context).saveString(SharedPrefConfig.KEY_JPUSH_REGISTRATION_ID, regId);
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
            // 用户点击打开了通知
            if(BoCoolManager.getInstance(context).isTaskFront(context.getPackageName())) {
                // 当前程序在前端
                JPushObserverManager.getInstance().notifyNotificationOpened(bundle);
            } else {
                openDefaultActivity(context, bundle);
            }
        }
    }

    /**
     * 打开默认的程序入口Activity
     * @param context
     * @param bundle
     */
    private static void openDefaultActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SplashActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            }
            else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
