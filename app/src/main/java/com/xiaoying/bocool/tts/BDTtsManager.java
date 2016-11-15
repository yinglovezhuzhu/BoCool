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
import android.os.Bundle;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.DataInfoUtils;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.baidu.speechsynthesizer.publicutility.SpeechPlayer;
import com.baidu.speechsynthesizer.publicutility.SpeechPlayerListener;
import com.xiaoying.bocool.config.Config;
import com.xiaoying.bocool.config.SharedPrefConfig;
import com.xiaoying.bocool.data.SharedPrefHelper;
import com.xiaoying.bocool.utils.Utils;

/**
 * 百度TTS语音合成管理工具
 * Created by yinglovezhuzhu@gmail.com on 2015/6/15.
 */
public class BDTtsManager {

    private static final String DEFAULT_TTS_SPEED = "3";

    private static final String DEFAULT_TTS_VOLUME = "9";

    // 语音合成对象
    private SpeechSynthesizer mSpeechSynthesizer;
    private SpeechPlayer mSpeechPlayer;
    private String mTtsLicensePath;
    private SpeechSynthesizerListener mSpeechSynthesizerListener;
    private SpeechPlayerListener mSpeechPlayerListener;

    private boolean mInitialized = false;

    private boolean mSynthesizerWorking = false;

    private String mText;

    private static BDTtsManager mInstance = null;

    private BDTtsManager() {
    }

    public static BDTtsManager getInstance() {
        if(null == mInstance) {
            mInstance = new BDTtsManager();
        }
        return mInstance;
    }

    /**
     * 设置语音合成监听
     * @param listener
     */
    public void setSpeechSynthesizerListener(SpeechSynthesizerListener listener) {
        this.mSpeechSynthesizerListener = listener;
    }

    /**
     * 设置播放器监听
     * @param listener
     */
    public void setSpeechPlayerListener(SpeechPlayerListener listener) {
        this.mSpeechPlayerListener = listener;
    }

    /**
     * 在Activity的onPause中使用
     */
    public void onPause() {
        if(null != mSpeechPlayer) {
            mSpeechPlayer.stop();
        }
        cancelSpeechSynthesizer();
    }

    public void onDestroy() {
        this.mSpeechPlayerListener = null;
        this.mSpeechSynthesizerListener = null;
        mSynthesizerWorking = false;
    }

    /**
     * 在程序退出的时候使用
     */
    public void releaseBDTts() {
        if(null != mSpeechPlayer) {
            mSpeechPlayer.releaseMediaPlayer();
        }
        if(null != mSpeechSynthesizer) {
            mSpeechSynthesizer.releaseSynthesizer();
        }
        mInitialized = false;
        mSynthesizerWorking = false;
    }

    /**
     * 合成语音，但是不播放
     * @param text
     */
    public void synthesize(String text) {
        if(null == mSpeechSynthesizer && null != mSpeechSynthesizerListener) {
            mSpeechSynthesizerListener.onError(null,
                    new SpeechError(SpeechSynthesizer.SYNTHESIZER_ERROR_UNINITIALIZED_ERROR));
            return;
        }
        mText = text;
        mSpeechSynthesizer.synthesize(text);
    }

    /**
     *  重新合成上一次的文本（如果合成失败调用，合成成功后上一次文本会被清空）
     */
    public void reSynthesize() {
        if(null == mText) {
            return;
        }
        synthesize(mText);
    }

    /**
     * 合成语音并且播放
     * @param text
     */
    public void speak(String text) {
        if(null == mSpeechSynthesizer && null != mSpeechSynthesizerListener) {
            mSpeechSynthesizerListener.onError(null,
                    new SpeechError(SpeechSynthesizer.SYNTHESIZER_ENGINE_NOT_INITIALIZED));
            return;
        }
        mText = text;
        mSpeechSynthesizer.speak(text);
    }

    /**
     * 取消合成和播放
     */
    public void cancelSpeechSynthesizer() {
        if(mSynthesizerWorking) {
            if(null != mSpeechSynthesizer) {
                mSpeechSynthesizer.cancel();
            }
        }
    }

    /**
     * 播放声音字节
     * @param audioData
     */
    public void playAudioData(byte [] audioData) {
        if(null == mSpeechPlayer && null != mSpeechPlayerListener) {
            mSpeechPlayerListener.onError(null,
                    new SpeechError(SpeechSynthesizer.PLAYER_STATE_NOT_INIT));
            return;
        }
        mSpeechPlayer.playAudioData(audioData);
    }

    /**
     * 停止播放
     */
    public void stopPlayer() {
        if(null == mSpeechPlayer) {
            return;
        }
        mSpeechPlayer.stop();
    }


