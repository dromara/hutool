package com.xiaoleilu.hutool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * Http请求工具类
 * 
 * @author xiaoleilu
 */
public class HttpUtil {

	/** 未知的标识 */
	public final static String UNKNOW = "unknown";

	public final static Pattern CHARSET_PATTERN = Pattern.compile("charset=(.*?)\"");

	private static Map<String, String> cookies = new HashMap<String, String>();

	/**
	 * 编码字符为 application/x-www-form-urlencoded
	 * 
	 * @param content 被编码内容
	 * @return 编码后的字符
	 */
	public static String encode(String content, String charset) {
		if (StrUtil.isBlank(content)) return content;

		String encodeContent = null;
		try {
			encodeContent = URLEncoder.encode(content, charset);
		} catch (UnsupportedEncodingException e) {
			throw new UtilException(StrUtil.format("Unsupported encoding: [{}]", charset), e);
		}
		return encodeContent;
	}

	/**
	 * 解码application/x-www-form-urlencoded字符
	 * 
	 * @param content 被编码内容
	 * @return 编码后的字符
	 */
	public static String decode(String content, String charset) {
		if (StrUtil.isBlank(content)) return content;
		String encodeContnt = null;
		try {
			encodeContnt = URLDecoder.decode(content, charset);
		} catch (UnsupportedEncodingException e) {
			throw new UtilException(StrUtil.format("Unsupported encoding: [{}]", charset), e);
		}
		return encodeContnt;
	}

