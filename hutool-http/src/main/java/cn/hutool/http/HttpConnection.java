package cn.hutool.http;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ssl.DefaultSSLInfo;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http连接对象，对HttpURLConnection的包装
 *
 * @author Looly
 */
public class HttpConnection {

	private final URL url;
	private final Proxy proxy;
	private HttpURLConnection conn;

	/**
	 * 创建HttpConnection
	 *
	 * @param urlStr URL
	 * @param proxy  代理，无代理传{@code null}
	 * @return HttpConnection
	 */
	public static HttpConnection create(String urlStr, Proxy proxy) {
		return create(URLUtil.toUrlForHttp(urlStr), proxy);
	}

	/**
	 * 创建HttpConnection
	 *
	 * @param url   URL
	 * @param proxy 代理，无代理传{@code null}
	 * @return HttpConnection
	 */
	public static HttpConnection create(URL url, Proxy proxy) {
		return new HttpConnection(url, proxy);
	}

	// --------------------------------------------------------------- Constructor start

	/**
	 * 构造HttpConnection
	 *
	 * @param url   URL
	 * @param proxy 代理
	 */
	public HttpConnection(URL url, Proxy proxy) {
		this.url = url;
		this.proxy = proxy;

		// 初始化Http连接
		initConn();
	}

	// --------------------------------------------------------------- Constructor end

	/**
	 * 初始化连接相关信息
	 *
	 * @return HttpConnection
	 * @since 4.4.1
	 */
	public HttpConnection initConn() {
		try {
			this.conn = openHttp();
		} catch (IOException e) {
			throw new HttpException(e);
		}

		// 默认读取响应内容
		this.conn.setDoInput(true);

		return this;
	}

	// --------------------------------------------------------------- Getters And Setters start

	/**
	 * 获取请求方法,GET/POST
	 *
	 * @return 请求方法, GET/POST
	 */
	public Method getMethod() {
		return Method.valueOf(this.conn.getRequestMethod());
	}

	/**
	 * 设置请求方法
	 *
	 * @param method 请求方法
	 * @return 自己
	 */
	public HttpConnection setMethod(Method method) {
		if (Method.POST.equals(method) //
				|| Method.PUT.equals(method)//
				|| Method.PATCH.equals(method)//
				|| Method.DELETE.equals(method)) {
			this.conn.setUseCaches(false);

			// 增加PATCH方法支持
			if (Method.PATCH.equals(method)) {
				allowPatch();
			}
		}

		// method
		try {
			this.conn.setRequestMethod(method.toString());
		} catch (ProtocolException e) {
			throw new HttpException(e);
		}

		return this;
	}

	/**
	 * 获取请求URL
	 *
	 * @return 请求URL
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * 获得代理
	 *
	 * @return {@link Proxy}
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * 获取HttpURLConnection对象
	 *
	 * @return HttpURLConnection
	 */
	public HttpURLConnection getHttpURLConnection() {
		return conn;
	}

	// --------------------------------------------------------------- Getters And Setters end

	// ---------------------------------------------------------------- Headers start

	/**
	 * 设置请求头<br>
	 * 当请求头存在时，覆盖之
	 *
	 * @param header     头名
	 * @param value      头值
	 * @param isOverride 是否覆盖旧值
	 * @return HttpConnection
	 */
	public HttpConnection header(String header, String value, boolean isOverride) {
		if (null != this.conn) {
			if (isOverride) {
				this.conn.setRequestProperty(header, value);
			} else {
				this.conn.addRequestProperty(header, value);
			}
		}

		return this;
	}

	/**
	 * 设置请求头<br>
	 * 当请求头存在时，覆盖之
	 *
	 * @param header     头名
	 * @param value      头值
	 * @param isOverride 是否覆盖旧值
	 * @return HttpConnection
	 */
	public HttpConnection header(Header header, String value, boolean isOverride) {
		return header(header.toString(), value, isOverride);
	}

	/**
	 * 设置请求头<br>
	 * 不覆盖原有请求头
	 *
	 * @param headerMap  请求头
	 * @param isOverride 是否覆盖
	 * @return this
	 */
	public HttpConnection header(Map<String, List<String>> headerMap, boolean isOverride) {
		if (MapUtil.isNotEmpty(headerMap)) {
			String name;
			for (Entry<String, List<String>> entry : headerMap.entrySet()) {
				name = entry.getKey();
				for (String value : entry.getValue()) {
					this.header(name, StrUtil.nullToEmpty(value), isOverride);
				}
			}
		}
		return this;
	}

	/**
	 * 获取Http请求头
	 *
	 * @param name Header名
	 * @return Http请求头值
	 */
	public String header(String name) {
		return this.conn.getHeaderField(name);
	}

