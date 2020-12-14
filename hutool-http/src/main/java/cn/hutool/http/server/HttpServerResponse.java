package cn.hutool.http.server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Http响应对象，用于写出数据到客户端
 */
public class HttpServerResponse extends HttpServerBase {

	private Charset charset;
	/**
	 * 是否已经发送了Http状态码，如果没有，提前写出状态码
	 */
	private boolean isSendCode;

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
	 * 发送成功状态码
	 *
	 * @return this
	 */
	public HttpServerResponse sendOk() {
		return send(HttpStatus.HTTP_OK);
	}

	/**
	 * 发送404错误页
	 *
	 * @param content 错误页页面内容，默认text/html类型
	 * @return this
	 */
	public HttpServerResponse send404(String content) {
		return sendError(HttpStatus.HTTP_NOT_FOUND, content);
	}

	/**
	 * 发送错误页
	 *
	 * @param errorCode HTTP错误状态码，见HttpStatus
	 * @param content   错误页页面内容，默认text/html类型
	 * @return this
	 */
	public HttpServerResponse sendError(int errorCode, String content) {
		send(errorCode);
		setContentType(ContentType.TEXT_HTML.toString());
		return write(content);
	}

	/**
	 * 发送HTTP状态码
	 *
	 * @param httpStatusCode HTTP状态码，见HttpStatus
	 * @param bodyLength     响应体长度，默认0
	 * @return this
	 */
	public HttpServerResponse send(int httpStatusCode, long bodyLength) {
		if (this.isSendCode) {
			throw new IORuntimeException("Http status code has been send!");
		}

		try {
			this.httpExchange.sendResponseHeaders(httpStatusCode, bodyLength);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		this.isSendCode = true;
		return this;
	}

	/**
	 * 获得所有响应头，获取后可以添加新的响应头
	 *
	 * @return 响应头
	 */
	public Headers getHeaders() {
		return this.httpExchange.getResponseHeaders();
	}

	/**
	 * 添加响应头，如果已经存在，则追加
	 *
	 * @param header 头key
	 * @param value  值
	 * @return this
	 */
	public HttpServerResponse addHeader(String header, String value) {
		getHeaders().add(header, value);
		return this;
	}

	/**
	 * 设置响应头，如果已经存在，则覆盖
	 *
	 * @param header 头key
	 * @param value  值
	 * @return this
	 */
	public HttpServerResponse setHeader(Header header, String value) {
		return setHeader(header.getValue(), value);
	}

	/**
	 * 设置响应头，如果已经存在，则覆盖
	 *
	 * @param header 头key
	 * @param value  值
	 * @return this
	 */
	public HttpServerResponse setHeader(String header, String value) {
		getHeaders().set(header, value);
		return this;
	}

	/**
	 * 设置响应头，如果已经存在，则覆盖
	 *
	 * @param header 头key
	 * @param value  值列表
	 * @return this
	 */
	public HttpServerResponse setHeader(String header, List<String> value) {
		getHeaders().put(header, value);
		return this;
	}

	/**
	 * 设置所有响应头，如果已经存在，则覆盖
	 *
	 * @param headers 响应头map
	 * @return this
	 */
	public HttpServerResponse setHeaders(Map<String, List<String>> headers) {
		getHeaders().putAll(headers);
		return this;
	}

	/**
	 * 设置Content-Type头，类似于:text/html;charset=utf-8<br>
	 * 如果用户传入的信息无charset信息，自动根据charset补充，charset设置见{@link #setCharset(Charset)}
	 *
	 * @param contentType Content-Type头内容
	 * @return this
	 */
	public HttpServerResponse setContentType(String contentType) {
		if (null != contentType && null != this.charset) {
			if (false == contentType.contains(";charset=")) {
				contentType = ContentType.build(contentType, this.charset);
			}
		}

		return setHeader(Header.CONTENT_TYPE, contentType);
	}

	/**
	 * 设置Content-Length头
	 *
	 * @param contentLength Content-Length头内容
	 * @return this
	 */
	public HttpServerResponse setContentLength(long contentLength) {
		return setHeader(Header.CONTENT_LENGTH, String.valueOf(contentLength));
	}

	/**
	 * 设置响应的编码
	 *
	 * @param charset 编码
	 * @return this
	 */
	public HttpServerResponse setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * 设置属性
	 *
	 * @param name  属性名
	 * @param value 属性值
	 * @return this
	 */
	public HttpServerResponse setAttr(String name, Object value) {
		this.httpExchange.setAttribute(name, value);
		return this;
	}

	/**
	 * 获取响应数据流
	 *
	 * @return 响应数据流
	 */
	public OutputStream getOut() {
		if (false == this.isSendCode) {
			sendOk();
		}
		return this.httpExchange.getResponseBody();
	}

	/**
	 * 获取响应数据流
	 *
	 * @return 响应数据流
	 */
	public PrintWriter getWriter() {
		final Charset charset = ObjectUtil.defaultIfNull(this.charset, DEFAULT_CHARSET);
		return new PrintWriter(new OutputStreamWriter(getOut(), charset));
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data        数据
	 * @param contentType Content-Type类型
	 * @return this
	 */
	public HttpServerResponse write(String data, String contentType) {
		setContentType(contentType);
		return write(data);
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data 数据
	 * @return this
	 */
	public HttpServerResponse write(String data) {
		final Charset charset = ObjectUtil.defaultIfNull(this.charset, DEFAULT_CHARSET);
		return write(StrUtil.bytes(data, charset));
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data        数据
	 * @param contentType 返回的类型
	 * @return this
	 */
	public HttpServerResponse write(byte[] data, String contentType) {
		setContentType(contentType);
		return write(data);
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data 数据
	 * @return this
	 */
	public HttpServerResponse write(byte[] data) {
		return write(new ByteArrayInputStream(data));
	}

	/**
	 * 返回数据给客户端
	 *
	 * @param in          需要返回客户端的内容
	 * @param contentType 返回的类型
	 * @return this
	 * @since 5.2.6
	 */
	public HttpServerResponse write(InputStream in, String contentType) {
		setContentType(contentType);
		return write(in);
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

	/**
	 * 返回文件给客户端（文件下载）
	 *
	 * @param file 写出的文件对象
	 * @return this
	 * @since 5.2.6
	 */
	public HttpServerResponse write(File file) {
		final String fileName = file.getName();
		final String contentType = ObjectUtil.defaultIfNull(HttpUtil.getMimeType(fileName), "application/octet-stream");
		BufferedInputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			write(in, contentType, fileName);
		} finally {
			IoUtil.close(in);
		}
		return this;
	}

	/**
	 * 返回文件数据给客户端（文件下载）
	 *
	 * @param in          需要返回客户端的内容
	 * @param contentType 返回的类型
	 * @param fileName    文件名
	 * @since 5.2.6
	 */
		public void write(InputStream in, String contentType, String fileName) {
		final Charset charset = ObjectUtil.defaultIfNull(this.charset, DEFAULT_CHARSET);

		if(false == contentType.startsWith("text/")){
			// 非文本类型数据直接走下载
			setHeader(Header.CONTENT_DISPOSITION, StrUtil.format("attachment;filename={}", URLUtil.encode(fileName, charset)));
		}
		write(in, contentType);
	}
}
