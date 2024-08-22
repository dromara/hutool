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
