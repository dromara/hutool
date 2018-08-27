package cn.hutool.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ssl.AndroidSupportSSLFactory;
import cn.hutool.http.ssl.SSLSocketFactoryBuilder;
import cn.hutool.http.ssl.TrustAnyHostnameVerifier;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * http连接对象，对HttpURLConnection的包装
 * 
 * @author Looly
 *
 */
public class HttpConnection {
	private final static Log log = LogFactory.get();

	private URL url;
	/** method请求方法 */
	private Method method;
	private Proxy proxy;
	private HttpURLConnection conn;

	/**
	 * 创建HttpConnection
	 * 
	 * @param urlStr URL
	 * @param method HTTP方法
	 * @return HttpConnection
	 */
	public static HttpConnection create(String urlStr, Method method) {
		return new HttpConnection(urlStr, method);
	}

	/**
	 * 创建HttpConnection
	 * 
	 * @param urlStr URL
	 * @param method HTTP方法
	 * @param timeout 超时时长
	 * @return HttpConnection
	 */
	public static HttpConnection create(String urlStr, Method method, int timeout) {
		return new HttpConnection(urlStr, method, timeout);
	}

	/**
	 * 创建HttpConnection
	 * 
	 * @param urlStr URL
	 * @param method HTTP方法
	 * @param hostnameVerifier {@link HostnameVerifier}
	 * @param ssf {@link SSLSocketFactory}
	 * @return HttpConnection
	 */
	public static HttpConnection create(String urlStr, Method method, HostnameVerifier hostnameVerifier, SSLSocketFactory ssf) {
		return new HttpConnection(urlStr, method, hostnameVerifier, ssf, 0, null);
	}

	/**
	 * 创建HttpConnection
	 * 
	 * @param urlStr URL
	 * @param method HTTP方法
	 * @param hostnameVerifier {@link HostnameVerifier}
	 * @param ssf {@link SSLSocketFactory}
	 * @param timeout 超时时间
	 * @param proxy 代理
	 * @return HttpConnection
	 */
	public static HttpConnection create(String urlStr, Method method, HostnameVerifier hostnameVerifier, SSLSocketFactory ssf, int timeout, Proxy proxy) {
		return new HttpConnection(urlStr, method, hostnameVerifier, ssf, timeout, proxy);
	}

	/**
	 * 创建HttpConnection
	 * 
	 * @param url URL
	 * @param method HTTP方法
	 * @param hostnameVerifier {@link HostnameVerifier}
	 * @param ssf {@link SSLSocketFactory}
	 * @param timeout 超时时间
	 * @param proxy 代理
	 * @return HttpConnection
	 * @since 4.1.9
	 */
	public static HttpConnection create(URL url, Method method, HostnameVerifier hostnameVerifier, SSLSocketFactory ssf, int timeout, Proxy proxy) {
		return new HttpConnection(url, method, hostnameVerifier, ssf, timeout, proxy);
	}

	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造HttpConnection
	 * 
	 * @param urlStr URL
	 * @param method HTTP方法
	 */
	public HttpConnection(String urlStr, Method method) {
		this(urlStr, method, null, null, 0, null);
	}

	/**
	 * 构造HttpConnection
	 * 
	 * @param urlStr URL
	 * @param method HTTP方法
	 * @param timeout 超时时长
	 */
	public HttpConnection(String urlStr, Method method, int timeout) {
		this(urlStr, method, null, null, timeout, null);
	}

	/**
	 * 构造HttpConnection
	 * 
	 * @param urlStr URL
	 * @param method HTTP方法
	 * @param hostnameVerifier 域名验证器
	 * @param ssf SSLSocketFactory
	 * @param timeout 超时时长
	 * @param proxy 代理
	 */
	public HttpConnection(String urlStr, Method method, HostnameVerifier hostnameVerifier, SSLSocketFactory ssf, int timeout, Proxy proxy) {
		this(URLUtil.toUrlForHttp(urlStr), method, hostnameVerifier, ssf, timeout, proxy);
	}

	/**
	 * 构造HttpConnection
	 * 
	 * @param url URL
	 * @param method HTTP方法
	 * @param hostnameVerifier 域名验证器
	 * @param ssf SSLSocketFactory
	 * @param timeout 超时时长
	 * @param proxy 代理
	 */
	public HttpConnection(URL url, Method method, HostnameVerifier hostnameVerifier, SSLSocketFactory ssf, int timeout, Proxy proxy) {
		this.url = url;
		this.method = ObjectUtil.isNull(method) ? Method.GET : method;
		this.proxy = proxy;

		try {
			this.conn = openHttp(hostnameVerifier, ssf);
		} catch (Exception e) {
			throw new HttpException(e.getMessage(), e);
		}
		if (timeout > 0) {
			this.setConnectionAndReadTimeout(timeout);
		}

		initConn();
	}

