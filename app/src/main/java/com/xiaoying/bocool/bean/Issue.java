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
package com.xiaoying.bocool.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;

/**
 * 专辑数据实体类
 * Create by yinglovezhuzhu@gmail.com in 2015年04月20日
 */
public class Issue extends BmobObject {

	private BmobPointer storage;
	private String title;
	private String content;
	private BmobFile thumb;
	private BmobFile img;
	private BmobFile voice;
	private Integer voiceTime = 0;
	private String voiceText;
	private String extra;
	private String extraA;
	private String extraB;
	private Integer sort;
    private Integer status = Storage.STATUS_ONLINE;

	public BmobPointer getStorage() {
		return storage;
	}

	public void setStorage(BmobPointer storage) {
		this.storage = storage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BmobFile getThumb() {
		return thumb;
	}

	public void setThumb(BmobFile thumb) {
		this.thumb = thumb;
	}

	public BmobFile getImg() {
		return img;
	}

	public void setImg(BmobFile img) {
		this.img = img;
	}

	public BmobFile getVoice() {
		return voice;
	}

	public void setVoice(BmobFile voice) {
		this.voice = voice;
	}

	public Integer getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(Integer voiceTime) {
		this.voiceTime = voiceTime;
	}

	public String getVoiceText() {
		return voiceText;
	}

	public void setVoiceText(String voiceText) {
		this.voiceText = voiceText;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getExtraA() {
		return extraA;
	}

	public void setExtraA(String extraA) {
		this.extraA = extraA;
	}

	public String getExtraB() {
		return extraB;
	}

	public void setExtraB(String extraB) {
		this.extraB = extraB;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	@Override
	public String toString() {
		return "Issue{" +
				"storage=" + storage +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", thumb=" + thumb +
				", img=" + img +
				", voice=" + voice +
				", voiceTime=" + voiceTime +
				", voiceText='" + voiceText + '\'' +
				", extra='" + extra + '\'' +
				", extraA='" + extraA + '\'' +
				", extraB='" + extraB + '\'' +
				", sort=" + sort +
				", status=" + status +
				'}';
	}
}
