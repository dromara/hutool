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

package cn.hutool.http.meta;

import cn.hutool.core.regex.ReUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharsetUtil;

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
