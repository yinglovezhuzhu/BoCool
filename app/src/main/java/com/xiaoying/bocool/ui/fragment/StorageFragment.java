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
package com.xiaoying.bocool.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.opensource.bitmaploader.ImageCache;
import com.opensource.bitmaploader.ImageFetcher;
import com.xiaoying.bocool.R;
import com.xiaoying.bocool.adapter.StorageAdapter;
import com.xiaoying.bocool.bean.Storage;
import com.xiaoying.bocool.config.Config;
import com.xiaoying.bocool.observer.NetworkObserver;
import com.xiaoying.bocool.observer.NetworkObserverManager;
import com.xiaoying.bocool.ui.IssueActivity;
import com.xiaoying.bocool.ui.MainActivity;
import com.xiaoying.bocool.ui.base.BaseFragment;
import com.xiaoying.bocool.utils.CacheManager;
import com.xiaoying.bocool.utils.FileUtil;
import com.xiaoying.bocool.widget.ErrorPageView;
import com.xiaoying.bocool.widget.LoadingView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 知识库页面
 * Create by yinglovezhuzhu@gmail.com in 2014年12月12日
 */
public class StorageFragment extends BaseFragment {

	private GridView mGvStorages;
    private LoadingView mHeaderLoadingView;
    private LoadingView mFooterLoadingView;
	private ErrorPageView mErrorPage;
	
	private StorageAdapter mAdapter;
	
	private ImageFetcher mImageFetcher;
	private ImageCache mImageCache;

    private boolean mLoading = false;
    private boolean mCanceled = false;
    private int mPage = 1;
    private int mPageCount = Config.GRID_PAGE_ROW;
    private boolean mLoadmoreable = false;

