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

package org.dromara.hutool.http.client.engine.okhttp;

import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import org.dromara.hutool.core.io.stream.EmptyInputStream;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.GlobalCompressStreamRegister;
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
		final Headers headers = rawRes.headers();
		final HashMap<String, List<String>> result = new LinkedHashMap<>(headers.size(), 1);
		for (final Pair<? extends String, ? extends String> header : headers) {
			final List<String> valueList = result.computeIfAbsent(header.getFirst(), k -> new ArrayList<>());
			valueList.add(header.getSecond());
		}
		return result;
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
}