	/**
	 * 获取Http请求头
	 *
	 * @param name Header名
	 * @return Http请求头值
	 */
	public String header(Header name) {
		return header(name.toString());
	}

	/**
	 * 获取所有Http请求头
	 *
	 * @return Http请求头Map
	 */
	public Map<String, List<String>> headers() {
		return this.conn.getHeaderFields();
	}

	// ---------------------------------------------------------------- Headers end

	/**
	 * 设置https请求参数<br>
	 * 有些时候htts请求会出现com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl的实现，此为sun内部api，按照普通http请求处理
	 *
	 * @param hostnameVerifier 域名验证器，非https传入null
	 * @param ssf              SSLSocketFactory，非https传入null
	 * @return this
	 * @throws HttpException KeyManagementException和NoSuchAlgorithmException异常包装
	 */
	public HttpConnection setHttpsInfo(HostnameVerifier hostnameVerifier, SSLSocketFactory ssf) throws HttpException {
		final HttpURLConnection conn = this.conn;

		if (conn instanceof HttpsURLConnection) {
			// Https请求
			final HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
			// 验证域
			httpsConn.setHostnameVerifier(ObjectUtil.defaultIfNull(hostnameVerifier, DefaultSSLInfo.TRUST_ANY_HOSTNAME_VERIFIER));
			httpsConn.setSSLSocketFactory(ObjectUtil.defaultIfNull(ssf, DefaultSSLInfo.DEFAULT_SSF));
		}

		return this;
	}

	/**
	 * 关闭缓存
	 *
	 * @return this
	 * @see HttpURLConnection#setUseCaches(boolean)
	 */
	public HttpConnection disableCache() {
		this.conn.setUseCaches(false);
		return this;
	}

	/**
	 * 设置连接超时
	 *
	 * @param timeout 超时
	 * @return this
	 */
	public HttpConnection setConnectTimeout(int timeout) {
		if (timeout > 0 && null != this.conn) {
			this.conn.setConnectTimeout(timeout);
		}

		return this;
	}

	/**
	 * 设置读取超时
	 *
	 * @param timeout 超时
	 * @return this
	 */
	public HttpConnection setReadTimeout(int timeout) {
		if (timeout > 0 && null != this.conn) {
			this.conn.setReadTimeout(timeout);
		}

		return this;
	}

	/**
	 * 设置连接和读取的超时时间
	 *
	 * @param timeout 超时时间
	 * @return this
	 */
	public HttpConnection setConnectionAndReadTimeout(int timeout) {
		setConnectTimeout(timeout);
		setReadTimeout(timeout);

		return this;
	}

	/**
	 * 设置Cookie
	 *
	 * @param cookie Cookie
	 * @return this
	 */
	public HttpConnection setCookie(String cookie) {
		if (cookie != null) {
			header(Header.COOKIE, cookie, true);
		}
		return this;
	}

	/**
	 * 采用流方式上传数据，无需本地缓存数据。<br>
	 * HttpUrlConnection默认是将所有数据读到本地缓存，然后再发送给服务器，这样上传大文件时就会导致内存溢出。
	 *
	 * @param blockSize 块大小（bytes数），0或小于0表示不设置Chuncked模式
	 * @return this
	 */
	public HttpConnection setChunkedStreamingMode(int blockSize) {
		if (blockSize > 0) {
			conn.setChunkedStreamingMode(blockSize);
		}
		return this;
	}

	/**
	 * 设置自动HTTP 30X跳转
	 *
	 * @param isInstanceFollowRedirects 是否自定跳转
	 * @return this
	 */
	public HttpConnection setInstanceFollowRedirects(boolean isInstanceFollowRedirects) {
		conn.setInstanceFollowRedirects(isInstanceFollowRedirects);
		return this;
	}

	/**
	 * 连接
	 *
	 * @return this
	 * @throws IOException IO异常
	 */
	public HttpConnection connect() throws IOException {
		if (null != this.conn) {
			this.conn.connect();
		}
		return this;
	}

	/**
	 * 静默断开连接。不抛出异常
	 *
	 * @return this
	 * @since 4.6.0
	 */
	public HttpConnection disconnectQuietly() {
		try {
			disconnect();
		} catch (Throwable e) {
			// ignore
		}

		return this;
	}

	/**
	 * 断开连接
	 *
	 * @return this
	 */
	public HttpConnection disconnect() {
		if (null != this.conn) {
			this.conn.disconnect();
		}
		return this;
	}

	/**
	 * 获得输入流对象<br>
	 * 输入流对象用于读取数据
	 *
	 * @return 输入流对象
	 * @throws IOException IO异常
	 */
	public InputStream getInputStream() throws IOException {
		if (null != this.conn) {
			return this.conn.getInputStream();
		}
		return null;
	}

