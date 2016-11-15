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

/*
 * 文件名：StringUtil.java
 * 版权：<版权>
 * 描述：<描述>
 * 创建人：xiaoying
 * 创建时间：2013-6-9
 * 修改人：xiaoying
 * 修改时间：2013-6-9
 * 版本：v1.0
 */

package com.xiaoying.bocool.utils;

/**
 * 功能：
 * @author xiaoying
 *
 */
public class StringUtils {
	
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}
	
	public static boolean isEquals(String str1, String str2) {
		if(str1 == null || str2 == null) {
			return false;
		}
		return str1.equals(str2);
	}
	
	/**
	 * 删除所有在startStr和endStr之间的字符串，包括startStr和endStr,即删除闭区间［startStr，endStr］
	 * @param sb
	 * @param startStr
	 * @param endStr
	 */
	public static void deleteAllIn(StringBuilder sb, String startStr, String endStr) {
		int startIndex = 0;
		int endIndex = 0;
		while((startIndex = sb.indexOf(startStr)) >= 0 && (endIndex = sb.indexOf(endStr)) >= 0) {
			sb.delete(startIndex, endIndex + endStr.length());
		}
	}
	
	/**
	 * 根据相对／绝对路径获取文件名
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		return path.substring(path.lastIndexOf("/") + 1, path.length());
	}
	
	/**
	 * 获取字符串两个字符串之间的字符（第一个）
	 * @param source
	 * @param start
	 * @param end
	 * @return
	 */
	public static String getStringIn(String source, String start, String end) {
		return source.substring(source.indexOf(start) + start.length(), source.indexOf(end));
	}
}
