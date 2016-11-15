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

package com.xiaoying.bocool.tts;

import android.content.Context;

import com.xiaoying.bocool.utils.MD5Util;


/**
 *
 * Created by yinglovezhuzhu@gmail.com on 2015/6/15.
 */
public class TtsWorker {

    private static final String TAG = "TtsWorker";

    private static TtsCache mTtsCache;
    private Context mContext;

    public TtsWorker(Context context) {
        this.mContext = context;
    }

    public void setTtsCache(TtsCache ttsCache) {
        this.mTtsCache = ttsCache;
    }

    public TtsCache getTtsCache() {
        return mTtsCache;
    }

    public void saveDataToCache(String text, byte [] data) {
        String key = MD5Util.toMD5Hex(text);
        mTtsCache.addDataToDiskCache(key, data);
    }

    public byte [] loadCacheData(String text) {
        String key = MD5Util.toMD5Hex(text);
        return mTtsCache.getDataFromDiskCache(key);
    }

}
