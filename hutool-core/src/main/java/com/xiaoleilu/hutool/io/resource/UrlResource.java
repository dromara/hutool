package com.xiaoleilu.hutool.io.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.URLUtil;

/**
 * URL资源访问类
 * @author Looly
 *
 */
public class UrlResource {
	protected URL url;
	
	//-------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param url URL
	 */
	public UrlResource(URL url) {
		this.url = url;
	}
	
	/**
	 * 构造
	 * @param file 文件路径
	 */
	public UrlResource(File file) {
		this.url = URLUtil.getURL(file);
	}
	//-------------------------------------------------------------------------------------- Constructor end
	
	/**
	 * 获得解析后的{@link URL}
	 */
	public final URL getUrl(){
		return this.url;
	}
	
	/**
	 * 获得 {@link InputStream}
	 * @return {@link InputStream}
	 */
	public InputStream getStream(){
		if(null == this.url){
			throw new IORuntimeException("Resource [{}] not exist!", this.url);
		}
		try {
			return this.url.openStream();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * 获得Reader
	 * @param charset 编码
	 * @return {@link BufferedReader}
	 * @since 3.0.1
	 */
	public BufferedReader getReader(Charset charset){
		return IoUtil.getReader(getStream(), charset);
	}
	
	/**
	 * 获得File
	 * @return {@link File}
	 */
	public File getFile(){
		return FileUtil.file(this.url);
	}
	
	@Override
	public String toString() {
		return (null == this.url) ? "null" : this.url.toString();
	}
}
