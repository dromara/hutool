package cn.hutool.core.util;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.net.url.UrlQuery;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * URL（Uniform Resource Locator）统一资源定位符相关工具类
 *
 * <p>
 * 统一资源定位符，描述了一台特定服务器上某资源的特定位置。
 * </p>
 * URL组成：
 * <pre>
 *   协议://主机名[:端口]/ 路径/[:参数] [?查询]#Fragment
 *   protocol :// hostname[:port] / path / [:parameters][?query]#fragment
 * </pre>
 *
 * @author xiaoleilu
 */
public class URLUtil {

	/**
	 * 针对ClassPath路径的伪协议前缀（兼容Spring）: "classpath:"
	 */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";
	/**
	 * URL 前缀表示文件: "file:"
	 */
	public static final String FILE_URL_PREFIX = "file:";
	/**
	 * URL 前缀表示jar: "jar:"
	 */
	public static final String JAR_URL_PREFIX = "jar:";
	/**
	 * URL 前缀表示war: "war:"
	 */
	public static final String WAR_URL_PREFIX = "war:";
	/**
	 * URL 协议表示文件: "file"
	 */
	public static final String URL_PROTOCOL_FILE = "file";
	/**
	 * URL 协议表示Jar文件: "jar"
	 */
	public static final String URL_PROTOCOL_JAR = "jar";
	/**
	 * URL 协议表示zip文件: "zip"
	 */
	public static final String URL_PROTOCOL_ZIP = "zip";
	/**
	 * URL 协议表示WebSphere文件: "wsjar"
	 */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";
	/**
	 * URL 协议表示JBoss zip文件: "vfszip"
	 */
	public static final String URL_PROTOCOL_VFSZIP = "vfszip";
	/**
	 * URL 协议表示JBoss文件: "vfsfile"
	 */
	public static final String URL_PROTOCOL_VFSFILE = "vfsfile";
	/**
	 * URL 协议表示JBoss VFS资源: "vfs"
	 */
	public static final String URL_PROTOCOL_VFS = "vfs";
	/**
	 * Jar路径以及内部文件路径的分界符: "!/"
	 */
	public static final String JAR_URL_SEPARATOR = "!/";
	/**
	 * WAR路径及内部文件路径分界符
	 */
	public static final String WAR_URL_SEPARATOR = "*/";

	/**
	 * 通过一个字符串形式的URL地址创建URL对象
	 *
	 * @param url URL
	 * @return URL对象
	 */
	public static URL url(String url) {
		return url(url, null);
	}

	/**
	 * 通过一个字符串形式的URL地址创建URL对象
	 *
	 * @param url     URL
	 * @param handler {@link URLStreamHandler}
	 * @return URL对象
	 * @since 4.1.1
	 */
	public static URL url(String url, URLStreamHandler handler) {
		Assert.notNull(url, "URL must not be null");

		// 兼容Spring的ClassPath路径
		if (url.startsWith(CLASSPATH_URL_PREFIX)) {
			url = url.substring(CLASSPATH_URL_PREFIX.length());
			return ClassLoaderUtil.getClassLoader().getResource(url);
		}

		try {
			return new URL(null, url, handler);
		} catch (MalformedURLException e) {
			// 尝试文件路径
			try {
				return new File(url).toURI().toURL();
			} catch (MalformedURLException ex2) {
				throw new UtilException(e);
			}
		}
	}

	/**
	 * 将URL字符串转换为URL对象，并做必要验证
	 *
	 * @param urlStr URL字符串
	 * @return URL
	 * @since 4.1.9
	 */
	public static URL toUrlForHttp(String urlStr) {
		return toUrlForHttp(urlStr, null);
	}

