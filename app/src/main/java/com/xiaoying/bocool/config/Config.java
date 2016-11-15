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
package com.xiaoying.bocool.config;
/**
 * 配置文件,静态变量
 * Create by yinglovezhuzhu@gmail.com in 2015年04月22日
 */
public class Config {

	public static final String APP_FOLDER = "BoCool";
	
	public static final String CACHE_FOLDER = ".cache";
	
	public static final String THUMB_FOLDER = "thumb";
	
	public static final String IMAGE_FOLDER = "image";

    public static final String VOICE_FOLDER = "voice";

    public static final String TTS_FOLDER = "tts";

	public static final String META_KEY_BMOB_APPID = "BMOB_APPID";

    public static final String META_KEY_BAIDU_API_KEY = "BAIDU_TTS_API_KEY";

    public static final String META_KEY_BAIDU_SECRET_KEY = "BAIDU_TTS_SECRET_KEY";


    public static final int GRID_PAGE_ROW = 10;

    public static final int MAX_DATA_COUNT = 200;

    public static final int INVALID_POSITION = -1;
	
	public static final int SUCCESS = 0;
	
	public static final int FAILED = 1;

    /** Bmob RESULT CODE START **/
    /** Application Id为空，请初始化。 **/
    public static final int BMOB_UNINITIALIZED = 9001;

    /** 解析返回数据出错 **/
    public static final int BMOB_PARSE_DATA_EROR = 9002;

    /** 上传文件出错 **/
    public static final int BMOB_UPLOAD_FILE_ERROR = 9003;

    /** 文件上传失败 **/
    public static final int BMOB_UPLOAD_FILE_FAILURE = 9004;

    /** 批量操作只支持最多50条 **/
    public static final int BMOB_TO_MUCH_BATCH = 9005;

    /** objectId为空 **/
    public static final int BMOB_OBJECTID_IS_NULL = 9006;

    /** 文件大小超过10M **/
    public static final int BMOB_FILESIZE_TOO_LARGE = 9007;

    /** 上传文件不存在 **/
    public static final int BMOB_FILE_DOES_NOT_EXIST = 9008;

    /** 没有缓存数据 **/
    public static final int BMOB_NO_CACHE_DATA = 9009;

    /** 网络超时 **/
    public static final int BMOB_NETWORK_TIMEOUT = 9010;

    /** BmobUser类不支持批量操作 **/
    public static final int BMOB_USER_NOT_SUPPORT_BATCH = 9011;

    /** 上下文为空 **/
    public static final int BMOB_CONTEXT_IS_NULL = 9012;

    /** BmobObject（数据表名称）格式不正确 **/
    public static final int BMOB_TABLENAME_NOT_CORRECT = 9013;

    /** 第三方账号授权失败 **/
    public static final int BMOB_THIRD_PART_AUTHORIZATION_FAILURE = 9014;

    /** 其他错误均返回此code **/
    public static final int BMOB_OTHER_ERROR = 9015;

    /** 无网络连接，请检查您的手机网络 **/
    public static final int BMOB_NO_NETWORK = 9016;
    /** Bmob RESULT CODE END **/

	/** 传递数据的key **/
	public static final String EXTRA_DATA = "extra_data";

    /** 传递数据的key--position **/
    public static final String EXTRA_POSITION = "extra_position";


    /** 推送通知类型字段名称 **/
    public static final String FIELD_NAME_NOTIFICATION_TYPE = "type";
    /** 推送通知类型-默认 **/
    public static final int TYPE_NOTIFICATION_DEFAULT = 0;
    /** 推送通知类型-新的内容 **/
    public static final int TYPE_NOTIFICATION_NEW_CONTENT = 1;

}
