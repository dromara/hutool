/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.cron.task;

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.cron.CronException;

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
			throw new HutoolException("Invalid classNameWithMethodName [{}]!", classNameWithMethodName);
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
		this.method = MethodUtil.getPublicMethod(clazz, false, methodName);
		if(null == this.method) {
			throw new IllegalArgumentException("No method with name of [" + methodName + "] !");
		}
	}

	@Override
	public void execute() {
		try {
			MethodUtil.invoke(this.obj, this.method);
		} catch (final HutoolException e) {
			throw new CronException(e.getCause());
		}
	}
}
