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

import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import org.dromara.hutool.core.io.stream.EmptyInputStream;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.GlobalCompressStreamRegister;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.meta.HeaderName;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * OkHttp3的{@link okhttp3.Response} 响应包装
 *
 * @author looly
 */
public class OkHttpResponse implements Response {

	private final okhttp3.Response rawRes;
	/**
	 * 请求时的默认编码
	 */
	private final Charset requestCharset;

	/**
	 * @param rawRes         {@link okhttp3.Response}
	 * @param requestCharset 请求时的默认编码
	 */
	public OkHttpResponse(final okhttp3.Response rawRes, final Charset requestCharset) {
		this.rawRes = rawRes;
		this.requestCharset = requestCharset;
	}

	@Override
	public int getStatus() {
		return rawRes.code();
	}

	@Override
	public String header(final String name) {
		return rawRes.header(name);
	}

	@Override
	public Map<String, List<String>> headers() {
		return rawRes.headers().toMultimap();
	}

	@Override
	public Charset charset() {
		return ObjUtil.defaultIfNull(Response.super.charset(), requestCharset);
	}

	@Override
	public InputStream bodyStream() {
		final ResponseBody body = rawRes.body();
		if(null == body){
			return EmptyInputStream.INSTANCE;
		}

		return GlobalCompressStreamRegister.INSTANCE.wrapStream(body.byteStream(),
			this.rawRes.header(HeaderName.CONTENT_ENCODING.getValue()));
	}

	@Override
	public void close() {
		if(null != this.rawRes){
			rawRes.close();
		}
	}

	@Override
	public String toString() {
		return HttpUtil.toString(this);
	}
}
