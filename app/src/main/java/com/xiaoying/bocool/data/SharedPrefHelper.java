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
package com.xiaoying.bocool.data;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 配置文件工具类
 * Create by yinglovezhuzhu@gmail.com in 2014年12月20日
 */
public class SharedPrefHelper {
	
	/** 配置文件名 **/
	private static final String SHARE_PRE_FILE_NAME = "bocoll_pref";
	
	private SharedPreferences mSharePref;
	
	private static SharedPrefHelper mInstance = null;
	
	private SharedPrefHelper(Context context) {
		mSharePref = context.getSharedPreferences(SHARE_PRE_FILE_NAME, Context.MODE_APPEND);
	}
	
	/**
	 * 获取但实例对象，创建单实例对象的时候会初始化的配置文件
	 * @param context
	 * @return
	 */
	public static synchronized SharedPrefHelper getInstance(Context context) {
		if (mInstance == null) {
			synchronized (SharedPrefHelper.class) {
				if (mInstance == null) {
					mInstance = new SharedPrefHelper(context);
				}
			}
		}
		return mInstance;
	}
	
	
	/**
	 * 清除配置文件的所有数据
	 * 
	 */
	public boolean clearAll() {
		return mSharePref.edit().clear().commit();
	}
	
	/**
	 * 保存一个字符数据到配置文件中
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveString(String key, String value) {
		return mSharePref.edit().putString(key, value).commit();
	}
	
	
	/**
	 * 保存一个整型数据到配置文件中
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveInt(String key, int value) {
		return mSharePref.edit().putInt(key, value).commit();
	}
	
	/**
	 * 保存一个浮点型数据到配置文件中
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveFloat(String key, float value) {
		return mSharePref.edit().putFloat(key, value).commit();
	}
	
	/**
	 * 保存一个长整型数据到配置文件中
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveLong(String key, long value) {
		return mSharePref.edit().putLong(key, value).commit();
	}
	
	
	/**
	 * 保存一个布尔型数据到配置文件中
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveBoolean(String key, boolean value) {
		return mSharePref.edit().putBoolean(key, value).commit();
	}
	
	/**
	 * 保存一个字符Set数据到配置文件中
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveStringSet(String key, Set<String> values) {
		return mSharePref.edit().putStringSet(key, values).commit();
	}
	
	
	/**
	 * 从配置文件中删除某个字段的数据
	 * @param key
	 * @return
	 */
	public boolean remove(String key) {
		return mSharePref.edit().remove(key).commit();
	}
	
	
	/**
	 * 配置文件中是否包含某个字段数据
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		return mSharePref.contains(key);
	}
	
	/**
	 * 从配置文件中读取一个字符数据
	 * @param key
	 * @param defValue
	 * @return
	 */
	public String getString(String key, String defValue) {
		return mSharePref.getString(key, defValue);
	}
	
	/**
	 * 从配置文件中读取一个整型数据
	 * @param key
	 * @param defValue
	 * @return
	 */
	public int getInt(String key, int defValue) {
		return mSharePref.getInt(key, defValue);
	}
	
	
	/**
	 * 从配置文件中读取一个浮点型数据
	 * @param key
	 * @param defValue
	 * @return
	 */
	public float getFloat(String key, float defValue) {
		return mSharePref.getFloat(key, defValue);
	}
	
	
	/**
	 * 从配置文件中读取一个长整型数据
	 * @param key
	 * @param defValue
	 * @return
	 */
	public long getLong(String key, long defValue) {
		return mSharePref.getLong(key, defValue);
	}
	
	
	/**
	 * 从配置文件中读取一个布尔型数据
	 * @param key
	 * @param defValue
	 * @return
	 */
	public boolean getBoolean(String key, boolean defValue) {
		return mSharePref.getBoolean(key, defValue);
	}
	
	
	/**
	 * 从配置文件中读取一个字符Set数据
	 * @param key
	 * @param defValue
	 * @return
	 */
	public Set<String> getStringSet(String key, Set<String> defValue) {
		return mSharePref.getStringSet(key, defValue);
	}
}
