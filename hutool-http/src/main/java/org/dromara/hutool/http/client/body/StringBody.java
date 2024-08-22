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

package org.dromara.hutool.http.client.body;

import org.dromara.hutool.core.io.resource.HttpResource;
import org.dromara.hutool.core.io.resource.StringResource;
import org.dromara.hutool.http.meta.ContentTypeUtil;

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
