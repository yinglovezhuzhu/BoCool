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
package com.xiaoying.bocool.config;

/**
 * 配置文件的常量变量配置
 * Create by yinglovezhuzhu@gmail.com in 2014年12月20日
 */
public class SharedPrefConfig {

	private SharedPrefConfig() {}
	
	/**
	 * 是否第一次使用、卸载后重新安装、应用数据被清除（清除数据而不是清除缓存）
	 */
	public static final String KEY_FIRST_USE = "first_use";
	
	/**
	 * 自动检查更新
	 */
	public static final String KEY_AUTO_CHECK_UPDATE = "auto_check_update";
	
	/**
	 * 在WIFI下自动下载APK
	 */
	public static final String KEY_AUTO_DOWNLOAD_APK_WIFI = "auto_download_api_wifi";
	
	/**
	 * 自动播放声音（如果有声音文件）
	 */
	public static final String KEY_AUTO_PLAY_VOICE = "auto_play_voice";

	/**
	 * 接收推送通知
	 */
	public static final String KEY_RECEIVE_PUSH_MESSAGE = "receive_push_message";

    /**
     * 自动播放时循环播放
     */
    public static final String KEY_AUTO_PLAY_RECYCLE = "auto_play_recycle";

    /**
     * 自动播放延时，整型
     */
    public static final String KEY_AUTO_PLAY_DELAY = "auto_play_delay";

	/**
	 * 百度语音生成器授权文件的路径
	 */
	public static final String KEY_TTS_LICENSE_PATH = "tts_license_path";

	/**
	 * JPush的设备注册id
	 */
	public static final String KEY_JPUSH_REGISTRATION_ID = "jpush_registration_id";

	public static final String KEY_TTS_SPEED = "tts_speed";

	public static final String KEY_TTS_VOLUME = "tts_volume";

    public static final String KEY_TTS_SPEAKER = "tts_speaker";
}