	private NetworkObserver mNetworkObserver = new NetworkObserver() {
        @Override
        public void onNetworkStateChaged(boolean noConnectivity, NetworkInfo currentNetwok, NetworkInfo lastNetwork) {
            if(null == mErrorPage || !mErrorPage.isShowing()) {
                return;
            }
            if(!noConnectivity) {
                mErrorPage.setErrorMessage(R.string.network_restored);
                mErrorPage.setReloadButton(R.string.click_to_reload, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queryStorages(mPage);
                    }
                });
            }
        }
    };

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        NetworkObserverManager.getInstance().registerNetworkObserver(mNetworkObserver);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        queryStorages(mPage);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_storage, container, false);
		initImageWorker();
		initView(contentView);
		return contentView;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(null != mImageFetcher) {
			mImageFetcher.setExitTasksEarly(true);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(null != mImageFetcher) {
			mImageFetcher.setImageCache(mImageCache);
			mImageFetcher.setExitTasksEarly(false);
			mAdapter.notifyDataSetChanged();
		}
	}

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!isHidden()) {
            Activity activity = getActivity();
            if(activity instanceof MainActivity && ((MainActivity) activity).reloadStorage()) {
                mPage = 1;
                mAdapter.clear(true);
                queryStorages(mPage);
                ((MainActivity) activity).setReloadStorage(false);
            }
        }
    }

    @Override
	public void onDestroy() {
        mCanceled = true;
        NetworkObserverManager.getInstance().unregisterNetworkObserver(mNetworkObserver);
		super.onDestroy();
	}
	
	private void initView(View contentView) {
		
		mGvStorages = (GridView) contentView.findViewById(R.id.gv_storages);
        mHeaderLoadingView = (LoadingView) contentView.findViewById(R.id.loading_storage_top);
        mFooterLoadingView = (LoadingView) contentView.findViewById(R.id.loading_storage_bottom);
		mErrorPage = (ErrorPageView) contentView.findViewById(R.id.error_page_storage);
		mGvStorages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Storage storage = mAdapter.getItem(position);
				if(null == storage) {
					return;
				}
				Intent intent = new Intent(getActivity(), IssueActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable(Config.EXTRA_DATA, storage);
                intent.putExtras(extras);
				startActivityLeft(intent);
			}
		});

        mGvStorages.setOnScrollListener(new AbsListView.OnScrollListener() {

            boolean mmLastItemVisible = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState
                        && mLoadmoreable && mmLastItemVisible && !mLoading) {
                    mPage++;
                    queryStorages(mPage);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                mmLastItemVisible = (firstVisibleItem + visibleItemCount == totalItemCount);
            }
        });
		
		int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int padding = getResources().getDimensionPixelSize(R.dimen.issue_grid_padding);
        int contentWidth = screenWidth - padding * 2;
		int columnWidth = contentWidth;
		int column = contentWidth / 300;
		int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.issue_grid_horizontalSpacing);
		if(column < 2) {
			column = 2;
		}
        columnWidth = (contentWidth - horizontalSpacing * (column - 1)) / column;
		int columnHeight = columnWidth * 4 / 3;
        mGvStorages.setColumnWidth(columnWidth);
        mPageCount *= column; // 分页大小用行数乘以列数

		if(null == mAdapter) {
			mAdapter = new StorageAdapter(getActivity(), columnWidth, columnHeight, mImageFetcher);
		}
		mGvStorages.setAdapter(mAdapter);
		
	}
	
	/**
	 * 初始化图片加载工具
	 */
	private void initImageWorker() {
		if(null == mImageFetcher) {
			mImageFetcher = new ImageFetcher(getActivity(), 240, 320);
			mImageFetcher.setLoadingImage(R.drawable.default_image);
			mImageFetcher.setLoadFailedImage(R.drawable.default_image);
		}
		if(null == mImageCache) {
			mImageCache = CacheManager.getInstance().getThumbCache(getActivity(),
                    FileUtil.createDirs(getActivity(), Config.APP_FOLDER, Config.CACHE_FOLDER),
                    Config.THUMB_FOLDER, true);
		}
		mImageFetcher.setImageCache(mImageCache);
	}

	private void queryStorages(int page) {
		BmobQuery<Storage> query = new BmobQuery<Storage>();
		if(page > 1) {
            mFooterLoadingView.setProgressVisibility(View.VISIBLE);
            mFooterLoadingView.show(R.string.loading_data);
			query.setSkip(page * mPageCount);
		} else {
            mHeaderLoadingView.setProgressVisibility(View.VISIBLE);
            mHeaderLoadingView.show(R.string.loading_data);
        }
//        query.addWhereEqualTo("status", Storage.STATUS_ONLINE);
		query.setLimit(mPageCount);
		query.order("sort");
        mErrorPage.dismiss();
        mLoading = true;
		query.findObjects(getActivity(), new FindListener<Storage>() {
			@Override
			public void onSuccess(List<Storage> list) {
                if(mCanceled) {
                    mHeaderLoadingView.dismiss();
                    mFooterLoadingView.dismiss();
                    mLoading = false;
                    return;
                }
                mAdapter.addAll(list);
                // 是否可以加载更多，因为没有分页，所以当获取的数据为满页时，当作还有更多数据
                mLoadmoreable = list.size() >= mPageCount;
                mHeaderLoadingView.dismiss();
                mFooterLoadingView.dismiss();
                mLoading = false;
			}

			@Override
			public void onError(int code, String msg) {
                mHeaderLoadingView.dismiss();
                mFooterLoadingView.dismiss();
                switch (code) {
                    case Config.BMOB_NO_NETWORK:
                        mErrorPage.setErrorMessage(R.string.no_network);
                        mErrorPage.setReloadButton(R.string.checking_network, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                            }
                        });
                        break;
                    case Config.BMOB_NETWORK_TIMEOUT:
                        mErrorPage.setErrorMessage(R.string.net_timeout);
                        mErrorPage.setReloadButton(R.string.click_to_reload, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                queryStorages(mPage);
                            }
                        });
                    default:
                        mErrorPage.setErrorMessage(R.string.load_error);
                        mErrorPage.setReloadButton(R.string.click_to_reload, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                queryStorages(mPage);
                            }
                        });
                        break;
                }
                mErrorPage.show();
                mLoading = false;
			}
		});
	}
}
