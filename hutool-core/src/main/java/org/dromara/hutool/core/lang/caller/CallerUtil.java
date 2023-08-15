/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.caller;

/**
 * 调用者。可以通过此类的方法获取调用者、多级调用者以及判断是否被调用
 *
 * @author Looly
 * @since 4.1.6
 */
public class CallerUtil {
	private static final Caller INSTANCE;
	static {
		INSTANCE = tryCreateCaller();
	}

	/**
	 * 获得调用者
	 *
	 * @return 调用者
	 */
	public static Class<?> getCaller() {
		return INSTANCE.getCaller();
	}

	/**
	 * 获得调用者的调用者
	 *
	 * @return 调用者的调用者
	 */
	public static Class<?> getCallerCaller() {
		return INSTANCE.getCallerCaller();
	}

	/**
	 * 获得调用者，指定第几级调用者<br>
	 * 调用者层级关系：
	 *
	 * <pre>
	 * 0 CallerUtil
	 * 1 调用CallerUtil中方法的类
	 * 2 调用者的调用者
	 * ...
	 * </pre>
	 *
	 * @param depth 层级。0表示CallerUtil本身，1表示调用CallerUtil的类，2表示调用者的调用者，依次类推
	 * @return 第几级调用者
	 */
	public static Class<?> getCaller(final int depth) {
		return INSTANCE.getCaller(depth);
	}

	/**
	 * 是否被指定类调用
	 *
	 * @param clazz 调用者类
	 * @return 是否被调用
	 */
	public static boolean isCalledBy(final Class<?> clazz) {
		return INSTANCE.isCalledBy(clazz);
	}

	/**
	 * 获取调用此方法的方法名
	 *
	 * @param isFullName 是否返回全名，全名包括方法所在类的全路径名
	 * @return 调用此方法的方法名
	 * @since 5.2.4
	 */
	public static String getCallerMethodName(final boolean isFullName){
		final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
		final String methodName = stackTraceElement.getMethodName();
		if(!isFullName){
			return methodName;
		}

		return stackTraceElement.getClassName() + "." + methodName;
	}

	/**
	 * 尝试创建{@link Caller}实现
	 *
	 * @return {@link Caller}实现
	 */
	private static Caller tryCreateCaller() {
		Caller caller;
		try {
			caller = new SecurityManagerCaller();
			if(null != caller.getCaller() && null != caller.getCallerCaller()) {
				return caller;
			}
		} catch (final Throwable e) {
			//ignore
		}

		caller = new StackTraceCaller();
		return caller;
	}
	// ---------------------------------------------------------------------------------------------- static interface and class
}
