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
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.http.meta.ContentType;
import org.dromara.hutool.http.HttpGlobalConfig;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Multipart/form-data数据的请求体封装<br>
 * 遵循RFC2387规范，见：https://www.rfc-editor.org/rfc/rfc2387
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
	public String contentType() {
		return CONTENT_TYPE_MULTIPART_PREFIX + boundary;
	}

	@Override
	public String contentType(final Charset charset) {
		// multipart的Content-Type头指定"Content-Type; boundary=XXXX"，编码无效
		return contentType();
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
