package com.xiaoleilu.hutool.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Http响应类
 * @author Looly
 *
 */
public class HttpResponse extends HttpBase<HttpResponse>{

	/**
	 * 响应状态码
	 */
	private int statusCode;
	
	/**
	 * 请求对象
	 */
	private HttpRequest httpRequest;

	public HttpResponse() {
	}

	/**
	 * 获取状态码
	 * @return
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * 设置状态码
	 * @param statusCode
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * 读取响应流
	 * @param in
	 * @return HttpResponse
	 * @throws IOException
	 */
	public static HttpResponse readResponse(InputStream in) throws IOException {
		HttpResponse httpResponse = new HttpResponse();
		String body = httpResponse.readBody(in);
		httpResponse.body = body;
		return httpResponse;
	}

	/**
	 * 获取请求对象
	 * @return
	 */
	public HttpRequest getHttpRequest() {
		return httpRequest;
	}

	/**
	 * 设置请求对象
	 * @param httpRequest
	 */
	public void setHttpRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
	
	/**
	 * 设置请求头
	 * @param headerFields
	 */
	public void setHeader(Map<String, List<String>> headerFields) {
		for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
			this.header(entry.getKey(), entry.getValue().get(0));
		}
	}
	
}
