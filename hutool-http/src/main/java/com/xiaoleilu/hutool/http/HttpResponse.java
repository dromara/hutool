package com.xiaoleilu.hutool.http;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.io.FastByteArrayOutputStream;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.io.StreamProgress;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Http响应类<br>
 * 非线程安全对象
 * 
 * @author Looly
 *
 */
public class HttpResponse extends HttpBase<HttpResponse> implements Closeable{
	
	/** 持有连接对象 */
	private HttpConnection httpConnection;
	/** Http请求原始流 */
	private InputStream in;
	/** 是否异步，异步下只持有流，否则将在初始化时直接读取body内容 */
	private volatile boolean isAsync;
	/** 读取服务器返回的流保存至内存 */
	private FastByteArrayOutputStream out;
	/** 响应状态码 */
	private int status;
	/** 是否忽略读取Http响应体 */
	private boolean ignoreBody;

	/**
	 * 构造
	 * 
	 * @param httpConnection {@link HttpConnection}
	 * @param charset 编码
	 * @param isAsync 是否异步
	 * @param isIgnoreBody 是否忽略读取响应体
	 * @since 3.1.2
	 */
	protected HttpResponse(HttpConnection httpConnection, Charset charset, boolean isAsync, boolean isIgnoreBody) {
		this.httpConnection = httpConnection;
		this.charset = charset;
		this.isAsync = isAsync;
		this.ignoreBody = isIgnoreBody;
		init();
	}
	
	/**
	 * 获取状态码
	 * 
	 * @return 状态码
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * 同步<br>
	 * 如果为异步状态，则暂时不读取服务器中响应的内容，而是持有Http链接的{@link InputStream}。<br>
	 * 当调用此方法时，异步状态转为同步状态，此时从Http链接流中读取body内容并暂存在内容中。如果已经是同步状态，则不进行任何操作。
	 * 
	 * @return this
	 * @throws HttpException IO异常
	 */
	public HttpResponse sync() throws HttpException{
		return this.isAsync ? forceSync() : this;
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
		final String contentEncoding = contentEncoding();
		return contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip");
	}
	
	/**
	 * 获取本次请求服务器返回的Cookie信息
	 * @return Cookie字符串
	 * @since 3.1.1
	 */
	public String getCookieStr() {
		return header(Header.SET_COOKIE);
	}
	
