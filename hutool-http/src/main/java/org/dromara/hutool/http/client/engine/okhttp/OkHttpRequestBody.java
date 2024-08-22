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

package org.dromara.hutool.http.client.engine.okhttp;

import org.dromara.hutool.http.client.body.HttpBody;
import okhttp3.MediaType;
import okio.BufferedSink;

import java.io.IOException;

/**
 * OkHttp的请求体实现，通过{@link HttpBody}转换实现
 *
 * @author looly
 */
public class OkHttpRequestBody extends okhttp3.RequestBody {

	private final HttpBody body;

	/**
	 * 构造
	 *
	 * @param body 请求体{@link HttpBody}
	 */
	public OkHttpRequestBody(final HttpBody body) {
		this.body = body;
	}

	@Override
	public MediaType contentType() {
		return null;
	}

	@Override
	public long contentLength() throws IOException {
		return this.body.contentLength();
	}

	@Override
	public void writeTo(final BufferedSink bufferedSink) {
		body.writeClose(bufferedSink.outputStream());
	}
}
