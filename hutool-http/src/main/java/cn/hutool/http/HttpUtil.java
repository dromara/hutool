package cn.hutool.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

/**
 * Http请求工具类
 * 
 * @author xiaoleilu
 */
public class HttpUtil {

	/** 正则：Content-Type中的编码信息 */
	public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);
	/** 正则：匹配meta标签的编码信息 */
	public static final Pattern META_CHARSET_PATTERN = Pattern.compile("<meta[^>]*?charset\\s*=\\s*['\"]?([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

	/**
	 * 检测是否https
	 * 
	 * @param url URL
	 * @return 是否https
	 */
	public static boolean isHttps(String url) {
		return url.toLowerCase().startsWith("https");
	}

	/**
	 * 创建Http请求对象
	 * 
	 * @param method 方法枚举{@link Method}
	 * @param url 请求的URL，可以使HTTP或者HTTPS
	 * @return {@link HttpRequest}
	 * @since 3.0.9
	 */
	public static HttpRequest createRequest(Method method, String url) {
		return new HttpRequest(url).method(method);
	}

	/**
	 * 创建Http GET请求对象
	 * 
	 * @param url 请求的URL，可以使HTTP或者HTTPS
	 * @return {@link HttpRequest}
	 * @since 3.2.0
	 */
	public static HttpRequest createGet(String url) {
		return HttpRequest.get(url);
	}

	/**
	 * 创建Http POST请求对象
	 * 
	 * @param url 请求的URL，可以使HTTP或者HTTPS
	 * @return {@link HttpRequest}
	 * @since 3.2.0
	 */
	public static HttpRequest createPost(String url) {
		return HttpRequest.post(url);
	}

	/**
	 * 发送get请求
	 * 
	 * @param urlString 网址
	 * @param customCharset 自定义请求字符集，如果字符集获取不到，使用此字符集
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 */
	public static String get(String urlString, Charset customCharset) {
		return HttpRequest.get(urlString).charset(customCharset).execute().body();
	}

	/**
	 * 发送get请求
	 * 
	 * @param urlString 网址
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 */
	public static String get(String urlString) {
		return get(urlString, HttpRequest.TIMEOUT_DEFAULT);
	}

	/**
	 * 发送get请求
	 * 
	 * @param urlString 网址
	 * @param timeout 超时时长，-1表示默认超时，单位毫秒
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 * @since 3.2.0
	 */
	public static String get(String urlString, int timeout) {
		return HttpRequest.get(urlString).timeout(timeout).execute().body();
	}

	/**
	 * 发送get请求
	 * 
	 * @param urlString 网址
	 * @param paramMap post表单数据
	 * @return 返回数据
	 */
	public static String get(String urlString, Map<String, Object> paramMap) {
		return HttpRequest.get(urlString).form(paramMap).execute().body();
	}

	/**
	 * 发送get请求
	 * 
	 * @param urlString 网址
	 * @param paramMap post表单数据
	 * @param timeout 超时时长，-1表示默认超时，单位毫秒
	 * @return 返回数据
	 * @since 3.3.0
	 */
	public static String get(String urlString, Map<String, Object> paramMap, int timeout) {
		return HttpRequest.get(urlString).form(paramMap).timeout(timeout).execute().body();
	}

	/**
	 * 发送post请求
	 * 
	 * @param urlString 网址
	 * @param paramMap post表单数据
	 * @return 返回数据
	 */
	public static String post(String urlString, Map<String, Object> paramMap) {
		return post(urlString, paramMap, HttpRequest.TIMEOUT_DEFAULT);
	}

	/**
	 * 发送post请求
	 * 
	 * @param urlString 网址
	 * @param paramMap post表单数据
	 * @param timeout 超时时长，-1表示默认超时，单位毫秒
	 * @return 返回数据
	 * @since 3.2.0
	 */
	public static String post(String urlString, Map<String, Object> paramMap, int timeout) {
		return HttpRequest.post(urlString).form(paramMap).timeout(timeout).execute().body();
	}

	/**
	 * 发送post请求<br>
	 * 请求体body参数支持两种类型：
	 * 
	 * <pre>
	 * 1. 标准参数，例如 a=1&amp;b=2 这种格式
	 * 2. Rest模式，此时body需要传入一个JSON或者XML字符串，Hutool会自动绑定其对应的Content-Type
	 * </pre>
	 * 
	 * @param urlString 网址
	 * @param body post表单数据
	 * @return 返回数据
	 */
	public static String post(String urlString, String body) {
		return post(urlString, body, HttpRequest.TIMEOUT_DEFAULT);
	}

	/**
	 * 发送post请求<br>
	 * 请求体body参数支持两种类型：
	 * 
	 * <pre>
	 * 1. 标准参数，例如 a=1&amp;b=2 这种格式
	 * 2. Rest模式，此时body需要传入一个JSON或者XML字符串，Hutool会自动绑定其对应的Content-Type
	 * </pre>
	 * 
	 * @param urlString 网址
	 * @param body post表单数据
	 * @param timeout 超时时长，-1表示默认超时，单位毫秒
	 * @return 返回数据
	 * @since 3.2.0
	 */
	public static String post(String urlString, String body, int timeout) {
		return HttpRequest.post(urlString).timeout(timeout).body(body).execute().body();
	}

	// ---------------------------------------------------------------------------------------- download
	/**
	 * 下载远程文本
	 * 
	 * @param url 请求的url
	 * @param customCharsetName 自定义的字符集
	 * @return 文本
	 */
	public static String downloadString(String url, String customCharsetName) {
		return downloadString(url, CharsetUtil.charset(customCharsetName), null);
	}

	/**
	 * 下载远程文本
	 * 
	 * @param url 请求的url
	 * @param customCharset 自定义的字符集，可以使用{@link CharsetUtil#charset} 方法转换
	 * @return 文本
	 */
	public static String downloadString(String url, Charset customCharset) {
		return downloadString(url, customCharset, null);
	}

	/**
	 * 下载远程文本
	 * 
	 * @param url 请求的url
	 * @param customCharset 自定义的字符集，可以使用{@link CharsetUtil#charset} 方法转换
	 * @param streamPress 进度条 {@link StreamProgress}
	 * @return 文本
	 */
	public static String downloadString(String url, Charset customCharset, StreamProgress streamPress) {
		if (StrUtil.isBlank(url)) {
			throw new NullPointerException("[url] is null!");
		}

		FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		download(url, out, true, streamPress);
		return null == customCharset ? out.toString() : out.toString(customCharset);
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url 请求的url
	 * @param dest 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @return 文件大小
	 */
	public static long downloadFile(String url, String dest) {
		return downloadFile(url, FileUtil.file(dest));
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url 请求的url
	 * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @return 文件大小
	 */
	public static long downloadFile(String url, File destFile) {
		return downloadFile(url, destFile, null);
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url 请求的url
	 * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param timeout 超时，单位毫秒，-1表示默认超时
	 * @return 文件大小
	 * @since 4.0.4
	 */
	public static long downloadFile(String url, File destFile, int timeout) {
		return downloadFile(url, destFile, timeout, null);
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url 请求的url
	 * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param streamProgress 进度条
	 * @return 文件大小
	 */
	public static long downloadFile(String url, File destFile, StreamProgress streamProgress) {
		return downloadFile(url, destFile, -1, streamProgress);
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url 请求的url
	 * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param timeout 超时，单位毫秒，-1表示默认超时
	 * @param streamProgress 进度条
	 * @return 文件大小
	 * @since 4.0.4
	 */
	public static long downloadFile(String url, File destFile, int timeout, StreamProgress streamProgress) {
		if (StrUtil.isBlank(url)) {
			throw new NullPointerException("[url] is null!");
		}
		if (null == destFile) {
			throw new NullPointerException("[destFile] is null!");
		}
		final HttpResponse response = HttpRequest.get(url).timeout(timeout).executeAsync();
		if (false == response.isOk()) {
			throw new HttpException("Server response error with status code: [{}]", response.getStatus());
		}
		return response.writeBody(destFile, streamProgress);
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url 请求的url
	 * @param out 将下载内容写到输出流中 {@link OutputStream}
	 * @param isCloseOut 是否关闭输出流
	 * @return 文件大小
	 */
	public static long download(String url, OutputStream out, boolean isCloseOut) {
		return download(url, out, isCloseOut, null);
	}

	/**
	 * 下载远程文件
	 * 
	 * @param url 请求的url
	 * @param out 将下载内容写到输出流中 {@link OutputStream}
	 * @param isCloseOut 是否关闭输出流
	 * @param streamProgress 进度条
	 * @return 文件大小
	 */
	public static long download(String url, OutputStream out, boolean isCloseOut, StreamProgress streamProgress) {
		if (StrUtil.isBlank(url)) {
			throw new NullPointerException("[url] is null!");
		}
		if (null == out) {
			throw new NullPointerException("[out] is null!");
		}

		final HttpResponse response = HttpRequest.get(url).executeAsync();
		if (false == response.isOk()) {
			throw new HttpException("Server response error with status code: [{}]", response.getStatus());
		}
		return response.writeBody(out, isCloseOut, streamProgress);
	}

	/**
	 * 将Map形式的Form表单数据转换为Url参数形式，不做编码
	 * 
	 * @param paramMap 表单数据
	 * @return url参数
	 */
	public static String toParams(Map<String, ?> paramMap) {
		return toParams(paramMap, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将Map形式的Form表单数据转换为Url参数形式<br>
	 * 编码键和值对
	 * 
	 * @param paramMap 表单数据
	 * @param charsetName 编码
	 * @return url参数
	 */
	public static String toParams(Map<String, Object> paramMap, String charsetName) {
		return toParams(paramMap, CharsetUtil.charset(charsetName));
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
	 * @param charset 编码
	 * @return url参数
	 */
	public static String toParams(Map<String, ?> paramMap, Charset charset) {
		if (CollectionUtil.isEmpty(paramMap)) {
			return StrUtil.EMPTY;
		}
		if (null == charset) {// 默认编码为系统编码
			charset = CharsetUtil.CHARSET_UTF_8;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		String key;
		Object value;
		String valueStr;
		for (Entry<String, ?> item : paramMap.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append("&");
			}
			key = item.getKey();
			value = item.getValue();
			if (value instanceof Iterable) {
				value = CollectionUtil.join((Iterable<?>) value, ",");
			} else if (value instanceof Iterator) {
				value = CollectionUtil.join((Iterator<?>) value, ",");
			}
			valueStr = Convert.toStr(value);
			if (StrUtil.isNotEmpty(key)) {
				sb.append(URLUtil.encodeQuery(key, charset)).append("=");
				if (StrUtil.isNotEmpty(valueStr)) {
					sb.append(URLUtil.encodeQuery(valueStr, charset));
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 编码字符为 application/x-www-form-urlencoded，使用UTF-8编码
	 * 
	 * @param content 被编码内容
	 * @return 编码后的字符
	 * @deprecated 请使用{@link URLUtil#encode(String)}
	 */
	@Deprecated
	public static String encodeUtf8(String content) {
		return encode(content, CharsetUtil.UTF_8);
	}

	/**
	 * 编码字符为 application/x-www-form-urlencoded
	 * 
	 * @param content 被编码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 * @deprecated 请使用{@link URLUtil#encode(String, Charset)}
	 */
	@Deprecated
	public static String encode(String content, Charset charset) {
		return URLUtil.encode(content, charset);
	}

	/**
	 * 编码字符为 application/x-www-form-urlencoded
	 * 
	 * @param content 被编码内容
	 * @param charsetStr 编码
	 * @return 编码后的字符
	 * @see URLUtil#encode(String, String)
	 */
	public static String encode(String content, String charsetStr) {
		return URLUtil.encode(content, charsetStr);
	}

	/**
	 * 解码application/x-www-form-urlencoded字符
	 * 
	 * @param content 被解码内容
	 * @param charset 编码
	 * @return 编码后的字符
	 * @deprecated 请使用{@link URLUtil#decode(String, Charset)}
	 */
	@Deprecated
	public static String decode(String content, Charset charset) {
		return URLUtil.decode(content, charset);
	}

	/**
	 * 解码application/x-www-form-urlencoded字符
	 * 
	 * @param content 被解码内容
	 * @param charsetStr 编码
	 * @return 编码后的字符
	 * @deprecated 请使用{@link URLUtil#decode(String, String)}
	 */
	@Deprecated
	public static String decode(String content, String charsetStr) {
		return URLUtil.decode(content, charsetStr);
	}

	/**
	 * 对URL参数做编码，只编码键和值<br>
	 * 提供的值可以是url附带编码，但是不能只是url
	 * 
	 * 
	 * @param paramsStr url参数，可以包含url本身
	 * @param charset 编码
	 * @return 编码后的url和参数
	 * @since 4.0.1
	 */
	public static String encodeParams(final String paramsStr, Charset charset) {
		if (StrUtil.isBlank(paramsStr)) {
			return StrUtil.EMPTY;
		}

		String urlPart = null; // url部分，不包括问号
		String paramPart; // 参数部分
		int pathEndPos = paramsStr.indexOf('?');
		if (pathEndPos > -1) {
			// url + 参数
			urlPart = StrUtil.subPre(paramsStr, pathEndPos);
			paramPart = StrUtil.subSuf(paramsStr, pathEndPos + 1);
			if (StrUtil.isBlank(paramPart)) {
				// 无参数，返回url
				return urlPart;
			}
		} else {
			// 无URL
			paramPart = paramsStr;
		}

		final StrBuilder builder = StrBuilder.create(paramPart.length() + 16);
		final int len = paramPart.length();
		String name = null;
		int pos = 0; // 未处理字符开始位置
		char c; // 当前字符
		int i; // 当前字符位置
		for (i = 0; i < len; i++) {
			c = paramPart.charAt(i);
			if (c == '=') { // 键值对的分界点
				if (null == name) {
					// 只有=前未定义name时被当作键值分界符，否则做为普通字符
					name = (pos == i) ? StrUtil.EMPTY : paramPart.substring(pos, i);
					pos = i + 1;
				}
			} else if (c == '&') { // 参数对的分界点
				if (pos != i) {
					if (null == name) {
						// 对于像&a&这类无参数值的字符串，我们将name为a的值设为""
						name = paramPart.substring(pos, i);
						builder.append(URLUtil.encodeQuery(name, charset)).append('=');
					} else {
						builder.append(URLUtil.encodeQuery(name, charset)).append('=').append(URLUtil.encodeQuery(paramPart.substring(pos, i), charset)).append('&');
					}
					name = null;
				}
				pos = i + 1;
			}
		}

		// 结尾处理
		if (null != name) {
			builder.append(URLUtil.encodeQuery(name, charset)).append('=');
		}
		if (pos != i) {
			if (null == name) {
				builder.append('=');
			}
			builder.append(URLUtil.encodeQuery(paramPart.substring(pos, i), charset));
		}

		int lastIndex = builder.length() - 1;
		if ('&' == builder.charAt(lastIndex)) {
			builder.delTo(lastIndex);
		}
		return StrUtil.isBlank(urlPart) ? builder.toString() : urlPart + "?" + builder.toString();
	}

	/**
	 * 将URL参数解析为Map（也可以解析Post中的键值对参数）
	 * 
	 * @param paramsStr 参数字符串（或者带参数的Path）
	 * @param charset 字符集
	 * @return 参数Map
	 * @since 4.0.2
	 */
	public static HashMap<String, String> decodeParamMap(String paramsStr, String charset) {
		final Map<String, List<String>> paramsMap = decodeParams(paramsStr, charset);
		final HashMap<String, String> result = MapUtil.newHashMap(paramsMap.size());
		List<String> valueList;
		for (Entry<String, List<String>> entry : paramsMap.entrySet()) {
			valueList = entry.getValue();
			result.put(entry.getKey(), CollUtil.isEmpty(valueList) ? null : valueList.get(0));
		}
		return result;
	}

	/**
	 * 将URL参数解析为Map（也可以解析Post中的键值对参数）
	 * 
	 * @param paramsStr 参数字符串（或者带参数的Path）
	 * @param charset 字符集
	 * @return 参数Map
	 */
	public static Map<String, List<String>> decodeParams(String paramsStr, String charset) {
		if (StrUtil.isBlank(paramsStr)) {
			return Collections.emptyMap();
		}

		// 去掉Path部分
		int pathEndPos = paramsStr.indexOf('?');
		if (pathEndPos > -1) {
			paramsStr = StrUtil.subSuf(paramsStr, pathEndPos + 1);
		}

		final Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		final int len = paramsStr.length();
		String name = null;
		int pos = 0; // 未处理字符开始位置
		int i; // 未处理字符结束位置
		char c; // 当前字符
		for (i = 0; i < len; i++) {
			c = paramsStr.charAt(i);
			if (c == '=') { // 键值对的分界点
				if (null == name) {
					// name可以是""
					name = paramsStr.substring(pos, i);
				}
				pos = i + 1;
			} else if (c == '&') { // 参数对的分界点
				if (null == name && pos != i) {
					// 对于像&a&这类无参数值的字符串，我们将name为a的值设为""
					addParam(params, paramsStr.substring(pos, i), StrUtil.EMPTY, charset);
				} else if (name != null) {
					addParam(params, name, paramsStr.substring(pos, i), charset);
					name = null;
				}
				pos = i + 1;
			}
		}

		// 处理结尾
		if (pos != i) {
			if (name == null) {
				addParam(params, paramsStr.substring(pos, i), StrUtil.EMPTY, charset);
			} else {
				addParam(params, name, paramsStr.substring(pos, i), charset);
			}
		} else if (name != null) {
			addParam(params, name, StrUtil.EMPTY, charset);
		}

		return params;
	}

	/**
	 * 将表单数据加到URL中（用于GET表单提交）<br>
	 * 表单的键值对会被url编码，但是url中原参数不会被编码
	 * 
	 * @param url URL
	 * @param form 表单数据
	 * @param charset 编码
	 * @param isEncodeParams 是否对键和值做转义处理
	 * @return 合成后的URL
	 */
	public static String urlWithForm(String url, Map<String, Object> form, Charset charset, boolean isEncodeParams) {
		if (isEncodeParams && StrUtil.contains(url, '?')) {
			// 在需要编码的情况下，如果url中已经有部分参数，则编码之
			url = encodeParams(url, charset);
		}

		// url和参数是分别编码的
		return urlWithForm(url, toParams(form, charset), charset, false);
	}

	/**
	 * 将表单数据字符串加到URL中（用于GET表单提交）
	 * 
	 * @param url URL
	 * @param queryString 表单数据字符串
	 * @param charset 编码
	 * @param isEncode 是否对键和值做转义处理
	 * @return 拼接后的字符串
	 */
	public static String urlWithForm(String url, String queryString, Charset charset, boolean isEncode) {
		if (StrUtil.isBlank(queryString)) {
			// 无额外参数
			if (StrUtil.contains(url, '?')) {
				// url中包含参数
				return isEncode ? encodeParams(url, charset) : url;
			}
			return url;
		}

		// 始终有参数
		final StrBuilder urlBuilder = StrBuilder.create(url.length() + queryString.length() + 16);
		int qmIndex = url.indexOf('?');
		if (qmIndex > 0) {
			// 原URL带参数，则对这部分参数单独编码（如果选项为进行编码）
			urlBuilder.append(isEncode ? encodeParams(url, charset) : url);
			if (false == StrUtil.endWith(url, '&')) {
				// 已经带参数的情况下追加参数
				urlBuilder.append('&');
			}
		} else {
			// 原url无参数，则不做编码
			urlBuilder.append(url);
			if (qmIndex < 0) {
				// 无 '?' 追加之
				urlBuilder.append('?');
			}
		}
		urlBuilder.append(isEncode ? encodeParams(queryString, charset) : queryString);
		return urlBuilder.toString();
	}

	/**
	 * 从Http连接的头信息中获得字符集<br>
	 * 从ContentType中获取
	 * 
	 * @param conn HTTP连接对象
	 * @return 字符集
	 */
	public static String getCharset(HttpURLConnection conn) {
		if (conn == null) {
			return null;
		}
		return ReUtil.get(CHARSET_PATTERN, conn.getContentType(), 1);
	}

	/**
	 * 从流中读取内容<br>
	 * 首先尝试使用charset编码读取内容（如果为空默认UTF-8），如果isGetCharsetFromContent为true，则通过正则在正文中获取编码信息，转换为指定编码；
	 * 
	 * @param in 输入流
	 * @param charset 字符集
	 * @param isGetCharsetFromContent 是否从返回内容中获得编码信息
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public static String getString(InputStream in, Charset charset, boolean isGetCharsetFromContent) throws IOException {
		final byte[] contentBytes = IoUtil.readBytes(in);
		return getString(contentBytes, charset, isGetCharsetFromContent);
	}

	/**
	 * 从流中读取内容<br>
	 * 首先尝试使用charset编码读取内容（如果为空默认UTF-8），如果isGetCharsetFromContent为true，则通过正则在正文中获取编码信息，转换为指定编码；
	 * 
	 * @param contentBytes 内容byte数组
	 * @param charset 字符集
	 * @param isGetCharsetFromContent 是否从返回内容中获得编码信息
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public static String getString(byte[] contentBytes, Charset charset, boolean isGetCharsetFromContent) throws IOException {
		if (null == contentBytes) {
			return null;
		}

		if (null == charset) {
			charset = CharsetUtil.CHARSET_UTF_8;
		}
		String content = new String(contentBytes, charset);
		if (isGetCharsetFromContent) {
			final String charsetInContentStr = ReUtil.get(META_CHARSET_PATTERN, content, 1);
			if (StrUtil.isNotBlank(charsetInContentStr)) {
				Charset charsetInContent = null;
				try {
					charsetInContent = Charset.forName(charsetInContentStr);
				} catch (Exception e) {
					if (StrUtil.containsIgnoreCase(charsetInContentStr, "utf-8") || StrUtil.containsIgnoreCase(charsetInContentStr, "utf8")) {
						charsetInContent = CharsetUtil.CHARSET_UTF_8;
					} else if (StrUtil.containsIgnoreCase(charsetInContentStr, "gbk")) {
						charsetInContent = CharsetUtil.CHARSET_GBK;
					}
					// ignore
				}
				if (null != charsetInContent && false == charset.equals(charsetInContent)) {
					content = new String(contentBytes, charsetInContent);
				}
			}
		}
		return content;
	}

	/**
	 * 根据文件扩展名获得MimeType
	 * 
	 * @param filePath 文件路径或文件名
	 * @return MimeType
	 * @see FileUtil#getMimeType(String)
	 */
	public static String getMimeType(String filePath) {
		return FileUtil.getMimeType(filePath);
	}

	/**
	 * 从请求参数的body中判断请求的Content-Type类型，支持的类型有：
	 * 
	 * <pre>
	 * 1. application/json
	 * 1. application/xml
	 * </pre>
	 * 
	 * @param body 请求参数体
	 * @return Content-Type类型，如果无法判断返回null
	 * @since 3.2.0
	 * @see ContentType#get(String)
	 */
	public static String getContentTypeByRequestBody(String body) {
		final ContentType contentType = ContentType.get(body);
		return (null == contentType) ? null : contentType.toString();
	}
	// ----------------------------------------------------------------------------------------- Private method start

	/**
	 * 将键值对加入到值为List类型的Map中
	 * 
	 * @param params 参数
	 * @param name key
	 * @param value value
	 * @param charset 编码
	 */
	private static void addParam(Map<String, List<String>> params, String name, String value, String charset) {
		name = URLUtil.decode(name, charset);
		value = URLUtil.decode(value, charset);
		List<String> values = params.get(name);
		if (values == null) {
			values = new ArrayList<String>(1); // 一般是一个参数
			params.put(name, values);
		}
		values.add(value);
	}

	// ----------------------------------------------------------------------------------------- Private method start end
}