	/**
	 * 当返回错误代码时，获得错误内容流
	 *
	 * @return 错误内容
	 */
	public InputStream getErrorStream() {
		if (null != this.conn) {
			return this.conn.getErrorStream();
		}
		return null;
	}

	/**
	 * 获取输出流对象 输出流对象用于发送数据
	 *
	 * @return OutputStream
	 * @throws IOException IO异常
	 */
	public OutputStream getOutputStream() throws IOException {
		if (null == this.conn) {
			throw new IOException("HttpURLConnection has not been initialized.");
		}

		final Method method = getMethod();

		// 当有写出需求时，自动打开之
		this.conn.setDoOutput(true);
		final OutputStream out = this.conn.getOutputStream();

		// 解决在Rest请求中，GET请求附带body导致GET请求被强制转换为POST
		// 在sun.net.www.protocol.http.HttpURLConnection.getOutputStream0方法中，会把GET方法
		// 修改为POST，而且无法调用setRequestMethod方法修改，因此此处使用反射强制修改字段属性值
		// https://stackoverflow.com/questions/978061/http-get-with-request-body/983458
		if(method == Method.GET && method != getMethod()){
			ReflectUtil.setFieldValue(this.conn, "method", Method.GET.name());
		}

		return out;
	}

	/**
	 * 获取响应码
	 *
	 * @return 响应码
	 * @throws IOException IO异常
	 */
	public int responseCode() throws IOException {
		if (null != this.conn) {
			return this.conn.getResponseCode();
		}
		return 0;
	}

	/**
	 * 获得字符集编码<br>
	 * 从Http连接的头信息中获得字符集<br>
	 * 从ContentType中获取
	 *
	 * @return 字符集编码
	 */
	public String getCharsetName() {
		return HttpUtil.getCharset(conn);
	}

	/**
	 * 获取字符集编码<br>
	 * 从Http连接的头信息中获得字符集<br>
	 * 从ContentType中获取
	 *
	 * @return {@link Charset}编码
	 * @since 3.0.9
	 */
	public Charset getCharset() {
		Charset charset = null;
		final String charsetName = getCharsetName();
		if (StrUtil.isNotBlank(charsetName)) {
			try {
				charset = Charset.forName(charsetName);
			} catch (UnsupportedCharsetException e) {
				// ignore
			}
		}
		return charset;
	}

	@Override
	public String toString() {
		StringBuilder sb = StrUtil.builder();
		sb.append("Request URL: ").append(this.url).append(StrUtil.CRLF);
		sb.append("Request Method: ").append(this.getMethod()).append(StrUtil.CRLF);
		// sb.append("Request Headers: ").append(StrUtil.CRLF);
		// for (Entry<String, List<String>> entry : this.conn.getHeaderFields().entrySet()) {
		// sb.append(" ").append(entry).append(StrUtil.CRLF);
		// }

		return sb.toString();
	}

	// --------------------------------------------------------------- Private Method start

	/**
	 * 初始化http或https请求参数<br>
	 * 有些时候https请求会出现com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl的实现，此为sun内部api，按照普通http请求处理
	 *
	 * @return {@link HttpURLConnection}，https返回{@link HttpsURLConnection}
	 */
	private HttpURLConnection openHttp() throws IOException {
		final URLConnection conn = openConnection();
		if (false == conn instanceof HttpURLConnection) {
			// 防止其它协议造成的转换异常
			throw new HttpException("'{}' of URL [{}] is not a http connection, make sure URL is format for http.", conn.getClass().getName(), this.url);
		}

		return (HttpURLConnection) conn;
	}

	/**
	 * 建立连接
	 *
	 * @return {@link URLConnection}
	 * @throws IOException IO异常
	 */
	private URLConnection openConnection() throws IOException {
		return (null == this.proxy) ? url.openConnection() : url.openConnection(this.proxy);
	}

	/**
	 * 增加支持的METHOD方法
	 * see: https://stackoverflow.com/questions/25163131/httpurlconnection-invalid-http-method-patch
	 *
	 * @since 5.1.6
	 */
	private static void allowPatch() {
		final Field methodsField = ReflectUtil.getField(HttpURLConnection.class, "methods");
		if (null != methodsField) {
			// 去除final修饰
			ReflectUtil.setFieldValue(methodsField, "modifiers", methodsField.getModifiers() & ~Modifier.FINAL);
			final String[] methods = {
					"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE", "PATCH"
			};
			ReflectUtil.setFieldValue(null, methodsField, methods);
		}
	}
	// --------------------------------------------------------------- Private Method end
}