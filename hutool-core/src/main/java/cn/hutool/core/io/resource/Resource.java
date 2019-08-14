package cn.hutool.core.io.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import cn.hutool.core.io.IORuntimeException;

/**
 * 资源接口定义<br>
 * 资源可以是文件、URL、ClassPath中的文件亦或者jar包中的文件
 * 
 * @author looly
 * @since 3.2.1
 */
public interface Resource {
	
	/**
	 * 获取资源名，例如文件资源的资源名为文件名
	 * @return 资源名
	 * @since 4.0.13
	 */
	String getName();
	
	/**
	 * 获得解析后的{@link URL}
	 * @return 解析后的{@link URL}
	 */
	URL getUrl();
	
	/**
	 * 获得 {@link InputStream}
	 * @return {@link InputStream}
	 */
	InputStream getStream();
	
	/**
	 * 获得Reader
	 * @param charset 编码
	 * @return {@link BufferedReader}
	 */
	BufferedReader getReader(Charset charset);
	
	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 * 
	 * @param charset 编码
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装{@link IOException}
	 */
	String readStr(Charset charset) throws IORuntimeException;
	
	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 * 
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装IOException
	 */
	String readUtf8Str() throws IORuntimeException;
	
	/**
	 * 读取资源内容，读取完毕后会关闭流<br>
	 * 关闭流并不影响下一次读取
	 * 
	 * @return 读取资源内容
	 * @throws IORuntimeException 包装IOException
	 */
	byte[] readBytes() throws IORuntimeException;
}
