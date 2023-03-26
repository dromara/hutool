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

package cn.hutool.http.client.engine.okhttp;

import cn.hutool.http.client.body.HttpBody;
import okhttp3.MediaType;
import okio.BufferedSink;

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

	public MediaType contentType() {
		return null;
	}

	@Override
	public void writeTo(final BufferedSink bufferedSink) {
		body.writeClose(bufferedSink.outputStream());
	}
}
