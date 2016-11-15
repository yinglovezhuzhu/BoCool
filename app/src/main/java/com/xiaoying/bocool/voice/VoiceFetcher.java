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

package com.xiaoying.bocool.voice;

import android.content.Context;
import android.util.Log;

import com.opensource.bitmaploader.DiskLruCache;
import com.opensource.bitmaploader.FileUtil;
import com.opensource.bitmaploader.ImageWorker;
import com.opensource.bitmaploader.LoadListener;
import com.opensource.bitmaploader.Utils;
import com.xiaoying.bocool.task.CustomAsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 声音文件加载器
 * Created by yinglovezhuzhu@gmail.com on 2015/6/12.
 */
public class VoiceFetcher {

    private static final String TAG = "VoiceFetcher";

    public static final String HTTP_CACHE_DIR = "http";

    private static final int DEFAULT_BUFF_SIZE = 1024 * 8; //8KB

    private static final int HTTP_CACHE_SIZE = 20 * 1024 * 1024; // 20MB

    private static final int DEFAULT_HTTP_CACHE_ITEM_SIZE = 128;

    private static VoiceCache mVoiceCache;
    private boolean mExitTasksEarly = false;
    private Context mContext;

    public VoiceFetcher(Context context) {
        this.mContext = context;
    }

    public void loadVoice(String data, FetchVoiceListener l) {
        new VoiceFetchTask(l).execute(data);
    }

    public void setVoiceCache(VoiceCache voiceCache) {
        this.mVoiceCache = voiceCache;
    }

    public VoiceCache getVoiceCache() {
        return mVoiceCache;
    }

    public void setExitTasksEarly(boolean exitTasksEarly) {
        mExitTasksEarly = exitTasksEarly;
    }


    /**
     * 加载音频文件的异步线程类
     */
    private class VoiceFetchTask extends CustomAsyncTask<String, Void, File> {

        private String mmData;
        private FetchVoiceListener mmFetchListner;

        public VoiceFetchTask(FetchVoiceListener l) {
            this.mmFetchListner = l;
        }

        @Override
        protected File doInBackground(String... objects) {
            mmData = objects[0];
            File cacheFile = null;

            if(null != mVoiceCache && !isCancelled() && !mExitTasksEarly) {
                cacheFile = mVoiceCache.getVoiceFileFromDiskCache(mmData);
            }

            if(null == cacheFile && !isCancelled() && !mExitTasksEarly) {
                cacheFile = downloadFile(mContext, mmData, mmFetchListner);
            }

            if(null != cacheFile) {
                // 获取缓存文件,如果缓存成功,返回缓存文件地址,如果失败,返回下载的缓存地址
                cacheFile = mVoiceCache.addVoiceFileToDiskCache(mmData, cacheFile);
            }

            return cacheFile;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if(null != file && file.exists()) {
                mmFetchListner.onFinished(mmData, file);
            } else {
                mmFetchListner.onError(mmData, FetchVoiceListener.CODE_FILE_ERROR,
                        "File dose not exists");
            }
        }
    }

    /**
     * Download a voice file from a URL, write it to a disk and return the File pointer. This
     * implementation uses a simple disk cache.
     *
     * @param context   The context to use
     * @param urlString The URL to fetch
     * @return A File pointing to the fetched bitmap
     */
    private File downloadFile(Context context, String urlString, FetchVoiceListener l) {

        final File cacheDir = DiskLruCache.getDiskCacheDir(context, mVoiceCache == null ?
                null : mVoiceCache.getCacheParams().cachePath, HTTP_CACHE_DIR);

        final DiskLruCache cache = DiskLruCache.openCache(context, cacheDir,
                null == mVoiceCache ? HTTP_CACHE_SIZE : mVoiceCache.getCacheParams().httpCacheSize);

        cache.setMaxCacheItemSize(null == mVoiceCache ? DEFAULT_HTTP_CACHE_ITEM_SIZE : mVoiceCache.getCacheParams().httpCacheItemSize);

        final String cacheFilename = cache.createFilePath(urlString);

        if(null == cacheFilename) {
            Log.e(TAG, "downloadVoice - create cache file path failed");
            l.onError(urlString, FetchVoiceListener.CODE_FILE_ERROR, "Create cache file path error");
            return null;
        }

        final File cacheFile = new File(cacheFilename);

        if (cache.containsKey(urlString)) {
            if (ImageWorker.DEBUG) {
                Log.d(TAG, "downloadVoice - found in http cache - " + urlString);
            }
            return cacheFile;
        }

        if (ImageWorker.DEBUG) {
            Log.d(TAG, "downloadVoice - downloading - " + urlString);
        }

        Utils.disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setRequestProperty("Referer", urlString);
            // 设置用户代理
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; "
                    + "MSIE 8.0; Windows NT 5.2;"
                    + " Trident/4.0; .NET CLR 1.1.4322;"
                    + ".NET CLR 2.0.50727; " + ".NET CLR 3.0.04506.30;"
                    + " .NET CLR 3.0.4506.2152; " + ".NET CLR 3.5.30729)");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                final InputStream in =
                        new BufferedInputStream(urlConnection.getInputStream(), Utils.IO_BUFFER_SIZE);
                out = new BufferedOutputStream(new FileOutputStream(cacheFile), Utils.IO_BUFFER_SIZE);

                int b;
                long total = urlConnection.getContentLength();
                if (total < 0) {
                    urlConnection.connect();
                    total = urlConnection.getContentLength();
                }
                long downloaded = 0;
                if (total < 0) {
                    byte[] buff = new byte[DEFAULT_BUFF_SIZE];
                    int count = 0;
                    while ((count = in.read(buff)) != -1) {
                        out.write(buff, 0, count);
                        downloaded += count;
                        if (l != null) {
                            l.onProgress(urlString, total, downloaded);
                        }
                    }
                } else {
                    long size = total / 100;
                    if (size > 1) {
                        byte[] buff = new byte[(int) size];
                        int count = 0;
                        while ((count = in.read(buff)) != -1) {
                            out.write(buff, 0, count);
                            downloaded += count;
                            if (l != null) {
                                l.onProgress(urlString, total, downloaded);
                            }
                        }
                    } else {
                        while ((b = in.read()) != -1) {
                            out.write(b);
                            downloaded++;
                            if (l != null) {
                                l.onProgress(urlString, total, downloaded);
                            }
                        }
                    }
                }
                return cacheFile;
            } else {
                return null;
            }
        } catch (final IOException e) {
            Log.e(TAG, "Error in downloadVoice - " + e);
            FileUtil.deleteFile(cacheFile); //delete file if download failed
            if (l != null) {
                l.onError(urlString, FetchVoiceListener.CODE_DOWNLOAD_ERROR, e.toString());
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error in downloadVoice - " + e);
                    if (l != null) {
                        l.onError(urlString, FetchVoiceListener.CODE_DOWNLOAD_ERROR, e.toString());
                    }
                }
            }
        }

        return null;
    }
}
