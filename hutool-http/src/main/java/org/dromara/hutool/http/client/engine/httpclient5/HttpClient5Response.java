/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.http.client.engine.httpclient5;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.client.Response;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpClient响应包装<br>
 * 通过包装{@link CloseableHttpResponse}，实现获取响应状态码、响应头、响应体等内容
 *
 * @author looly
 */
public class HttpClient5Response implements Response {

	/**
	 * HttpClient的响应对象
	 */
	private final ClassicHttpResponse rawRes;
	/**
	 * 请求时的默认编码
	 */
	private final Charset requestCharset;

	/**
	 * 构造<br>
	 * 通过传入一个请求时的编码，当无法获取响应内容的编码时，默认使用响应时的编码
	 *
	 * @param rawRes         {@link CloseableHttpResponse}
	 * @param requestCharset 请求时的编码
	 */
	public HttpClient5Response(final ClassicHttpResponse rawRes, final Charset requestCharset) {
		this.rawRes = rawRes;
		this.requestCharset = requestCharset;
	}


	@Override
	public int getStatus() {
		return rawRes.getCode();
	}

	@Override
	public String header(final String name) {
		final Header[] headers = rawRes.getHeaders(name);
		if (ArrayUtil.isNotEmpty(headers)) {
			return headers[0].getValue();
		}

		return null;
	}

	@Override
	public Map<String, List<String>> headers() {
		final Header[] headers = rawRes.getHeaders();
		final HashMap<String, List<String>> result = new LinkedHashMap<>(headers.length, 1);
		for (final Header header : headers) {
			final List<String> valueList = result.computeIfAbsent(header.getName(), k -> new ArrayList<>());
			valueList.add(header.getValue());
		}
		return result;
	}

	@Override
	public long contentLength() {
		return rawRes.getEntity().getContentLength();
	}

	@Override
	public Charset charset() {
		return ObjUtil.defaultIfNull(Response.super.charset(), requestCharset);
	}

	@Override
	public InputStream bodyStream() {
		try {
			return rawRes.getEntity().getContent();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public String bodyStr() throws HttpException {
		try {
			return EntityUtils.toString(rawRes.getEntity(), charset());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} catch (final ParseException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public void close() throws IOException {
		rawRes.close();
	}

	@Override
	public String toString() {
		return HttpUtil.toString(this);
	}
}
