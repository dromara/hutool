package looly.github.hutool;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import looly.github.hutool.exceptions.UtilException;

/**
 * 统一资源定位符相关工具类
 * 
 * @author xiaoleilu
 * 
 */
public class URLUtil {

	/**
	 * 获得URL
	 * 
	 * @param pathBaseClassLoader 相对路径（相对于classes）
	 * @return URL
	 */
	public static URL getURL(String pathBaseClassLoader) {
		ClassLoader classLoader = ClassUtil.getClassLoader();
		return classLoader.getResource(pathBaseClassLoader);
	}

	/**
	 * 获得URL
	 * 
	 * @param path 相对给定 class所在的路径
	 * @param clazz 指定class
	 * @return URL
	 */
	public static URL getURL(String path, Class<?> clazz) {
		return clazz.getResource(path);
	}

	/**
	 * 获得URL，常用于使用绝对路径时的情况
	 * 
	 * @param configFile URL对应的文件对象
	 * @return URL
	 */
	public static URL getURL(File configFile) {
		try {
			return configFile.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new UtilException("Error occured when get URL!", e);
		}
	}
	
	/**
	 * 格式化URL链接
	 * 
	 * @param url 需要格式化的URL
	 * @return 格式化后的URL，如果提供了null或者空串，返回null
	 */
	public static String formatUrl(String url) {
		if (StrUtil.isBlank(url)) return null;
		if (url.startsWith("http://") || url.startsWith("https://")) return url;
		return "http://" + url;
	}

	/**
	 * 补全相对路径
	 * 
	 * @param baseUrl 基准URL
	 * @param relativePath 相对URL
	 * @return
	 */
	public static String complateUrl(String baseUrl, String relativePath) {
		baseUrl = formatUrl(baseUrl);
		if (StrUtil.isBlank(baseUrl)) {
			return null;
		}

		try {
			final URL absoluteUrl = new URL(baseUrl);
			final URL parseUrl = new URL(absoluteUrl, relativePath);
			return parseUrl.toString();
		} catch (MalformedURLException e) {
			throw new UtilException(e);
		}
	}
}
