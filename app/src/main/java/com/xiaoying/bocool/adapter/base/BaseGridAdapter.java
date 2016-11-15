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

/**
 * 
 */
package com.xiaoying.bocool.adapter.base;

import android.util.SparseBooleanArray;
import android.widget.BaseAdapter;

/**
 * @author xiaoying
 *
 */
public abstract class BaseGridAdapter extends BaseAdapter {
	
	public static final int INVALID_POSITION = -1;
	
	public static final int CHOICE_MODE_NONE = 0x0; 
	public static final int CHOICE_MODE_SINGLE = 0x1;
	public static final int CHOICE_MODE_MULTIPLE = 0x2;

	protected int mChoiceMode = CHOICE_MODE_NONE;
	
	protected int mCheckedItemCount = 0;
	
	protected int mLastCheckItem = INVALID_POSITION;
	
	protected SparseBooleanArray mCheckStates;
	
	protected OnItemCheckListener mOnItemCheckListener;
	
	/**
     * Returns the number of items currently selected. This will only be valid
     * if the choice mode is not {@link ChoiceMode#CHOICE_MODE_NONE} (default).
     *
     * <p>To determine the specific items that are currently selected, use one of
     * the <code>getChecked*</code> methods.
     *
     * @return The number of items currently selected
     *
     * @see #getCheckedItemPosition()
     * @see #getCheckedItemPositions()
     */
	public int getCheckItemCount() {
		return mCheckedItemCount;
	}
	
	/**
     * Returns the currently checked item. The result is only valid if the choice
     * mode has been set to {@link ChoiceMode#CHOICE_MODE_SINGLE}.
     *
     * @return The position of the currently checked item or
     *         {@link #INVALID_POSITION} if nothing is selected
     *
     * @see #setChoiceMode(ChoiceMode)
     */
	public int getCheckItemPosition() {
		if (mChoiceMode == CHOICE_MODE_SINGLE && mCheckStates != null && mCheckStates.size() == 1) {
            return mCheckStates.keyAt(0);
        }
        return INVALID_POSITION;
	}
	
	/**
     * Returns the set of checked items in the list. The result is only valid if
     * the choice mode has not been set to {@link ChoiceMode#CHOICE_MODE_NONE}.
     *
     * @return  A SparseBooleanArray which will return true for each call to
     *          get(int position) where position is a position in the list,
     *          or <code>null</code> if the choice mode is set to
     *          {@link ChoiceMode#CHOICE_MODE_NONE}.
     */
    public SparseBooleanArray getCheckedItemPositions() {
        if (mChoiceMode != CHOICE_MODE_NONE) {
            return mCheckStates;
        }
        return null;
    }
	
	/**
     * Sets the checked state of the specified position. The is only valid if
     * the choice mode has been set to {@link ChoiceMode#CHOICE_MODE_SINGLE} or
     * {@link ChoiceMode#CHOICE_MODE_MULTIPLE}.
     *
     * @param position The item whose checked state is to be checked
     * @param value The new checked state for the item
     */
	public void setItemCheck(int position, boolean value) {
		if(mChoiceMode == CHOICE_MODE_NONE) {
			return;
		}
		if(position == INVALID_POSITION) {
			return;
		}
		if(mChoiceMode == CHOICE_MODE_SINGLE) {
			// Clear all values if we're checking something, or unchecking the currently
            // selected item
            if (value || isItemChecked(position)) {
                mCheckStates.clear();
            }
            // this may end up selecting the value we just cleared but this way
            // we ensure length of mCheckStates is 1, a fact getCheckedItemPosition relies on
            if (value) {
                mCheckStates.put(position, true);
                mCheckedItemCount = 1;
            } else if (mCheckStates.size() == 0 || !mCheckStates.valueAt(0)) {
                mCheckedItemCount = 0;
            }
		} else if(mChoiceMode == CHOICE_MODE_MULTIPLE) {
			boolean oldValue = mCheckStates.get(position);
            mCheckStates.put(position, value);
            if (oldValue != value) {
                if (value) {
                    mCheckedItemCount++;
                } else {
                    mCheckedItemCount--;
                }
            }
		}
		if(mOnItemCheckListener != null) {
			mOnItemCheckListener.onItemCheck(position, value, getCheckItemCount());
		}
		notifyDataSetChanged();
	}
	
	public void toggle(int position) {
		setItemCheck(position, !isItemChecked(position));
	}
	
	/**
     * Returns the checked state of the specified position. The result is only
     * valid if the choice mode has been set to {@link ChoiceMode#CHOICE_MODE_SINGLE
     * or {@link ChoiceMode#CHOICE_MODE_MULTIPLE}.
     *
     * @param position The item whose checked state to return
     * @return The item's checked state or <code>false</code> if choice mode
     *         is invalid
     *
     * @see #setChoiceMode(ChoiceMode)
     */
    public boolean isItemChecked(int position) {
        if (mChoiceMode != CHOICE_MODE_NONE && mCheckStates != null) {
            return mCheckStates.get(position);
        }
        return false;
    }
    
    /**
     * Returns the state of mode whether it is in a choice mode.
     * 
     * @return The state of mode. when choice mode is {@link ChoiceMode#CHOICE_MODE_NONE}, 
     * return <code>false</code>
     * 
     * @see #setChoiceMode(ChoiceMode)
     */
    public boolean isChoiceMode() {
    	return mChoiceMode != CHOICE_MODE_NONE;
    }
    
    /**
     * Choose all items
     * 
     */
    public void checkAll() {
    	for(int i = 0; i < getCount(); i++) {
    		mCheckStates.put(i, true);
    	}
    	mCheckedItemCount = getCheckItemCount();
    }
    
    /**
     * Clear any choices previously set
     */
    public void clearChoices() {
        if (mCheckStates != null) {
            mCheckStates.clear();
        }
        mCheckedItemCount = 0;
        notifyDataSetChanged();
    }
	
	/**
	 * Set Choice mode.
	 * @param choiceMode
	 */
	public void setChoiceMode(int choiceMode) {
		if(choiceMode != CHOICE_MODE_NONE) {
			if(mCheckStates == null) {
				mCheckStates = new SparseBooleanArray();
			} else {
				mCheckStates.clear();
			}
			mCheckedItemCount = 0;
		}
		mChoiceMode = choiceMode;
		notifyDataSetChanged();
	}
	
	/**
	 * Get Choice mode.
	 * @return
	 */
	public int getChoiceMode() {
		return mChoiceMode;
	}
	
	/**
	 * Set check listener
	 * @param l
	 */
	public void setOnItemCheckListener(OnItemCheckListener l) {
		this.mOnItemCheckListener = l;
	}
	
	/**
	 * Remove check listener
	 */
	public void removeOnItemCheckListener() {
		this.mOnItemCheckListener = null;
	}
	
	/**
	 * Item check listener
	 * @author yinglovezhuzhu@gmail.com
	 *
	 */
	public static interface OnItemCheckListener {
		/**
		 * callback
		 * @param position check item position
		 * @param isChecked check item state
		 * @param count the count of the checked item.
		 */
		public void onItemCheck(int position, boolean isChecked, int count);
	}
}
