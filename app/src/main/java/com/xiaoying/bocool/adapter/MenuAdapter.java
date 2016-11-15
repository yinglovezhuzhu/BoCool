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

/*
 * 文件名：MenuAdapter.java
 * 版权：<版权>
 * 描述：<描述>
 * 创建人：xiaoying
 * 创建时间：2013-5-31
 * 修改人：xiaoying
 * 修改时间：2013-5-31
 * 版本：v1.0
 */

package com.xiaoying.bocool.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.xiaoying.bocool.R;

/**
 * 功能：菜单列表适配器
 * @author xiaoying
 *
 */
public class MenuAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private Resources mResource;
	
	private List<String> mMenuDatas = new ArrayList<String>();
	
	private List<Integer> mMenuIcons = new ArrayList<Integer>();
	
	public MenuAdapter(Context context) {
		this.mContext = context;
		this.mResource = mContext.getResources();
		init();
	}
	
	private void init() {
		mMenuDatas.add(mResource.getString(R.string.storage));
//		mMenuDatas.add(mResource.getString(R.string.favorite));
		mMenuDatas.add(mResource.getString(R.string.setting));
		
		mMenuIcons.add(R.drawable.ic_menu_storage);
//		mMenuIcons.add(R.drawable.ic_menu_favorite);
		mMenuIcons.add(R.drawable.ic_menu_setting);
	}
	

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mMenuDatas.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public String getItem(int position) {
		return mMenuDatas.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RadioButton itemView = null;
		if(convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_slidingmenu, null);
			itemView = (RadioButton) convertView;
			convertView.setTag(itemView);
		} else {
			itemView = (RadioButton) convertView.getTag();
		}
		itemView.setCompoundDrawablesWithIntrinsicBounds(mMenuIcons.get(position), 0, 0, 0);
		itemView.setText(mMenuDatas.get(position));
		return convertView;
	}
}
