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
package com.xiaoying.bocool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.xiaoying.bocool.R;
import com.xiaoying.bocool.adapter.base.BaseGridAdapter;

/**
 * 专辑列表数据GridView
 * 
 * Create by yinglovezhuzhu@gmail.com in 2015年4月20日
 */
public class GridDataView extends FrameLayout {
	
	private GridView mGridView = null;
	
	private View mRefreshView = null;
	private TextView mTvRefresh = null;
	private View mLoadMoreView = null;
	private TextView mTvLoadMore = null;
	
	private OnRefreshListener mOnRefreshListener = null;
	private OnLoadMoreListener mOnLoadMoreListener = null;
	
	private boolean mIsRefreshing = false;
	private boolean mIsLoadingMore = false;
	private boolean mIsLoadMoreable = false;
	
	private int mNumColumns = 0;
	private int mLastVisibleItem = 0;
	private int mTotalItemCount = 0;
	private int mHorizontalSpacing = 1;
	private int mVerticalSpacing = 1;
	
	private BaseGridAdapter mAdapter = null;
	
	private AdapterView.OnItemClickListener mViewModeItemClickListener = null;
	
	private LoadMode mLoadMode = LoadMode.AUTO_LOAD;
	
	private boolean mIsIgoreLastItem = false;

	public GridDataView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public GridDataView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public GridDataView(Context context) {
		super(context);
		initView(context);
	}

	
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.layout_griddataview, this);
		mGridView = (GridView) findViewById(R.id.gv_griddataview);
		mRefreshView = findViewById(R.id.ll_griddataview_refresh_view);
		mTvRefresh = (TextView) findViewById(R.id.tv_griddataview_refresh);
		mLoadMoreView = findViewById(R.id.ll_griddataview_loadmore_view);
		mTvLoadMore = (TextView) findViewById(R.id.tv_griddataview_loadmore);
		mRefreshView.setVisibility(View.GONE);
		mLoadMoreView.setVisibility(View.GONE);
		
		mGridView.setOnScrollListener(mOnScrollListener);
		mGridView.setOnItemClickListener(mItemClickListener);
		
		mHorizontalSpacing = context.getResources().getDimensionPixelSize(R.dimen.issue_grid_horizontalSpacing);
		mVerticalSpacing = context.getResources().getDimensionPixelSize(R.dimen.issue_grid_verticalSpacing);
		
	}
	
	private OnScrollListener mOnScrollListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(scrollState == SCROLL_STATE_IDLE && mLoadMode == LoadMode.AUTO_LOAD && mLastVisibleItem == mTotalItemCount) {
				onLoadMore();
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			mLastVisibleItem = firstVisibleItem + visibleItemCount;
			mTotalItemCount = totalItemCount;
		}
	};
	
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(mAdapter == null) {
				return;
			}
			if(mAdapter.getChoiceMode() == BaseGridAdapter.CHOICE_MODE_NONE 
					|| (mIsIgoreLastItem && position == mAdapter.getCount() - 1)) {
				if(mViewModeItemClickListener != null) {
					mViewModeItemClickListener.onItemClick(parent, view, position, id);
				}
			} else {
				mAdapter.toggle(position);
			}
		}
		
	};
	
	/********************************************************************************************************/
	public void setAdapter(BaseGridAdapter adapter) {
		this.mAdapter = adapter;
		mGridView.setAdapter(mAdapter);
	}
	
	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		this.mViewModeItemClickListener = listener;
	}
	
	public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
		mGridView.setOnItemLongClickListener(listener);
	}
	
	public void setIgnoreLastItem(boolean value) {
		this.mIsIgoreLastItem = value;
	}
	
	public int getChoiceMode() {
		if(mAdapter == null) {
			return BaseGridAdapter.CHOICE_MODE_NONE;
		}
		return mAdapter.getChoiceMode();
	}
	
	public void setChoiceMode(int choiceMode) {
		if(mAdapter == null) {
			return;
		}
		mAdapter.setChoiceMode(choiceMode);
	}
	
	public boolean isChoiceMode() {
		if(mAdapter == null) {
			return false;
		}
		return mAdapter.isChoiceMode();
	}
	
	public void setItemChecked(int position, boolean value) {
		if(mAdapter == null) {
			return;
		}
		mAdapter.setItemCheck(position, value);
	}
	
	public SparseBooleanArray getCheckedItemPositions() {
		if(mAdapter == null) {
			return null;
		}
		return mAdapter.getCheckedItemPositions();
	}
	
	public int getCheckItemPosition() {
		if(mAdapter == null) {
			return BaseGridAdapter.INVALID_POSITION;
		}
		return mAdapter.getCheckItemPosition();
	}
	
	public void clearChoices() {
		if(mAdapter == null) {
			return;
		}
		mAdapter.clearChoices();
	}
	
	public int getCheckItemCount() {
		if(mAdapter == null) {
			return 0;
		}
		return mAdapter.getCheckItemCount();
	}
	
	/**
	 * Set item check listener<br>
	 * <p>This method should be use after {@link #setAdapter(BaseGridAdapter)}<br>
	 * and the Adapter to be set should not be null.
	 * @param l
	 */
	public void setOnItemCheckListener(BaseGridAdapter.OnItemCheckListener l) {
		if(mAdapter == null) {
			return;
		}
		mAdapter.setOnItemCheckListener(l);
	}
	
	
	public void removeOnItemCheckListener() {
		if(mAdapter == null) {
			return;
		}
		mAdapter.removeOnItemCheckListener();
	}
	

	public void setOnRefreshListener(OnRefreshListener listener) {
		this.mOnRefreshListener = listener;
	}
	
	public void setOnLoadMoreListener(OnLoadMoreListener listener) {
		this.mOnLoadMoreListener = listener;
	}
	
	public void setLoadMoreable(boolean isLoadMoreable) {
		this.mIsLoadMoreable = isLoadMoreable;
	}
	
	/**
	 * 设置GridView的列宽<br>
	 * <p>这个方法的调用是为了根据屏幕对GridView的显示做适应，<br>
	 * 通过最小的列款和最小列数进行调整。首先保证最小列，<br>
	 * 再保证最大的列款
	 * @param minColumnWidth
	 * @param minColumn
	 * @return
	 */
	public int getColumnWidth(int minColumnWidth, int minColumn) {
		int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		int contentWidth = screenWidth - mGridView.getPaddingLeft() - mGridView.getPaddingRight();
		int columnWidth = contentWidth;
		int column = contentWidth / minColumnWidth;
		if(column > minColumn) {
			columnWidth = (contentWidth - (column - 1) * mHorizontalSpacing * (column - 1)) / column;
		} else {
			column = minColumn;
			columnWidth = (contentWidth - mHorizontalSpacing * (column - 1)) / column; 
		}
		mNumColumns = column;
		setColumnWidth(columnWidth);
		return columnWidth;
	}
	
	public void setColumnWidth(int columnWidth) {
		mGridView.setColumnWidth(columnWidth);
	}
	
	public int getNumColumns() {
		return mNumColumns;
	}
	
	/**
	 * 设置水平列间距<br>
	 * <p>设置水平列间距，必须在{@link GridDataView#getColumnWidth(int, int)}之前调用<br>
	 * @param horizontalSpacing
	 */
	public void setHorizontalSpacing(int horizontalSpacing) {
		this.mHorizontalSpacing = horizontalSpacing;
		mGridView.setHorizontalSpacing(mHorizontalSpacing);
	}
	
	/**
	 * 设置垂直行距<br>
	 * <p>设置水平列间距，必须在{@link GridDataView#getColumnWidth(int, int)}之前调用<br>
	 * @param verticalSpacing
	 */
	public void setVerticalSpacing(int verticalSpacing) {
		this.mVerticalSpacing = verticalSpacing;
		mGridView.setVerticalSpacing(mVerticalSpacing);
	}
	
	public boolean isLoading() {
		return mIsRefreshing || mIsLoadingMore;
	}
	
	public boolean isRefreshing() {
		return mIsRefreshing;
	}
	
	public boolean isLoadingMore() {
		return mIsLoadingMore;
	}
	
	public boolean isLoadingMoreable() {
		return mIsLoadingMore;
	}
	
	public void onRefresh() {
		if(isLoading()) {
			return;
		}
		mIsRefreshing = true;
		mRefreshView.setVisibility(View.VISIBLE);
		mRefreshView.bringToFront();
		if(mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh();
		}
	}
	
	/**
	 * 刷新数据<br>
	 * <p>显示刷新加载框,设置了{@link OnRefreshListener}对象，<br>
	 * 会调用{@link OnRefreshListener#onRefresh()}回调
	 * @see GridDataView#onRefresh()
	 * @param msg 需要显示的文字信息
	 */
	public void onRefresh(CharSequence msg) {
		onRefresh();
		mTvRefresh.setText(msg);
	}
	
	public void onRefresh(int resId) {
		onRefresh();
		mTvRefresh.setText(resId);
	}
	
	public void onRefreshCompleted() {
		mIsRefreshing = false;
		mRefreshView.setVisibility(View.GONE);
		mGridView.bringToFront();
	}
	
	public void onLoadMore() {
		if(isLoading() || ! mIsLoadMoreable) {
			return;
		}
		mIsLoadingMore = true;
		mLoadMoreView.setVisibility(View.VISIBLE);
		mLoadMoreView.bringToFront();
		if(mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
	}
	
	public void onLoadMore(CharSequence msg) {
		onLoadMore();
		mTvLoadMore.setText(msg);
	}
	
	public void onLoadMore(int resId) {
		onLoadMore();
		mTvLoadMore.setText(resId);
	}
	
	public void onLoadMoreCompleted() {
		mIsLoadingMore = false;
		mLoadMoreView.setVisibility(View.GONE);
		mGridView.bringToFront();
	}
	
	public static interface OnRefreshListener {
		public void onRefresh();
	}
	
	public static interface OnLoadMoreListener {
		public void onLoadMore();
	}
	
	
	public enum LoadMode {
		AUTO_LOAD, MAN_HAND, 
	}
}
