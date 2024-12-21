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

package org.dromara.hutool.poi.excel.sax;

import org.dromara.hutool.poi.POIException;

/**
 * 读取结束异常，用于标记读取结束<br>
 * Sax方式读取时，如果用户在RowHandler中抛出此异常，表示读取结束，此时不再读取其他数据
 *
 * @author Looly
 * @since 5.8.35
 */
public class StopReadException extends POIException {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 */
	public StopReadException() {
		this("Stop read by user.");
	}

	/**
	 * 构造
	 *
	 * @param message 消息
	 */
	public StopReadException(final String message) {
		super(message);
		// 去除堆栈
		setStackTrace(new StackTraceElement[0]);
	}
}
