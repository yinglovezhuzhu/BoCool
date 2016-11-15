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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoying.bocool.R;

public class CustomDialog extends Dialog {
	
	/**
	 * The identifier for the close button. 
	 */
	public static final int BUTTON_CLOSE = -4;
	
	private RelativeLayout mRlTitle = null;
	
	private FrameLayout mFlBody = null;
	
	private LinearLayout mLlFoot = null;
	
	private ImageView mIvIcon = null;
	
	private TextView mTvTitle = null;
	
	private Button mBtnClose = null;
	
	private TextView mTvMessage = null;

	private EditText mEtInput = null;
	
	private ListView mListView = null;
	
	private Button mBtnPositive = null;
	
	private Button mBtnNegative = null;
	
	private DialogInterface.OnClickListener mColseClickListener = null;
	
	private DialogInterface.OnClickListener mPositiveClickListener = null;
	
	private DialogInterface.OnClickListener mNegativeClickListener = null;
	
	private DialogInterface.OnClickListener mItemClickListener = null;
	
	private DialogInterface.OnMultiChoiceClickListener mMultiChoiceClickListener = null;
	
	public CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public CustomDialog(Context context) {
		this(context, R.style.CustomDialogTheme);
	}
	
	private void init(Context context) {
		setContentView(R.layout.layout_dialog);

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		LayoutParams lp = getWindow().getAttributes();
		lp.width = dm.widthPixels * 7 / 8;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;
		getWindow().setAttributes(lp);
		
		initView();
	}
	
	private void initView() {
		mRlTitle = (RelativeLayout) findViewById(R.id.rl_dialog_title);
		mFlBody = (FrameLayout) findViewById(R.id.fl_dialog_body);
		mLlFoot = (LinearLayout) findViewById(R.id.ll_dialog_foot);
		
		
		mIvIcon = (ImageView) findViewById(R.id.iv_dialog_icon);
		mTvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		mBtnClose = (Button) findViewById(R.id.btn_dialog_close);
		mTvMessage = (TextView) findViewById(R.id.tv_dialog_message);
		mEtInput = (EditText) findViewById(R.id.tv_dialog_input);
		mListView = (ListView) findViewById(R.id.lv_dialog_items);
		mBtnPositive = (Button) findViewById(R.id.btn_dialog_positive);
		mBtnNegative = (Button) findViewById(R.id.btn_dialog_negative);
	}
	
	/**
	 * Set dialog icon.
	 * @param resId
	 * @return
	 */
	public CustomDialog setIcon(int resId) {
		if(resId != 0) {
			mIvIcon.setVisibility(View.VISIBLE);
			mIvIcon.setImageResource(resId);
			mTvTitle.setGravity(Gravity.LEFT);
		}
		return this;
	}
	
	
	@Override
	public void setTitle(CharSequence title) {
		setTitleText(title);
	}
	
	@Override
	public void setTitle(int titleId) {
		setTitleText(titleId);
	}
	
	/**
	 * Set dialog title text.
	 * @param resId
	 * @return
	 */
	public CustomDialog setTitleText(int resId) {
		if(resId != 0) {
			mTvTitle.setVisibility(View.VISIBLE);
			mTvTitle.setText(resId);
		}
		return this;
	}
	
	/**
	 * Set dialog title text.
	 * @param text
	 * @return
	 */
	public CustomDialog setTitleText(CharSequence text) {
		if(text != null) {
			mTvTitle.setVisibility(View.VISIBLE);
			mTvTitle.setText(text);
		}
		return this;
	}
	
	/**
	 * Set dialog title text gravity.
	 * Note:If the icon has been set, title text always Gravity.LEFT.
	 * @param gravity
	 * @return
	 */
	public CustomDialog setTitleTextGravity(int gravity) {
		mTvTitle.setGravity(mIvIcon.getVisibility() == View.VISIBLE ? Gravity.LEFT : gravity);
		return this;
	}
	
