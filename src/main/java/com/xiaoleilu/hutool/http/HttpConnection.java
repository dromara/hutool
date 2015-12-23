package com.xiaoleilu.hutool.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.xiaoleilu.hutool.CollectionUtil;
import com.xiaoleilu.hutool.Func;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.URLUtil;
import com.xiaoleilu.hutool.Validator;
import com.xiaoleilu.hutool.exceptions.HttpException;
import com.xiaoleilu.hutool.http.ssl.DefaultTrustManager;
import com.xiaoleilu.hutool.http.ssl.TrustAnyHostnameVerifier;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

/**
 * http连接对象
 * 
 * @author Looly
 *
 */
public class HttpConnection {
	private final static Log log = StaticLog.get();

	private URL url;
	/** method请求方法 */
	private Method method;
	private HttpURLConnection conn;
	
	/**
	 * 创建HttpConnection
	 * @param urlStr URL
	 * @param method HTTP方法
	 * @return HttpConnection
	 */
	public static HttpConnection create(String urlStr, Method method) {
		return new HttpConnection(urlStr, method);
	}

	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造HttpConnection
	 * 
	 * @param urlStr URL
	 * @param method HTTP方法
	 */
	public HttpConnection(String urlStr, Method method) {
		if(StrUtil.isBlank(urlStr)) {
			throw new HttpException("Url is blank !");
		}
		if(Validator.isUrl(urlStr) == false) {
			throw new HttpException("{} is not a url !", urlStr);
		}
		
		this.url = URLUtil.url(urlStr);
		this.method = Func.isNull(method) ? Method.GET : method;

		try {
			this.conn = HttpUtil.isHttps(urlStr) ? openHttps() : openHttp();
		} catch (Exception e) {
			throw new HttpException(e.getMessage(), e);
		}
		
		initConn();
	}

	// --------------------------------------------------------------- Constructor end

	/**
	 * 初始化连接相关信息
	 * @return HttpConnection
	 */
	public HttpConnection initConn() {
		// method
		try {
			this.conn.setRequestMethod(this.method.toString());
		} catch (ProtocolException e) {
			throw new HttpException(e.getMessage(), e);
		}

		// do input and output
		if (this.method.equals(Method.POST)) {
			this.conn.setDoOutput(true);
			this.conn.setUseCaches(false);
		}
		this.conn.setDoInput(true);

		// default header
		header(Header.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", true);
		header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded", true);
		header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0 Hutool", true);
		//Cookie
		setCookie(CookiePool.get(this.url.getHost()));

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
	 */
	public void setMethod(Method method) {
		this.method = method;
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
	 * 设置请求URL
	 * 
	 * @param url 请求URL
	 */
	public void setUrl(URL url) {
		this.url = url;
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
	 * @param headers 请求头
	 */
	public HttpConnection header(Map<String, List<String>> headers) {
		if(CollectionUtil.isNotEmpty(headers)) {
			String name;
			for (Entry<String, List<String>> entry : headers.entrySet()) {
				name = entry.getKey();
				for (String value : entry.getValue()) {
					this.header(name, StrUtil.nullToEmpty(value), false);
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
	 * 设置连接超时
	 * 
	 * @param timeout 超时
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
	 */
	public HttpConnection setReadTimeout(int timeout) {
		if (timeout > 0 && null != this.conn) {
			this.conn.setReadTimeout(timeout);
		}
		
		return this;
	}
	
	/**
	 * 设置连接和读取的超时时间
	 * @param timeout 超时时间
	 */
	public HttpConnection setConnectionAndReadTimeout(int timeout) {
		setConnectTimeout(timeout);
		setReadTimeout(timeout);
		
		return this;
	}
	
	/**
	 * 设置Cookie
	 * @param cookie Cookie
	 * @return HttpConnection
	 */
	public HttpConnection setCookie(String cookie){
		if(cookie != null) {
			header(Header.COOKIE, cookie, true);
		}
		return this;
	}
	
	/**
	 * 采用流方式上传数据，无需本地缓存数据。<br>
	 * HttpUrlConnection默认是将所有数据读到本地缓存，然后再发送给服务器，这样上传大文件时就会导致内存溢出。
	 * @param blockSize 块大小（bytes数）
	 * @return HttpConnection
	 */
	public HttpConnection setChunkedStreamingMode(int blockSize) {
		conn.setChunkedStreamingMode(blockSize);
		return this;
	}
	
	/**
	 * 连接
	 * 
	 * @throws IOException
	 */
	public HttpConnection connect() throws IOException {
		if (null != this.conn) {
			this.conn.connect();
		}
		return this;
	}

	/**
	 * 断开连接
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
	 * @return 输入流对象
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException {
		// Get Cookies
		final String setCookie = header(Header.SET_COOKIE);
		if (StrUtil.isBlank(setCookie) == false) {
			log.debug("Set cookie: [{}]", setCookie);
			CookiePool.put(url.getHost(), setCookie);
		}

		if (null != this.conn) {
			return this.conn.getInputStream();
		}
		return null;
	}

	/**
	 * 获取输出流对象
	 * 输出流对象用于发送数据
	 * @return OutputStream
	 * @throws IOException
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
	 * @return int
	 * @throws IOException
	 */
	public int responseCode() throws IOException {
		if (null != this.conn) {
			return this.conn.getResponseCode();
		}
		return 0;
	}
	
	/**
	 * 获得字符集编码
	 * @return 字符集编码
	 */
	public String charset() {
		return HttpUtil.getCharset(conn);
	}
	
	// --------------------------------------------------------------- Private Method start
	/**
	 * 初始化http请求参数
	 */
	private HttpURLConnection openHttp() throws IOException {
		return (HttpURLConnection) url.openConnection();
	}

	/**
	 * 初始化http请求参数
	 */
	private HttpsURLConnection openHttps() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

		// 验证域
		httpsURLConnection.setHostnameVerifier(new TrustAnyHostnameVerifier());

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
		httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
		
		return httpsURLConnection;
	}
	// --------------------------------------------------------------- Private Method end
}