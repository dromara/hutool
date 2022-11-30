package cn.hutool.http.client.engine.okhttp;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.client.ClientConfig;
import cn.hutool.http.client.ClientEngine;
import cn.hutool.http.client.Request;
import cn.hutool.http.client.Response;
import okhttp3.OkHttpClient;
import okhttp3.internal.http.HttpMethod;

import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp3客户端引擎封装
 *
 * @author looly
 * @since 6.0.0
 */
public class OkHttpEngine implements ClientEngine {

	private ClientConfig config;
	private OkHttpClient client;

	/**
	 * 构造
	 */
	public OkHttpEngine() {
		this.client = new OkHttpClient();
	}

	@Override
	public OkHttpEngine setConfig(final ClientConfig config) {
		this.config = config;
		// 重置客户端
		this.client = null;
		return this;
	}

	@Override
	public Response send(final Request message) {
		initEngine();

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
	 * 初始化引擎
	 */
	private void initEngine() {
		if (null != this.client) {
			return;
		}

		final ClientConfig config = ObjUtil.defaultIfNull(this.config, ClientConfig::of);
		final OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectTimeout(config.getConnectionTimeout(), TimeUnit.MILLISECONDS)
				.readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS);

		// 设置代理
		final Proxy proxy = config.getProxy();
		if(null != proxy){
			builder.proxy(proxy);
		}

		this.client = builder.build();
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
		if (HttpMethod.permitsRequestBody(method)) {
			builder.method(method, new OkHttpRequestBody(message.body()));
		} else {
			builder.method(method, null);
		}

		return builder.build();
	}
}
