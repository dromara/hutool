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

package cn.hutool.http.client.body;

import cn.hutool.core.io.resource.HttpResource;
import cn.hutool.core.io.resource.StringResource;
import cn.hutool.http.meta.ContentTypeUtil;

import java.nio.charset.Charset;

/**
 * String类型的body，一般用于Rest请求的请求体，如JSON或XML
 *
 * @author looly
 */
public class StringBody extends ResourceBody {

	/**
	 * 构造，根据body内容类型，自动识别Content-Type
	 *
	 * @param body    Body内容
	 * @param charset 自定义编码
	 */
	public StringBody(final String body, final Charset charset) {
		this(body, ContentTypeUtil.getContentTypeByRequestBody(body), charset);
	}

	/**
	 * 构造
	 *
	 * @param body        Body内容
	 * @param contentType 自定义Content-Type
	 * @param charset     自定义编码
	 */
	public StringBody(final String body, final String contentType, final Charset charset) {
		super(new HttpResource(new StringResource(body, contentType, charset), contentType));
	}
}
