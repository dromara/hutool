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
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.http.meta.ContentType;
import org.dromara.hutool.http.HttpGlobalConfig;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Multipart/form-data数据的请求体封装<br>
 * 遵循RFC2388规范
 *
 * @author looly
 * @since 5.3.5
 */
public class MultipartBody extends FormBody<MultipartBody> {

	private static final String CONTENT_TYPE_MULTIPART_PREFIX = ContentType.MULTIPART.getValue() + "; boundary=";

	/**
	 * 边界
	 */
	private final String boundary;

	/**
	 * 根据已有表单内容，构建MultipartBody，使用全局默认的边界符{@link HttpGlobalConfig#getBoundary()}
	 *
	 * @param form    表单
	 * @param charset 编码
	 * @return MultipartBody
	 */
	public static MultipartBody of(final Map<String, Object> form, final Charset charset) {
		return of(form, charset, HttpGlobalConfig.getBoundary());
	}

	/**
	 * 根据已有表单内容，构建MultipartBody
	 *
	 * @param form     表单
	 * @param charset  编码
	 * @param boundary Multipart边界符
	 * @return MultipartBody
	 */
	public static MultipartBody of(final Map<String, Object> form, final Charset charset, final String boundary) {
		return new MultipartBody(form, charset, boundary);
	}

	/**
	 * 构造
	 *
	 * @param form     表单
	 * @param charset  编码
	 * @param boundary Multipart边界符
	 */
	public MultipartBody(final Map<String, Object> form, final Charset charset, final String boundary) {
		super(form, charset);
		this.boundary = boundary;

	}

	/**
	 * 获取Multipart的Content-Type类型
	 *
	 * @return Multipart的Content-Type类型
	 */
	@Override
	public String getContentType() {
		return CONTENT_TYPE_MULTIPART_PREFIX + boundary;
	}

	/**
	 * 写出Multiparty数据，不关闭流
	 *
	 * @param out out流
	 */
	@Override
	public void write(final OutputStream out) {
		final MultipartOutputStream stream = new MultipartOutputStream(out, this.charset, this.boundary);
		if (MapUtil.isNotEmpty(this.form)) {
			this.form.forEach(stream::write);
		}
		stream.finish();
	}

	@Override
	public String toString() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(out);
		return IoUtil.toStr(out, this.charset);
	}
}
