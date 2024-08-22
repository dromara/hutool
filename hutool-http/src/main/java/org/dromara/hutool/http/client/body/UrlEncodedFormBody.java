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

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.net.url.UrlQuery;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.http.meta.ContentType;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * application/x-www-form-urlencoded 类型请求body封装
 *
 * @author looly
 * @since 5.7.17
 */
public class UrlEncodedFormBody extends FormBody<UrlEncodedFormBody> {

	private byte[] content;

	/**
	 * 创建 Http request body
	 *
	 * @param form    表单
	 * @param charset 编码
	 * @return FormUrlEncodedBody
	 */
	public static UrlEncodedFormBody of(final Map<String, Object> form, final Charset charset) {
		return new UrlEncodedFormBody(form, charset);
	}

	/**
	 * 构造
	 *
	 * @param form    表单
	 * @param charset 编码
	 */
	public UrlEncodedFormBody(final Map<String, Object> form, final Charset charset) {
		super(form, charset);
	}

	@Override
	public UrlEncodedFormBody form(final String name, final Object value) {
		// 有新键值对加入时，清空生成的字节
		if(null != this.content){
			this.content = null;
		}
		return super.form(name, value);
	}

	@Override
	public void write(final OutputStream out) {
		IoUtil.write(out, getGeneratedBytes());
	}

	@Override
	public String contentType() {
		return ContentType.FORM_URLENCODED.toString(charset);
	}

	@Override
	public long contentLength() {
		return getGeneratedBytes().length;
	}

	/**
	 * 获取生成的字节数组
	 *
	 * @return 生成的字节数组
	 */
	private byte[] getGeneratedBytes() {
		if (null == this.content) {
			this.content = ByteUtil.toBytes(UrlQuery.of(form, UrlQuery.EncodeMode.FORM_URL_ENCODED).build(charset), charset);
		}
		return this.content;
	}
}
