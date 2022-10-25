package cn.hutool.http.client.engine.okhttp;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.http.client.ClientEngine;
import cn.hutool.http.client.Request;
import cn.hutool.http.client.Response;
import okhttp3.OkHttpClient;
import okhttp3.internal.http.HttpMethod;

import java.io.IOException;

/**
 * OkHttp3客户端引擎封装
 *
 * @author looly
 * @since 6.0.0
 */
public class OkHttpEngine implements ClientEngine {

	private final OkHttpClient client;

	/**
	 * 构造
	 */
	public OkHttpEngine() {
		this.client = new OkHttpClient();
	}

	@Override
	public Response send(final Request message) {
		final okhttp3.Response response;
		try {
			response = client.newCall(buildRequest(message)).execute();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		return new OkHttpResponse(response, message.charset());
	}

	@Override
	public Object getRawEngine() {
		return this.client;
	}

	@Override
	public void close() throws IOException {
		// ignore
	}

	/**
	 * 构建请求体
	 *
	 * @param message {@link Request}
	 * @return {@link okhttp3.Request}
	 */
	private static okhttp3.Request buildRequest(final Request message) {
		final okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
				.url(message.url().toURL());

		final String method = message.method().name();
		if(HttpMethod.permitsRequestBody(method)){
			builder.method(method, new OkHttpRequestBody(message.body()));
		}else{
			builder.method(method, null);
		}

		return builder.build();
	}
}
