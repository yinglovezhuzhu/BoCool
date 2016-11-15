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
package com.xiaoying.bocool.utils;

import android.util.Log;

import com.xiaoying.bocool.BuildConfig;

/**
 * 功能：日志打印工具类
 * @author xiaoying
 *
 */
public class LogUtils {
	
	private static boolean PRINT_LOG = BuildConfig.DEBUG;
//	private static boolean PRINT_LOG = true;

	public static void i(String tag, String msg) {
		if(PRINT_LOG) {
			Log.i(tag, msg);
		}
	}
	
	public static void i(String tag, Object msg) {
		if(PRINT_LOG) {
			Log.i(tag, msg.toString());
		}
	}
	
	public static void w(String tag, String msg) {
		if(PRINT_LOG) {
			Log.w(tag, msg);
		}
	}

	public static void w(String tag, Object msg) {
		if(PRINT_LOG) {
			Log.w(tag, msg.toString());
		}
	}
	
	public static void e(String tag, String msg) {
		if(PRINT_LOG) {
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, Object msg) {
		if(PRINT_LOG) {
			Log.e(tag, msg.toString());
		}
	}
	
	public static void d(String tag, String msg) {
		if(PRINT_LOG) {
			Log.d(tag, msg);
		}
	}
	
	public static void d(String tag, Object msg) {
		if(PRINT_LOG) {
			Log.d(tag, msg.toString());
		}
	}
	
	public static void v(String tag, String msg) {
		if(PRINT_LOG) {
			Log.v(tag, msg);
		}
	}
	
	public static void v(String tag, Object msg) {
		if(PRINT_LOG) {
			Log.v(tag, msg.toString());
		}
	}
}
