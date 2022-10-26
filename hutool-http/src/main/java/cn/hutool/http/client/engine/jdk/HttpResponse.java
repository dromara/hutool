package cn.hutool.http.client.engine.jdk;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.stream.FastByteArrayOutputStream;
import cn.hutool.core.text.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.client.Response;
import cn.hutool.http.client.body.ResponseBody;
import cn.hutool.http.client.cookie.GlobalCookieManager;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;

/**
 * Http响应类<br>
 * 非线程安全对象
 *
 * @author Looly
 */
public class HttpResponse extends HttpBase<HttpResponse> implements Response, Closeable {

	/**
	 * 是否忽略响应读取时可能的EOF异常。<br>
	 * 在Http协议中，对于Transfer-Encoding: Chunked在正常情况下末尾会写入一个Length为0的的chunk标识完整结束。<br>
	 * 如果服务端未遵循这个规范或响应没有正常结束，会报EOF异常，此选项用于是否忽略这个异常。
	 */
	protected boolean ignoreEOFError;
	/**
	 * 持有连接对象
	 */
	protected HttpConnection httpConnection;
	/**
	 * Http请求原始流
	 */
	protected InputStream in;
	/**
	 * 是否异步，异步下只持有流，否则将在初始化时直接读取body内容
	 */
	private volatile boolean isAsync;
	/**
	 * 响应状态码
	 */
	protected int status;
	/**
	 * 是否忽略读取Http响应体
	 */
	private final boolean ignoreBody;
	/**
	 * 从响应中获取的编码
	 */
	private Charset charsetFromResponse;

	/**
	 * 构造
	 *
	 * @param httpConnection {@link HttpConnection}
	 * @param ignoreEOFError 是否忽略响应读取时可能的EOF异常
	 * @param charset        编码，从请求编码中获取默认编码
	 * @param isAsync        是否异步
	 * @param isIgnoreBody   是否忽略读取响应体
	 */
	protected HttpResponse(final HttpConnection httpConnection, final boolean ignoreEOFError, final Charset charset, final boolean isAsync, final boolean isIgnoreBody) {
		this.httpConnection = httpConnection;
		this.ignoreEOFError = ignoreEOFError;
		this.charset = charset;
		this.isAsync = isAsync;
		this.ignoreBody = isIgnoreBody;
		initWithDisconnect();
	}

	/**
	 * 获取状态码
	 *
	 * @return 状态码
	 */
	@Override
	public int getStatus() {
		return this.status;
	}

	/**
	 * 同步<br>
	 * 如果为异步状态，则暂时不读取服务器中响应的内容，而是持有Http链接的{@link InputStream}。<br>
	 * 当调用此方法时，异步状态转为同步状态，此时从Http链接流中读取body内容并暂存在内容中。如果已经是同步状态，则不进行任何操作。
	 *
	 * @return this
	 */
	public HttpResponse sync() {
		return this.isAsync ? forceSync() : this;
	}

	// ---------------------------------------------------------------- Http Response Header start

	/**
	 * 获取Cookie
	 *
	 * @return Cookie列表
	 * @since 3.1.1
	 */
	public List<HttpCookie> getCookies() {
		return GlobalCookieManager.getCookies(this.httpConnection);
	}

	/**
	 * 获取Cookie
	 *
	 * @param name Cookie名
	 * @return {@link HttpCookie}
	 * @since 4.1.4
	 */
	public HttpCookie getCookie(final String name) {
		final List<HttpCookie> cookie = getCookies();
		if (null != cookie) {
			for (final HttpCookie httpCookie : cookie) {
				if (httpCookie.getName().equals(name)) {
					return httpCookie;
				}
			}
		}
		return null;
	}

	/**
	 * 获取Cookie值
	 *
	 * @param name Cookie名
	 * @return Cookie值
	 * @since 4.1.4
	 */
	public String getCookieValue(final String name) {
		final HttpCookie cookie = getCookie(name);
		return (null == cookie) ? null : cookie.getValue();
	}
	// ---------------------------------------------------------------- Http Response Header end

	// ---------------------------------------------------------------- Body start

	/**
	 * 获得服务区响应流<br>
	 * 异步模式下获取Http原生流，同步模式下获取获取到的在内存中的副本<br>
	 * 如果想在同步模式下获取流，请先调用{@link #sync()}方法强制同步<br>
	 * 流获取后处理完毕需关闭此类
	 *
	 * @return 响应流
	 */
	@Override
	public InputStream bodyStream() {
		if (isAsync) {
			return this.in;
		}
		return new ByteArrayInputStream(this.bodyBytes);
	}

	@Override
	public ResponseBody body() {
		return new ResponseBody(this, this.ignoreEOFError);
	}

