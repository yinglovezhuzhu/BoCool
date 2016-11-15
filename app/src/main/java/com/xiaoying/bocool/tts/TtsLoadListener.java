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

package com.xiaoying.bocool.tts;

import java.io.File;

/**
 * 声音文件加载监听
 * Created by yinglovezhuzhu@gmail.com on 2015/6/13.
 */
public interface TtsLoadListener {

    /** 成功 **/
    public static final int CODE_SUCCESS = 0x00000001;

    /** 文件读写出现错误 **/
    public static final int CODE_FILE_ERROR = 0x00000003;


    public void onStart(String text);

    public void onProgress(String text, long total, long downloaded);

    public void onError(String text, int code, String msg);

    public void onCanceled(String text);

    public void onFinished(String text, byte [] data);
}
