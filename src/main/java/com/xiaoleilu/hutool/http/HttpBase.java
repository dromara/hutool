package com.xiaoleilu.hutool.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoleilu.hutool.CharsetUtil;
import com.xiaoleilu.hutool.CollectionUtil;
import com.xiaoleilu.hutool.Conver;
import com.xiaoleilu.hutool.IoUtil;
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
	
	/**编码*/
	protected String charset = "UTF-8";
	
	/**默认缓冲*/
	protected static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	/**http版本*/
	protected String httpVersion = HTTP_1_1;
	
	/**存储头信息*/
	protected Map<String, List<String>> headers = new HashMap<String, List<String>>();
	
	/**存储表单数据*/
	protected Map<String, Object> form = new HashMap<String, Object>();
	
	/**存储响应主体*/
	protected String body;
	
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
	
	// ---------------------------------------------------------------- headers

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
	 * 获取headers
	 * @return Map<String, List<String>>
	 */
	public Map<String, List<String>> headers() {
		return Collections.unmodifiableMap(headers);
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

	/**mediaType*/
	protected String mediaType;

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

	/**
	 * 设置表单数据
	 * @param name
	 * @param value
	 * @return T
	 */
	public T form(String name, Object value) {
		form.put(name, Conver.toStr(value, null));

		return (T) this;
	}

	/**
	 * 设置表单数据
	 * @param name 名
	 * @param value 值
	 * @param parameters 参数对，奇数为名，偶数为值
	 * @return T
	 */
	public T form(String name, Object value, Object... parameters) {
		form(name, value);

		for (int i = 0; i < parameters.length; i += 2) {
			name = parameters[i].toString();
			form(name, parameters[i + 1]);
		}
		return (T) this;
	}

	/**
	 * 设置map类型表单数据
	 * @param formMap
	 * @return T
	 */
	public T form(Map<String, Object> formMap) {
		for (Map.Entry<String, Object> entry : formMap.entrySet()) {
			form(entry.getKey(), entry.getValue());
		}
		return (T) this;
	}

	/**
	 * 获取表单数据
	 * @return Map<String, Object>
	 */
	public Map<String, Object> form() {
		return form;
	}

	// ---------------------------------------------------------------- form encoding

	/**formEncoding*/
	protected String formEncoding = "UTF-8";
	
	/**
	 * 设置表单编码
	 * @param encoding
	 * @return T
	 */
	public T formEncoding(String encoding) {
		this.formEncoding = encoding;
		return (T) this;
	}

	/**
	 * 获取响应主体
	 * @return String
	 */
	public String body() {
		return body;
	}

	/**
	 * 获取响应流字节码
	 * @return byte[]
	 */
	public byte[] bodyBytes() {
		if (body == null) {
			return null;
		}
		try {
			return body.getBytes("ISO8859-1");
		} catch (UnsupportedEncodingException ignore) {
			return null;
		}
	}

	/**
	 * 获取响应文本
	 * @return String
	 */
	public String bodyText() {
		if (body == null) {
			return null;
		}
		return Conver.convertCharset(body, CharsetUtil.ISO_8859_1, charset);
	}

	/**
	 * 设置内容主体
	 * @param body
	 * @return T
	 */
	public T body(String body) {
		this.body = body;
		this.form = null;
		contentLength(body.length());
		return (T) this;
	}

	/**
	 * 设置内容主体编码
	 * @param charset
	 * @return String
	 */
	public String bodyText(String charset) {
		try {
			return new String(body.getBytes(charset), CharsetUtil.ISO_8859_1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 设置内容mediaType
	 * @param body
	 * @param mediaType
	 * @return T
	 */
	public T bodyText(String body, String mediaType) {
		return bodyText(body, mediaType, "UTF-8");
	}
	
	/**
	 * 设置内容html
	 * @param body
	 * @return T
	 */
	public T bodyHtml(String body) {
		return bodyText(body, "text/html", "UTF-8");
	}
	
	/**
	 * 设置主体文本
	 * @param body
	 * @param mediaType
	 * @param charset
	 * @return T
	 */
	public T bodyText(String body, String mediaType, String charset) {
		try {
			body = new String(body.getBytes(charset), CharsetUtil.ISO_8859_1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		contentType(mediaType, charset);
		body(body);
		return (T) this;
	}

	/**
	 * 设置主体字节码
	 * @param content
	 * @param contentType
	 * @return T
	 */
	public T body(byte[] content, String contentType) {
		String body = null;
		try {
			body = new String(content, "ISO8859-1");
		} catch (UnsupportedEncodingException ignore) {
		}
		contentType(contentType);
		return body(body);
	}
	
	/**
	 * 读取主体
	 * @param input
	 * @return String
	 * @throws IOException
	 */
	public String readBody(InputStream input) throws IOException{
		return IoUtil.getString(input, charset);
	}
}
