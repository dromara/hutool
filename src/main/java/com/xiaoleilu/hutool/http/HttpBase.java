package com.xiaoleilu.hutool.http;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.CollectionUtil;
import com.xiaoleilu.hutool.StrUtil;

/**
 * http基类
 * @author Looly
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class HttpBase<T> {

	/**HTTP/1.0*/
	public static final String HTTP_1_0 = "HTTP/1.0";
	/**HTTP/1.1*/
	public static final String HTTP_1_1 = "HTTP/1.1";
	
	/**存储头信息*/
	protected Map<String, List<String>> headers = new HashMap<String, List<String>>();
	/**编码*/
	protected String charset;
	/**http版本*/
	protected String httpVersion = HTTP_1_1;
	/**mediaType*/
	protected String mediaType;
	/**存储主体*/
	protected String body;
	
	// ---------------------------------------------------------------- Headers start
	/**
	 * 根据name获取头信息
	 * @param name Header名
	 * @return Header值
	 */
	public String header(String name) {
		if(StrUtil.isBlank(name)) {
			return null;
		}
		
		List<String> values = headers.get(name.trim());
		if(CollectionUtil.isEmpty(values)) {
			return null;
		}
		return values.get(0);
	}
	
	/**
	 * 根据name获取头信息
	 * @param name Header名
	 * @return Header值
	 */
	public String header(Header name) {
		return header(name.toString());
	}
	
	/**
	 * 移除一个头信息
	 * @param name Header名
	 */
	public void removeHeader(String name) {
		if(name != null) {
			headers.remove(name.trim());
		}
	}
	
	/**
	 * 移除一个头信息
	 * @param name Header名
	 */
	public void removeHeader(Header name) {
		removeHeader(name.toString());
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 * @param name Header名
	 * @param value Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T 本身
	 */
	public T header(String name, String value, boolean isOverride) {
		if(null != name && null != value){
			final List<String> values = headers.get(name.trim());
			if(CollectionUtil.isEmpty(values)) {
				headers.put(name.trim(), Arrays.asList(value.trim()));
			}else {
				values.add(value.trim());
			}
		}
		return (T) this;
	}
	
	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 * @param name Header名
	 * @param value Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T 本身
	 */
	public T header(Header name, String value, boolean isOverride) {
		return header(name.toString(), value, isOverride);
	}
	
	/**
	 * 设置一个header<br>
	 * 覆盖模式，则替换之前的值
	 * @param name Header名
	 * @param value Header值
	 * @return T 本身
	 */
	public T header(Header name, String value) {
		return header(name.toString(), value, true);
	}
	
	/**
	 * 设置一个header<br>
	 * 覆盖模式，则替换之前的值
	 * @param name Header名
	 * @param value Header值
	 * @return T 本身
	 */
	public T header(String name, String value) {
		return header(name, value, true);
	}
	
	/**
	 * 设置请求头<br>
	 * 不覆盖原有请求头
	 * 
	 * @param headers 请求头
	 */
	public void header(Map<String, List<String>> headers) {
		if(CollectionUtil.isEmpty(headers)) {
			return;
		}
		
		String name;
		for (Entry<String, List<String>> entry : headers.entrySet()) {
			name = entry.getKey();
			for (String value : entry.getValue()) {
				this.header(name, StrUtil.nullToEmpty(value), false);
			}
		}
	}

	/**
	 * 获取headers
	 * @return Map<String, List<String>>
	 */
	public Map<String, List<String>> headers() {
		return Collections.unmodifiableMap(headers);
	}
	// ---------------------------------------------------------------- Headers end
	
	/**
	 * 返回http版本
	 * @return String
	 */
	public String httpVersion() {
		return httpVersion;
	}
	/**
	 * 设置http版本
	 * @param httpVersion
	 * @return T
	 */
	public T httpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
		return (T) this;
	}

	/**
	 * 返回字符集
	 * @return 字符集
	 */
	public String charset() {
		return charset;
	}
	/**
	 * 设置字符集
	 * @param charset 字符集
	 * @return T 自己
	 */
	public T charset(String charset) {
		this.charset = null;
		contentType(null, charset);
		return (T) this;
	}

	/**
	 * 获取mediaType
	 * @return String
	 */
	public String mediaType() {
		return mediaType;
	}
	/**
	 * 设置mediaType
	 * @param mediaType
	 * @return T
	 */
	public T mediaType(String mediaType) {
		contentType(mediaType, null);
		return (T) this;
	}

	/**
	 * 获取contentType
	 * @return contentType
	 */
	public String contentType() {
		return header(Header.CONTENT_TYPE);
	}
	/**
	 * 设置contentType
	 * @param contentType contentType
	 * @return T 自己
	 */
	public T contentType(String contentType) {
		header(Header.CONTENT_TYPE, contentType);
		return (T) this;
	}
	/**
	 * 设置mediaType包含字符集
	 * @param mediaType mediaType
	 * @param charset 字符集
	 * @return T 自己
	 */
	public T contentType(String mediaType, String charset) {
		if(StrUtil.isNotBlank(mediaType)) {
			this.mediaType = mediaType;
		}
		if(StrUtil.isNotBlank(charset)) {
			this.charset = charset;
		}

		String contentType = this.mediaType;
		if (this.charset != null) {
			contentType += ";charset=" + charset;
		}

		header(Header.CONTENT_TYPE, contentType);
		return (T) this;
	}
	
	/**
	 * 设置是否为长连接
	 * @param isKeepAlive 是否长连接
	 * @return T 自己
	 */
	public T keepAlive(boolean isKeepAlive) {
		Header value = isKeepAlive ? Header.KEEP_ALIVE : Header.CLOSE;
		header(Header.CONNECTION, value.toString());
		return (T) this;
	}
	/**
	 * 获取是否为活动连接
	 * @return boolean
	 */
	public boolean isKeepAlive() {
		String connection = header(Header.CONNECTION);
		if (connection == null) {
			return !httpVersion.equalsIgnoreCase(HTTP_1_0);
		}

		return !connection.equalsIgnoreCase(Header.CLOSE.toString());
	}

	/**
	 * 获取内容长度
	 * @return String
	 */
	public String contentLength() {
		return header(Header.CONTENT_LENGTH);
	}
	/**
	 * 设置内容长度
	 * @param value
	 * @return T
	 */
	public T contentLength(int value) {
		header(Header.CONTENT_LENGTH, String.valueOf(value));
		return (T) this;
	}

	/**
	 * 获取内容编码
	 * @return String
	 */
	public String contentEncoding() {
		return header(Header.CONTENT_ENCODING);
	}
	/**
	 * 获取请求头
	 * @return String
	 */
	public String accept() {
		return header(Header.ACCEPT);
	}
	
	/**
	 * 设置请求头
	 * @param accept
	 * @return T
	 */
	public T accept(String accept) {
		header(Header.ACCEPT, accept);
		return (T) this;
	}
	
	/**
	 * 获取请求编码
	 * @return String
	 */
	public String acceptEncoding() {
		return header(Header.ACCEPT_ENCODING);
	}
	/**
	 * 设置请求编码
	 * @param encodings
	 * @return T
	 */
	public T acceptEncoding(String encodings) {
		header(Header.ACCEPT_ENCODING, encodings);
		return (T) this;
	}
}
