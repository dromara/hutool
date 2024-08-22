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

package org.dromara.hutool.http.client.engine.httpclient4;

import org.apache.http.entity.AbstractHttpEntity;
import org.dromara.hutool.http.client.body.BytesBody;
import org.dromara.hutool.http.client.body.HttpBody;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link HttpBody}转换为{@link org.apache.hc.core5.http.HttpEntity}对象
 *
 * @author looly
 * @since 6.0.0
 */
public class HttpClient4BodyEntity extends AbstractHttpEntity {

	private final HttpBody body;

	/**
	 * 构造
	 *
	 * @param contentType Content-Type类型
	 * @param contentEncoding 压缩媒体类型，如gzip、deflate等
	 * @param chunked     是否块模式传输
	 * @param body        {@link HttpBody}
	 */
	public HttpClient4BodyEntity(final String contentType, final String contentEncoding, final boolean chunked, final HttpBody body) {
		super();
		setContentType(contentType);
		setContentEncoding(contentEncoding);
		setChunked(chunked);
		this.body = body;
	}

	@Override
	public void writeTo(final OutputStream outStream) {
		if (null != body) {
			body.writeClose(outStream);
		}
	}

	@Override
	public InputStream getContent() {
		return body.getStream();
	}

	@Override
	public boolean isStreaming() {
		return body instanceof BytesBody;
	}

	@Override
	public boolean isRepeatable() {
		return false;
	}

	@Override
	public long getContentLength() {
		return body.contentLength();
	}
}
