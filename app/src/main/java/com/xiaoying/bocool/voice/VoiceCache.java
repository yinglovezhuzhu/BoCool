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

package com.xiaoying.bocool.voice;

import android.content.Context;
import android.util.Log;

import com.opensource.bitmaploader.DiskLruCache;

import java.io.File;

/**
 * 声音缓存
 * Created by yinglovezhuzhu@gmail.com on 2015/6/12.
 */
public class VoiceCache {

    private static final String TAG = "VoiceCache";

    // Default disk cache size
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

    private static final int DEFAULT_DISK_CACHE_ITEM_SIZE = DEFAULT_DISK_CACHE_SIZE / (20 * 1024);

    private static final int DEFAULT_HTTP_CACHE_SIZE = 64 * 1024 * 1024; //64MB

    private static final int DEFAULT_HTTP_CACHE_ITEM_SIZE = 128;

    private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;

    private VoiceCacheParams mCacheParams;

    private DiskLruCache mDiskCache;

    /**
     * Creating a new VoiceCache object using the specified parameters.
     *
     * @param context     The context to use
     * @param cacheParams The cache parameters to use to initialize the cache
     */
    public VoiceCache(Context context, VoiceCacheParams cacheParams) {
        init(context, cacheParams);
    }

    /**
     * Creating a new ImageCache object using the default parameters.
     *
     * @param context    The context to use
     * @param uniqueName A unique name that will be appended to the cache directory
     */
    public VoiceCache(Context context, String uniqueName) {
        init(context, new VoiceCacheParams(uniqueName));
    }

    /**
     * Initialize the cache, providing all parameters.
     *
     * @param context     The context to use
     * @param cacheParams The cache parameters to initialize the cache
     */
    private void init(Context context, VoiceCacheParams cacheParams) {
        mCacheParams = cacheParams;
        //get a cache floder
        final File diskCacheDir = DiskLruCache.getDiskCacheDir(context, cacheParams.cachePath, cacheParams.uniqueName);

        // Set up disk cache
        mDiskCache = DiskLruCache.openCache(context, diskCacheDir, cacheParams.diskCacheSize);
        if(null != mDiskCache) {
            mDiskCache.setMaxCacheItemSize(cacheParams.diskCacheItemSize);
            if (cacheParams.clearDiskCacheOnStart) {
                mDiskCache.clearCache();
            }
        } else {
            Log.e(TAG, "Can't create DiskCache");
        }
    }

    public File getVoiceFileFromDiskCache(String data) {
        if(null == mDiskCache) {
            return null;
        }
        return mDiskCache.getDiskCacheFile(data);
    }

    public File addVoiceFileToDiskCache(String data, File file) {
        if(null == mDiskCache || null == file || !file.exists()) {
            return null;
        }
        return mDiskCache.put(data, file);
    }

    public VoiceCacheParams getCacheParams() {
        return mCacheParams;
    }

    public void clearCache() {
        mDiskCache.clearCache();
    }

    /**
     * A holder class that contains cache parameters.
     */
    public static class VoiceCacheParams {
        public File cachePath = null;
        public String uniqueName;
        public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
        public int diskCacheItemSize = DEFAULT_DISK_CACHE_ITEM_SIZE;
        public int httpCacheSize = DEFAULT_HTTP_CACHE_SIZE;
        public int httpCacheItemSize = DEFAULT_HTTP_CACHE_ITEM_SIZE;
        public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;

        public VoiceCacheParams(String uniqueName) {
            this.uniqueName = uniqueName;
        }

        public VoiceCacheParams(File cachePath, String uniqueName) {
            this.cachePath = cachePath;
            this.uniqueName = uniqueName;
        }
    }
}
