package com.xiaoleilu.hutool.cron.task;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.util.ClassUtil;

/**
 * 反射执行任务<br>
 * 通过传入类名#方法名，通过反射执行相应的方法<br>
 * 如果是静态方法直接执行，如果是对象方法，需要类有默认的构造方法。
 * 
 * @author Looly
 *
 */
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
