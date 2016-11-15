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

import java.io.File;

/**
 * 声音文件加载监听
 * Created by yinglovezhuzhu@gmail.com on 2015/6/13.
 */
public abstract class SimpleFetchVoiceListener implements FetchVoiceListener {

    public void onStart(String url) {

    }

    public void onProgress(String url, long total, long downloaded) {

    }

    public void onError(String url, int code, String msg) {

    }

    public void onCanceled(String url) {

    }

    public abstract void onFinished(String url, File file);
}
