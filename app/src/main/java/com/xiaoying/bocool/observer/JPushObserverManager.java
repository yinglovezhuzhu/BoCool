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

package com.xiaoying.bocool.observer;

import android.net.NetworkInfo;
import android.os.Bundle;

/**
 * JPush观察者管理者
 * Created by yinglovezhuzhu@gmail.com on 2015/6/29.
 */
public class JPushObserverManager {

    private JPushObservable mJPushObservable = null;

    private static JPushObserverManager mInstance = null;

    private JPushObserverManager() {
        mJPushObservable = new JPushObservable();
    }

    public static JPushObserverManager getInstance() {
        if(null == mInstance) {
            mInstance = new JPushObserverManager();
        }
        return mInstance;
    }

    public void registerJPushObserver(JPushObserver observer) {
        synchronized (mJPushObservable) {
            mJPushObservable.registerObserver(observer);
        }
    }

    public void unregisterJPushObserver(JPushObserver observer) {
        synchronized (mJPushObservable) {
            mJPushObservable.unregisterObserver(observer);
        }
    }

    /**
     * 反注册所有的观察者，建议这个只在退出程序时做清理用
     */
    public void unregisterAllObservers() {
        synchronized (mJPushObservable) {
            mJPushObservable.unregisterAll();
        }
    }

    public void notifyNotificationOpened(Bundle bundle) {
        synchronized (mJPushObservable) {
            mJPushObservable.notifyNotificationOpened(bundle);
        }
    }
}
