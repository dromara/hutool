package cn.hutool.http;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 全局头部信息<br>
 * 所有Http请求将共用此全局头部信息，除非在{@link HttpRequest}中自定义头部信息覆盖之
 *
 * @author looly
 */
public enum GlobalHeaders {
	INSTANCE;

	/**
	 * 存储头信息
	 */
	Map<String, List<String>> headers = new HashMap<>();

	/**
	 * 构造
	 */
	GlobalHeaders() {
		putDefault(false);
	}

	/**
	 * 加入默认的头部信息
	 *
	 * @param isReset 是否重置所有头部信息（删除自定义保留默认）
	 * @return this
	 */
	public GlobalHeaders putDefault(boolean isReset) {
		// 解决HttpURLConnection中无法自定义Host等头信息的问题
		// https://stackoverflow.com/questions/9096987/how-to-overwrite-http-header-host-in-a-httpurlconnection/9098440
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

		if (isReset) {
			this.headers.clear();
		}

		header(Header.ACCEPT, "text/html,application/json,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", true);
		header(Header.ACCEPT_ENCODING, "gzip, deflate", true);
		header(Header.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8", true);
		// 此Header只有在post请求中有用，因此在HttpRequest的method方法中设置此头信息，此处去掉
		// header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.toString(CharsetUtil.CHARSET_UTF_8), true);
		header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36 Hutool", true);
		return this;
	}

	// ---------------------------------------------------------------- Headers start

	/**
	 * 根据name获取头信息
	 *
	 * @param name Header名
	 * @return Header值
	 */
	public String header(String name) {
		final List<String> values = headerList(name);
		if (CollectionUtil.isEmpty(values)) {
			return null;
		}
		return values.get(0);
	}

	/**
	 * 根据name获取头信息列表
	 *
	 * @param name Header名
	 * @return Header值
	 * @since 3.1.1
	 */
	public List<String> headerList(String name) {
		if (StrUtil.isBlank(name)) {
			return null;
		}

		return headers.get(name.trim());
	}

	/**
	 * 根据name获取头信息
	 *
	 * @param name Header名
	 * @return Header值
	 */
	public String header(Header name) {
		if (null == name) {
			return null;
		}
		return header(name.toString());
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 *
	 * @param name       Header名
	 * @param value      Header值
	 * @param isOverride 是否覆盖已有值
	 * @return this
	 */
	public GlobalHeaders header(String name, String value, boolean isOverride) {
		if (null != name && null != value) {
			final List<String> values = headers.get(name.trim());
			if (isOverride || CollectionUtil.isEmpty(values)) {
				final ArrayList<String> valueList = new ArrayList<>();
				valueList.add(value);
				headers.put(name.trim(), valueList);
			} else {
				values.add(value.trim());
			}
		}
		return this;
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 *
	 * @param name       Header名
	 * @param value      Header值
	 * @param isOverride 是否覆盖已有值
	 * @return this
	 */
	public GlobalHeaders header(Header name, String value, boolean isOverride) {
		return header(name.toString(), value, isOverride);
	}

	/**
	 * 设置一个header<br>
	 * 覆盖模式，则替换之前的值
	 *
	 * @param name  Header名
	 * @param value Header值
	 * @return this
	 */
	public GlobalHeaders header(Header name, String value) {
		return header(name.toString(), value, true);
	}

	/**
	 * 设置一个header<br>
	 * 覆盖模式，则替换之前的值
	 *
	 * @param name  Header名
	 * @param value Header值
	 * @return this
	 */
	public GlobalHeaders header(String name, String value) {
		return header(name, value, true);
	}

	/**
	 * 设置请求头<br>
	 * 不覆盖原有请求头
	 *
	 * @param headers 请求头
	 * @return this
	 */
	public GlobalHeaders header(Map<String, List<String>> headers) {
		if (MapUtil.isEmpty(headers)) {
			return this;
		}

		String name;
		for (Entry<String, List<String>> entry : headers.entrySet()) {
			name = entry.getKey();
			for (String value : entry.getValue()) {
				this.header(name, StrUtil.nullToEmpty(value), false);
			}
		}
		return this;
	}

	/**
	 * 移除一个头信息
	 *
	 * @param name Header名
	 * @return this
	 */
	public GlobalHeaders removeHeader(String name) {
		if (name != null) {
			headers.remove(name.trim());
		}
		return this;
	}

	/**
	 * 移除一个头信息
	 *
	 * @param name Header名
	 * @return this
	 */
	public GlobalHeaders removeHeader(Header name) {
		return removeHeader(name.toString());
	}

	/**
	 * 获取headers
	 *
	 * @return Headers Map
	 */
	public Map<String, List<String>> headers() {
		return Collections.unmodifiableMap(headers);
	}

	/**
	 * 清除所有头信息，包括全局头信息
	 *
	 * @return this
	 * @since 5.7.13
	 */
	public GlobalHeaders clearHeaders() {
		this.headers.clear();
		return this;
	}
	// ---------------------------------------------------------------- Headers end

}
