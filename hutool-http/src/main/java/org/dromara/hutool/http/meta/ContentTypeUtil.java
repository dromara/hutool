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

import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * Content-Type解析工具
 *
 * @author looly
 * @since 6.0.0
 */
public class ContentTypeUtil {

	/**
	 * 正则：Content-Type中的编码信息
	 */
	public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

	/**
	 * 从Http连接的头信息中获得字符集<br>
	 * 从ContentType中获取
	 *
	 * @param contentType Content-Type
	 * @return 字符集
	 * @since 5.2.6
	 */
	public static Charset getCharset(final String contentType) {
		return CharsetUtil.parse(getCharsetName(contentType), null);
	}

	/**
	 * 从Http连接的头信息中获得字符集<br>
	 * 从ContentType中获取
	 *
	 * @param contentType Content-Type
	 * @return 字符集
	 * @since 5.2.6
	 */
	public static String getCharsetName(final String contentType) {
		if (StrUtil.isBlank(contentType)) {
			return null;
		}
		return ReUtil.get(CHARSET_PATTERN, contentType, 1);
	}

	/**
	 * 从请求参数的body中判断请求的Content-Type类型，支持的类型有：
	 *
	 * <pre>
	 * 1. application/json
	 * 1. application/xml
	 * </pre>
	 *
	 * @param body 请求参数体
	 * @return Content-Type类型，如果无法判断返回null
	 * @see ContentType#get(String)
	 * @since 3.2.0
	 */
	public static String getContentTypeByRequestBody(final String body) {
		final ContentType contentType = ContentType.get(body);
		return (null == contentType) ? null : contentType.toString();
	}
}
