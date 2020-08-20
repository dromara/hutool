package cn.hutool.http;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.server.SimpleServer;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Http请求工具类
 *
 * @author xiaoleilu
 */
public class HttpUtil {

	/**
	 * 正则：Content-Type中的编码信息
	 */
	public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);
	/**
	 * 正则：匹配meta标签的编码信息
	 */
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
	 * 检测是否http
	 *
	 * @param url URL
	 * @return 是否https
	 * @since 5.3.8
	 */
	public static boolean isHttp(String url) {
		return url.toLowerCase().startsWith("http");
	}

	/**
	 * 创建Http请求对象
	 *
	 * @param method 方法枚举{@link Method}
	 * @param url    请求的URL，可以使HTTP或者HTTPS
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
	 * @param urlString     网址
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
		return get(urlString, HttpGlobalConfig.timeout);
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @param timeout   超时时长，-1表示默认超时，单位毫秒
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
	 * @param paramMap  post表单数据
	 * @return 返回数据
	 */
	public static String get(String urlString, Map<String, Object> paramMap) {
		return HttpRequest.get(urlString).form(paramMap).execute().body();
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @param paramMap  post表单数据
	 * @param timeout   超时时长，-1表示默认超时，单位毫秒
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
	 * @param paramMap  post表单数据
	 * @return 返回数据
	 */
	public static String post(String urlString, Map<String, Object> paramMap) {
		return post(urlString, paramMap, HttpGlobalConfig.timeout);
	}

	/**
	 * 发送post请求
	 *
	 * @param urlString 网址
	 * @param paramMap  post表单数据
	 * @param timeout   超时时长，-1表示默认超时，单位毫秒
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
	 * @param body      post表单数据
	 * @return 返回数据
	 */
	public static String post(String urlString, String body) {
		return post(urlString, body, HttpGlobalConfig.timeout);
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
	 * @param body      post表单数据
	 * @param timeout   超时时长，-1表示默认超时，单位毫秒
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
	 * @param url               请求的url
	 * @param customCharsetName 自定义的字符集
	 * @return 文本
	 */
	public static String downloadString(String url, String customCharsetName) {
		return downloadString(url, CharsetUtil.charset(customCharsetName), null);
	}

	/**
	 * 下载远程文本
	 *
	 * @param url           请求的url
	 * @param customCharset 自定义的字符集，可以使用{@link CharsetUtil#charset} 方法转换
	 * @return 文本
	 */
	public static String downloadString(String url, Charset customCharset) {
		return downloadString(url, customCharset, null);
	}

	/**
	 * 下载远程文本
	 *
	 * @param url           请求的url
	 * @param customCharset 自定义的字符集，可以使用{@link CharsetUtil#charset} 方法转换
	 * @param streamPress   进度条 {@link StreamProgress}
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
	 * @param url  请求的url
	 * @param dest 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @return 文件大小
	 */
	public static long downloadFile(String url, String dest) {
		return downloadFile(url, FileUtil.file(dest));
	}

	/**
	 * 下载远程文件
	 *
	 * @param url      请求的url
	 * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @return 文件大小
	 */
	public static long downloadFile(String url, File destFile) {
		return downloadFile(url, destFile, null);
	}

	/**
	 * 下载远程文件
	 *
	 * @param url      请求的url
	 * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param timeout  超时，单位毫秒，-1表示默认超时
	 * @return 文件大小
	 * @since 4.0.4
	 */
	public static long downloadFile(String url, File destFile, int timeout) {
		return downloadFile(url, destFile, timeout, null);
	}

	/**
	 * 下载远程文件
	 *
	 * @param url            请求的url
	 * @param destFile       目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param streamProgress 进度条
	 * @return 文件大小
	 */
	public static long downloadFile(String url, File destFile, StreamProgress streamProgress) {
		return downloadFile(url, destFile, -1, streamProgress);
	}

	/**
	 * 下载远程文件
	 *
	 * @param url            请求的url
	 * @param destFile       目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param timeout        超时，单位毫秒，-1表示默认超时
	 * @param streamProgress 进度条
	 * @return 文件大小
	 * @since 4.0.4
	 */
	public static long downloadFile(String url, File destFile, int timeout, StreamProgress streamProgress) {
		return requestDownloadFile(url, destFile, timeout).writeBody(destFile, streamProgress);
	}
	
	/**
	 * 下载远程文件
	 *
	 * @param url  请求的url
	 * @param dest 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 *
	 * @return 下载的文件对象
	 * @since 5.4.1
	 */
	public static File downloadFileFromUrl(String url, String dest) {
		return downloadFileFromUrl(url, FileUtil.file(dest));
	}
	
	/**
	 * 下载远程文件
	 *
	 * @param url      请求的url
	 * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 *
	 * @return 下载的文件对象
	 * @since 5.4.1
	 */
	public static File downloadFileFromUrl(String url, File destFile) {
		return downloadFileFromUrl(url, destFile, null);
	}
	
	/**
	 * 下载远程文件
	 *
	 * @param url      请求的url
	 * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param timeout  超时，单位毫秒，-1表示默认超时
	 *
	 * @return 下载的文件对象
	 * @since 5.4.1
	 */
	public static File downloadFileFromUrl(String url, File destFile, int timeout) {
		return downloadFileFromUrl(url, destFile, timeout, null);
	}
	
	/**
	 * 下载远程文件
	 *
	 * @param url            请求的url
	 * @param destFile       目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param streamProgress 进度条
	 *
	 * @return 下载的文件对象
	 * @since 5.4.1
	 */
	public static File downloadFileFromUrl(String url, File destFile, StreamProgress streamProgress) {
		return downloadFileFromUrl(url, destFile, -1, streamProgress);
	}
	
	/**
	 * 下载远程文件
	 *
	 * @param url            请求的url
	 * @param destFile       目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param timeout        超时，单位毫秒，-1表示默认超时
	 * @param streamProgress 进度条
	 *
	 * @return 下载的文件对象
	 * @since 5.4.1
	 */
	public static File downloadFileFromUrl(String url, File destFile, int timeout, StreamProgress streamProgress) {
		HttpResponse response = requestDownloadFile(url, destFile, timeout);
		
		final File outFile = response.completeFileNameFromHeader(destFile);
		long writeBytes = response.writeBody(outFile, streamProgress);
		return outFile;
	}
	
	/**
	 * 请求下载文件
	 *
	 * @param url      请求下载文件地址
	 * @param destFile 目标目录或者目标文件
	 * @param timeout  超时时间
	 *
	 * @return HttpResponse
	 * @since 5.4.1
	 */
	private static HttpResponse requestDownloadFile(String url, File destFile, int timeout) {
		Assert.notBlank(url, "[url] is blank !");
		Assert.notNull(url, "[destFile] is null !");

		final HttpResponse response = HttpRequest.get(url).timeout(timeout).executeAsync();
		if (response.isOk()) {
			return response;
		}

		throw new HttpException("Server response error with status code: [{}]", response.getStatus());
	}
	
	/**
	 * 下载远程文件
	 *
	 * @param url        请求的url
	 * @param out        将下载内容写到输出流中 {@link OutputStream}
	 * @param isCloseOut 是否关闭输出流
	 *
	 * @return 文件大小
	 */
	public static long download(String url, OutputStream out, boolean isCloseOut) {
		return download(url, out, isCloseOut, null);
	}

	/**
	 * 下载远程文件
	 *
	 * @param url            请求的url
	 * @param out            将下载内容写到输出流中 {@link OutputStream}
	 * @param isCloseOut     是否关闭输出流
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
		if (!response.isOk()) {
			throw new HttpException("Server response error with status code: [{}]", response.getStatus());
		}
		return response.writeBody(out, isCloseOut, streamProgress);
	}
	
	/**
	 * 下载远程文件数据，支持30x跳转
	 *
	 * @param url 请求的url
	 *
	 * @return 文件数据
	 *
	 * @since 5.3.6
	 */
	public static byte[] downloadBytes(String url) {
		if (StrUtil.isBlank(url)) {
			throw new NullPointerException("[url] is null!");
		}
		
		final HttpResponse response = HttpRequest.get(url)
				.setFollowRedirects(true).executeAsync();
		if (!response.isOk()) {
			throw new HttpException("Server response error with status code: [{}]", response.getStatus());
		}
		return response.bodyBytes();
	}

	/**
	 * 将Map形式的Form表单数据转换为Url参数形式，会自动url编码键和值
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
	 * @param paramMap    表单数据
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
	 * @param charset  编码，null表示不encode键值对
	 * @return url参数
	 */
	public static String toParams(Map<String, ?> paramMap, Charset charset) {
		return URLUtil.buildQuery(paramMap, charset);
	}

	/**
	 * 对URL参数做编码，只编码键和值<br>
	 * 提供的值可以是url附带参数，但是不能只是url
	 *
	 * <p>注意，此方法只能标准化整个URL，并不适合于单独编码参数值</p>
	 *
	 * @param urlWithParams url和参数，可以包含url本身，也可以单独参数
	 * @param charset       编码
	 * @return 编码后的url和参数
	 * @since 4.0.1
	 */
	public static String encodeParams(String urlWithParams, Charset charset) {
		if (StrUtil.isBlank(urlWithParams)) {
			return StrUtil.EMPTY;
		}

		String urlPart = null; // url部分，不包括问号
		String paramPart; // 参数部分
		final int pathEndPos = urlWithParams.indexOf('?');
		if (pathEndPos > -1) {
			// url + 参数
			urlPart = StrUtil.subPre(urlWithParams, pathEndPos);
			paramPart = StrUtil.subSuf(urlWithParams, pathEndPos + 1);
			if (StrUtil.isBlank(paramPart)) {
				// 无参数，返回url
				return urlPart;
			}
		} else if (false == StrUtil.contains(urlWithParams, '=')) {
			// 无参数的URL
			return urlWithParams;
		} else {
			// 无URL的参数
			paramPart = urlWithParams;
		}

		paramPart = normalizeParams(paramPart, charset);

		return StrUtil.isBlank(urlPart) ? paramPart : urlPart + "?" + paramPart;
	}

	/**
	 * 标准化参数字符串，即URL中？后的部分
	 *
	 * <p>注意，此方法只能标准化整个URL，并不适合于单独编码参数值</p>
	 *
	 * @param paramPart 参数字符串
	 * @param charset   编码
	 * @return 标准化的参数字符串
	 * @since 4.5.2
	 */
	public static String normalizeParams(String paramPart, Charset charset) {
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
			if (null == name && pos > 0) {
				builder.append('=');
			}
			builder.append(URLUtil.encodeQuery(paramPart.substring(pos, i), charset));
		}

		// 以&结尾则去除之
		int lastIndex = builder.length() - 1;
		if ('&' == builder.charAt(lastIndex)) {
			builder.delTo(lastIndex);
		}
		return builder.toString();
	}

	/**
	 * 将URL参数解析为Map（也可以解析Post中的键值对参数）
	 *
	 * @param paramsStr 参数字符串（或者带参数的Path）
	 * @param charset   字符集
	 * @return 参数Map
	 * @since 4.0.2
	 * @deprecated 请使用 {@link #decodeParamMap(String, Charset)}
	 */
	@Deprecated
	public static Map<String, String> decodeParamMap(String paramsStr, String charset) {
		return decodeParamMap(paramsStr, CharsetUtil.charset(charset));
	}

	/**
	 * 将URL参数解析为Map（也可以解析Post中的键值对参数）
	 *
	 * @param paramsStr 参数字符串（或者带参数的Path）
	 * @param charset   字符集
	 * @return 参数Map
	 * @since 5.2.6
	 */
	public static Map<String, String> decodeParamMap(String paramsStr, Charset charset) {
		final Map<CharSequence, CharSequence> queryMap = UrlQuery.of(paramsStr, charset).getQueryMap();
		if (MapUtil.isEmpty(queryMap)) {
			return MapUtil.empty();
		}
		return Convert.toMap(String.class, String.class, queryMap);
	}

	/**
	 * 将URL参数解析为Map（也可以解析Post中的键值对参数）
	 *
	 * @param paramsStr 参数字符串（或者带参数的Path）
	 * @param charset   字符集
	 * @return 参数Map
	 */
	public static Map<String, List<String>> decodeParams(String paramsStr, String charset) {
		return decodeParams(paramsStr, CharsetUtil.charset(charset));
	}

	/**
	 * 将URL参数解析为Map（也可以解析Post中的键值对参数）
	 *
	 * @param paramsStr 参数字符串（或者带参数的Path）
	 * @param charset   字符集
	 * @return 参数Map
	 * @since 5.2.6
	 */
	public static Map<String, List<String>> decodeParams(String paramsStr, Charset charset) {
		final Map<CharSequence, CharSequence> queryMap = UrlQuery.of(paramsStr, charset).getQueryMap();
		if (MapUtil.isEmpty(queryMap)) {
			return MapUtil.empty();
		}

		final Map<String, List<String>> params = new LinkedHashMap<>();
		queryMap.forEach((key, value) -> {
			final List<String> values = params.computeIfAbsent(StrUtil.str(key), k -> new ArrayList<>(1));
			// 一般是一个参数
			values.add(StrUtil.str(value));
		});
		return params;
	}

	/**
	 * 将表单数据加到URL中（用于GET表单提交）<br>
	 * 表单的键值对会被url编码，但是url中原参数不会被编码
	 *
	 * @param url            URL
	 * @param form           表单数据
	 * @param charset        编码
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
	 * @param url         URL
	 * @param queryString 表单数据字符串
	 * @param charset     编码
	 * @param isEncode    是否对键和值做转义处理
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
		return getCharset(conn.getContentType());
	}

	/**
	 * 从Http连接的头信息中获得字符集<br>
	 * 从ContentType中获取
	 *
	 * @param contentType Content-Type
	 * @return 字符集
	 * @since 5.2.6
	 */
	public static String getCharset(String contentType) {
		if (StrUtil.isBlank(contentType)) {
			return null;
		}
		return ReUtil.get(CHARSET_PATTERN, contentType, 1);
	}

	/**
	 * 从流中读取内容<br>
	 * 首先尝试使用charset编码读取内容（如果为空默认UTF-8），如果isGetCharsetFromContent为true，则通过正则在正文中获取编码信息，转换为指定编码；
	 *
	 * @param in                      输入流
	 * @param charset                 字符集
	 * @param isGetCharsetFromContent 是否从返回内容中获得编码信息
	 * @return 内容
	 */
	public static String getString(InputStream in, Charset charset, boolean isGetCharsetFromContent) {
		final byte[] contentBytes = IoUtil.readBytes(in);
		return getString(contentBytes, charset, isGetCharsetFromContent);
	}

	/**
	 * 从流中读取内容<br>
	 * 首先尝试使用charset编码读取内容（如果为空默认UTF-8），如果isGetCharsetFromContent为true，则通过正则在正文中获取编码信息，转换为指定编码；
	 *
	 * @param contentBytes            内容byte数组
	 * @param charset                 字符集
	 * @param isGetCharsetFromContent 是否从返回内容中获得编码信息
	 * @return 内容
	 */
	public static String getString(byte[] contentBytes, Charset charset, boolean isGetCharsetFromContent) {
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
	 * @param filePath     文件路径或文件名
	 * @param defaultValue 当获取MimeType为null时的默认值
	 * @return MimeType
	 * @see FileUtil#getMimeType(String)
	 * @since 4.6.5
	 */
	public static String getMimeType(String filePath, String defaultValue) {
		return ObjectUtil.defaultIfNull(getMimeType(filePath), defaultValue);
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
	 * @see ContentType#get(String)
	 * @since 3.2.0
	 */
	public static String getContentTypeByRequestBody(String body) {
		final ContentType contentType = ContentType.get(body);
		return (null == contentType) ? null : contentType.toString();
	}

	/**
	 * 创建简易的Http服务器
	 *
	 * @param port 端口
	 * @return {@link SimpleServer}
	 * @since 5.2.6
	 */
	public static SimpleServer createServer(int port) {
		return new SimpleServer(port);
	}
}
