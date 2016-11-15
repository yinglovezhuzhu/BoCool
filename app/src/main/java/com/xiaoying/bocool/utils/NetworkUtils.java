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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Use：网络相关工具类
 * 
 * @author yinglovezhuzhu@gmail.com
 */
public class NetworkUtils {
	
	private NetworkUtils() {}
	
	/**
	 * 网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAwailable(Context context) {
		if(context == null) {
			return false;
		}
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info == null) {
			return false;
		}
		return info.isAvailable();
	}
	
	/**
	 * 网络是否已经连接
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if(context == null) {
			return false;
		}
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info == null) {
			return false;
		}
		return info.isConnected();
	}
	
	/**
	 * WIFI网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isWifiAwailable(Context context) {
		if(context == null) {
			return false;
		}
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(info == null) {
			return false;
		}
		return info.isAvailable();
	}
	
	
	/**
	 * WIFI网络是否已连接
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if(context == null) {
			return false;
		}
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(info == null) {
			return false;
		}
		return info.isConnected();
	}
	
	/**
	 * 获取网络连接类型
	 * @param context
	 * @return
	 */
	public static int getNetworkType(Context context) {
		if(context == null) {
			return -1;
		}
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(info == null) {
			return -1;
		}
		return info.getType();
	}
}
