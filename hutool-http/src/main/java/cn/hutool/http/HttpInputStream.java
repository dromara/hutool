package cn.hutool.http;

import cn.hutool.core.util.StrUtil;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * HTTP输入流，此流用于包装Http请求响应内容的流，用于解析各种压缩、分段的响应流内容
 * 
 * @author Looly
 *
 */
public class HttpInputStream extends InputStream {

	/** 原始流 */
	private InputStream in;

	/**
	 * 构造
	 * 
	 * @param response 响应对象
	 */
	public HttpInputStream(HttpResponse response) {
		init(response);
	}

	@Override
	public int read() throws IOException {
		return this.in.read();
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return this.in.read(b, off, len);
	}
	
	@Override
	public long skip(long n) throws IOException {
		return this.in.skip(n);
	}
	
	@Override
	public int available() throws IOException {
		return this.in.available();
	}
	
	@Override
	public void close() throws IOException {
		this.in.close();
	}
	
	@Override
	public synchronized void mark(int readlimit) {
		this.in.mark(readlimit);
	}
	
	@Override
	public synchronized void reset() throws IOException {
		this.in.reset();
	}
	
	@Override
	public boolean markSupported() {
		return this.in.markSupported();
	}

	/**
	 * 初始化流
	 * 
	 * @param response 响应对象
	 */
	private void init(HttpResponse response) {
		try {
			this.in = (response.status < HttpStatus.HTTP_BAD_REQUEST) ? response.httpConnection.getInputStream() : response.httpConnection.getErrorStream();
		} catch (IOException e) {
			if (false == (e instanceof FileNotFoundException)) {
				throw new HttpException(e);
			}
			// 服务器无返回内容，忽略之
		}
		
		// 在一些情况下，返回的流为null，此时提供状态码说明
		if (null == this.in) {
			this.in = new ByteArrayInputStream(StrUtil.format("Error request, response status: {}", response.status).getBytes());
			return;
		}
		
		if (response.isGzip() && false == (response.in instanceof GZIPInputStream)) {
			// Accept-Encoding: gzip
			try {
				this.in = new GZIPInputStream(this.in);
			} catch (IOException e) {
				// 在类似于Head等方法中无body返回，此时GZIPInputStream构造会出现错误，在此忽略此错误读取普通数据
				// ignore
			}
		} else if (response.isDeflate() && false == (this.in instanceof InflaterInputStream)) {
			// Accept-Encoding: defalte
			this.in = new InflaterInputStream(this.in, new Inflater(true));
		}
	}
}
