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
package com.xiaoying.bocool.widget.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoying.bocool.R;

/**
 * Single choice item view.
 * @author xiaoying
 *
 */
public class MultiChoiceItem extends LinearLayout implements Checkable {
	/** icon */
	private ImageView mImageView = null;
	/** text */
	private TextView mTextView = null;
	/** CheckBox */
	private CheckBox mCheckBox = null;

	public MultiChoiceItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public MultiChoiceItem(Context context) {
		this(context, null);
	}
	
	private void initView(Context context) {
		View.inflate(context, R.layout.item_dialog_multichoice, this);
		mImageView = (ImageView) findViewById(R.id.iv_item_dialog_multichoice_icon);
		mTextView = (TextView) findViewById(R.id.tv_item_dialog_multichoice_text);
		mCheckBox = (CheckBox) findViewById(R.id.cb_item_dialog_multichoice_radio);
	}
	
	/**
	 * Set item icon.
	 * @param resId
	 */
	public void setIcon(int resId) {
		mImageView.setImageResource(resId);
	}
	
	/**
	 * Set item icon.
	 * @param drawable
	 */
	public void setIcon(Drawable drawable) {
		mImageView.setImageDrawable(drawable);
	}
	
	/**
	 * Set Item icon's visibility.
	 * @param visibility
	 */
	public void setIconVisibility(int visibility) {
		mImageView.setVisibility(visibility);
	}
	
	/**
	 * Set item text.
	 * @param resId
	 */
	public void setText(int resId) {
		mTextView.setText(resId);
	}
	
	/**
	 * Set item text.
	 * @param text
	 */
	public void setText(CharSequence text) {
		mTextView.setText(text);
	}
	
	@Override
	public void setChecked(boolean checked) {
		mCheckBox.setChecked(checked);
	}

	@Override
	public boolean isChecked() {
		return mCheckBox.isChecked();
	}

	@Override
	public void toggle() {
		mCheckBox.setChecked(!mCheckBox.isChecked());
	}

}
