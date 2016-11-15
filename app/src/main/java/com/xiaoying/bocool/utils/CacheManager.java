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

import android.content.Context;
import android.graphics.Bitmap;

import com.opensource.bitmaploader.ImageCache;
import com.opensource.bitmaploader.ImageCache.ImageCacheParams;
import com.opensource.bitmaploader.Utils;
import com.xiaoying.bocool.tts.TtsCache;
import com.xiaoying.bocool.tts.TtsCache.TtsCacheParams;
import com.xiaoying.bocool.voice.VoiceCache;
import com.xiaoying.bocool.voice.VoiceCache.VoiceCacheParams;

import java.io.File;

/**
 * Usage 缓存管理工具类
 * 
 * @author yinglovezhuzhu@gmail.com
 */
public class CacheManager {
	
	private ImageCacheParams mImageCacheParams = null;

    private VoiceCacheParams mVoiceCacheParams = null;

    private TtsCacheParams mTtsCacheParams = null;
	
	private static CacheManager mInstance = null;

	private CacheManager() {}
	
	public static CacheManager getInstance() {
		if(null == mInstance) {
			mInstance = new CacheManager();
		}
		return mInstance;
	}
	
	public synchronized ImageCache getImageCache(Context context, String uniqueName) {
		return new ImageCache(context, getImageCacheParams(context, uniqueName, true));
	}



    /**
     * 获取图片缓存（平均每张64KB大小）
     * @param context
     * @param cachePath
     * @param uniqueName
     * @param diskCacheEnabled
     * @return
     */
	public synchronized ImageCache getImageCache(Context context, File cachePath, String uniqueName, boolean diskCacheEnabled) {
		return new ImageCache(context, getImageCacheParams(context, cachePath, uniqueName, diskCacheEnabled, 1024 * 64));
	}

    /**
     * 获取缩略图缓存（平均每张16KB大小）
     * @param context
     * @param cachePath
     * @param uniqueName
     * @param diskCacheEnabled
     * @return
     */
	public synchronized ImageCache getThumbCache(Context context, File cachePath, String uniqueName,
                                                 boolean diskCacheEnabled) {
		return new ImageCache(context, getImageCacheParams(context, cachePath, uniqueName,
                diskCacheEnabled, 1024 * 16));
	}

    /**
     * 获取声音文件缓存（平均每个音频文件为16KB）
     * @param context
     * @param cachePath
     * @param uniqueName
     * @return
     */
	public synchronized VoiceCache getVoiceCache(Context context, File cachePath, String uniqueName) {
		return new VoiceCache(context, getVoiceCacheParams(context, cachePath, uniqueName, 1024 * 16));
	}

    /**
     * 获取TTS音频合成数据缓存
     * @param context
     * @param cachePath
     * @param uniqueName
     * @return
     */
    public synchronized TtsCache getTtsCache(Context context, File cachePath, String uniqueName) {
        return new TtsCache(context, getTtsCacheParams(context, cachePath, uniqueName, 1024 * 8));
    }

	public synchronized ImageCacheParams getImageCacheParams(Context context, String uniqueName,
                                                             boolean diskCacheEnabled) {
		if(mImageCacheParams == null) {
			mImageCacheParams = new ImageCacheParams(uniqueName);
			mImageCacheParams.memCacheSize = 1024 * 1024 * Utils.getMemoryClass(context) / 3;
		}
		mImageCacheParams.uniqueName = uniqueName;
		mImageCacheParams.diskCacheEnabled = diskCacheEnabled;
		return mImageCacheParams;
	}

    public synchronized ImageCacheParams getImageCacheParams(Context context, File cachePath,
                                                             String uniqueName, boolean diskCacheEnabled,
                                                             int avgItemSize) {
        if (mImageCacheParams == null) {
            mImageCacheParams = new ImageCacheParams(uniqueName);
            mImageCacheParams.memCacheSize = 1024 * 1024 * Utils.getMemoryClass(context) / 3;
            mImageCacheParams.httpCacheSize = 1024 * 1024 * 64;
            mImageCacheParams.diskCacheSize = 1024 * 1024 * 128;
            mImageCacheParams.diskCacheItemSize = mImageCacheParams.diskCacheSize / avgItemSize;
        }
        mImageCacheParams.cachePath = cachePath;
        mImageCacheParams.uniqueName = uniqueName;
        mImageCacheParams.diskCacheEnabled = diskCacheEnabled;
        mImageCacheParams.compressFormat = Bitmap.CompressFormat.PNG;
        return mImageCacheParams;
    }

	public synchronized VoiceCacheParams getVoiceCacheParams(Context context, File cachePath,
                                                             String uniqueName, int avgItemSize) {
        if(null == mVoiceCacheParams) {
            mVoiceCacheParams = new VoiceCacheParams(uniqueName);
            mVoiceCacheParams.httpCacheSize = 1024 * 1024 * 64;
            mVoiceCacheParams.diskCacheSize = 1024 * 1024 * 128;
            mVoiceCacheParams.diskCacheItemSize = mImageCacheParams.diskCacheSize / avgItemSize;
        }
        mVoiceCacheParams.cachePath = cachePath;
        mVoiceCacheParams.uniqueName = uniqueName;
        return mVoiceCacheParams;
    }

    public synchronized TtsCacheParams getTtsCacheParams(Context context, File cachePath,
                                                         String uniqueName, int avgItemSize) {
        if(null == mTtsCacheParams) {
            mTtsCacheParams = new TtsCacheParams(uniqueName);
            mTtsCacheParams.diskCacheSize = 1024 * 1024 * 128;
            mTtsCacheParams.diskCacheItemSize = mImageCacheParams.diskCacheSize / avgItemSize;
        }
        mTtsCacheParams.cachePath = cachePath;
        mTtsCacheParams.uniqueName = uniqueName;
        return mTtsCacheParams;
    }
}
