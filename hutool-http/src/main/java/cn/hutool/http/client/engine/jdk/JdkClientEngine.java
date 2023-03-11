package cn.hutool.http.client.engine.jdk;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.client.ClientConfig;
import cn.hutool.http.client.ClientEngine;
import cn.hutool.http.client.Request;
import cn.hutool.http.client.Response;
import cn.hutool.http.client.body.HttpBody;
import cn.hutool.http.client.cookie.GlobalCookieManager;
import cn.hutool.http.meta.Header;
import cn.hutool.http.meta.HttpStatus;
import cn.hutool.http.meta.Method;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * 基于JDK的UrlConnection的Http客户端引擎实现
 *
 * @author looly
 */
public class JdkClientEngine implements ClientEngine {

	private ClientConfig config;
	private HttpConnection conn;
	/**
	 * 重定向次数计数器，内部使用
	 */
	private int redirectCount;

	/**
	 * 构造
	 */
	public JdkClientEngine() {}

	@Override
	public JdkClientEngine setConfig(final ClientConfig config) {
		this.config = config;
		if(null != this.conn){
			this.conn.disconnectQuietly();
			this.conn = null;
		}
		return this;
	}

	@Override
	public Response send(final Request message) {
		return send(message, true);
	}

	/**
	 * 发送请求
	 *
	 * @param message 请求消息
	 * @param isAsync 是否异步，异步不会立即读取响应内容
	 * @return {@link Response}
	 */
	public HttpResponse send(final Request message, final boolean isAsync) {
		initConn(message);
		try {
			doSend(message);
		} catch (final IOException e) {
			// 出错后关闭连接
			IoUtil.close(this);
			throw new RuntimeException(e);
		}

		return sendRedirectIfPossible(message, isAsync);
	}

	@Override
	public Object getRawEngine() {
		return this;
	}

	@Override
	public void close() throws IOException {
		if (null != conn) {
			conn.disconnectQuietly();
		}
	}

	/**
	 * 执行发送
	 *
	 * @param message 请求消息
	 * @throws IOException IO异常
	 */
	private void doSend(final Request message) throws IOException {
		final HttpBody body = message.body();
		if (null != body) {
			// 带有消息体，一律按照Rest方式发送
			body.writeClose(this.conn.getOutputStream());
			return;
		}

		// 非Rest简单GET请求
		this.conn.connect();
	}

	/**
	 * 初始化连接对象
	 *
	 * @param message 请求消息
	 */
	private void initConn(final Request message) {
		// 执行下次请求时自动关闭上次请求（常用于转发）
		IoUtil.close(this);

		this.conn = buildConn(message);
	}

	/**
	 * 构建{@link HttpConnection}
	 *
	 * @param message {@link Request}消息
	 * @return {@link HttpConnection}
	 */
	private HttpConnection buildConn(final Request message) {
		final ClientConfig config = ObjUtil.defaultIfNull(this.config, ClientConfig::of);

		final HttpConnection conn = HttpConnection
				.of(message.url().toURL(), config.getProxy())
				.setConnectTimeout(config.getConnectionTimeout())
				.setReadTimeout(config.getReadTimeout())
				.setMethod(message.method())//
				.setSSLInfo(config.getSslInfo())
				// 关闭JDK自动转发，采用手动转发方式
				.setInstanceFollowRedirects(false)
				.setChunkedStreamingMode(message.isChunked() ? 4096 : -1)
				.setDisableCache(config.isDisableCache())
				// 覆盖默认Header
				.header(message.headers(), true);

		if (null == message.header(Header.COOKIE)) {
			// 用户没有自定义Cookie，则读取全局Cookie信息并附带到请求中
			GlobalCookieManager.add(conn);
		}

		return conn;
	}

	/**
	 * 调用转发，如果需要转发返回转发结果，否则返回{@code null}
	 *
	 * @param isAsync 最终请求是否异步
	 * @return {@link HttpResponse}，无转发返回 {@code null}
	 */
	private HttpResponse sendRedirectIfPossible(final Request message, final boolean isAsync) {
		final HttpConnection conn = this.conn;
		// 手动实现重定向
		if (message.maxRedirectCount() > 0) {
			final int code;
			try {
				code = conn.getCode();
			} catch (final IOException e) {
				// 错误时静默关闭连接
				conn.disconnectQuietly();
				throw new HttpException(e);
			}

			if (code != HttpURLConnection.HTTP_OK) {
				if (HttpStatus.isRedirected(code)) {
					message.url(getLocationUrl(message.url(), conn.header(Header.LOCATION)));
					if (redirectCount < message.maxRedirectCount()) {
						redirectCount++;
						return send(message, isAsync);
					}
				}
			}
		}

		// 最终页面
		return new HttpResponse(this.conn, true, message.charset(), isAsync,
				isIgnoreResponseBody(message.method()));
	}

	/**
	 * 获取转发的新的URL
	 *
	 * @param parentUrl 上级请求的URL
	 * @param location  获取的Location
	 * @return 新的URL
	 */
	private static UrlBuilder getLocationUrl(final UrlBuilder parentUrl, String location) {
		final UrlBuilder redirectUrl;
		if (false == HttpUtil.isHttp(location) && false == HttpUtil.isHttps(location)) {
			// issue#I5TPSY
			// location可能为相对路径
			if (false == location.startsWith("/")) {
				location = StrUtil.addSuffixIfNot(parentUrl.getPathStr(), "/") + location;
			}
			redirectUrl = UrlBuilder.of(parentUrl.getScheme(), parentUrl.getHost(), parentUrl.getPort(),
					location, null, null, parentUrl.getCharset());
		} else {
			redirectUrl = UrlBuilder.ofHttpWithoutEncode(location);
		}

		return redirectUrl;
	}

	/**
	 * 是否忽略读取响应body部分<br>
	 * HEAD、CONNECT、OPTIONS、TRACE方法将不读取响应体
	 *
	 * @return 是否需要忽略响应body部分
	 */
	private static boolean isIgnoreResponseBody(final Method method) {
		return Method.HEAD == method //
				|| Method.CONNECT == method //
				|| Method.OPTIONS == method //
				|| Method.TRACE == method;
	}
}
