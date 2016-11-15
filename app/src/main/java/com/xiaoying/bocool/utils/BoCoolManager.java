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

package com.xiaoying.bocool.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * 宝酷公用管理类
 * Created by yinglovezhuzhu@gmail.com on 2015/6/29.
 */
public class BoCoolManager {

    private ActivityManager mActivityManager;

    private static BoCoolManager mInstance = null;

    private BoCoolManager(Context context) {
        mActivityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
    }

    public static BoCoolManager getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new BoCoolManager(context);
        }
        return mInstance;
    }

    /**
     * 判断包名为packageName的task是否在前端
     * @param packageName
     * @return
     */
    public boolean isTaskFront(String packageName) {
        if(null == packageName || packageName.length() < 1) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = mActivityManager.getRunningTasks(1);
        if(runningTaskInfoList.isEmpty()) {
            return false;
        }
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTaskInfoList.get(0);
        if(null == runningTaskInfo) {
            return false;
        }
        ComponentName componentName = runningTaskInfo.topActivity;
        if(null == componentName) {
            return false;
        }
        return packageName.equals(componentName.getPackageName());
    }

    /**
     * 判断当前Activity是否在最前端展示
     * @param activity
     * @return
     */
    public boolean isActivityFront(Activity activity) {
        if(null == activity) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = mActivityManager.getRunningTasks(1);
        if(runningTaskInfoList.isEmpty()) {
            return false;
        }
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTaskInfoList.get(0);
        if(null == runningTaskInfo) {
            return false;
        }
        ComponentName componentName = runningTaskInfo.topActivity;
        if(null == componentName) {
            return false;
        }
        ComponentName activityComponentName = activity.getComponentName();
        return componentName.compareTo(activityComponentName) == 0;
//        if(null == activityComponentName) {
//            return false;
//        }
//        return activityComponentName.getClassName().equals(componentName.getClassName());
    }
}
