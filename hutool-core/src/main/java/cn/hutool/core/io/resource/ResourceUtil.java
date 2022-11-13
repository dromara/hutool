package cn.hutool.core.io.resource;

import cn.hutool.core.collection.iter.EnumerationIter;
import cn.hutool.core.collection.iter.IterUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.classloader.ClassLoaderUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.net.url.URLUtil;
import cn.hutool.core.util.ObjUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;

/**
 * Resource资源工具类
 *
 * @author Looly
 */
public class ResourceUtil {

	/**
	 * 读取Classpath下的资源为字符串，使用UTF-8编码
	 *
	 * @param resource 资源路径，使用相对ClassPath的路径
	 * @return 资源内容
	 * @since 3.1.1
	 */
	public static String readUtf8Str(final String resource) {
		return getResource(resource).readUtf8Str();
	}

	/**
	 * 读取Classpath下的资源为字符串
	 *
	 * @param resource 可以是绝对路径，也可以是相对路径（相对ClassPath）
	 * @param charset  编码
	 * @return 资源内容
	 * @since 3.1.1
	 */
	public static String readStr(final String resource, final Charset charset) {
		return getResource(resource).readStr(charset);
	}

	/**
	 * 读取Classpath下的资源为byte[]
	 *
	 * @param resource 可以是绝对路径，也可以是相对路径（相对ClassPath）
	 * @return 资源内容
	 * @since 4.5.19
	 */
	public static byte[] readBytes(final String resource) {
		return getResource(resource).readBytes();
	}

	/**
	 * 从ClassPath资源中获取{@link InputStream}
	 *
	 * @param resource ClassPath资源
	 * @return {@link InputStream}
	 * @throws NoResourceException 资源不存在异常
	 * @since 3.1.2
	 */
	public static InputStream getStream(final String resource) throws NoResourceException {
		return getResource(resource).getStream();
	}

	/**
	 * 从ClassPath资源中获取{@link InputStream}，当资源不存在时返回null
	 *
	 * @param resource ClassPath资源
	 * @return {@link InputStream}
	 * @since 4.0.3
	 */
	public static InputStream getStreamSafe(final String resource) {
		try {
			return getResource(resource).getStream();
		} catch (final NoResourceException e) {
			// ignore
		}
		return null;
	}

	/**
	 * 从ClassPath资源中获取{@link BufferedReader}
	 *
	 * @param resource ClassPath资源
	 * @return {@link InputStream}
	 * @since 5.3.6
	 */
	public static BufferedReader getUtf8Reader(final String resource) {
		return getReader(resource, CharsetUtil.UTF_8);
	}

	/**
	 * 从ClassPath资源中获取{@link BufferedReader}
	 *
	 * @param resource ClassPath资源
	 * @param charset  编码
	 * @return {@link InputStream}
	 * @since 3.1.2
	 */
	public static BufferedReader getReader(final String resource, final Charset charset) {
		return getResource(resource).getReader(charset);
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
	public static URL getResourceUrl(final String resource) throws IORuntimeException {
		return getResourceUrl(resource, null);
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
	public static List<URL> getResourceUrls(final String resource) {
		return getResourceUrls(resource, null);
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
	 * @param filter   过滤器，用于过滤不需要的资源，{@code null}表示不过滤，保留所有元素
	 * @return 资源列表
	 */
	public static List<URL> getResourceUrls(final String resource, final Predicate<URL> filter) {
		return IterUtil.filterToList(getResourceUrlIter(resource), filter);
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
	public static EnumerationIter<URL> getResourceUrlIter(final String resource) {
		return getResourceUrlIter(resource, null);
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
	 * @param resource    资源路径
	 * @param classLoader {@link ClassLoader}
	 * @return 资源列表
	 */
	public static EnumerationIter<URL> getResourceUrlIter(final String resource, final ClassLoader classLoader) {
		final Enumeration<URL> resources;
		try {
			resources = ObjUtil.defaultIfNull(classLoader, ClassLoaderUtil::getClassLoader).getResources(resource);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return new EnumerationIter<>(resources);
	}

	/**
	 * 获得资源相对路径对应的URL
	 *
	 * @param resource  资源相对路径，{@code null}和""都表示classpath根路径
	 * @param baseClass 基准Class，获得的相对路径相对于此Class所在路径，如果为{@code null}则相对ClassPath
	 * @return {@link URL}
	 */
	public static URL getResourceUrl(String resource, final Class<?> baseClass) {
		resource = StrUtil.emptyIfNull(resource);
		return (null != baseClass) ? baseClass.getResource(resource) : ClassLoaderUtil.getClassLoader().getResource(resource);
	}

	/**
	 * 获取{@link Resource} 资源对象<br>
	 * 如果提供路径为绝对路径或路径以file:开头，返回{@link FileResource}，否则返回{@link ClassPathResource}
	 *
	 * @param path 路径，可以是绝对路径，也可以是相对路径（相对ClassPath）
	 * @return {@link Resource} 资源对象
	 * @since 3.2.1
	 */
	public static Resource getResource(final String path) {
		if (StrUtil.isNotBlank(path)) {
			if (path.startsWith(URLUtil.FILE_URL_PREFIX) || FileUtil.isAbsolutePath(path)) {
				return new FileResource(path);
			}
		}
		return new ClassPathResource(path);
	}

	/**
	 * 获取{@link UrlResource} 资源对象
	 *
	 * @param url URL
	 * @return {@link Resource} 资源对象
	 * @since 6.0.0
	 */
	public static Resource getResource(final URL url) {
		return new UrlResource(url);
	}

	/**
	 * 获取{@link FileResource} 资源对象
	 *
	 * @param file {@link File}
	 * @return {@link Resource} 资源对象
	 * @since 6.0.0
	 */
	public static Resource getResource(final File file) {
		return new FileResource(file);
	}

	/**
	 * 获取同名的所有资源
	 *
	 * @param resource 资源名
	 * @return {@link MultiResource}
	 */
	public static MultiResource getResources(final String resource) {
		return getResources(resource, null);
	}

	/**
	 * 获取同名的所有资源
	 *
	 * @param resource    资源名
	 * @param classLoader {@link ClassLoader}，{@code null}表示使用默认的当前上下文ClassLoader
	 * @return {@link MultiResource}
	 */
	public static MultiResource getResources(final String resource, final ClassLoader classLoader) {
		final EnumerationIter<URL> iter = getResourceUrlIter(resource, classLoader);
		final MultiResource resources = new MultiResource();
		for (final URL url : iter) {
			resources.add(getResource(url));
		}
		return resources;
	}
}
