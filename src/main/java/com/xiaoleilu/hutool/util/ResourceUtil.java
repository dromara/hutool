package com.xiaoleilu.hutool.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.setting.dialect.Props;

/**
 * 资源工具类<br>
 * 资源是一些存在于Classpath下的文件，常用于读取配置文件
 * 
 * @author Looly
 *
 */
public class ResourceUtil {
	
	/**
	 * 获得ClassPath
	 * @return ClassPath
	 */
	public static String getClassPath() {
		return getClassPathURL().getPath();
	}
	
	/**
	 * 获得ClassPath URL
	 * @return ClassPath URL
	 */
	public static URL getClassPathURL() {
		return ClassUtil.getClassLoader().getResource(StrUtil.EMPTY);
	}
	
	/**
	 * 获得资源的URL
	 * 
	 * @param resource 资源（相对Classpath的路径）
	 * @return 资源URL
	 */
	public static URL getURL(String resource) {
		return ClassUtil.getClassLoader().getResource(resource);
	}

	/**
	 * 获得资源的输入流
	 * 
	 * @param resource 资源（相对Classpath的路径）
	 * @return 资源的InputStream
	 */
	public static InputStream getStream(String resource) {
		return ClassUtil.getClassLoader().getResourceAsStream(resource);
	}
	
	/**
	 * 获得资源的Reader
	 * @param resource 资源（相对Classpath的路径）
	 * @param charsetName 编码
	 * @return Reader
	 * @throws IOException 
	 */
	public static Reader getReader(String resource, String charsetName) throws IOException{
		return getReader(resource, Charset.forName(charsetName));
	}

	/**
	 * 获得资源的Reader
	 * @param resource 资源（相对Classpath的路径）
	 * @param charset 编码
	 * @return Reader
	 * @throws IOException 
	 */
	public static Reader getReader(String resource, Charset charset) throws IOException{
		return IoUtil.getReader(getStream(resource), charset);
	}
	
	/**
	 * 获得指定资源文件
	 * @param resource 资源（相对Classpath的路径）
	 * @return 资源文件
	 */
	public static File getFile(String resource){
		try {
			return new File(getURL(resource).toURI());
		} catch (URISyntaxException e) {
			throw new UtilException(e);
		}
	}
	
	/**
	 * 获得Classpath下的Properties文件
	 * 
	 * @param resource 资源（相对Classpath的路径）
	 * @return Properties
	 */
	public static Properties getProp(String resource) {
		return new Props(resource);
	}
}
