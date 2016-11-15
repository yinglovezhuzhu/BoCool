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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xiaoying.bocool.observer.NetworkObserverManager;

/**
 * 网络状态广播接收器
 * Created by yinglovezhuzhu@gmail.com on 2015/6/11.
 */
public class NetworkReceiver extends BroadcastReceiver {

    private ConnectivityManager mConnectivityManager = null;

    private NetworkInfo mCurrentNetwork = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(null == intent) {
            return;
        }
        String action = intent.getAction();
        if(!ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            return;
        }

        NetworkInfo lastNetwork = mCurrentNetwork;
        boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if(noConnectivity) {
            // 没有网络连接，直接返回
            mCurrentNetwork = null;
            NetworkObserverManager.getInstance().notifyNetworkChaged(noConnectivity, null, lastNetwork);
            return;
        }

        if(null == mConnectivityManager) {
            mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        mCurrentNetwork = mConnectivityManager.getActiveNetworkInfo();
        NetworkObserverManager.getInstance().notifyNetworkChaged(noConnectivity, mCurrentNetwork, lastNetwork);
    }
}
