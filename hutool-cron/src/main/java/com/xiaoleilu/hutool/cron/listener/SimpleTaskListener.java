package com.xiaoleilu.hutool.cron.listener;

import com.xiaoleilu.hutool.cron.TaskExecutor;

/**
 * 简单监听实现，不做任何操作<br>
 * 继承此监听后实现需要的方法即可
 * @author Looly
 *
 */
public class SimpleTaskListener implements TaskListener{

	@Override
	public void onStart(TaskExecutor executor) {
	}

	@Override
	public void onSucceeded(TaskExecutor executor) {
		
	}

	@Override
	public void onFailed(TaskExecutor executor, Throwable exception) {

	}

}
