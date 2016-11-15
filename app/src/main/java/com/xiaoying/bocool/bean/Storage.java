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

import android.os.Parcel;
import android.os.Parcelable;

import com.xiaoying.bocool.config.Config;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 知识库列表数据表实体类
 * Created by yinglovezhuzhu@gmail.com on 2015/6/4.
 */
public class Storage extends BmobObject {

    /** 在线 **/
    public static final int STATUS_ONLINE = 1;

    /** 离线 **/
    public static final int STATUS_OUTLINE = 2;

    /** 测试 **/
    public static final int STATUS_TEST = 3;

    private String title;
    private String content;
    private String description;
    private BmobFile thumb;
    private Boolean isNew;
    private Integer sort;
    private Integer version;
    private BmobFile outline;
    private Integer status = STATUS_ONLINE;
    private BmobRelation likes;

    public Storage() {
    }

    public Storage(String tableName) {
        super(tableName);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BmobFile getThumb() {
        return thumb;
    }

    public void setThumb(BmobFile thumb) {
        this.thumb = thumb;
    }

    public Boolean isNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BmobFile getOutline() {
        return outline;
    }

    public void setOutline(BmobFile outline) {
        this.outline = outline;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", thumb=" + thumb +
                ", isNew=" + isNew +
                ", sort=" + sort +
                ", version=" + version +
                ", outline=" + outline +
                ", status=" + status +
                ", likes=" + likes +
                '}';
    }

//    private Storage(Parcel source) {
//        setObjectId(source.readString());
//        setCreatedAt(source.readString());
//        setUpdatedAt(source.readString());
//        setTableName(source.readString());
//        setACL((BmobACL) source.readSerializable());
//        this.title = source.readString();
//        this.content = source.readString();
//        this.description = source.readString();
//        this.thumb = (BmobFile) source.readSerializable();
//        this.isNew = source.readInt() == 1;
//        this.sort = source.readInt();
//        this.version = source.readInt();
//        this.outline = (BmobFile) source.readSerializable();
//        this.status = source.readInt();
//        this.likes = (BmobRelation) source.readSerializable();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(getObjectId());
//        dest.writeString(getCreatedAt());
//        dest.writeString(getUpdatedAt());
//        dest.writeString(getTableName());
//        dest.writeSerializable(getACL());
//        dest.writeString(this.title);
//        dest.writeString(this.content);
//        dest.writeString(this.description);
//        dest.writeSerializable(this.thumb);
//        dest.writeInt(this.isNew ? 1 : 0);
//        dest.writeInt(this.sort);
//        dest.writeInt(this.version);
//        dest.writeSerializable(this.outline);
//        dest.writeInt(this.status);
//        dest.writeSerializable(this.likes);
//    }
//
//    public static final Parcelable.Creator<Storage> CREATOR = new Parcelable.Creator<Storage>() {
//
//        @Override
//        public Storage createFromParcel(Parcel parcel) {
//            return null;
//        }
//
//        @Override
//        public Storage[] newArray(int i) {
//            return new Storage[i];
//        }
//    };
}
