/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.meta;

/**
 * Http方法枚举
 *
 * @author Looly
 */
public enum Method {
	GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE, CONNECT, PATCH;

	/**
	 * 是否忽略读取响应body部分<br>
	 * HEAD、CONNECT、TRACE方法将不读取响应体
	 *
	 * @return 是否需要忽略响应body部分
	 */
	public boolean isIgnoreBody() {
		//https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Methods/OPTIONS
		// OPTIONS请求可以带有响应体
		switch (this){
			case HEAD:
			case CONNECT:
			case TRACE:
				return true;
			default:
				return false;
		}
	}
}
