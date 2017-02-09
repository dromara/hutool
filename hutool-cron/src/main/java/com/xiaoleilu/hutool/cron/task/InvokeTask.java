package com.xiaoleilu.hutool.cron.task;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.util.ClassUtil;

public class InvokeTask implements Task{
	
	private String className;
	private String methodName;
	
	/**
	 * 
	 * @param classNameWithMethodName
	 */
	public InvokeTask(String classNameWithMethodName) {
		int splitIndex = classNameWithMethodName.lastIndexOf('#');
		if(splitIndex <= 0){
			splitIndex = classNameWithMethodName.lastIndexOf('.');
		}
		if (splitIndex <= 0) {
			throw new UtilException("Invalid classNameWithMethodName [{}]!", classNameWithMethodName);
		}

		this.className = classNameWithMethodName.substring(0, splitIndex);
		this.methodName = classNameWithMethodName.substring(splitIndex + 1);
	}

	@Override
	public void execute() {
		ClassUtil.invoke(this.className, this.methodName, new Object[]{});
	}
}