	/**
	 * 获取客户端IP
	 * 
	 * @param request 请求对象
	 * @return IP地址
	 */
	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (isUnknow(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (isUnknow(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (isUnknow(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (isUnknow(ip)) {
			ip = request.getRemoteAddr();
		}
		// 多级反向代理检测
		if (ip != null && ip.indexOf(",") > 0) {
			ip = ip.trim().split(",")[0];
		}
		return ip;
	}

	/**
	 * 发送get请求
	 * 
	 * @param urlString 网址
	 * @param customCharset 自定义请求字符集，如果字符集获取不到，使用此字符集
	 * @param isPassCodeError 是否跳过非200异常
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 * @throws IOException
	 */
	public static String get(String urlString, String customCharset, boolean isPassCodeError) throws IOException {
		final URL url = new URL(urlString);
		final String host = url.getHost();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.83 Safari/537.1");
		final String cookie = cookies.get(host);
		if (cookie != null) conn.addRequestProperty("Cookie", cookie);

		conn.setRequestMethod("GET");
		conn.setDoInput(true);

		if (conn.getResponseCode() != 200) {
			if (!isPassCodeError) {
				throw new IOException("Status code not 200!");
			}
		}

		final String setCookie = conn.getHeaderField("Set-Cookie");
		if (StrUtil.isBlank(setCookie) == false) {
			Log.debug("Set cookie: [{}]", setCookie);
			cookies.put(host, setCookie);
		}

		/* 获取内容 */
		String charset = getCharsetFromConn(conn);
		boolean isGetCharsetFromContent = false;
		if (StrUtil.isBlank(charset)) {
			charset = customCharset;
			isGetCharsetFromContent = true;
		}
		String content = getString(conn.getInputStream(), charset, isGetCharsetFromContent);
		conn.disconnect();

		return content;
	}

	/**
	 * 发送post请求
	 * 
	 * @param urlString 网址
	 * @param paramMap post表单数据
	 * @param customCharset 自定义请求字符集，发送时使用此字符集，获取返回内容如果字符集获取不到，使用此字符集
	 * @param isPassCodeError 是否跳过非200异常
	 * @return 返回数据
	 * @throws IOException
	 */
	public static String post(String urlString, Map<String, Object> paramMap, String customCharset, boolean isPassCodeError) throws IOException {
		return post(urlString, toParams(paramMap), customCharset, isPassCodeError);
	}

	/**
	 * 发送post请求
	 * 
	 * @param urlString 网址
	 * @param params post表单数据
	 * @param customCharset 自定义请求字符集，发送时使用此字符集，获取返回内容如果字符集获取不到，使用此字符集
	 * @param isPassCodeError 是否跳过非200异常
	 * @return 返回数据
	 * @throws IOException
	 */
	public static String post(String urlString, String params, String customCharset, boolean isPassCodeError) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		IoUtil.write(conn.getOutputStream(), customCharset, true, params);

		if (conn.getResponseCode() != 200) {
			if (!isPassCodeError) {
				throw new IOException("Status code not 200!");
			}
		}

		/* 获取内容 */
		String charset = getCharsetFromConn(conn);
		String content = IoUtil.getString(conn.getInputStream(), StrUtil.isBlank(charset) ? customCharset : charset);
		conn.disconnect();

		return content;
	}

	/**
	 * 获得远程String
	 * 
	 * @param url 请求的url
	 * @param customCharset 自定义的字符集
	 * @return 文本
	 * @throws IOException
	 */
	public static String downloadString(String url, String customCharset) throws IOException {
		InputStream inputStream = new URL(url).openStream();
		return IoUtil.getString(inputStream, customCharset);
	}

	/**
	 * 将Map形式的Form表单数据转换为Url参数形式
	 * 
	 * @param paramMap 表单数据
	 * @return url参数
	 */
	public static String toParams(Map<String, Object> paramMap) {
		return CollectionUtil.join(paramMap.entrySet(), "&");
	}

	/**
	 * 将URL参数解析为Map（也可以解析Post中的键值对参数）
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
		if (pathEndPos > 0) {
			paramsStr = StrUtil.subSuf(paramsStr, pathEndPos + 1);
		}
		paramsStr = decode(paramsStr, charset);

		final Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		String name = null;
		int pos = 0; // 未处理字符开始位置
		int i; // 未处理字符结束位置
		char c; // 当前字符
		for (i = 0; i < paramsStr.length(); i++) {
			c = paramsStr.charAt(i);
			if (c == '=' && name == null) { // 键值对的分界点
				if (pos != i) {
					name = paramsStr.substring(pos, i);
				}
				pos = i + 1;
			} else if (c == '&' || c == ';') { // 参数对的分界点
				if (name == null && pos != i) {
					// 对于像&a&这类无参数值的字符串，我们将name为a的值设为""
					addParam(params, paramsStr.substring(pos, i), StrUtil.EMPTY);
				} else if (name != null) {
					addParam(params, name, paramsStr.substring(pos, i));
					name = null;
				}
				pos = i + 1;
			}
		}

		if (pos != i) {
			if (name == null) {
				addParam(params, paramsStr.substring(pos, i), StrUtil.EMPTY);
			} else {
				addParam(params, name, paramsStr.substring(pos, i));
			}
		} else if (name != null) {
			addParam(params, name, StrUtil.EMPTY);
		}

		return params;
	}

	// ----------------------------------------------------------------------------------------- Private method start
	/**
	 * 从Http连接的头信息中获得字符集
	 * 
	 * @param conn HTTP连接对象
	 * @return 字符集
	 */
	private static String getCharsetFromConn(HttpURLConnection conn) {
		String charset = conn.getContentEncoding();
		if (charset == null || "".equals(charset.trim())) {
			String contentType = conn.getContentType();
			charset = ReUtil.get("charset=(.*)", contentType, 1);
		}
		return charset;
	}

	/**
	 * 检测给定字符串是否为未知，多用于检测HTTP请求相关<br/>
	 * 
	 * @param checkString 被检测的字符串
	 * @return 是否未知
	 */
	private static boolean isUnknow(String checkString) {
		return StrUtil.isBlank(checkString) || UNKNOW.equalsIgnoreCase(checkString);
	}

	/**
	 * 从流中读取内容
	 * 
	 * @param in 输入流
	 * @param charset 字符集
	 * @return 内容
	 * @throws IOException
	 */
	private static String getString(InputStream in, String charset, boolean isGetCharsetFromContent) throws IOException {
		StringBuilder content = new StringBuilder(); // 存储返回的内容

		// 从返回的内容中读取所需内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
		String line = null;
		while ((line = reader.readLine()) != null) {
			content.append(line).append('\n');
			if (isGetCharsetFromContent) {
				String charsetInContent = ReUtil.get(CHARSET_PATTERN, line, 1);
				if (StrUtil.isBlank(charsetInContent) == false) {
					charset = charsetInContent;
					reader = new BufferedReader(new InputStreamReader(in, charset));
					isGetCharsetFromContent = true;
				}
			}
		}

		return content.toString();
	}

	/**
	 * 将键值对加入到值为List类型的Map中
	 * 
	 * @param params 参数
	 * @param name key
	 * @param value value
	 * @return 是否成功
	 */
	private static boolean addParam(Map<String, List<String>> params, String name, String value) {
		List<String> values = params.get(name);
		if (values == null) {
			values = new ArrayList<String>(1); // 一般是一个参数
			params.put(name, values);
		}
		values.add(value);
		return true;
	}

	// ----------------------------------------------------------------------------------------- Private method start end
}
