package com.xiaoleilu.hutool.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.xiaoleilu.hutool.IoUtil;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.exceptions.HttpException;

/**
 * http请求类
 * @author biezhi
 */
public class HttpRequest extends HttpBase<HttpRequest>{

	/**
	 * method
	 */
	protected Method method = Method.GET;
	
	/**
	 * URL
	 */
	private String url = "";
	
	/**
	 * 默认超时
	 */
	private int timeout = -1;
	
	/**
	 * 连接对象
	 */
	private HttpConnection httpConnection;
	
	/**
	 * 设置请求方法
	 * @param method
	 * @return HttpRequest
	 */
	private HttpRequest method(Method method) {
		this.method = method;
		return this;
	}
	
	/**
	 * 设置请求URL
	 * @param url
	 */
	public HttpRequest(String url) {
		this.url = url;
	}
	
	/**
	 * POST请求
	 * @param url
	 * @return HttpRequest
	 */
	public static HttpRequest post(String url) {
		return new HttpRequest(url).method(Method.POST);
	}
	
	/**
	 * GET请求
	 * @param url
	 * @return HttpRequest
	 */
	public static HttpRequest get(String url) {
		return new HttpRequest(url).method(Method.GET);
	}
	
	/**
	 * 设置超时
	 * @param milliseconds
	 * @return HttpRequest
	 */
	public HttpRequest timeout(int milliseconds) {
		this.timeout = milliseconds;
		return this;
	}
	
	/**
	 * 执行Reuqest请求 
	 * @return HttpResponse
	 */
	public HttpResponse execute(){
		
		if(this.method.equals(Method.GET)){
			withUrl();
		}
		// init connection
		this.httpConnection = new HttpConnection(this.url, this.method);
		
		// response
		HttpResponse httpResponse = null;
		if(this.timeout != -1){
			// connect timeout
			this.httpConnection.setConnectTimeout(this.timeout);
			// read timeout
			this.httpConnection.setReadTimeout(this.timeout);
		}
		
		// set header
		this.httpConnection.header(this.headers);
		
		try {
			if(this.method.equals(Method.POST)){
				sendTo(httpConnection.getOutputStream());
			} else {
				this.httpConnection.connect();
			}
			
			InputStream inputStream = this.httpConnection.getInputStream();
			Map<String, List<String>> headers = this.httpConnection.headers();
			
			httpResponse = HttpResponse.readResponse(inputStream);
			
			httpResponse.setHeader(headers);
			httpResponse.setStatusCode(this.httpConnection.responseCode());
			httpResponse.setHttpRequest(this);
			
		} catch (IOException ioex) {
			throw new HttpException(ioex);
		}
		
		if (httpResponse.isKeepAlive() == false) {
			// closes connection if keep alive is false, or if counter reached 0
			this.httpConnection.disconnect();
			this.httpConnection = null;
		}
		
		return httpResponse;
	}
	
	/**
	 * 转换URL
	 */
	private void withUrl(){
		final String queryString = HttpUtil.toParams(this.form);
		if(StrUtil.isNotBlank(queryString)){
			this.url += this.url.endsWith("?") ? queryString : "?" + queryString;
		}
	}
	
	/**
	 * 发送数据流
	 * @param outputStream
	 * @throws IOException
	 */
	private void sendTo(OutputStream outputStream) throws IOException {
		if(null != outputStream){
			String queryString = HttpUtil.toParams(this.form);
			IoUtil.write(outputStream, this.formEncoding, true, queryString);
		}
	}
	
}