	/**
	 * Set close button.
	 * @param resid
	 * @param listener
	 * @return
	 */
	public CustomDialog setCloseButton(int resid, DialogInterface.OnClickListener listener) {
		mBtnClose.setVisibility(View.VISIBLE);
		mBtnClose.setBackgroundResource(resid);
		this.mColseClickListener = listener;
		mBtnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mColseClickListener != null) {
					mColseClickListener.onClick(CustomDialog.this, BUTTON_CLOSE);
				}
			}
		});
		return this;
	}
	
	/**
	 * Set close button, with default listener
	 * @param resid
	 * @return
	 */
	public CustomDialog setCloseButton(int resid) {
		return setCloseButton(resid, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
	}
	
	/**
	 * Set dialog message text.
	 * @param resId
	 * @return
	 */
	public CustomDialog setMessage(int resId) {
		if(resId != 0) {
			mFlBody.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mTvMessage.setVisibility(View.VISIBLE);
			mTvMessage.setText(resId);
		}
		return this;
	}
	
	/**
	 * Set dialog message text.
	 * @param message
	 * @return
	 */
	public CustomDialog setMessage(CharSequence message) {
		if(message != null) {
			mFlBody.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mTvMessage.setVisibility(View.VISIBLE);
			mTvMessage.setText(message);
		}
		return this;
	}
	/**
	 * Set dialog input.
	 * @param hint
	 * @return
	 */
	public CustomDialog setInput(CharSequence hint) {
		mFlBody.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		mEtInput.setVisibility(View.VISIBLE);
		mEtInput.setText("");
		if(hint != null) {
			mEtInput.setHint(hint);
		}
		return this;
	}
	
	/**
	 * Set dialog input.
	 * @param hint
	 * @return
	 */
	public CustomDialog setInput(int hintRes) {
		mFlBody.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		mEtInput.setVisibility(View.VISIBLE);
		mEtInput.setText("");
		if(hintRes > 0) {
			mEtInput.setHint(hintRes);
		}
		return this;
	}
	
	/**
	 * Set dialog input background resource id<br>
	 * <p>Only when the input mode can be seen(When dialog has use {@link #setInput(int)} or {@link #setInput(CharSequence)}<br>
	 * to set dialog is input mode).
	 * @see {@link #setInput(CharSequence)}
	 * @see {@link #setInput(int)}
	 * @param resid
	 * @return
	 */
	public CustomDialog setInputBackgroudResource(int resid) {
		mEtInput.setBackgroundResource(resid);
		return this;
	}
	
	/**
	 * Set dialog input max length.
	 * @param maxLength
	 * @return
	 */
	public CustomDialog setInputMaxLength(int maxLength) {
		InputFilter [] filters = {new InputFilter.LengthFilter(maxLength)};
		mEtInput.setFilters(filters);
		return this;
	}
	
	/**
	 * Set dialog input text color<br>
	 * <p>Only when the input mode can be seen(When dialog has use {@link #setInput(int)} or {@link #setInput(CharSequence)}<br>
	 * to set dialog is input mode).
	 * @see {@link #setInput(CharSequence)}
	 * @see {@link #setInput(int)}
	 * @param color
	 * @return
	 */
	public CustomDialog setInputTextColor(int color) {
		mEtInput.setTextColor(color);
		return this;
	}
	
	/**
	 * Get dialog input text. 
	 * @return The text in input EditText, but null text when input EditText is not visibility.
	 */
	public CharSequence getInputText() {
		if(mEtInput.getVisibility() == View.VISIBLE) {
			return mEtInput.getText();
		}
		return "";
	}
	
	/**
	 * Set dialog input text. If input EditText dose not visibility, it would't set.
	 * @param text
	 */
	public void setInputText(CharSequence text) {
		if(mEtInput.getVisibility() == View.VISIBLE) {
			mEtInput.setText(text);
		}
	}
	
	/**
	 * Set dialog input text. If input EditText dose not visibility, it would't set.
	 * @param text
	 */
	public void setInputText(int resId) {
		if(mEtInput.getVisibility() == View.VISIBLE) {
			mEtInput.setText(resId);
		}
	}
	
	
	/**
	 * Set positive button text and listener.
	 * @param resId
	 * @param listener
	 * @return
	 */
	public CustomDialog setPositiveButton(int resId, DialogInterface.OnClickListener listener) {
		mBtnPositive.setVisibility(View.VISIBLE);
		mBtnPositive.setText(resId);
		mPositiveClickListener = listener;
		mBtnPositive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPositiveClickListener != null) {
					mPositiveClickListener.onClick(CustomDialog.this, DialogInterface.BUTTON_POSITIVE);
				}
			}
		});
		layoutButtons();
		return this;
	}
	
	/**
	 * Set positive button text and listener.
	 * @param text
	 * @param listener
	 * @return
	 */
	public CustomDialog setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
		mBtnPositive.setVisibility(View.VISIBLE);
		mBtnPositive.setText(text);
		mPositiveClickListener = listener;
		mBtnPositive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mPositiveClickListener != null) {
					mPositiveClickListener.onClick(CustomDialog.this, DialogInterface.BUTTON_POSITIVE);
				}
			}
		});
		layoutButtons();
		return this;
	}
	
	/**
	 * Set negative button text and listener.
	 * @param resId
	 * @param listener
	 * @return
	 */
	public CustomDialog setNegativeButton(int resId, DialogInterface.OnClickListener listener) {
		mBtnNegative.setVisibility(View.VISIBLE);
		mBtnNegative.setText(resId);
		mNegativeClickListener = listener;
		mBtnNegative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mNegativeClickListener != null) {
					mNegativeClickListener.onClick(CustomDialog.this, DialogInterface.BUTTON_NEGATIVE);
				}
			}
		});
		layoutButtons();
		return this;
	}
	
	/**
	 * Set negative button text and listener.
	 * @param text
	 * @param listener
	 * @return
	 */
	public CustomDialog setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
		mBtnNegative.setVisibility(View.VISIBLE);
		mBtnNegative.setText(text);
		mNegativeClickListener = listener;
		mBtnNegative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mNegativeClickListener != null) {
					mNegativeClickListener.onClick(CustomDialog.this, DialogInterface.BUTTON_NEGATIVE);
				}
			}
		});
		layoutButtons();
		return this;
	}
	
	/**
	 * ReLayout the buttons according to the their visibility.
	 */
	private void layoutButtons() {
		int windowWidth = getWindow().getAttributes().width;
		if(mBtnPositive.getVisibility() == View.VISIBLE && mBtnNegative.getVisibility() != View.VISIBLE) {
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBtnPositive.getLayoutParams();
			lp.leftMargin = windowWidth / 6;
			lp.rightMargin = windowWidth / 6;
			mBtnPositive.setLayoutParams(lp);
		} else if(mBtnPositive.getVisibility() != View.VISIBLE && mBtnNegative.getVisibility() == View.VISIBLE) {
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBtnNegative.getLayoutParams();
			lp.leftMargin = windowWidth / 6;
			lp.rightMargin = windowWidth / 6;
			mBtnNegative.setLayoutParams(lp);
		} else if(mBtnPositive.getVisibility() == View.VISIBLE && mBtnNegative.getVisibility() == View.VISIBLE) {
			LinearLayout.LayoutParams positiveLp = (LinearLayout.LayoutParams) mBtnPositive.getLayoutParams();
			positiveLp.rightMargin = 0;
			positiveLp.leftMargin = getContext().getResources().getDimensionPixelSize(R.dimen.custom_dialog_button_margin);
			mBtnPositive.setLayoutParams(positiveLp);
			LinearLayout.LayoutParams negativeLp = (LinearLayout.LayoutParams) mBtnNegative.getLayoutParams();
			negativeLp.leftMargin = 0;
			negativeLp.rightMargin = getContext().getResources().getDimensionPixelSize(R.dimen.custom_dialog_button_margin);
			mBtnNegative.setLayoutParams(negativeLp);
		}
	}
	
	/**
	 * Set dialog items. with item icons.
	 * @param items Items array.
	 * @param icons Icons array.
	 * @param textGravity Item text align. Only effect when icons is null, others it would be Gravity.LEFT.
	 * @param listener
	 * @return
	 */
	public CustomDialog setItems(CharSequence [] items, int [] icons, int textGravity, DialogInterface.OnClickListener listener) {
		this.mItemClickListener = listener;
		if(items != null && items.length > 0) {
			if(icons != null && icons.length != items.length) {
				throw new IllegalArgumentException("The items length dosen't equals to icons length.");
			}
			mFlBody.setVisibility(View.VISIBLE);
			mTvMessage.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			mListView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
			ItemsAdapter adapter = new ItemsAdapter(getContext(), icons, items, textGravity);
			mListView.setAdapter(adapter);
			mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if(mItemClickListener != null) {
						mItemClickListener.onClick(CustomDialog.this, position);
					}
				}
			});
		}
		return this;
	}
	
	
	/**
	 * Set dialog items. with no item icons.
	 * @param items Items array.
	 * @param textGravity Item text align.
	 * @param listener
	 * @return
	 */
	public CustomDialog setItems(CharSequence [] items, int textGravity, DialogInterface.OnClickListener listener) {
		return setItems(items, null, textGravity, listener);
	}
	
	/**
	 * Set dialog items. with item icons.
	 * @param items Items array.
	 * @param icons Icons array.
	 * @param listener
	 * @return
	 */
	public CustomDialog setItems(CharSequence [] items, int [] icons, DialogInterface.OnClickListener listener) {
		return setItems(items, icons, Gravity.LEFT, listener);
	}
	
	/**
	 * Set dialog items, with no item icons.
	 * @param items
	 * @param listener
	 * @return
	 */
	public CustomDialog setItems(CharSequence [] items, DialogInterface.OnClickListener listener) {
		return setItems(items, null, Gravity.LEFT, listener);
	}
	
	/**
	 * Set dialog single choice items, with item icons.
	 * @param items
	 * @param icons
	 * @param checkedItem The item which checked default.
	 * @param listener
	 * @return
	 */
	public CustomDialog setSingleChoiceItems(CharSequence [] items, int [] icons, int checkedItem, DialogInterface.OnClickListener listener) {
		this.mItemClickListener = listener;
		if(items != null && items.length > 0) {
			if(icons != null && icons.length != items.length) {
				throw new IllegalArgumentException("The items length dosen't equals to icons length.");
			}
			mFlBody.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.VISIBLE);
			mTvMessage.setVisibility(View.GONE);
			SingleChoiceAdapter adapter = new SingleChoiceAdapter(getContext(), icons, items);
			mListView.setAdapter(adapter);
			mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			mListView.setItemChecked(checkedItem, true);
			mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if(mItemClickListener != null) {
						mItemClickListener.onClick(CustomDialog.this, position);
					}
				}
			});
		}
		return this;
	}
	
	/**
	 * Set dialog single choice item, with no item icons.
	 * @param items
	 * @param checkedItem
	 * @param listener
	 * @return
	 */
	public CustomDialog setSingleChoiceItems(CharSequence [] items, int checkedItem, DialogInterface.OnClickListener listener) {
		return setSingleChoiceItems(items, null, checkedItem, listener);
	}
	
	/**
	 * Set dialog single choice item, with no item icons, the string and icons get from resources.
	 * @param textResId
	 * @param checkedItem
	 * @param listener
	 * @return
	 */
	public CustomDialog setSingleChoiceItems(int textResId, int checkedItem, DialogInterface.OnClickListener listener) {
		CharSequence [] items = getContext().getResources().getStringArray(textResId);
		return setSingleChoiceItems(items, null, checkedItem, listener);
	}

	/**
	 * Returns the currently checked item. 
	 * Note: The result is only valid if the items is {@link CustomDialog#setSingleChoiceItems(CharSequence[], int, android.content.DialogInterface.OnClickListener)}(include all of method named setSingleChoiceItems())
	 * @return The position of the currently checked item or INVALID_POSITION if nothing is selected
	 */
	public int getCheckedPosition() {
		if(mListView.getChoiceMode() == AbsListView.CHOICE_MODE_SINGLE) {
			return mListView.getCheckedItemPosition();
		}
		return AbsListView.INVALID_POSITION;
	}
	
	/**
	 * Set dialog multiple choice items, with item icons.
	 * @param items
	 * @param icons
	 * @param checkedItems The item those checked default.
	 * @param listener
	 * @return
	 */
	public CustomDialog setMultiChoiceItems(CharSequence [] items, int [] icons, int [] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
		this.mMultiChoiceClickListener = listener;
		if(items != null && items.length > 0) {
			if(icons != null && icons.length != items.length) {
				throw new IllegalArgumentException("The length of items dosen't equals to icons'.");
			}
			if(checkedItems != null && checkedItems.length > items.length) {
				throw new IllegalArgumentException("The length of checkedItems should not larger then items'");
			}
			mFlBody.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.VISIBLE);
			mTvMessage.setVisibility(View.GONE);
			MuiltiChoiceAdapter adapter = new MuiltiChoiceAdapter(getContext(), icons, items);
			mListView.setAdapter(adapter);
			mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			if(checkedItems != null) {
				for (int i : checkedItems) {
					mListView.setItemChecked(i, true);
				}
			}
			mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if(mMultiChoiceClickListener != null) {
						SparseBooleanArray array = mListView.getCheckedItemPositions();
						mMultiChoiceClickListener.onClick(CustomDialog.this, position, array.get(position, false));
					}
				}
			});
		}
		return this;
	}
	
	/**
	 * Set dialog multiple choice item, with no item icons.
	 * @param items
	 * @param checkedItems
	 * @param listener
	 * @return
	 */
	public CustomDialog setMultiChoiceItems(CharSequence [] items, int [] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
		return setMultiChoiceItems(items, null, checkedItems, listener);
	}
	
	/**
	 * Set dialog multiple choice item, with no item icons, the string and icons get from resources.
	 * @param textResId
	 * @param checkedItems
	 * @param listener
	 * @return
	 */
	public CustomDialog setMultiChoiceItems(int textResId, int [] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
		CharSequence [] items = getContext().getResources().getStringArray(textResId);
		return setMultiChoiceItems(items, null, checkedItems, listener);
	}
	
	/**
	 * Set title background resource
	 * @param resid
	 * @return
	 */
	public CustomDialog setTitleBackgroundResource(int resid) {
		mRlTitle.setBackgroundResource(resid);
		return this;
	}
	
	/**
	 * Set title background color
	 * @param color
	 * @return
	 */
	public CustomDialog setTitleBackgroudColor(int color) {
		mRlTitle.setBackgroundColor(color);
		return this;
	}
	
	/**
	 * Set content background resource
	 * @param resid
	 * @return
	 */
	public CustomDialog setContentBackgroundResource(int resid) {
		mFlBody.setBackgroundResource(resid);
		return this;
	}
	
	/**
	 * Set content background color
	 * @param color
	 * @return
	 */
	public CustomDialog setContentBackgroudColor(int color) {
		mFlBody.setBackgroundColor(color);
		return this;
	}
	
	/**
	 * Set foot background resource
	 * @param resid
	 * @return
	 */
	public CustomDialog setFootBackgroundResource(int resid) {
		mLlFoot.setBackgroundResource(resid);
		return this;
	}
	
	/**
	 * Set foot background color
	 * @param color
	 * @return
	 */
	public CustomDialog setFootBackgroudColor(int color) {
		mLlFoot.setBackgroundColor(color);
		return this;
	}
	
	/**
	 * Set background resources.
	 * @param title
	 * @param content
	 * @param foot
	 * @return
	 */
	public CustomDialog setBackgroudResources(int title, int content, int foot) {
		mRlTitle.setBackgroundResource(title);
		mFlBody.setBackgroundResource(content);
		mLlFoot.setBackgroundResource(foot);
		return this;
	}

	/**
	 * Set Background colors.
	 * @param title
	 * @param content
	 * @param foot
	 * @return
	 */
	public CustomDialog setBackgroudColors(int title, int content, int foot) {
		mRlTitle.setBackgroundColor(title);
		mFlBody.setBackgroundColor(content);
		mLlFoot.setBackgroundColor(foot);
		return this;
	}
	
	/**
	 * Set custom view to the custom dialog.
	 * @param view
	 * @return
	 */
	public CustomDialog setCustomView(View view) {
		mTvMessage.setVisibility(View.GONE);
		mEtInput.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 
				FrameLayout.LayoutParams.MATCH_PARENT);
		mFlBody.addView(view, lp);
		mFlBody.bringChildToFront(view);
		return this;
	}
	
	
	/**
	 * Returns the currently checked items. 
	 * Note: The result is only valid if the items is {@link CustomDialog#setMultiChoiceItems(CharSequence[], int[], android.content.DialogInterface.OnMultiChoiceClickListener)}(include all of method named setMultiChoiceItems())
	 * @return The position of the currently checked item or null if items are not multiple choice.
	 */
	public SparseBooleanArray getCheckedItemPositions() {
		return mListView.getCheckedItemPositions();
	}
	
	/**
	 * Adapter for simple item view.
	 * @author xiaoying
	 *
	 */
	private class ItemsAdapter extends BaseAdapter {

		private Context mmContext = null;
		private int [] mmIcons = null;
		private CharSequence [] mmItems = null;
		private int mmTextGravity = Gravity.LEFT;
		
		public ItemsAdapter(Context context, int [] icons, CharSequence [] items, int gravity) {
			this.mmContext = context;
			this.mmIcons = icons;
			this.mmItems = items;
			this.mmTextGravity = icons == null ? gravity : Gravity.LEFT;
		}
		
		@Override
		public int getCount() {
			return mmItems.length;
		}

		@Override
		public CharSequence getItem(int position) {
			return mmItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(mmContext, R.layout.item_dialog, null);
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_item_dialog_icon);
				viewHolder.text = (TextView) convertView.findViewById(R.id.tv_item_dialog_text);
				viewHolder.icon.setVisibility(mmIcons == null ? View.GONE : View.VISIBLE);
				viewHolder.text.setGravity(mmTextGravity);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if(mmIcons != null) {
				viewHolder.icon.setImageResource(mmIcons[position]);
			}
			viewHolder.text.setText(mmItems[position]);
			return convertView;
		}
		
		private class ViewHolder {
			ImageView icon;
			TextView text;
		}
		
	}
	
	/**
	 * Adapter for single choice item view.
	 * @author xiaoying
	 *
	 */
	private class SingleChoiceAdapter extends BaseAdapter {
		private Context mmContext = null;
		private int [] mmIcons = null;
		private CharSequence [] mmItems = null;
		
		public SingleChoiceAdapter(Context context, int [] icons, CharSequence [] items) {
			this.mmContext = context;
			this.mmIcons = icons;
			this.mmItems = items;
		}
		
		@Override
		public int getCount() {
			return mmItems.length;
		}

		@Override
		public CharSequence getItem(int position) {
			return mmItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SingleChoiceItem item = null;
			if(convertView == null) {
				item = new SingleChoiceItem(mmContext);
				if(mmIcons == null) {
					item.setIconVisibility(View.GONE);
				}
				convertView = item;
			} else {
				item = (SingleChoiceItem) convertView;
			}
			if(mmIcons != null) {
				item.setIcon(mmIcons[position]);
			}
			item.setText(mmItems[position]);
			return convertView;
		}
	}
	
	/**
	 * Adapter for multiple choice item view.
	 * @author xiaoying
	 *
	 */
	private class MuiltiChoiceAdapter extends BaseAdapter {
		private Context mmContext = null;
		private int [] mmIcons = null;
		private CharSequence [] mmItems = null;
		
		public MuiltiChoiceAdapter(Context context, int [] icons, CharSequence [] items) {
			this.mmContext = context;
			this.mmIcons = icons;
			this.mmItems = items;
		}
		
		@Override
		public int getCount() {
			return mmItems.length;
		}

		@Override
		public CharSequence getItem(int position) {
			return mmItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MultiChoiceItem item = null;
			if(convertView == null) {
				item = new MultiChoiceItem(mmContext);
				if(mmIcons == null) {
					item.setIconVisibility(View.GONE);
				}
				convertView = item;
			} else {
				item = (MultiChoiceItem) convertView;
			}
			if(mmIcons != null) {
				item.setIcon(mmIcons[position]);
			}
			item.setText(mmItems[position]);
			return convertView;
		}
	}
}
