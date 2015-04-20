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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;

import com.xiaoleilu.hutool.CollectionUtil;
import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.URLUtil;
import com.xiaoleilu.hutool.exceptions.HttpException;
import com.xiaoleilu.hutool.http.ssl.DefaultTrustManager;
import com.xiaoleilu.hutool.http.ssl.TrustAnyHostnameVerifier;

/**
 * http连接对象
 * 
 * @author Looly
 *
 */
public class HttpConnection {
	private final static Logger log = Log.get();

	private HttpURLConnection conn;

	private static Map<String, String> cookies = new HashMap<String, String>();

	/** 超时，单位：毫秒 */
	private int timeout;
	private URL url;

	/** method请求方法 */
	private Method method = Method.GET;

	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造HttpConnection
	 * 
	 * @param urlStr URL
	 * @param method HTTP方法
	 */
	public HttpConnection(String urlStr, Method method) {
		this.url = URLUtil.url(urlStr);
		this.method = method;

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
	 */
	public void initConn() {
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

		// header
		header(Header.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", true);
		header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded", true);
		header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0", true);

		// cookie
		// Add Cookies
		final String cookie = cookies.get(this.url.getHost());
		if (cookie != null) header(Header.COOKIE, cookie, true);
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
	 * 获取超时
	 * 
	 * @return 超时
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * 设置超时
	 * 
	 * @param timeout 超时
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
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

	/**
	 * 设置连接超时
	 * 
	 * @param timeout 超时
	 */
	public void setConnectTimeout(int timeout) {
		if (null != this.conn) {
			this.conn.setConnectTimeout(timeout);
		}
	}

	/**
	 * 设置读取超时
	 * 
	 * @param timeout 超时
	 */
	public void setReadTimeout(int timeout) {
		if (null != this.conn) {
			this.conn.setReadTimeout(timeout);
		}
	}

	/**
	 * 设置请求头<br>
	 * 当请求头存在时，覆盖之
	 * 
	 * @param header 头名
	 * @param value 头值
	 * @param 是否覆盖旧值
	 */
	public void header(String header, String value, boolean isOverride) {
		if (null != this.conn) {
			if (isOverride) {
				this.conn.setRequestProperty(header, value);
			} else {
				this.conn.addRequestProperty(header, value);
			}
		}
	}

	/**
	 * 设置请求头<br>
	 * 当请求头存在时，覆盖之
	 * 
	 * @param header 头名
	 * @param value 头值
	 * @param 是否覆盖旧值
	 */
	public void header(Header header, String value, boolean isOverride) {
		header(header.toString(), value, isOverride);
	}

	/**
	 * 设置请求头<br>
	 * 不覆盖原有请求头
	 * 
	 * @param headers 请求头
	 */
	public void header(Map<String, List<String>> headers) {
		if(CollectionUtil.isEmpty(headers)) {
			return;
		}
		
		String name;
		for (Entry<String, List<String>> entry : headers.entrySet()) {
			name = entry.getKey();
			for (String value : entry.getValue()) {
				this.header(name, StrUtil.nullToEmpty(value), false);
			}
		}
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
	 * @param name Header名
	 * @return Http请求头值
	 */
	public Map<String, List<String>> headers() {
		return this.conn.getHeaderFields();
	}
	
	/**
	 * 连接
	 * 
	 * @throws IOException
	 */
	public void connect() throws IOException {
		if (null != this.conn) {
			this.conn.connect();
		}
	}

	/**
	 * 断开连接
	 */
	public void disconnect() {
		if (null != this.conn) {
			this.conn.disconnect();
		}
	}

	/**
	 * 获得输入流对象
	 * @return 输入流对象
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException {
		// Get Cookies
		final String setCookie = header(Header.SET_COOKIE);
		if (StrUtil.isBlank(setCookie) == false) {
			log.debug("Set cookie: [{}]", setCookie);
			cookies.put(url.getHost(), setCookie);
		}

		if (null != this.conn) {
			return this.conn.getInputStream();
		}
		return null;
	}

	/**
	 * 获取输出流对象
	 * 
	 * @return OutputStream
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException {
		if (null != this.conn) {
			return this.conn.getOutputStream();
		}
		return null;
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

		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
		httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
		
		return httpsURLConnection;
	}
	// --------------------------------------------------------------- Private Method end
}