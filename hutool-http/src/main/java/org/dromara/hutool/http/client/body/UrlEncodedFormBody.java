/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
	public void write(final OutputStream out) {
		final byte[] bytes = ByteUtil.toBytes(UrlQuery.of(form, UrlQuery.EncodeMode.FORM_URL_ENCODED).build(charset), charset);
		IoUtil.write(out, false, bytes);
	}

	@Override
	public String getContentType() {
		return ContentType.FORM_URLENCODED.toString(charset);
	}
}
