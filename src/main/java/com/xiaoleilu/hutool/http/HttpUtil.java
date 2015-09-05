package com.xiaoleilu.hutool.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.xiaoleilu.hutool.CollectionUtil;
import com.xiaoleilu.hutool.FileUtil;
import com.xiaoleilu.hutool.IoUtil;
import com.xiaoleilu.hutool.ReUtil;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * Http请求工具类
 * 
 * @author xiaoleilu
 */
public class HttpUtil {

	public final static Pattern CHARSET_PATTERN = Pattern.compile("charset=(.*?)\"");

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
	public static String getClientIP(javax.servlet.http.HttpServletRequest request) {
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
		return getMultistageReverseProxyIp(ip);
	}
	
	/**
	 * 检测是否https
	 * @param url URL
	 * @return 是否https
	 */
	public static boolean isHttps(String url) {
		return url.toLowerCase().startsWith("https");
	}
	
	/**
	 * 发送get请求
	 * 
	 * @param urlString 网址
	 * @param customCharset 自定义请求字符集，如果字符集获取不到，使用此字符集
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 * @throws IOException
	 */
	public static String get(String urlString, String customCharset) throws IOException {
		return HttpRequest.get(urlString).charset(customCharset).execute().body();
	}

	/**
	 * 发送post请求
	 * 
	 * @param urlString 网址
	 * @param paramMap post表单数据
	 * @return 返回数据
	 * @throws IOException
	 */
	public static String post(String urlString, Map<String, Object> paramMap) throws IOException {
		return HttpRequest.post(urlString).form(paramMap).execute().body();
	}

	/**
	 * 发送post请求
	 * 
	 * @param urlString 网址
	 * @param params post表单数据
	 * @return 返回数据
	 * @throws IOException
	 */
	public static String post(String urlString, String params) throws IOException {
		return HttpRequest.post(urlString).body(params).execute().body();
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
		InputStream in = null;
		try {
			in = new URL(url).openStream();
			return IoUtil.getString(in, customCharset);
		} finally {
			FileUtil.close(in);
		}
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
	
	/**
	 * 将表单数据加到URL中（用于GET表单提交）
	 * @param url URL
	 * @param form 表单数据
	 * @return 合成后的URL
	 */
	public static String urlWithForm(String url, Map<String, Object> form) {
		final String queryString = toParams(form);
		return urlWithForm(url, queryString);
	}
	
	/**
	 * 将表单数据字符串加到URL中（用于GET表单提交）
	 * @param url URL
	 * @param queryString 表单数据字符串
	 * @return 拼接后的字符串
	 */
	public static String urlWithForm(String url, String queryString) {
		if(StrUtil.isNotBlank(queryString)){
			if(url.contains("?")) {
				//原URL已经带参数
				url += "&" + queryString;
			}
			url += url.endsWith("?") ? queryString : "?" + queryString;
		}
		
		return url;
	}

	/**
	 * 从Http连接的头信息中获得字符集
	 * 
	 * @param conn HTTP连接对象
	 * @return 字符集
	 */
	public static String getCharset(HttpURLConnection conn) {
		if(conn == null){
			return null;
		}

		String charset = conn.getContentEncoding();
		if (StrUtil.isBlank(charset)) {
			charset = ReUtil.get(CHARSET_PATTERN, conn.getContentType(), 1);
		}
		return charset;
	}
	
	/**
	 * 从多级反向代理中获得第一个非unknown IP地址
	 * @param ip 获得的IP地址
	 * @return 第一个非unknown IP地址
	 */
	public static String getMultistageReverseProxyIp(String ip){
		// 多级反向代理检测
		if (ip != null && ip.indexOf(",") > 0) {
			final String[] ips = ip.trim().split(",");
			for (String subIp : ips) {
				if(false == isUnknow(subIp)){
					ip = subIp;
					break; 
				}
			}
		}
		return ip;
	}
	
	/**
	 * 检测给定字符串是否为未知，多用于检测HTTP请求相关<br/>
	 * 
	 * @param checkString 被检测的字符串
	 * @return 是否未知
	 */
	public static boolean isUnknow(String checkString) {
		return StrUtil.isBlank(checkString) || Header.UNKNOW.toString().equalsIgnoreCase(checkString);
	}
	
	/**
	 * 从流中读取内容
	 * 
	 * @param in 输入流
	 * @param charset 字符集
	 * @return 内容
	 * @throws IOException
	 */
	public static String getString(InputStream in, String charset, boolean isGetCharsetFromContent) throws IOException {
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
					isGetCharsetFromContent = false;
				}
			}
		}
		
		return content.toString();
	}
	
	/**
	 * 根据文件扩展名获得MimeType
	 * @param filePath 文件路径或文件名
	 * @return MimeType
	 */
	public static String getMimeType(String filePath) {
		return URLConnection.getFileNameMap().getContentTypeFor(filePath);
	}
	// ----------------------------------------------------------------------------------------- Private method start

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
