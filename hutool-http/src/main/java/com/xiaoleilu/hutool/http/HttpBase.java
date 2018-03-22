package com.xiaoleilu.hutool.http;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.map.CaseInsensitiveMap;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * http基类
 * @author Looly
 * @param <T> 子类类型，方便链式编程
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
	protected Charset charset = CharsetUtil.CHARSET_UTF_8;
	/**http版本*/
	protected String httpVersion = HTTP_1_1;
	/**存储主体*/
	protected String body;
	
	// ---------------------------------------------------------------- Headers start
	/**
	 * 根据name获取头信息<br>
	 * 根据RFC2616规范，header的name不区分大小写
	 * 
	 * @param name Header名
	 * @return Header值
	 */
	public String header(String name) {
		final List<String> values = headerList(name);
		if(CollectionUtil.isEmpty(values)) {
			return null;
		}
		return values.get(0);
	}
	
	/**
	 * 根据name获取头信息列表
	 * @param name Header名
	 * @return Header值
	 * @since 3.1.1
	 */
	public List<String> headerList(String name) {
		if(StrUtil.isBlank(name)) {
			return null;
		}
		
		final CaseInsensitiveMap<String,List<String>> headersIgnoreCase = new CaseInsensitiveMap<>(this.headers);
		return headersIgnoreCase.get(name.trim());
	}
	
	/**
	 * 根据name获取头信息
	 * @param name Header名
	 * @return Header值
	 */
	public String header(Header name) {
		if(null == name) {
			return null;
		}
		return header(name.toString());
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
			if(isOverride || CollectionUtil.isEmpty(values)) {
				final ArrayList<String> valueList = new ArrayList<String>();
				valueList.add(value);
				headers.put(name.trim(), valueList);
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
	 * @return this
	 */
	public T header(Map<String, List<String>> headers) {
		if(CollectionUtil.isEmpty(headers)) {
			return (T)this;
		}
		
		String name;
		for (Entry<String, List<String>> entry : headers.entrySet()) {
			name = entry.getKey();
			for (String value : entry.getValue()) {
				this.header(name, StrUtil.nullToEmpty(value), false);
			}
		}
		return (T)this;
	}
	
	/**
	 * 移除一个头信息
	 * @param name Header名
	 * @return this
	 */
	public T removeHeader(String name) {
		if(name != null) {
			headers.remove(name.trim());
		}
		return (T)this;
	}
	
	/**
	 * 移除一个头信息
	 * @param name Header名
	 * @return this
	 */
	public T removeHeader(Header name) {
		return removeHeader(name.toString());
	}

	/**
	 * 获取headers
	 * @return Headers Map
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
	 * 
	 * @param httpVersion Http版本，{@link HttpBase#HTTP_1_0}，{@link HttpBase#HTTP_1_1}
	 * @return this
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
		return charset.name();
	}
	
	/**
	 * 设置字符集
	 * @param charset 字符集
	 * @return T 自己
	 * @see CharsetUtil
	 */
	public T charset(String charset) {
		if(StrUtil.isNotBlank(charset)){
			this.charset = Charset.forName(charset);
		}
		return (T) this;
	}
	
	/**
	 * 设置字符集
	 * @param charset 字符集
	 * @return T 自己
	 * @see CharsetUtil
	 */
	public T charset(Charset charset) {
		if(null != charset){
			this.charset = charset;
		}
		return (T) this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = StrUtil.builder();
		sb.append("Request Headers: ").append(StrUtil.CRLF);
		for (Entry<String, List<String>> entry : this.headers.entrySet()) {
			sb.append("    ").append(entry).append(StrUtil.CRLF);
		}
		
		sb.append("Request Body: ").append(StrUtil.CRLF);
		sb.append("    ").append(this.body).append(StrUtil.CRLF);
		
		return sb.toString();
	}
}
