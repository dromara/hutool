/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.http.client;

/**
 * 请求上下文，用于在多次请求时保存状态信息<br>
 * 非线程安全。
 *
 * @author Looly
 */
public class RequestContext {

	private Request request;
	private int redirectCount;

	/**
	 * 构造
	 *
	 * @param request 请求
	 */
	public RequestContext(final Request request) {
		this.request = request;
	}

	/**
	 * 获取请求
	 *
	 * @return 请求
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * 设置请求，在重新请求或重定向时，更新请求信息
	 *
	 * @param request 请求
	 * @return this
	 */
	public RequestContext setRequest(final Request request) {
		this.request = request;
		return this;
	}

	/**
	 * 获取重定向计数器
	 *
	 * @return 重定向计数器
	 */
	public int getRedirectCount() {
		return redirectCount;
	}

	/**
	 * 重定向计数器+1
	 */
	public void incrementRedirectCount() {
		this.redirectCount++;
	}
}
