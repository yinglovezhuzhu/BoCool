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
package com.xiaoying.bocool.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * 
 * Create by yinglovezhuzhu@gmail.com in 2014年12月11日
 */
public class FragmentUtil {
	
	private FragmentManager mFragmentManager;

    private FragmentActivity mActivity;
    
    private final Map<String, Fragment> mFragments = new HashMap<String, Fragment>();
    
    private Fragment mCurrentFragment = null;

    private int mBody;


    private FragmentUtil(FragmentActivity activity, int body) {
        this.mActivity = activity;
        this.mBody = body;
        mFragmentManager = mActivity.getFragmentManager();
    }


    /**
     * Create new FragmentUtil instance.
     * @param activity
     * @param body
     * @return
     */
    public static FragmentUtil newInstance(FragmentActivity activity, int body) {
        return new FragmentUtil(activity, body);
    }

    /**
     * 打开一个Fragment<br>
     * <p>如果这个Fragment已经添加，那么直接将其显示，不用初始化，如果没有添加，则添加它。<br>
     * <p>注意：如果对于同一个Fragment类要显示不同的内容，不要用这种方法，这种方法是针对在某个容器内，
     * 该Fragment类只有一个界面的情形，如果一个Fragment类会同时出现多个不同的界面，请使用{@link #replaceFragment(Class, Bundle)}
     * @param cls
     * @param args
     * @see #replaceFragment(Class, Bundle)
     */
    public void openFragment(Class<? extends Fragment> cls, Bundle args) {
        Fragment fragment = mFragmentManager.findFragmentByTag(cls.getName());
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if(mCurrentFragment != null) {
        	ft.hide(mCurrentFragment);
        }
        if(fragment == null) {
            fragment = Fragment.instantiate(mActivity, cls.getName(), args);
            ft.add(mBody, fragment, cls.getName());
            mFragments.put(cls.getName(), fragment);
        } else {
            ft.attach(fragment);
        }
        mCurrentFragment = fragment;
        ft.show(fragment);
        ft.commit();
    }
    
    /**
     * 替换容器里面的Fragment，原来容器中的所有Fragment都会被移除，相当于remove，
     * 当前的Fragment相当于add进去，并且attach
     * @param cls
     * @param args
     */
    public void replaceFragment(Class<? extends Fragment> cls, Bundle args) {
    	FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = Fragment.instantiate(mActivity, cls.getName(), args);
        ft.replace(mBody, fragment, cls.getName());
        mFragments.clear();
        mFragments.put(cls.getName(), fragment);
        ft.commit();
    }
    
    /**
     * 移除所有已经添加的Fragment
     */
    public void clearFragments() {
    	FragmentTransaction ft = mFragmentManager.beginTransaction();
    	Set<String> keys = mFragments.keySet();
    	for (String key : keys) {
    		Fragment fragment = mFragments.get(key);
    		if(null == fragment) {
    			continue;
    		}
			ft.remove(fragment);
			mFragments.remove(key);
		}
        ft.commit();
    }
    
    /**
     * 移除某个Fragment
     * @param cls
     */
    public void removeFragment(Class<? extends Fragment> cls) {
    	FragmentTransaction ft = mFragmentManager.beginTransaction();
    	Fragment fragment = mFragments.get(cls.getName());
    	if(null != fragment) {
    		ft.remove(fragment);
    		mFragments.remove(cls.getName());
    	}
        ft.commit();
    }
}
