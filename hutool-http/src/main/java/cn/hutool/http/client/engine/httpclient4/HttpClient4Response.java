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

package cn.hutool.http.client.engine.httpclient4;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.array.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.client.Response;
import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

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
public class HttpClient4Response implements Response {

	/**
	 * HttpClient的响应对象
	 */
	private final CloseableHttpResponse rawRes;
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
	public HttpClient4Response(final CloseableHttpResponse rawRes, final Charset requestCharset) {
		this.rawRes = rawRes;
		this.requestCharset = requestCharset;
	}


	@Override
	public int getStatus() {
		return rawRes.getStatusLine().getStatusCode();
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
		final Header[] headers = rawRes.getAllHeaders();
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
}