	/**
	 * 获取Cookie
	 * @return Cookie列表
	 * @since 3.1.1
	 */
	public List<HttpCookie> getCookie(){
		final String cookieStr = getCookieStr();
		if(StrUtil.isNotBlank(cookieStr)) {
			return HttpCookie.parse(cookieStr);
		}
		return null;
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
	public InputStream bodyStream(){
		if(isAsync) {
			return this.in;
		}
		return new ByteArrayInputStream(this.out.toByteArray());
	}
	
	/**
	 * 获取响应流字节码<br>
	 * 此方法会转为同步模式
	 * 
	 * @return byte[]
	 */
	public byte[] bodyBytes() {
		sync();
		return (null == this.out) ? null : this.out.toByteArray();
	}

	/**
	 * 获取响应主体
	 * @return String
	 * @throws HttpException 包装IO异常
	 */
	public String body() throws HttpException{
		try {
			return HttpUtil.getString(bodyBytes(), this.charset, null == this.charset);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
	/**
	 * 将响应内容写出到{@link OutputStream}<br>
	 * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
	 * 写出后会关闭Http流（异步模式）
	 * 
	 * @param out 写出的流
	 * @param isCloseOut 是否关闭输出流
	 * @param streamProgress 进度显示接口，通过实现此接口显示下载进度
	 * @return 写出bytes数
	 * @since 3.3.2
	 */
	public long writeBody(OutputStream out, boolean isCloseOut, StreamProgress streamProgress) {
		if (null == out) {
			throw new NullPointerException("[out] is null!");
		}
		try {
			return IoUtil.copyByNIO(in, out, IoUtil.DEFAULT_BUFFER_SIZE, streamProgress);
		} finally {
			IoUtil.close(this);
			if (isCloseOut) {
				IoUtil.close(out);
			}
		}
	}
	
	/**
	 * 将响应内容写出到文件<br>
	 * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
	 * 写出后会关闭Http流（异步模式）
	 * 
	 * @param destFile 写出到的文件
	 * @param streamProgress 进度显示接口，通过实现此接口显示下载进度
	 * @return 写出bytes数
	 * @since 3.3.2
	 */
	public long writeBody(File destFile, StreamProgress streamProgress) {
		if (null == destFile) {
			throw new NullPointerException("[destFile] is null!");
		}
		if (destFile.isDirectory()) {
			String path = this.httpConnection.getUrl().getPath();
			String fileName = StrUtil.subSuf(path, path.lastIndexOf('/') + 1);
			if (StrUtil.isBlank(fileName)) {
				fileName = HttpUtil.encode(path, CharsetUtil.CHARSET_UTF_8);
			}
			destFile = FileUtil.file(destFile, fileName);
		}
		OutputStream out = null;
		try {
			out = FileUtil.getOutputStream(destFile);
			return writeBody(out, false, streamProgress);
		} catch (IORuntimeException e) {
			throw new HttpException(e);
		} finally {
			IoUtil.close(out);
		}
	}
	
	/**
	 * 将响应内容写出到文件<br>
	 * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
	 * 写出后会关闭Http流（异步模式）
	 * 
	 * @param destFile 写出到的文件
	 * @return 写出bytes数
	 * @since 3.3.2
	 */
	public long writeBody(File destFile) {
		return writeBody(destFile, null);
	}
	
	/**
	 * 将响应内容写出到文件<br>
	 * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
	 * 写出后会关闭Http流（异步模式）
	 * 
	 * @param destFilePath 写出到的文件的路径
	 * @return 写出bytes数
	 * @since 3.3.2
	 */
	public long writeBody(String destFilePath) {
		return writeBody(FileUtil.file(destFilePath));
	}
	// ---------------------------------------------------------------- Body end
	
	@Override
	public void close() {
		IoUtil.close(this.in);
		//关闭连接
		this.httpConnection.disconnect();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = StrUtil.builder();
		sb.append("Response Headers: ").append(StrUtil.CRLF);
		for (Entry<String, List<String>> entry : this.headers.entrySet()) {
			sb.append("    ").append(entry).append(StrUtil.CRLF);
		}
		
		sb.append("Response Body: ").append(StrUtil.CRLF);
		sb.append("    ").append(this.body()).append(StrUtil.CRLF);
		
		return sb.toString();
	}
	
	// ---------------------------------------------------------------- Private method start
	/**
	 * 初始化Http响应<br>
	 * 初始化包括：
	 * <pre>
	 * 1、读取Http状态
	 * 2、读取头信息
	 * 3、持有Http流，并不关闭流
	 * </pre>
	 * 
	 * @return this
	 * @throws HttpException IO异常
	 */
	private HttpResponse init() throws HttpException{
		try {
			this.status = httpConnection.responseCode();
			this.headers = httpConnection.headers();
			final Charset charset = httpConnection.getCharset();
			if(null != charset) {
				this.charset = charset;
			}
			
			this.in = (this.status < HttpStatus.HTTP_BAD_REQUEST) ? httpConnection.getInputStream() : httpConnection.getErrorStream();
		} catch (IOException e) {
			if(e instanceof FileNotFoundException){
				//服务器无返回内容，忽略之
			}else{
				throw new HttpException(e);
			}
		}
		if(null == this.in) {
			//在一些情况下，返回的流为null，此时提供状态码说明
			this.in = new ByteArrayInputStream(StrUtil.format("Error request, response status: {}", this.status).getBytes());
		} else if(isGzip() && false == (in instanceof GZIPInputStream)){
			try {
				in = new GZIPInputStream(in);
			} catch (IOException e) {
				//在类似于Head等方法中无body返回，此时GZIPInputStream构造会出现错误，在此忽略此错误读取普通数据
				//ignore
			}
		}
		
		//同步情况下强制同步
		return this.isAsync ? this : forceSync();
	}
	
	/**
	 * 读取主体，忽略EOFException异常
	 * @param in 输入流
	 * @return 自身
	 * @throws IORuntimeException IO异常
	 */
	private void readBody(InputStream in) throws IORuntimeException{
		if(ignoreBody) {
			return;
		}
		
		int contentLength = Convert.toInt(header(Header.CONTENT_LENGTH), 0);
		this.out = contentLength > 0 ? new FastByteArrayOutputStream(contentLength) : new FastByteArrayOutputStream();
		try {
			IoUtil.copy(in, this.out);
		} catch (IORuntimeException e) {
			if(e.getCause() instanceof EOFException) {
				//忽略读取HTTP流中的EOF错误
			}else {
				throw e;
			}
		}
	}
	
	/**
	 * 强制同步，用于初始化<br>
	 * 强制同步后变化如下：
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
		//非同步状态转为同步状态
		try {
			this.readBody(this.in);
		} catch (IORuntimeException e) {
			if(e.getCause() instanceof FileNotFoundException){
				//服务器无返回内容，忽略之
			}else{
				throw new HttpException(e);
			}
		}finally {
			if(this.isAsync) {
				this.isAsync = false;
			}
			this.close();
		}
		return this;
	}
	// ---------------------------------------------------------------- Private method end
}
