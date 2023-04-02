/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.task;

import org.dromara.hutool.CronException;
import org.dromara.hutool.classloader.ClassLoaderUtil;
import org.dromara.hutool.exceptions.UtilException;
import org.dromara.hutool.reflect.ConstructorUtil;
import org.dromara.hutool.reflect.MethodUtil;
import org.dromara.hutool.text.StrUtil;

import java.lang.reflect.Method;

/**
 * 反射执行任务<br>
 * 通过传入类名#方法名，通过反射执行相应的方法<br>
 * 如果是静态方法直接执行，如果是对象方法，需要类有默认的构造方法。
 *
 * @author Looly
 *
 */
public class InvokeTask implements Task{

	private final Object obj;
	private final Method method;

	/**
	 * 构造
	 * @param classNameWithMethodName 类名与方法名的字符串表示，方法名和类名使用#隔开或者.隔开
	 */
	public InvokeTask(final String classNameWithMethodName) {
		int splitIndex = classNameWithMethodName.lastIndexOf('#');
		if(splitIndex <= 0){
			splitIndex = classNameWithMethodName.lastIndexOf('.');
		}
		if (splitIndex <= 0) {
			throw new UtilException("Invalid classNameWithMethodName [{}]!", classNameWithMethodName);
		}

		//类
		final String className = classNameWithMethodName.substring(0, splitIndex);
		if(StrUtil.isBlank(className)) {
			throw new IllegalArgumentException("Class name is blank !");
		}
		final Class<?> clazz = ClassLoaderUtil.loadClass(className);
		if(null == clazz) {
			throw new IllegalArgumentException("Load class with name of [" + className + "] fail !");
		}
		this.obj = ConstructorUtil.newInstanceIfPossible(clazz);

		//方法
		final String methodName = classNameWithMethodName.substring(splitIndex + 1);
		if(StrUtil.isBlank(methodName)) {
			throw new IllegalArgumentException("Method name is blank !");
		}
		this.method = MethodUtil.getPublicMethod(clazz, methodName);
		if(null == this.method) {
			throw new IllegalArgumentException("No method with name of [" + methodName + "] !");
		}
	}

	@Override
	public void execute() {
		try {
			MethodUtil.invoke(this.obj, this.method);
		} catch (final UtilException e) {
			throw new CronException(e.getCause());
		}
	}
}
