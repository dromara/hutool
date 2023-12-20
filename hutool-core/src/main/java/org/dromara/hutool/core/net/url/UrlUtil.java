/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.net.url;

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Map;

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
 * @author looly
 */
public class UrlUtil {

	// region const
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
	 * Jar路径以及内部文件路径的分界符: "!/"
	 */
	public static final String JAR_URL_SEPARATOR = "!/";
	/**
	 * WAR路径及内部文件路径分界符
	 */
	public static final String WAR_URL_SEPARATOR = "*/";
	// endregion

	/**
	 * 将{@link URI}转换为{@link URL}
	 *
	 * @param uri {@link URI}
	 * @return URL对象
	 * @throws HutoolException {@link MalformedURLException}包装，URI格式有问题时抛出
	 * @see URI#toURL()
	 * @since 5.7.21
	 */
	public static URL url(final URI uri) throws HutoolException {
		if (null == uri) {
			return null;
		}
		try {
			return uri.toURL();
		} catch (final MalformedURLException e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 通过一个字符串形式的URL地址创建URL对象
	 *
	 * @param url URL
	 * @return URL对象
	 */
	public static URL url(final String url) {
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
	public static URL url(String url, final URLStreamHandler handler) {
		if (null == url) {
			return null;
		}

		// 兼容Spring的ClassPath路径
		if (url.startsWith(CLASSPATH_URL_PREFIX)) {
			url = url.substring(CLASSPATH_URL_PREFIX.length());
			return ClassLoaderUtil.getClassLoader().getResource(url);
		}

		try {
			return new URL(null, url, handler);
		} catch (final MalformedURLException e) {
			// issue#I8PY3Y
			if(e.getMessage().contains("Accessing an URL protocol that was not enabled")){
				// Graalvm打包需要手动指定参数开启协议：
				// --enable-url-protocols=http
				// --enable-url-protocols=https
				throw new HutoolException(e);
			}

			// 尝试文件路径
			try {
				return new File(url).toURI().toURL();
			} catch (final MalformedURLException ex2) {
				throw new HutoolException(e);
			}
		}
	}

	/**
	 * 获取string协议的URL，类似于string:///xxxxx
	 *
	 * @param content 正文
	 * @return URL
	 * @since 5.5.2
	 */
	public static URI getStringURI(final CharSequence content) {
		if (null == content) {
			return null;
		}
		final String contentStr = StrUtil.addPrefixIfNot(content, "string:///");
		return URI.create(contentStr);
	}

	/**
	 * 将URL字符串转换为URL对象，并做必要验证
	 *
	 * @param urlStr URL字符串
	 * @return URL
	 * @since 4.1.9
	 */
	public static URL toUrlForHttp(final String urlStr) {
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
	public static URL toUrlForHttp(String urlStr, final URLStreamHandler handler) {
		Assert.notBlank(urlStr, "Url is blank !");
		// 编码空白符，防止空格引起的请求异常
		urlStr = UrlEncoder.encodeBlank(urlStr);
		try {
			return new URL(null, urlStr, handler);
		} catch (final MalformedURLException e) {
			throw new HutoolException(e);
		}
	}

	// region getURL

	/**
	 * 获得URL
	 *
	 * @param pathBaseClassLoader 相对路径（相对于classes）
	 * @return URL
	 * @see ResourceUtil#getResourceUrl(String)
	 */
	public static URL getURL(final String pathBaseClassLoader) {
		return ResourceUtil.getResourceUrl(pathBaseClassLoader);
	}

	/**
	 * 获得URL
	 *
	 * @param path  相对给定 class所在的路径
	 * @param clazz 指定class
	 * @return URL
	 * @see ResourceUtil#getResourceUrl(String, Class)
	 */
	public static URL getURL(final String path, final Class<?> clazz) {
		return ResourceUtil.getResourceUrl(path, clazz);
	}

	/**
	 * 获得URL，常用于使用绝对路径时的情况
	 *
	 * @param file URL对应的文件对象
	 * @return URL
	 * @throws IORuntimeException URL格式错误
	 */
	public static URL getURL(final File file) {
		Assert.notNull(file, "File is null !");
		try {
			return file.toURI().toURL();
		} catch (final MalformedURLException e) {
			throw new IORuntimeException(e, "Error occurred when get URL!");
		}
	}

	/**
	 * 获取相对于给定URL的新的URL<br>
	 * 来自：org.springframework.core.io.UrlResource#createRelativeURL
	 *
	 * @param url 基础URL
	 * @param relativePath 相对路径
	 * @return 相对于URL的子路径URL
	 * @throws IORuntimeException URL格式错误
	 * @since 6.0.0
	 */
	public static URL getURL(final URL url, String relativePath) throws HutoolException {
		// # 在文件路径中合法，但是在URL中非法，此处转义
		relativePath = StrUtil.replace(StrUtil.removePrefix(relativePath, StrUtil.SLASH), "#", "%23");
		try {
			return new URL(url, relativePath);
		} catch (final MalformedURLException e) {
			throw new IORuntimeException(e, "Error occurred when get URL!");
		}
	}

	/**
	 * 获得URL，常用于使用绝对路径时的情况
	 *
	 * @param files URL对应的文件对象
	 * @return URL
	 * @throws IORuntimeException URL格式错误
	 */
	public static URL[] getURLs(final File... files) {
		final URL[] urls = new URL[files.length];
		try {
			for (int i = 0; i < files.length; i++) {
				urls[i] = files[i].toURI().toURL();
			}
		} catch (final MalformedURLException e) {
			throw new IORuntimeException(e, "Error occurred when get URL!");
		}

		return urls;
	}
	// endregion

	/**
	 * 获取URL中域名部分，只保留URL中的协议（Protocol）、Host，其它为null。
	 *
	 * @param url URL
	 * @return 域名的URI
	 * @since 4.6.9
	 */
	public static URI getHost(final URL url) {
		if (null == url) {
			return null;
		}

		try {
			return new URI(url.getProtocol(), url.getHost(), null, null);
		} catch (final URISyntaxException e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 补全相对路径
	 *
	 * @param baseUrl      基准URL
	 * @param relativePath 相对URL
	 * @return 相对路径
	 * @throws HutoolException MalformedURLException
	 */
	public static String completeUrl(String baseUrl, final String relativePath) {
		baseUrl = normalize(baseUrl, false);
		if (StrUtil.isBlank(baseUrl)) {
			return null;
		}

		try {
			final URL absoluteUrl = new URL(baseUrl);
			final URL parseUrl = new URL(absoluteUrl, relativePath);
			return parseUrl.toString();
		} catch (final MalformedURLException e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 获得path部分<br>
	 *
	 * @param uriStr URI路径
	 * @return path
	 * @throws HutoolException 包装URISyntaxException
	 */
	public static String getPath(final String uriStr) {
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
	public static String getDecodedPath(final URL url) {
		if (null == url) {
			return null;
		}

		String path = null;
		try {
			// URL对象的getPath方法对于包含中文或空格的问题
			path = toURI(url).getPath();
		} catch (final HutoolException e) {
			// ignore
		}
		return (null != path) ? path : url.getPath();
	}

	// region toURI

	/**
	 * 转URL为URI
	 *
	 * @param url URL
	 * @return URI
	 * @throws HutoolException 包装URISyntaxException
	 */
	public static URI toURI(final URL url) throws HutoolException {
		return toURI(url, false);
	}

	/**
	 * 转URL为URI
	 *
	 * @param url      URL
	 * @param isEncode 是否编码参数中的特殊字符（默认UTF-8编码）
	 * @return URI
	 * @throws HutoolException 包装URISyntaxException
	 * @since 4.6.9
	 */
	public static URI toURI(final URL url, final boolean isEncode) throws HutoolException {
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
	 * @throws HutoolException 包装URISyntaxException
	 */
	public static URI toURI(final String location) throws HutoolException {
		return toURI(location, false);
	}

	/**
	 * 转字符串为URI
	 *
	 * @param location 字符串路径
	 * @param isEncode 是否编码参数中的特殊字符（默认UTF-8编码）
	 * @return URI
	 * @throws HutoolException 包装URISyntaxException
	 * @since 4.6.9
	 */
	public static URI toURI(String location, final boolean isEncode) throws HutoolException {
		if (isEncode) {
			location = RFC3986.PATH.encode(location, CharsetUtil.UTF_8);
		}
		try {
			return new URI(StrUtil.trim(location));
		} catch (final URISyntaxException e) {
			throw new HutoolException(e);
		}
	}
	// endregion

	/**
	 * 从URL中获取流
	 *
	 * @param url {@link URL}
	 * @return InputStream流
	 * @since 3.2.1
	 */
	public static InputStream getStream(final URL url) {
		Assert.notNull(url, "URL must be not null");
		try {
			return url.openStream();
		} catch (final IOException e) {
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
	public static BufferedReader getReader(final URL url, final Charset charset) {
		return IoUtil.toReader(getStream(url), charset);
	}

	// region normalize

	/**
	 * 标准化URL字符串，包括：
	 *
	 * <ol>
	 *     <li>自动补齐“http://”头</li>
	 *     <li>去除开头的\或者/</li>
	 *     <li>替换\为/</li>
	 * </ol>
	 *
	 * @param url URL字符串
	 * @return 标准化后的URL字符串
	 */
	public static String normalize(final String url) {
		return normalize(url, false);
	}

	/**
	 * 标准化URL字符串，包括：
	 *
	 * <ol>
	 *     <li>自动补齐“http://”头</li>
	 *     <li>去除开头的\或者/</li>
	 *     <li>替换\为/</li>
	 * </ol>
	 *
	 * @param url          URL字符串
	 * @param isEncodePath 是否对URL中path部分的中文和特殊字符做转义（不包括 http:, /和域名部分）
	 * @return 标准化后的URL字符串
	 * @since 4.4.1
	 */
	public static String normalize(final String url, final boolean isEncodePath) {
		return normalize(url, isEncodePath, false);
	}

	/**
	 * 标准化URL字符串，包括：
	 *
	 * <ol>
	 *     <li>自动补齐“http://”头</li>
	 *     <li>去除开头的\或者/</li>
	 *     <li>替换\为/</li>
	 *     <li>如果replaceSlash为true，则替换多个/为一个</li>
	 * </ol>
	 *
	 * @param url          URL字符串
	 * @param isEncodePath 是否对URL中path部分的中文和特殊字符做转义（不包括 http:, /和域名部分）
	 * @param replaceSlash 是否替换url body中的 //
	 * @return 标准化后的URL字符串
	 * @since 5.5.5
	 */
	public static String normalize(final String url, final boolean isEncodePath, final boolean replaceSlash) {
		if (StrUtil.isBlank(url)) {
			return url;
		}
		final int sepIndex = url.indexOf("://");
		final String protocol;
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
			body = body.replaceAll("^[\\\\/]+", StrUtil.EMPTY);
			// 替换\为/
			body = body.replace("\\", "/");
			if (replaceSlash) {
				//issue#I25MZL@Gitee，双斜杠在URL中是允许存在的，默认不做替换
				body = body.replaceAll("//+", "/");
			}
		}

		final int pathSepIndex = StrUtil.indexOf(body, '/');
		String domain = body;
		String path = null;
		if (pathSepIndex > 0) {
			domain = StrUtil.subPre(body, pathSepIndex);
			path = StrUtil.subSuf(body, pathSepIndex);
		}
		if (isEncodePath) {
			path = RFC3986.PATH.encode(path, CharsetUtil.UTF_8);
		}
		return protocol + domain + StrUtil.emptyIfNull(path) + StrUtil.emptyIfNull(params);
	}
	// endregion

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
	public static String buildQuery(final Map<String, ?> paramMap, final Charset charset) {
		return UrlQuery.of(paramMap).build(charset);
	}

	// region getDataUri

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
	public static String getDataUriBase64(final String mimeType, final String data) {
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
	public static String getDataUri(final String mimeType, final String encoding, final String data) {
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
	public static String getDataUri(final String mimeType, final Charset charset, final String encoding, final String data) {
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
	// endregion

	/**
	 * 获取URL对应数据长度
	 * <ul>
	 *     <li>如果URL为文件，转换为文件获取文件长度。</li>
	 *     <li>其它情况获取{@link URLConnection#getContentLengthLong()}</li>
	 * </ul>
	 *
	 * @param url URL
	 * @return 长度
	 * @since 6.0.0
	 */
	public static long size(final URL url) {
		if (UrlProtocolUtil.isFileOrVfsURL(url)) {
			// 如果资源以独立文件形式存在，尝试获取文件长度
			final File file = FileUtil.file(url);
			final long length = file.length();
			if (length == 0L && !file.exists()) {
				throw new IORuntimeException("File not exist or size is zero!");
			}
			return length;
		} else {
			// 如果资源打在jar包中或来自网络，使用网络请求长度
			// issue#3226, 来自Spring的AbstractFileResolvingResource
			URLConnection conn = null;
			try {
				conn = url.openConnection();
				useCachesIfNecessary(conn);
				if (conn instanceof HttpURLConnection) {
					final HttpURLConnection httpCon = (HttpURLConnection) conn;
					httpCon.setRequestMethod("HEAD");
				}
				return conn.getContentLengthLong();
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			} finally {
				if (conn instanceof HttpURLConnection) {
					((HttpURLConnection) conn).disconnect();
				}
			}
		}
	}

	/**
	 * 如果连接为JNLP方式，则打开缓存
	 *
	 * @param con {@link URLConnection}
	 * @since 6.0.0
	 */
	public static void useCachesIfNecessary(final URLConnection con) {
		con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
	}
}
