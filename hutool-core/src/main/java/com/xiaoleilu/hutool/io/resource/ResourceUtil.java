package com.xiaoleilu.hutool.io.resource;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;

import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.util.ClassLoaderUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * ClassPath资源工具类
 * @author Looly
 *
 */
public class ResourceUtil {
	
	/**
	 * 读取Classpath下的资源为字符串，使用UTF-8编码
	 * @param resource 资源路径，使用相对ClassPath的路径
	 * @return 资源内容
	 * @since 3.1.1
	 */
	public static String readUtf8Str(String resource) {
		return new ClassPathResource(resource).readUtf8Str();
	}
	
	/**
	 * 读取Classpath下的资源为字符串
	 * @param resource 资源路径，使用相对ClassPath的路径
	 * @param charset 编码
	 * @return 资源内容
	 * @since 3.1.1
	 */
	public static String readStr(String resource, Charset charset) {
		return new ClassPathResource(resource).readStr(charset);
	}
	
	/**
	 * 获得资源的URL<br>
	 * 路径用/分隔，例如:
	 * <pre>
	 * config/a/db.config
	 * spring/xml/test.xml
	 * </pre>
	 * 
	 * @param resource 资源（相对Classpath的路径）
	 * @return 资源URL
	 */
	public static URL getResource(String resource) throws IORuntimeException{
		return getResource(resource, null);
	}
	
	/**
	 * 获取指定路径下的资源列表<br>
	 * 路径格式必须为目录格式,用/分隔，例如:
	 * <pre>
	 * config/a
	 * spring/xml
	 * </pre>
	 * 
	 * @param resource 资源路径
	 * @return 资源列表
	 */
	public static List<URL> getResources(String resource){
		final Enumeration<URL> resources;
		try {
			resources = ClassLoaderUtil.getClassLoader().getResources(resource);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return CollectionUtil.newArrayList(resources);
	}
	
	/**
	 * 获得资源相对路径对应的URL
	 * @param resource 资源相对路径
	 * @param baseClass 基准Class，获得的相对路径相对于此Class所在路径，如果为{@code null}则相对ClassPath
	 * @return {@link URL}
	 */
	public static URL getResource(String resource, Class<?> baseClass){
		return (null != baseClass) ? baseClass.getResource(resource) : ClassLoaderUtil.getClassLoader().getResource(resource);
	}
}
