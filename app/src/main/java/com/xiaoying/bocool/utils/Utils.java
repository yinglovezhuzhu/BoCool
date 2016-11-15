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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.xiaoying.bocool.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 通用工具类
 * Create by yinglovezhuzhu@gmail.com in 2015年2月16日
 */
public class Utils {
	
	private Utils() {
		
	}
	
	/**
	 * 计算GridView的列宽
	 * @param displayWidth 屏幕（GridView）的宽度
	 * @param spacingWidth 水平间距
	 * @param inWidth 输入宽度，这个输入宽度只是一个预期水平，根据这个来计算最终的宽度
	 * @return
	 */
	public static int calculateColumnWidth(int displayWidth, int spacingWidth, int inWidth) {
		if(inWidth * 2 > displayWidth - spacingWidth * 2) {
			return (displayWidth - spacingWidth * 2) / 2; // 保证至少两列
		}
		int columnNoSpacing = displayWidth / inWidth;
		return (displayWidth - spacingWidth * columnNoSpacing) / columnNoSpacing;
	}


	/**
	 * 获取一个MetaData的String字符串
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getMetaDataAsString(Context context, String key) {
		String metaData = "";
		try {
			PackageManager pm = context.getPackageManager();
			if(null != pm) {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(),
						PackageManager.GET_META_DATA);
				if(null != applicationInfo) {
					Bundle metaDatas = applicationInfo.metaData;
					if(null != metaDatas && metaDatas.containsKey(key)) {
						metaData = String.valueOf(metaDatas.get(key));
					}
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return metaData;
	}

    /**
     * 获取MetaData的Bundle
     * @param context
     * @return
     */
	public static Bundle getMetaDatas(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            if(null != pm) {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(),
                        PackageManager.GET_META_DATA);
                if(null != applicationInfo) {
                    return applicationInfo.metaData;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
	}

	/**
	 * 从JPush的推送通知内容的Extras中获取推送通知的类型
	 * @param extrasString
	 * @return
	 */
	public static int getNotificationType(String extrasString) {
		if(null == extrasString || extrasString.length() < 2) {
			return Config.TYPE_NOTIFICATION_DEFAULT;
		}
		int type = Config.TYPE_NOTIFICATION_DEFAULT;
		try {
			JSONObject extrasJsonObject = new JSONObject(extrasString);
			type = extrasJsonObject.optInt(Config.FIELD_NAME_NOTIFICATION_TYPE, Config.TYPE_NOTIFICATION_DEFAULT);
		} catch (JSONException e) {
			// do nothing
		}
		return type;
	}

}
