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
import com.xiaoleilu.hutool.util.CharsetUtil;
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
	 * @return 解析后的{@link URL}
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
	
	//------------------------------------------------------------------------------- read
	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 * 
	 * @param charset 编码
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装{@link IOException}
	 * @since 3.0.8
	 */
	public String readStr(Charset charset) throws IORuntimeException{
		BufferedReader reader = null;
		try {
			reader = getReader(charset);
			return IoUtil.read(reader);
		} finally {
			IoUtil.close(reader);
		}
	}
	
	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 * 
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装{@link IOException}
	 * @since 3.0.8
	 */
	public String readUtf8Str() throws IORuntimeException{
		return readStr(CharsetUtil.CHARSET_UTF_8);
	}
	
	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 * 
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装{@link IOException}
	 * @since 3.0.8
	 */
	public byte[] readBytes() throws IORuntimeException{
		InputStream in = null;
		try {
			in = getStream();
			return IoUtil.readBytes(in);
		} finally {
			IoUtil.close(in);
		}
	}
	
	/**
	 * 获得File
	 * @return {@link File}
	 */
	public File getFile(){
		return FileUtil.file(this.url);
	}
	
	/**
	 * 返回路径
	 * @return 返回URL路径
	 */
	@Override
	public String toString() {
		return (null == this.url) ? "null" : this.url.toString();
	}
}