	// --------------------------------------------------------------- Constructor end

	/**
	 * 初始化连接相关信息
	 * 
	 * @return HttpConnection
	 */
	public HttpConnection initConn() {
		// method
		try {
			this.conn.setRequestMethod(this.method.toString());
		} catch (ProtocolException e) {
			throw new HttpException(e.getMessage(), e);
		}

		// //对于非GET请求，默认不支持30X跳转
		// if(false == Method.GET.equals(this.method)){
		// this.conn.setInstanceFollowRedirects(false);
		// }

		// do input and output
		this.conn.setDoInput(true);
		if (Method.POST.equals(this.method) || Method.PUT.equals(this.method) || Method.PATCH.equals(this.method) || Method.DELETE.equals(this.method)) {
			this.conn.setDoOutput(true);
			this.conn.setUseCaches(false);
		}

		// default header，默认头信息在构建HttpRequest时加入，便于操作，此处删除
		// this.header(GlobalHeaders.INSTANCE.headers, true);

		// Cookie
		// setCookie(CookiePool.get(this.url.getHost()));

		return this;
	}

	// --------------------------------------------------------------- Getters And Setters start
	/**
	 * 获取请求方法,GET/POST
	 * 
	 * @return 请求方法,GET/POST
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * 设置请求方法
	 * 
	 * @param method 请求方法
	 * @return 自己
	 */
	public HttpConnection setMethod(Method method) {
		this.method = method;
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
	 * @param header 头名
	 * @param value 头值
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
	 * @param header 头名
	 * @param value 头值
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
	 * @param headerMap 请求头
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
	 * 关闭缓存
	 * 
	 * @return this
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
			log.debug("With Cookie: {}", cookie);
			header(Header.COOKIE, cookie, true);
		}
		return this;
	}

	/**
	 * 采用流方式上传数据，无需本地缓存数据。<br>
	 * HttpUrlConnection默认是将所有数据读到本地缓存，然后再发送给服务器，这样上传大文件时就会导致内存溢出。
	 * 
	 * @param blockSize 块大小（bytes数）
	 * @return this
	 */
	public HttpConnection setChunkedStreamingMode(int blockSize) {
		conn.setChunkedStreamingMode(blockSize);
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
	 * @throws IOException IO异常
	 */
	public InputStream getErrorStream() throws IOException {
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
		return this.conn.getOutputStream();
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
		sb.append("Request Method: ").append(this.method).append(StrUtil.CRLF);
		// sb.append("Request Headers: ").append(StrUtil.CRLF);
		// for (Entry<String, List<String>> entry : this.conn.getHeaderFields().entrySet()) {
		// sb.append(" ").append(entry).append(StrUtil.CRLF);
		// }

		return sb.toString();
	}

	// --------------------------------------------------------------- Private Method start
	/**
	 * 初始化http或https请求参数<br>
	 * 有些时候htts请求会出现com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl的实现，此为sun内部api，按照普通http请求处理
	 * 
	 * @param hostnameVerifier 域名验证器，非https传入null
	 * @param ssf SSLSocketFactory，非https传入null
	 * @return {@link HttpURLConnection}，https返回{@link HttpsURLConnection}
	 */
	private HttpURLConnection openHttp(HostnameVerifier hostnameVerifier, SSLSocketFactory ssf) throws IOException, NoSuchAlgorithmException, KeyManagementException {
		final HttpURLConnection conn = (HttpURLConnection) openConnection();

		if (conn instanceof HttpsURLConnection) {
			// Https请求
			final HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
			// 验证域
			httpsConn.setHostnameVerifier(null != hostnameVerifier ? hostnameVerifier : new TrustAnyHostnameVerifier());
			if (null == ssf) {
				if (StrUtil.equalsIgnoreCase("dalvik", System.getProperty("java.vm.name"))) {
					// 兼容android低版本SSL连接
					ssf = new AndroidSupportSSLFactory();
				} else {
					ssf = SSLSocketFactoryBuilder.create().build();
				}
			}
			httpsConn.setSSLSocketFactory(ssf);
		}

		return conn;
	}

	/**
	 * 建立连接
	 * 
	 * @return {@link URLConnection}
	 * @throws IOException
	 */
	private URLConnection openConnection() throws IOException {
		return (null == this.proxy) ? url.openConnection() : url.openConnection(this.proxy);
	}

	// --------------------------------------------------------------- Private Method end
}