	/**
	 * 将URL字符串转换为URL对象，并做必要验证
	 *
	 * @param urlStr  URL字符串
	 * @param handler {@link URLStreamHandler}
	 * @return URL
	 * @since 4.1.9
	 */
	public static URL toUrlForHttp(String urlStr, URLStreamHandler handler) {
		Assert.notBlank(urlStr, "Url is blank !");
		// 编码空白符，防止空格引起的请求异常
		urlStr = encodeBlank(urlStr);
		try {
			return new URL(null, urlStr, handler);
		} catch (MalformedURLException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 单独编码URL中的空白符，空白符编码为%20
	 *
	 * @param urlStr URL字符串
	 * @return 编码后的字符串
	 * @since 4.5.14
	 */
	public static String encodeBlank(CharSequence urlStr) {
		if (urlStr == null) {
			return null;
		}

		int len = urlStr.length();
		final StringBuilder sb = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = urlStr.charAt(i);
			if (CharUtil.isBlankChar(c)) {
				sb.append("%20");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 获得URL
	 *
	 * @param pathBaseClassLoader 相对路径（相对于classes）
	 * @return URL
	 * @see ResourceUtil#getResource(String)
	 */
	public static URL getURL(String pathBaseClassLoader) {
		return ResourceUtil.getResource(pathBaseClassLoader);
	}

	/**
	 * 获得URL
	 *
	 * @param path  相对给定 class所在的路径
	 * @param clazz 指定class
	 * @return URL
	 * @see ResourceUtil#getResource(String, Class)
	 */
	public static URL getURL(String path, Class<?> clazz) {
		return ResourceUtil.getResource(path, clazz);
	}

	/**
	 * 获得URL，常用于使用绝对路径时的情况
	 *
	 * @param file URL对应的文件对象
	 * @return URL
	 * @throws UtilException MalformedURLException
	 */
	public static URL getURL(File file) {
		Assert.notNull(file, "File is null !");
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new UtilException(e, "Error occured when get URL!");
		}
	}

	/**
	 * 获得URL，常用于使用绝对路径时的情况
	 *
	 * @param files URL对应的文件对象
	 * @return URL
	 * @throws UtilException MalformedURLException
	 */
	public static URL[] getURLs(File... files) {
		final URL[] urls = new URL[files.length];
		try {
			for (int i = 0; i < files.length; i++) {
				urls[i] = files[i].toURI().toURL();
			}
		} catch (MalformedURLException e) {
			throw new UtilException(e, "Error occured when get URL!");
		}

		return urls;
	}

	/**
	 * 获取URL中域名部分，只保留URL中的协议（Protocol）、Host，其它为null。
	 *
	 * @param url URL
	 * @return 域名的URI
	 * @since 4.6.9
	 */
	public static URI getHost(URL url) {
		if (null == url) {
			return null;
		}

		try {
			return new URI(url.getProtocol(), url.getHost(), null, null);
		} catch (URISyntaxException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 补全相对路径
	 *
	 * @param baseUrl      基准URL
	 * @param relativePath 相对URL
	 * @return 相对路径
	 * @throws UtilException MalformedURLException
	 * @deprecated 拼写错误，请使用{@link #completeUrl(String, String)}
	 */
	@Deprecated
	public static String complateUrl(String baseUrl, String relativePath) {
		return completeUrl(baseUrl, relativePath);
	}

	/**
	 * 补全相对路径
	 *
	 * @param baseUrl      基准URL
	 * @param relativePath 相对URL
	 * @return 相对路径
	 * @throws UtilException MalformedURLException
	 */
	public static String completeUrl(String baseUrl, String relativePath) {
		baseUrl = normalize(baseUrl, false);
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
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。
	 *
	 * @param url URL
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String encodeAll(String url) {
		return encodeAll(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码URL<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。
	 *
	 * @param url     URL
	 * @param charset 编码，为null表示不编码
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String encodeAll(String url, Charset charset) throws UtilException {
		if (null == charset) {
			return url;
		}

		return URLEncoder.ALL.encode(url, charset);
	}

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
	 *
	 * @param url URL
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 * @since 3.1.2
	 */
	public static String encode(String url) throws UtilException {
		return encode(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码URL，默认使用UTF-8编码<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于POST请求中的请求体自动编码，转义大部分特殊字符
	 *
	 * @param url URL
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 * @since 3.1.2
	 */
	public static String encodeQuery(String url) throws UtilException {
		return encodeQuery(url, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 编码字符为 application/x-www-form-urlencoded<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
	 *
	 * @param url     被编码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 * @since 4.4.1
	 */
	public static String encode(String url, Charset charset) {
		if (StrUtil.isEmpty(url)) {
			return url;
		}
		if (null == charset) {
			charset = CharsetUtil.defaultCharset();
		}
		return URLEncoder.DEFAULT.encode(url, charset);
	}

	/**
	 * 编码字符为URL中查询语句<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于POST请求中的请求体自动编码，转义大部分特殊字符
	 *
	 * @param url     被编码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 * @since 4.4.1
	 */
	public static String encodeQuery(String url, Charset charset) {
		if (StrUtil.isEmpty(url)) {
			return url;
		}
		if (null == charset) {
			charset = CharsetUtil.defaultCharset();
		}
		return URLEncoder.QUERY.encode(url, charset);
	}

	/**
	 * 编码URL字符为 application/x-www-form-urlencoded<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于URL自动编码，类似于浏览器中键入地址自动编码，对于像类似于“/”的字符不再编码
	 *
	 * @param url     URL
	 * @param charset 编码
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String encode(String url, String charset) throws UtilException {
		if (StrUtil.isEmpty(url)) {
			return url;
		}
		return encode(url, StrUtil.isBlank(charset) ? CharsetUtil.defaultCharset() : CharsetUtil.charset(charset));
	}

	/**
	 * 编码URL<br>
	 * 将需要转换的内容（ASCII码形式之外的内容），用十六进制表示法转换出来，并在之前加上%开头。<br>
	 * 此方法用于POST请求中的请求体自动编码，转义大部分特殊字符
	 *
	 * @param url     URL
	 * @param charset 编码
	 * @return 编码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String encodeQuery(String url, String charset) throws UtilException {
		return encodeQuery(url, StrUtil.isBlank(charset) ? CharsetUtil.defaultCharset() : CharsetUtil.charset(charset));
	}

	/**
	 * 解码URL<br>
	 * 将%开头的16进制表示的内容解码。
	 *
	 * @param url URL
	 * @return 解码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 * @since 3.1.2
	 */
	public static String decode(String url) throws UtilException {
		return decode(url, CharsetUtil.UTF_8);
	}

	/**
	 * 解码application/x-www-form-urlencoded字符<br>
	 * 将%开头的16进制表示的内容解码。
	 *
	 * @param content 被解码内容
	 * @param charset 编码，null表示不解码
	 * @return 编码后的字符
	 * @since 4.4.1
	 */
	public static String decode(String content, Charset charset) {
		if (null == charset) {
			return content;
		}
		return URLDecoder.decode(content, charset);
	}

	/**
	 * 解码application/x-www-form-urlencoded字符<br>
	 * 将%开头的16进制表示的内容解码。
	 *
	 * @param content URL
	 * @param charset 编码
	 * @return 解码后的URL
	 * @throws UtilException UnsupportedEncodingException
	 */
	public static String decode(String content, String charset) throws UtilException {
		return decode(content, CharsetUtil.charset(charset));
	}

	/**
	 * 获得path部分<br>
	 *
	 * @param uriStr URI路径
	 * @return path
	 * @throws UtilException 包装URISyntaxException
	 */
	public static String getPath(String uriStr) {
		return toURI(uriStr).getPath();
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
		if (null == url) {
			return null;
		}

		String path = null;
		try {
			// URL对象的getPath方法对于包含中文或空格的问题
			path = toURI(url).getPath();
		} catch (UtilException e) {
			// ignore
		}
		return (null != path) ? path : url.getPath();
	}

	/**
	 * 转URL为URI
	 *
	 * @param url URL
	 * @return URI
	 * @throws UtilException 包装URISyntaxException
	 */
	public static URI toURI(URL url) throws UtilException {
		return toURI(url, false);
	}

	/**
	 * 转URL为URI
	 *
	 * @param url      URL
	 * @param isEncode 是否编码参数中的特殊字符（默认UTF-8编码）
	 * @return URI
	 * @throws UtilException 包装URISyntaxException
	 * @since 4.6.9
	 */
	public static URI toURI(URL url, boolean isEncode) throws UtilException {
		if (null == url) {
			return null;
		}

		return toURI(url.toString(), isEncode);
	}

	/**
	 * 转字符串为URI
	 *
	 * @param location 字符串路径
	 * @return URI
	 * @throws UtilException 包装URISyntaxException
	 */
	public static URI toURI(String location) throws UtilException {
		return toURI(location, false);
	}

	/**
	 * 转字符串为URI
	 *
	 * @param location 字符串路径
	 * @param isEncode 是否编码参数中的特殊字符（默认UTF-8编码）
	 * @return URI
	 * @throws UtilException 包装URISyntaxException
	 * @since 4.6.9
	 */
	public static URI toURI(String location, boolean isEncode) throws UtilException {
		if (isEncode) {
			location = encode(location);
		}
		try {
			return new URI(StrUtil.trim(location));
		} catch (URISyntaxException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 提供的URL是否为文件<br>
	 * 文件协议包括"file", "vfsfile" 或 "vfs".
	 *
	 * @param url {@link URL}
	 * @return 是否为文件
	 * @since 3.0.9
	 */
	public static boolean isFileURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_FILE.equals(protocol) || //
				URL_PROTOCOL_VFSFILE.equals(protocol) || //
				URL_PROTOCOL_VFS.equals(protocol));
	}

	/**
	 * 提供的URL是否为jar包URL 协议包括： "jar", "zip", "vfszip" 或 "wsjar".
	 *
	 * @param url {@link URL}
	 * @return 是否为jar包URL
	 */
	public static boolean isJarURL(URL url) {
		final String protocol = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(protocol) || //
				URL_PROTOCOL_ZIP.equals(protocol) || //
				URL_PROTOCOL_VFSZIP.equals(protocol) || //
				URL_PROTOCOL_WSJAR.equals(protocol));
	}

	/**
	 * 提供的URL是否为Jar文件URL 判断依据为file协议且扩展名为.jar
	 *
	 * @param url the URL to check
	 * @return whether the URL has been identified as a JAR file URL
	 * @since 4.1
	 */
	public static boolean isJarFileURL(URL url) {
		return (URL_PROTOCOL_FILE.equals(url.getProtocol()) && //
				url.getPath().toLowerCase().endsWith(FileUtil.JAR_FILE_EXT));
	}

	/**
	 * 从URL中获取流
	 *
	 * @param url {@link URL}
	 * @return InputStream流
	 * @since 3.2.1
	 */
	public static InputStream getStream(URL url) {
		Assert.notNull(url);
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得Reader
	 *
	 * @param url     {@link URL}
	 * @param charset 编码
	 * @return {@link BufferedReader}
	 * @since 3.2.1
	 */
	public static BufferedReader getReader(URL url, Charset charset) {
		return IoUtil.getReader(getStream(url), charset);
	}

	/**
	 * 从URL中获取JarFile
	 *
	 * @param url URL
	 * @return JarFile
	 * @since 4.1.5
	 */
	public static JarFile getJarFile(URL url) {
		try {
			JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
			return urlConnection.getJarFile();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 标准化URL字符串，包括：
	 *
	 * <pre>
	 * 1. 多个/替换为一个
	 * </pre>
	 *
	 * @param url URL字符串
	 * @return 标准化后的URL字符串
	 */
	public static String normalize(String url) {
		return normalize(url, false);
	}

	/**
	 * 标准化URL字符串，包括：
	 *
	 * <pre>
	 * 1. 多个/替换为一个
	 * </pre>
	 *
	 * @param url          URL字符串
	 * @param isEncodePath 是否对URL中path部分的中文和特殊字符做转义（不包括 http:, /和域名部分）
	 * @return 标准化后的URL字符串
	 * @since 4.4.1
	 */
	public static String normalize(String url, boolean isEncodePath) {
		if (StrUtil.isBlank(url)) {
			return url;
		}
		final int sepIndex = url.indexOf("://");
		String protocol;
		String body;
		if (sepIndex > 0) {
			protocol = StrUtil.subPre(url, sepIndex + 3);
			body = StrUtil.subSuf(url, sepIndex + 3);
		} else {
			protocol = "http://";
			body = url;
		}

		final int paramsSepIndex = StrUtil.indexOf(body, '?');
		String params = null;
		if (paramsSepIndex > 0) {
			params = StrUtil.subSuf(body, paramsSepIndex);
			body = StrUtil.subPre(body, paramsSepIndex);
		}

		if (StrUtil.isNotEmpty(body)) {
			// 去除开头的\或者/
			//noinspection ConstantConditions
			body = body.replaceAll("^[\\\\/]+", StrUtil.EMPTY);
			// 替换多个\或/为单个/
			body = body.replace("\\", "/");
			//issue#I25MZL，双斜杠在URL中是允许存在的，不做替换
			//.replaceAll("//+", "/");
		}

		final int pathSepIndex = StrUtil.indexOf(body, '/');
		String domain = body;
		String path = null;
		if (pathSepIndex > 0) {
			domain = StrUtil.subPre(body, pathSepIndex);
			path = StrUtil.subSuf(body, pathSepIndex);
		}
		if (isEncodePath) {
			path = encode(path);
		}
		return protocol + domain + StrUtil.nullToEmpty(path) + StrUtil.nullToEmpty(params);
	}

	/**
	 * 将Map形式的Form表单数据转换为Url参数形式<br>
	 * paramMap中如果key为空（null和""）会被忽略，如果value为null，会被做为空白符（""）<br>
	 * 会自动url编码键和值
	 *
	 * <pre>
	 * key1=v1&amp;key2=&amp;key3=v3
	 * </pre>
	 *
	 * @param paramMap 表单数据
	 * @param charset  编码，编码为null表示不编码
	 * @return url参数
	 */
	public static String buildQuery(Map<String, ?> paramMap, Charset charset) {
		return UrlQuery.of(paramMap).build(charset);
	}

	/**
	 * 获取指定URL对应资源的内容长度，对于Http，其长度使用Content-Length头决定。
	 *
	 * @param url URL
	 * @return 内容长度，未知返回-1
	 * @throws IORuntimeException IO异常
	 * @since 5.3.4
	 */
	public static long getContentLength(URL url) throws IORuntimeException {
		if (null == url) {
			return -1;
		}

		URLConnection conn = null;
		try {
			conn = url.openConnection();
			return conn.getContentLengthLong();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (conn instanceof HttpURLConnection) {
				((HttpURLConnection) conn).disconnect();
			}
		}
	}

	/**
	 * Data URI Scheme封装，数据格式为Base64。data URI scheme 允许我们使用内联（inline-code）的方式在网页中包含数据，<br>
	 * 目的是将一些小的数据，直接嵌入到网页中，从而不用再从外部文件载入。常用于将图片嵌入网页。
	 *
	 * <p>
	 * Data URI的格式规范：
	 * <pre>
	 *     data:[&lt;mime type&gt;][;charset=&lt;charset&gt;][;&lt;encoding&gt;],&lt;encoded data&gt;
	 * </pre>
	 *
	 * @param mimeType 可选项（null表示无），数据类型（image/png、text/plain等）
	 * @param data     编码后的数据
	 * @return Data URI字符串
	 * @since 5.3.11
	 */
	public static String getDataUriBase64(String mimeType, String data) {
		return getDataUri(mimeType, null, "base64", data);
	}

	/**
	 * Data URI Scheme封装。data URI scheme 允许我们使用内联（inline-code）的方式在网页中包含数据，<br>
	 * 目的是将一些小的数据，直接嵌入到网页中，从而不用再从外部文件载入。常用于将图片嵌入网页。
	 *
	 * <p>
	 * Data URI的格式规范：
	 * <pre>
	 *     data:[&lt;mime type&gt;][;charset=&lt;charset&gt;][;&lt;encoding&gt;],&lt;encoded data&gt;
	 * </pre>
	 *
	 * @param mimeType 可选项（null表示无），数据类型（image/png、text/plain等）
	 * @param encoding 数据编码方式（US-ASCII，BASE64等）
	 * @param data     编码后的数据
	 * @return Data URI字符串
	 * @since 5.3.6
	 */
	public static String getDataUri(String mimeType, String encoding, String data) {
		return getDataUri(mimeType, null, encoding, data);
	}

	/**
	 * Data URI Scheme封装。data URI scheme 允许我们使用内联（inline-code）的方式在网页中包含数据，<br>
	 * 目的是将一些小的数据，直接嵌入到网页中，从而不用再从外部文件载入。常用于将图片嵌入网页。
	 *
	 * <p>
	 * Data URI的格式规范：
	 * <pre>
	 *     data:[&lt;mime type&gt;][;charset=&lt;charset&gt;][;&lt;encoding&gt;],&lt;encoded data&gt;
	 * </pre>
	 *
	 * @param mimeType 可选项（null表示无），数据类型（image/png、text/plain等）
	 * @param charset  可选项（null表示无），源文本的字符集编码方式
	 * @param encoding 数据编码方式（US-ASCII，BASE64等）
	 * @param data     编码后的数据
	 * @return Data URI字符串
	 * @since 5.3.6
	 */
	public static String getDataUri(String mimeType, Charset charset, String encoding, String data) {
		final StringBuilder builder = StrUtil.builder("data:");
		if (StrUtil.isNotBlank(mimeType)) {
			builder.append(mimeType);
		}
		if (null != charset) {
			builder.append(";charset=").append(charset.name());
		}
		if (StrUtil.isNotBlank(encoding)) {
			builder.append(';').append(encoding);
		}
		builder.append(',').append(data);

		return builder.toString();
	}
}