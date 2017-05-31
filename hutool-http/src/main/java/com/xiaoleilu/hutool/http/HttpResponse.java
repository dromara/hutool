package com.xiaoleilu.hutool.http;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.io.FastByteArrayOutputStream;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Http响应类
 * 
 * @author Looly
 *
 */
public class HttpResponse extends HttpBase<HttpResponse> {
	
	/** 读取服务器返回的流保存至内存 */
	private FastByteArrayOutputStream out;
	/** 响应状态码 */
	private int status;

	protected HttpResponse() {
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
	 * 获得服务区响应流
	 * @return 响应流
	 */
	public InputStream bodyStream(){
		return new ByteArrayInputStream(this.out.toByteArray());
	}
	
	/**
	 * 获取响应流字节码
	 * @return byte[]
	 */
	public byte[] bodyBytes() {
		if(null == this.out){
			return null;
		}
		return this.out.toByteArray();
	}

	/**
	 * 获取响应主体
	 * @return String
	 * @throws HttpException 包装IO异常
	 */
	public String body() throws HttpException{
		try {
			return HttpUtil.getString(bodyStream(), this.charset, null == this.charset);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	// ---------------------------------------------------------------- Body end
	
	@Override
	public String toString() {
		StringBuilder sb = StrUtil.builder();
		sb.append("Request Headers: ").append(StrUtil.CRLF);
		for (Entry<String, List<String>> entry : this.headers.entrySet()) {
			sb.append("    ").append(entry).append(StrUtil.CRLF);
		}
		
		sb.append("Request Body: ").append(StrUtil.CRLF);
		sb.append("    ").append(this.body()).append(StrUtil.CRLF);
		
		return sb.toString();
	}
	
	// ---------------------------------------------------------------- Protected method start
	/**
	 * 读取响应信息
	 * 
	 * @param httpConnection Http连接对象
	 * @return HttpResponse
	 */
	protected static HttpResponse readResponse(HttpConnection httpConnection) {
		final HttpResponse httpResponse = new HttpResponse();
		
		InputStream in = null;
		try {
			httpResponse.status = httpConnection.responseCode();
			httpResponse.headers =  httpConnection.headers();
			httpResponse.charset = httpConnection.charset();
			
			if(httpResponse.status < HttpStatus.HTTP_BAD_REQUEST){
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
		}finally{
			IoUtil.close(in);
		}
		
		return httpResponse;
	}
	// ---------------------------------------------------------------- Protected method end
	
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
		
		int contentLength  = Convert.toInt(header(Header.CONTENT_LENGTH), 0);
		this.out = contentLength > 0 ? new FastByteArrayOutputStream(contentLength) : new FastByteArrayOutputStream();
		try {
			IoUtil.copy(in, this.out);
		} catch (EOFException e) {
			//忽略读取HTTP流中的EOF错误
		}
	}
	// ---------------------------------------------------------------- Private method end
}
