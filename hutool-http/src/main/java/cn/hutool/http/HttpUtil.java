package cn.hutool.http;

import cn.hutool.core.codec.BaseN.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.url.UrlQueryUtil;
import cn.hutool.core.regex.ReUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.client.ClientConfig;
import cn.hutool.http.client.Request;
import cn.hutool.http.client.Response;
import cn.hutool.http.client.cookie.GlobalCookieManager;
import cn.hutool.http.client.engine.ClientEngineFactory;
import cn.hutool.http.meta.ContentType;
import cn.hutool.http.meta.Method;
import cn.hutool.http.server.SimpleServer;

import java.io.InputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
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
	public static boolean isHttps(final String url) {
		return StrUtil.startWithIgnoreCase(url, "https:");
	}

	/**
	 * 检测是否http
	 *
	 * @param url URL
	 * @return 是否http
	 * @since 5.3.8
	 */
	public static boolean isHttp(final String url) {
		return StrUtil.startWithIgnoreCase(url, "http:");
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString     网址
	 * @param customCharset 自定义请求字符集，如果字符集获取不到，使用此字符集
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 */
	@SuppressWarnings("resource")
	public static String get(final String urlString, final Charset customCharset) {
		return send(Request.of(urlString).charset(customCharset)).bodyStr();
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 */
	public static String get(final String urlString) {
		return get(urlString, HttpGlobalConfig.getTimeout());
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @param timeout   超时时长，-1表示默认超时，单位毫秒
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 * @since 3.2.0
	 */
	@SuppressWarnings("resource")
	public static String get(final String urlString, final int timeout) {
		return ClientEngineFactory.get()
				.setConfig(ClientConfig.of().setConnectionTimeout(timeout).setReadTimeout(timeout))
				.send(Request.of(urlString)).bodyStr();
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @param paramMap  post表单数据
	 * @return 返回数据
	 */
	@SuppressWarnings("resource")
	public static String get(final String urlString, final Map<String, Object> paramMap) {
		return send(Request.of(urlString).form(paramMap))
				.bodyStr();
	}

	/**
	 * 发送post请求
	 *
	 * @param urlString 网址
	 * @param paramMap  post表单数据
	 * @return 返回数据
	 */
	@SuppressWarnings("resource")
	public static String post(final String urlString, final Map<String, Object> paramMap) {
		return send(Request.of(urlString).method(Method.POST).form(paramMap))
				.bodyStr();
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
	@SuppressWarnings("resource")
	public static String post(final String urlString, final String body) {
		return send(Request.of(urlString).method(Method.POST).body(body))
				.bodyStr();
	}

	/**
	 * 使用默认HTTP引擎，发送HTTP请求
	 *
	 * @param request HTTP请求
	 * @return HTTP响应
	 * @see ClientEngineFactory#get()#send(Request)
	 */
	public static Response send(final Request request){
		return ClientEngineFactory.get().send(request);
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
	public static String urlWithForm(String url, final Map<String, Object> form, final Charset charset, final boolean isEncodeParams) {
		if (isEncodeParams && StrUtil.contains(url, '?')) {
			// 在需要编码的情况下，如果url中已经有部分参数，则编码之
			url = UrlQueryUtil.encodeQuery(url, charset);
		}

		// url和参数是分别编码的
		return urlWithForm(url, UrlQueryUtil.toQuery(form, charset), charset, false);
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
	public static String urlWithForm(final String url, final String queryString, final Charset charset, final boolean isEncode) {
		if (StrUtil.isBlank(queryString)) {
			// 无额外参数
			if (StrUtil.contains(url, '?')) {
				// url中包含参数
				return isEncode ? UrlQueryUtil.encodeQuery(url, charset) : url;
			}
			return url;
		}

		// 始终有参数
		final StringBuilder urlBuilder = new StringBuilder(url.length() + queryString.length() + 16);
		final int qmIndex = url.indexOf('?');
		if (qmIndex > 0) {
			// 原URL带参数，则对这部分参数单独编码（如果选项为进行编码）
			urlBuilder.append(isEncode ? UrlQueryUtil.encodeQuery(url, charset) : url);
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
		urlBuilder.append(isEncode ? UrlQueryUtil.encodeQuery(queryString, charset) : queryString);
		return urlBuilder.toString();
	}

	/**
	 * 从Http连接的头信息中获得字符集<br>
	 * 从ContentType中获取
	 *
	 * @param conn HTTP连接对象
	 * @return 字符集
	 */
	public static Charset getCharset(final HttpURLConnection conn) {
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
	public static Charset getCharset(final String contentType) {
		return CharsetUtil.parse(getCharsetName(contentType), null);
	}

	/**
	 * 从Http连接的头信息中获得字符集<br>
	 * 从ContentType中获取
	 *
	 * @param contentType Content-Type
	 * @return 字符集
	 * @since 5.2.6
	 */
	public static String getCharsetName(final String contentType) {
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
	public static String getString(final InputStream in, final Charset charset, final boolean isGetCharsetFromContent) {
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
	public static String getString(final byte[] contentBytes, Charset charset, final boolean isGetCharsetFromContent) {
		if (null == contentBytes) {
			return null;
		}

		if (null == charset) {
			charset = CharsetUtil.UTF_8;
		}
		String content = new String(contentBytes, charset);
		if (isGetCharsetFromContent) {
			final String charsetInContentStr = ReUtil.get(META_CHARSET_PATTERN, content, 1);
			if (StrUtil.isNotBlank(charsetInContentStr)) {
				Charset charsetInContent = null;
				try {
					charsetInContent = Charset.forName(charsetInContentStr);
				} catch (final Exception e) {
					if (StrUtil.containsIgnoreCase(charsetInContentStr, "utf-8") || StrUtil.containsIgnoreCase(charsetInContentStr, "utf8")) {
						charsetInContent = CharsetUtil.UTF_8;
					} else if (StrUtil.containsIgnoreCase(charsetInContentStr, "gbk")) {
						charsetInContent = CharsetUtil.GBK;
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
	public static String getMimeType(final String filePath, final String defaultValue) {
		return ObjUtil.defaultIfNull(getMimeType(filePath), defaultValue);
	}

	/**
	 * 根据文件扩展名获得MimeType
	 *
	 * @param filePath 文件路径或文件名
	 * @return MimeType
	 * @see FileUtil#getMimeType(String)
	 */
	public static String getMimeType(final String filePath) {
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
	public static String getContentTypeByRequestBody(final String body) {
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
	public static SimpleServer createServer(final int port) {
		return new SimpleServer(port);
	}

	/**
	 * 构建简单的账号秘密验证信息，构建后类似于：
	 * <pre>
	 *     Basic YWxhZGRpbjpvcGVuc2VzYW1l
	 * </pre>
	 *
	 * @param username 账号
	 * @param password 密码
	 * @param charset  编码（如果账号或密码中有非ASCII字符适用）
	 * @return 密码验证信息
	 * @since 5.4.6
	 */
	public static String buildBasicAuth(final String username, final String password, final Charset charset) {
		final String data = username.concat(":").concat(password);
		return "Basic " + Base64.encode(data, charset);
	}

	/**
	 * 关闭Cookie
	 *
	 * @see GlobalCookieManager#setCookieManager(CookieManager)
	 * @since 5.6.5
	 */
	public static void closeCookie() {
		GlobalCookieManager.setCookieManager(null);
	}
}
