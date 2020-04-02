package cn.hutool.http.server;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Http响应对象，用于写出数据到客户端
 */
public class HttpServerResponse extends HttpServerBase{

	/**
	 * 构造
	 *
	 * @param httpExchange {@link HttpExchange}
	 */
	public HttpServerResponse(HttpExchange httpExchange) {
		super(httpExchange);
	}

	/**
	 * 发送HTTP状态码
	 *
	 * @param httpStatusCode HTTP状态码，见HttpStatus
	 * @return this
	 */
	public HttpServerResponse send(int httpStatusCode) {
		return send(httpStatusCode, 0);
	}

	/**
	 * 发送HTTP状态码
	 *
	 * @param httpStatusCode HTTP状态码，见HttpStatus
	 * @param bodyLength     响应体长度，默认0
	 * @return this
	 */
	public HttpServerResponse send(int httpStatusCode, long bodyLength) {
		try {
			this.httpExchange.sendResponseHeaders(httpStatusCode, bodyLength);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 获取响应数据流
	 *
	 * @return 响应数据流
	 */
	public OutputStream getOut() {
		return this.httpExchange.getResponseBody();
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data 数据
	 * @return this
	 */
	public HttpServerResponse write(byte[] data) {
		write(new ByteArrayInputStream(data));
		return this;
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param in 数据流
	 * @return this
	 */
	public HttpServerResponse write(InputStream in) {
		OutputStream out = null;
		try {
			out = getOut();
			IoUtil.copy(in, out);
		} finally {
			IoUtil.close(out);
			IoUtil.close(in);
		}
		return this;
	}
}
