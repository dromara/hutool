package com.xiaoleilu.hutool.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

import com.xiaoleilu.hutool.CharsetUtil;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.exceptions.HttpException;

/**
 * Http响应类
 * 
 * @author Looly
 *
 */
public class HttpResponse extends HttpBase<HttpResponse> {

	/**
	 * 读取响应信息
	 * 
	 * @param httpConnection Http连接对象
	 * @return HttpResponse
	 */
	public static HttpResponse readResponse(HttpConnection httpConnection) {
		final HttpResponse httpResponse = new HttpResponse();
		
		try {
			httpResponse.status = httpConnection.responseCode();
			httpResponse.headers =  httpConnection.headers();
			httpResponse.charset = httpConnection.charset();
			
			InputStream in;
			if(httpResponse.status < HttpURLConnection.HTTP_BAD_REQUEST){
				in = httpConnection.getInputStream();
			}else{
				in = httpConnection.getErrorStream();
			}
			httpResponse.readBody(in);
		} catch (IOException e) {
			if(e instanceof FileNotFoundException){
				//服务器无返回内容，忽略之
			}else{
				throw new HttpException(e.getMessage(), e);
			}
		}
		
		return httpResponse;
	}

	/** 响应状态码 */
	private int status;

	public HttpResponse() {
	}

	/**
	 * 获取状态码
	 * 
	 * @return 状态码
	 */
	public int getStatus() {
		return status;
	}
	
	// ---------------------------------------------------------------- Http Response Header start
	/**
	 * 获取内容编码
	 * @return String
	 */
	public String contentEncoding() {
		return header(Header.CONTENT_ENCODING);
	}
	
	/**
	 * @return 是否为gzip压缩过的内容
	 */
	public boolean isGzip(){
		String contentEncoding = contentEncoding();
		return contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip");
	}
	// ---------------------------------------------------------------- Http Response Header end
	
	// ---------------------------------------------------------------- Body start
	/**
	 * 获取响应流字节码
	 * @return byte[]
	 */
	public byte[] bodyBytes() {
		if (body == null) {
			return null;
		}
		return StrUtil.bytes(body, charset);
	}

	/**
	 * 获取响应主体
	 * @return String
	 */
	public String body() {
		return body;
	}
	// ---------------------------------------------------------------- Body end
	
	// ---------------------------------------------------------------- Private method start
	/**
	 * 读取主体
	 * @param in 输入流
	 * @return 自身
	 * @throws IOException
	 */
	private void readBody(InputStream in) throws IOException{
		if(isGzip()){
			in = new GZIPInputStream(in);
		}
		this.body = HttpUtil.getString(in, CharsetUtil.UTF_8, charset == null);
	}
	// ---------------------------------------------------------------- Private method end
}