    /**
     * 初始化百度TTS
     */
    public void initBDTts(Context context, boolean forceReInit) {

        if(mInitialized && !forceReInit) {
            return;
        }
        mTtsLicensePath = SharedPrefHelper.getInstance(context).getString(SharedPrefConfig.KEY_TTS_LICENSE_PATH, "");
        mSpeechSynthesizer = SpeechSynthesizer.newInstance(SpeechSynthesizer.SYNTHESIZER_AUTO, context,
                "Bocool", mLocalSpeechSynthesizerListener);
        Bundle bundle = Utils.getMetaDatas(context);
        if(bundle == null) {
            return;
        }
        String apiKey = bundle.getString(Config.META_KEY_BAIDU_API_KEY);
        String scretKey = bundle.getString(Config.META_KEY_BAIDU_SECRET_KEY);
//        mSpeechSynthesizer.setApiKey("zxaXwAndtgouwLqLqNO3OoEr", "6zdc0nsBIn2msn4zQLhaGQqrd2oFK6M4");
        mSpeechSynthesizer.setApiKey(apiKey, scretKey);

        // 设置授权文件路径
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mTtsLicensePath);
        // TTS所需的资源文件，可以放在任意可读目录，可以任意改名
        String ttsTextModelFilePath = context.getApplicationInfo().dataDir + "/lib/libbd_etts_text.dat.so";
        String ttsSpeechModelFilePath = context.getApplicationInfo().dataDir + "/lib/libbd_etts_speech_female.dat.so";
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, ttsTextModelFilePath);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, ttsSpeechModelFilePath);
        String speed = SharedPrefHelper.getInstance(context).getString(
                SharedPrefConfig.KEY_TTS_SPEED, DEFAULT_TTS_SPEED);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, speed);
        String volume = SharedPrefHelper.getInstance(context).getString(
                SharedPrefConfig.KEY_TTS_VOLUME, DEFAULT_TTS_VOLUME);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, volume);
        String speaker = SharedPrefHelper.getInstance(context).getString(
                SharedPrefConfig.KEY_TTS_SPEAKER, SpeechSynthesizer.SPEAKER_FEMALE);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, speaker);
        DataInfoUtils.verifyDataFile(ttsTextModelFilePath);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_DATE);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_SPEAKER);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_GENDER);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_CATEGORY);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_LANGUAGE);
        mSpeechSynthesizer.initEngine(forceReInit);

        mSpeechPlayer = new SpeechPlayer(context, mLocalSpeechPlayerListener);
        mInitialized = true;
    }

    /**
     * 单独初始化SpeechPlayer
     * @param context
     */
    public void initSpeechPlayer(Context context) {
        mSpeechPlayer = new SpeechPlayer(context, mLocalSpeechPlayerListener);
    }

    /**
     * 获取当前正在合成的文本，合成错误时才保留，成功会在回调执行完后置为null
     * @return
     */
    public String getCurrentVoiceText() {
        return mText;
    }

    /**
     * 是否正在工作
     * @return
     */
    public boolean isSynthesizerWorking() {
        return mSynthesizerWorking;
    }

    /**
     * 语音合成播放器的播放监听
     */
    private SpeechPlayerListener mLocalSpeechPlayerListener = new SpeechPlayerListener() {
        @Override
        public void onFinished(SpeechPlayer speechPlayer) {
            if (null != mSpeechPlayerListener) {
                mSpeechPlayerListener.onFinished(speechPlayer);
            }
        }

        @Override
        public void onError(SpeechPlayer speechPlayer, SpeechError speechError) {
            if(null != mSpeechPlayerListener) {
                mSpeechPlayerListener.onError(speechPlayer, speechError);
            }
        }
    };

    private SpeechSynthesizerListener mLocalSpeechSynthesizerListener = new SpeechSynthesizerListener() {
        @Override
        public void onStartWorking(SpeechSynthesizer speechSynthesizer) {
            mSynthesizerWorking = true;
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onStartWorking(speechSynthesizer);
            }
        }

        @Override
        public void onSpeechStart(SpeechSynthesizer speechSynthesizer) {
            mSynthesizerWorking = true;
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onSpeechStart(speechSynthesizer);
            }
        }

        @Override
        public void onNewDataArrive(SpeechSynthesizer speechSynthesizer, byte[] bytes, boolean b) {
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onNewDataArrive(speechSynthesizer, bytes, b);
            }
        }

        @Override
        public void onBufferProgressChanged(SpeechSynthesizer speechSynthesizer, int i) {
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onBufferProgressChanged(speechSynthesizer, i);
            }
        }

        @Override
        public void onSpeechProgressChanged(SpeechSynthesizer speechSynthesizer, int i) {
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onSpeechProgressChanged(speechSynthesizer, i);
            }
        }

        @Override
        public void onSpeechPause(SpeechSynthesizer speechSynthesizer) {
            mSynthesizerWorking = false;
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onSpeechPause(speechSynthesizer);
            }
        }

        @Override
        public void onSpeechResume(SpeechSynthesizer speechSynthesizer) {
            mSynthesizerWorking = true;
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onSpeechResume(speechSynthesizer);
            }
        }

        @Override
        public void onCancel(SpeechSynthesizer speechSynthesizer) {
            mSynthesizerWorking = false;
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onCancel(speechSynthesizer);
            }
//            mText = null;
        }

        @Override
        public void onSynthesizeFinish(SpeechSynthesizer speechSynthesizer) {
            mSynthesizerWorking = false;
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onSynthesizeFinish(speechSynthesizer);
            }
//            mText = null;
        }

        @Override
        public void onSpeechFinish(SpeechSynthesizer speechSynthesizer) {
            mSynthesizerWorking = false;
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onSpeechFinish(speechSynthesizer);
            }
//            mText = null;
        }

        @Override
        public void onError(SpeechSynthesizer speechSynthesizer, SpeechError speechError) {
            mSynthesizerWorking = false;
            if(null != mSpeechSynthesizerListener) {
                mSpeechSynthesizerListener.onError(speechSynthesizer, speechError);
            }
        }
    };

}
