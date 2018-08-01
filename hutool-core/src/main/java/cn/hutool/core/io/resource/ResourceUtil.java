package cn.hutool.core.io.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.EnumerationIter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ClassLoaderUtil;

/**
 * ClassPath资源工具类
 * 
 * @author Looly
 *
 */
public class ResourceUtil {

	/**
	 * 读取Classpath下的资源为字符串，使用UTF-8编码
	 * 
	 * @param resource 资源路径，使用相对ClassPath的路径
	 * @return 资源内容
	 * @since 3.1.1
	 */
	public static String readUtf8Str(String resource) {
		return new ClassPathResource(resource).readUtf8Str();
	}

	/**
	 * 读取Classpath下的资源为字符串
	 * 
	 * @param resource 资源路径，使用相对ClassPath的路径
	 * @param charset 编码
	 * @return 资源内容
	 * @since 3.1.1
	 */
	public static String readStr(String resource, Charset charset) {
		return new ClassPathResource(resource).readStr(charset);
	}

	/**
	 * 从ClassPath资源中获取{@link InputStream}
	 * 
	 * @param resurce ClassPath资源
	 * @return {@link InputStream}
	 * @throws NoResourceException 资源不存在异常
	 * @since 3.1.2
	 */
	public static InputStream getStream(String resurce) throws NoResourceException {
		return new ClassPathResource(resurce).getStream();
	}

	/**
	 * 从ClassPath资源中获取{@link InputStream}，当资源不存在时返回null
	 * 
	 * @param resurce ClassPath资源
	 * @return {@link InputStream}
	 * @since 4.0.3
	 */
	public static InputStream getStreamSafe(String resurce) {
		try {
			return new ClassPathResource(resurce).getStream();
		} catch (NoResourceException e) {
			// ignore
		}
		return null;
	}

	/**
	 * 从ClassPath资源中获取{@link BufferedReader}
	 * 
	 * @param resurce ClassPath资源
	 * @param charset 编码
	 * @return {@link InputStream}
	 * @since 3.1.2
	 */
	public static BufferedReader getReader(String resurce, Charset charset) {
		return new ClassPathResource(resurce).getReader(charset);
	}

	/**
	 * 获得资源的URL<br>
	 * 路径用/分隔，例如:
	 * 
	 * <pre>
	 * config/a/db.config
	 * spring/xml/test.xml
	 * </pre>
	 * 
	 * @param resource 资源（相对Classpath的路径）
	 * @return 资源URL
	 */
	public static URL getResource(String resource) throws IORuntimeException {
		return getResource(resource, null);
	}

	/**
	 * 获取指定路径下的资源列表<br>
	 * 路径格式必须为目录格式,用/分隔，例如:
	 * 
	 * <pre>
	 * config/a
	 * spring/xml
	 * </pre>
	 * 
	 * @param resource 资源路径
	 * @return 资源列表
	 */
	public static List<URL> getResources(String resource) {
		final Enumeration<URL> resources;
		try {
			resources = ClassLoaderUtil.getClassLoader().getResources(resource);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return CollectionUtil.newArrayList(resources);
	}

	/**
	 * 获取指定路径下的资源Iterator<br>
	 * 路径格式必须为目录格式,用/分隔，例如:
	 * 
	 * <pre>
	 * config/a
	 * spring/xml
	 * </pre>
	 * 
	 * @param resource 资源路径
	 * @return 资源列表
	 * @since 4.1.5
	 */
	public static EnumerationIter<URL> getResourceIter(String resource) {
		final Enumeration<URL> resources;
		try {
			resources = ClassLoaderUtil.getClassLoader().getResources(resource);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return new EnumerationIter<>(resources);
	}

	/**
	 * 获得资源相对路径对应的URL
	 * 
	 * @param resource 资源相对路径
	 * @param baseClass 基准Class，获得的相对路径相对于此Class所在路径，如果为{@code null}则相对ClassPath
	 * @return {@link URL}
	 */
	public static URL getResource(String resource, Class<?> baseClass) {
		return (null != baseClass) ? baseClass.getResource(resource) : ClassLoaderUtil.getClassLoader().getResource(resource);
	}

	/**
	 * 获取{@link Resource} 资源对象<br>
	 * 如果提供路径为绝对路径，返回{@link FileResource}，否则返回{@link ClassPathResource}
	 * 
	 * @param path 路径，可以是绝对路径，也可以是相对路径
	 * @return {@link Resource} 资源对象
	 * @since 3.2.1
	 */
	public static Resource getResourceObj(String path) {
		return FileUtil.isAbsolutePath(path) ? new FileResource(path) : new ClassPathResource(path);
	}
}
