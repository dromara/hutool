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

package org.dromara.hutool.http.client.engine.okhttp;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import java.util.List;

/**
 * OkHttp3 CookieStore接口
 *
 * @author Looly
 * @since 6.0.0
 */
public interface OkCookieStore {

	/**
	 * 添加cookie
	 *
	 * @param httpUrl HTTP url 地址
	 * @param cookie  cookie
	 */
	void add(HttpUrl httpUrl, Cookie cookie);

	/**
	 * 添加指定 http url cookie集合
	 *
	 * @param httpUrl HTTP url 地址
	 * @param cookies cookie列表
	 */
	void add(HttpUrl httpUrl, List<Cookie> cookies);

	/**
	 * 根据HttpUrl从缓存中读取cookie集合
	 *
	 * @param httpUrl HTTP url 地址
	 * @return cookie集合
	 */
	List<Cookie> get(HttpUrl httpUrl);

	/**
	 * 获取全部缓存cookie
	 *
	 * @return cookie集合
	 */
	List<Cookie> getCookies();

	/**
	 * 移除指定http url cookie集合
	 *
	 * @param httpUrl HTTP url 地址
	 * @param cookie  cookie
	 * @return 是否移除成功
	 */
	boolean remove(HttpUrl httpUrl, Cookie cookie);

	/**
	 * 移除所有cookie
	 *
	 * @return 是否移除成功
	 */
	boolean removeAll();
}
