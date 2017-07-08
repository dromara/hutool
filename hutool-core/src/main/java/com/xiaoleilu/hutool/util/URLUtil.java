package com.xiaoleilu.hutool.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.lang.Assert;

/**
 * 统一资源定位符相关工具类
 * 
 * @author xiaoleilu
 * 
 */
public class URLUtil {
	
	private URLUtil() {}
	
	/**
	 * 通过一个字符串形式的URL地址创建URL对象
	 * @param url URL
	 * @return URL对象
	 */
	public static URL url(String url){
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new UtilException(e.getMessage(), e);
		}
	}

	/**
	 * 获得URL
	 * 
	 * @param pathBaseClassLoader 相对路径（相对于classes）
	 * @return URL
	 */
	public static URL getURL(String pathBaseClassLoader) {
		return ClassUtil.getClassLoader().getResource(pathBaseClassLoader);
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
	 * @param file URL对应的文件对象
	 * @return URL
	 * @exception UtilException MalformedURLException
	 */
	public static URL getURL(File file) {
		Assert.notNull(file, "File is null !");
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new UtilException("Error occured when get URL!", e);
		}
	}
	
	/**
	 * 获得URL，常用于使用绝对路径时的情况
	 * 
	 * @param files URL对应的文件对象
	 * @return URL
	 * @exception UtilException MalformedURLException
	 */
	public static URL[] getURLs(File... files) {
		final URL[] urls = new URL[files.length];
		try {
			for(int i = 0; i < files.length; i++){
				urls[i] = files[i].toURI().toURL();
			}
		} catch (MalformedURLException e) {
			throw new UtilException("Error occured when get URL!", e);
		}
		
		return urls;
	}
	
	/**
	 * 格式化URL链接
	 * 
	 * @param url 需要格式化的URL
	 * @return 格式化后的URL，如果提供了null或者空串，返回null
	 */
	public static String formatUrl(String url) {
		if (StrUtil.isBlank(url)){
			return null;
		}
		if (url.startsWith("http://") || url.startsWith("https://")){
			return url;
		}
		return "http://" + url;
	}

	/**
	 * 补全相对路径
	 * 
	 * @param baseUrl 基准URL
	 * @param relativePath 相对URL
	 * @return 相对路径
	 * @exception UtilException MalformedURLException
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
	
	/**
	 * 编码URL<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。
	 * 
	 * @param url URL
	 * @param charset 编码
	 * @return 编码后的URL
	 * @exception UtilException UnsupportedEncodingException
	 */
	public static String encode(String url, String charset) {
		try {
			return URLEncoder.encode(url, charset);
		} catch (UnsupportedEncodingException e) {
			throw new UtilException(e);
		}
	}
	
	/**
	 * 解码URL<br>
	 * 将%开头的16进制表示的内容解码。
	 * 
	 * @param url URL
	 * @param charset 编码
	 * @return 解码后的URL
	 * @exception UtilException UnsupportedEncodingException
	 */
	public static String decode(String url, String charset) {
		try {
			return URLDecoder.decode(url, charset);
		} catch (UnsupportedEncodingException e) {
			throw new UtilException(e);
		}
	}
	
	/**
	 * 获得path部分<br>
	 * 
	 * @param uriStr URI路径
	 * @return path
	 * @exception UtilException 包装URISyntaxException
	 */
	public static String getPath(String uriStr){
		URI uri = null;
		try {
			uri = new URI(uriStr);
		} catch (URISyntaxException e) {
			throw new UtilException(e);
		}
		return uri.getPath();
	}
	
	/**
	 * 从URL对象中获取不被编码的路径Path<br>
	 * 对于本地路径，URL对象的getPath方法对于包含中文或空格时会被编码，导致本读路径读取错误。<br>
	 * 此方法将URL转为URI后获取路径用于解决路径被编码的问题
	 * 
	 * @param url {@link URL}
	 * @return 路径
	 * @since 3.0.8
	 */
	public static String getDecodedPath(URL url) {
		try {
			//URL对象的getPath方法对于包含中文或空格的问题
			return URLUtil.toURI(url).getPath();
		} catch (UtilException e) {
			return url.getPath();
		}
	}
	
	/**
	 * 转URL为URI
	 * @param url URL
	 * @return URI
	 * @exception UtilException 包装URISyntaxException
	 */
	public static URI toURI(URL url) {
		return toURI(url.toString());
	}
	
	/**
	 * 转字符串为URI
	 * @param location 字符串路径
	 * @return URI
	 * @exception UtilException 包装URISyntaxException
	 */
	public static URI toURI(String location) {
		try {
			return new URI(location.replace(" ", "%20"));
		} catch (URISyntaxException e) {
			throw new UtilException(e);
		}
	}
}