	/**
	 * 获取响应流字节码<br>
	 * 此方法会转为同步模式
	 *
	 * @return byte[]
	 */
	@SuppressWarnings("resource")
	@Override
	public byte[] bodyBytes() {
		sync();
		return this.bodyBytes;
	}

	/**
	 * 设置主体字节码，一般用于拦截器修改响应内容<br>
	 * 需在此方法调用前使用charset方法设置编码，否则使用默认编码UTF-8
	 *
	 * @param bodyBytes 主体
	 * @return this
	 */
	@SuppressWarnings("resource")
	public HttpResponse body(final byte[] bodyBytes) {
		sync();
		if (null != bodyBytes) {
			this.bodyBytes = bodyBytes;
		}
		return this;
	}
	// ---------------------------------------------------------------- Body end

	@Override
	public void close() {
		IoUtil.close(this.in);
		this.in = null;
		// 关闭连接
		this.httpConnection.disconnectQuietly();
	}

	@Override
	public String toString() {
		final StringBuilder sb = StrUtil.builder();
		sb.append("Response Headers: ").append(StrUtil.CRLF);
		for (final Entry<String, List<String>> entry : this.headers.entrySet()) {
			sb.append("    ").append(entry).append(StrUtil.CRLF);
		}

		sb.append("Response Body: ").append(StrUtil.CRLF);
		sb.append("    ").append(this.body()).append(StrUtil.CRLF);

		return sb.toString();
	}

	@Override
	public String header(final String name) {
		return super.header(name);
	}

	// ---------------------------------------------------------------- Private method start

	/**
	 * 初始化Http响应，并在报错时关闭连接。<br>
	 * 初始化包括：
	 *
	 * <pre>
	 * 1、读取Http状态
	 * 2、读取头信息
	 * 3、持有Http流，并不关闭流
	 * </pre>
	 *
	 * @throws HttpException IO异常
	 */
	private void initWithDisconnect() throws HttpException {
		try {
			init();
		} catch (final HttpException e) {
			this.httpConnection.disconnectQuietly();
			throw e;
		}
	}

	/**
	 * 初始化Http响应<br>
	 * 初始化包括：
	 *
	 * <pre>
	 * 1、读取Http状态
	 * 2、读取头信息
	 * 3、持有Http流，并不关闭流
	 * </pre>
	 *
	 * @throws HttpException IO异常
	 */
	@SuppressWarnings("resource")
	private void init() throws HttpException {
		// 获取响应状态码
		try {
			this.status = httpConnection.getCode();
		} catch (final IOException e) {
			if (false == (e instanceof FileNotFoundException)) {
				throw new HttpException(e);
			}
			// 服务器无返回内容，忽略之
		}

		// 读取响应头信息
		try {
			this.headers = httpConnection.headers();
		} catch (final IllegalArgumentException e) {
			// ignore
			// StaticLog.warn(e, e.getMessage());
		}


		// 存储服务端设置的Cookie信息
		GlobalCookieManager.store(httpConnection, this.headers);

		// 获取响应编码，如果非空，替换用户定义的编码
		final Charset charsetFromResponse = httpConnection.getCharset();
		if (null != charsetFromResponse) {
			this.charset = charsetFromResponse;
		}

		// 获取响应内容流
		this.in = new HttpInputStream(this);

		// 同步情况下强制同步
		if (!this.isAsync) {
			forceSync();
		}
	}

	/**
	 * 强制同步，用于初始化<br>
	 * 强制同步后变化如下：
	 *
	 * <pre>
	 * 1、读取body内容到内存
	 * 2、异步状态设为false（变为同步状态）
	 * 3、关闭Http流
	 * 4、断开与服务器连接
	 * </pre>
	 *
	 * @return this
	 */
	private HttpResponse forceSync() {
		// 非同步状态转为同步状态
		try {
			this.readBody(this.in);
		} catch (final IORuntimeException e) {
			//noinspection StatementWithEmptyBody
			if (e.getCause() instanceof FileNotFoundException) {
				// 服务器无返回内容，忽略之
			} else {
				throw new HttpException(e);
			}
		} finally {
			if (this.isAsync) {
				this.isAsync = false;
			}
			this.close();
		}
		return this;
	}

	/**
	 * 读取主体，忽略EOFException异常
	 *
	 * @param in 输入流
	 * @throws IORuntimeException IO异常
	 */
	private void readBody(final InputStream in) throws IORuntimeException {
		if (ignoreBody) {
			return;
		}

		final long contentLength = contentLength();
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream((int) contentLength);
		body().writeClose(out);
		this.bodyBytes = out.toByteArray();
	}
	// ---------------------------------------------------------------- Private method end
}
