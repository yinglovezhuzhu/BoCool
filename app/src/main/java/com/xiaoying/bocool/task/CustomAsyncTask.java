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
package com.xiaoying.bocool.task;

import android.os.AsyncTask;

/**
 * Use：自定义的异步线程类
 * 
 * @author yinglovezhuzhu@gmail.com
 */
public abstract class CustomAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	
	/**
	 * Cancel task<br>
	 * before use {@link #cancel(boolean)} to cancel the task,<br>
	 * will do the work defined in {@link #onCancel()}
	 * @see {@link #cancel(boolean)}
	 * @see {@link #onCancel()}
	 * @return
	 */
	public boolean cancel() {
		onCancel();
		return this.cancel(true);
	}
	
	/**
	 * Do something when cancel task<br>
	 * <p>This is different from {@link #onCancelled()}, because the work<br>
	 * defined in this method would be done before cancel work<br>
	 * 
	 * @see {@link #cancel()}
	 * 
	 */
	protected void onCancel(){}
}


