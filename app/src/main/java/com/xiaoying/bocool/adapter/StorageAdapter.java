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
package com.xiaoying.bocool.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;

import com.opensource.bitmaploader.ImageFetcher;
import com.xiaoying.bocool.R;
import com.xiaoying.bocool.adapter.base.BaseGridAdapter;
import com.xiaoying.bocool.bean.Issue;
import com.xiaoying.bocool.bean.Storage;
import com.xiaoying.bocool.utils.StringUtils;
import com.xiaoying.bocool.widget.IssueItemView;

import cn.bmob.v3.datatype.BmobFile;

/**
 * 专辑列表适配器
 * 
 * Create by yinglovezhuzhu@gmail.com in 2015年2月16日
 */
public class StorageAdapter extends BaseGridAdapter {

	private final Context mContext;
	
	private final List<Storage> mDatas = new ArrayList<Storage>();

	private final int mColumnWidth;
	private final int mColumnHeight;
	
	private final ImageFetcher mImageFetcher;
	
	public StorageAdapter(Context context, int columnWidth, int columnHeight, ImageFetcher imageFetcher) {
		this.mContext = context;
		this.mColumnWidth = columnWidth;
		this.mColumnHeight = columnHeight;
		this.mImageFetcher = imageFetcher;
	}
	
	public void addAll(Collection<Storage> datas) {
		if(null == datas || datas.isEmpty()) {
			return;
		}
		mDatas.addAll(datas);
		notifyDataSetChanged();
	}

    public void add(Storage s) {
        mDatas.add(s);
    }

	public void clear(boolean notifyDataSetChanged) {
		mDatas.clear();
		if(notifyDataSetChanged) {
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Storage getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IssueItemView itemView = null;
		if(null == convertView) {
			itemView = new IssueItemView(mContext);
			AbsListView.LayoutParams lp = (LayoutParams) itemView.getLayoutParams();
			if(null == lp) {
				lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mColumnHeight);
			} else {
                lp.width = mColumnWidth;
				lp.height = mColumnHeight;
			}
			itemView.setLayoutParams(lp);
			convertView = itemView;
		} else {
			itemView = (IssueItemView) convertView;
		}
		Storage storage = getItem(position);
		itemView.setNew(storage.isNew());
		itemView.setNameText(storage.getTitle());
		if(null == mImageFetcher) {
			
		} else {
			switch (storage.getStatus()) {
				case Storage.STATUS_ONLINE:
				case Storage.STATUS_TEST:
					BmobFile bFile = storage.getThumb();
					if(null != bFile && !StringUtils.isEmpty(bFile.getUrl())) {
                        String url = bFile.getFileUrl(mContext);
						if(!StringUtils.isEmpty(url)) {
							mImageFetcher.loadImage(url, itemView.getImageView());
						}
					}
					break;
				case Storage.STATUS_OUTLINE:
					break;
				default:
					itemView.getImageView().setImageResource(R.drawable.default_image);
					break;
			}
		}
		return convertView;
	}

}
