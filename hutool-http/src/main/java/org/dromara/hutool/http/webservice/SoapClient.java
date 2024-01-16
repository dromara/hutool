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

package org.dromara.hutool.http.webservice;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.core.xml.XmlUtil;
import org.dromara.hutool.http.client.HeaderOperation;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import jakarta.xml.soap.*;
import org.dromara.hutool.http.meta.Method;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * SOAP客户端
 *
 * <p>
 * 此对象用于构建一个SOAP消息，并通过HTTP接口发出消息内容。
 * SOAP消息本质上是一个XML文本，可以通过调用{@link #getMsgStr(boolean)} 方法获取消息体
 * <p>
 * 使用方法：
 *
 * <pre>
 * SoapClient client = SoapClient.create(url)
 * .setMethod(methodName, namespaceURI)
 * .setCharset(CharsetUtil.CHARSET_GBK)
 * .setParam(param1, "XXX");
 *
 * String response = client.send(true);
 *
 * </pre>
 *
 * @author looly
 * @since 4.5.4
 */
public class SoapClient implements HeaderOperation<SoapClient> {

	/**
	 * XML消息体的Content-Type
	 * soap1.1 : text/xml
	 * soap1.2 : application/soap+xml
	 * soap1.1与soap1.2区别:  https://www.cnblogs.com/qlqwjy/p/7577147.html
	 */
	private static final String CONTENT_TYPE_SOAP11_TEXT_XML = "text/xml;charset=";
	private static final String CONTENT_TYPE_SOAP12_SOAP_XML = "application/soap+xml;charset=";

	/**
	 * 请求的URL地址
	 */
	private String url;
	private Charset charset = CharsetUtil.UTF_8;

	/**
	 * 消息工厂，用于创建消息
	 */
	private MessageFactory factory;
	/**
	 * SOAP消息
	 */
	private SOAPMessage message;
	/**
	 * 消息方法节点
	 */
	private SOAPBodyElement methodEle;
	/**
	 * 应用于方法上的命名空间URI
	 */
	private final String namespaceURI;
	/**
	 * Soap协议
	 * soap1.1 : text/xml
	 * soap1.2 : application/soap+xml
	 */
	private final SoapProtocol protocol;
	/**
	 * 存储头信息
	 */
	private final Map<String, List<String>> headers = new HashMap<>();

	/**
	 * 创建SOAP客户端，默认使用soap1.1版本协议
	 *
	 * @param url WS的URL地址
	 * @return this
	 */
	public static SoapClient of(final String url) {
		return new SoapClient(url);
	}

	/**
	 * 创建SOAP客户端
	 *
	 * @param url      WS的URL地址
	 * @param protocol 协议，见{@link SoapProtocol}
	 * @return this
	 */
	public static SoapClient of(final String url, final SoapProtocol protocol) {
		return new SoapClient(url, protocol);
	}

	/**
	 * 创建SOAP客户端
	 *
	 * @param url          WS的URL地址
	 * @param protocol     协议，见{@link SoapProtocol}
	 * @param namespaceURI 方法上的命名空间URI
	 * @return this
	 * @since 4.5.6
	 */
	public static SoapClient of(final String url, final SoapProtocol protocol, final String namespaceURI) {
		return new SoapClient(url, protocol, namespaceURI);
	}

	/**
	 * 构造，默认使用soap1.1版本协议
	 *
	 * @param url WS的URL地址
	 */
	public SoapClient(final String url) {
		this(url, SoapProtocol.SOAP_1_1);
	}

	/**
	 * 构造
	 *
	 * @param url      WS的URL地址
	 * @param protocol 协议版本，见{@link SoapProtocol}
	 */
	public SoapClient(final String url, final SoapProtocol protocol) {
		this(url, protocol, null);
	}

	/**
	 * 构造
	 *
	 * @param url          WS的URL地址
	 * @param protocol     协议版本，见{@link SoapProtocol}
	 * @param namespaceURI 方法上的命名空间URI
	 * @since 4.5.6
	 */
	public SoapClient(final String url, final SoapProtocol protocol, final String namespaceURI) {
		this.url = url;
		this.namespaceURI = namespaceURI;
		this.protocol = protocol;
		init(protocol);
	}

	/**
	 * 初始化
	 *
	 * @param protocol 协议版本枚举，见{@link SoapProtocol}
	 * @return this
	 */
	public SoapClient init(final SoapProtocol protocol) {
		// 创建消息工厂
		try {
			this.factory = MessageFactory.newInstance(protocol.getValue());
			// 根据消息工厂创建SoapMessage
			this.message = factory.createMessage();
		} catch (final SOAPException e) {
			throw new SoapRuntimeException(e);
		}

		return this;
	}

	/**
	 * 重置SOAP客户端，用于客户端复用
	 *
	 * <p>
	 * 重置后需调用serMethod方法重新指定请求方法，并调用setParam方法重新定义参数
	 *
	 * @return this
	 * @since 4.6.7
	 */
	public SoapClient reset() {
		try {
			this.message = factory.createMessage();
		} catch (final SOAPException e) {
			throw new SoapRuntimeException(e);
		}
		this.methodEle = null;

		return this;
	}

	/**
	 * 设置编码
	 *
	 * @param charset 编码
	 * @return this
	 */
	public SoapClient charset(final Charset charset) {
		if (null != charset) {
			this.charset = charset;
			try {
				this.message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, charset.name());
				this.message.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
			} catch (final SOAPException e) {
				// ignore
			}
		}

		return this;
	}

	/**
	 * 设置Webservice请求地址
	 *
	 * @param url Webservice请求地址
	 * @return this
	 */
	public SoapClient setUrl(final String url) {
		this.url = url;
		return this;
	}

	/**
	 * 设置一个header<br>
	 * 如果覆盖模式，则替换之前的值，否则加入到值列表中
	 *
	 * @param name       Header名
	 * @param value      Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T 本身
	 */
	@Override
	public SoapClient header(final String name, final String value, final boolean isOverride) {
		if (null != name && null != value) {
			final List<String> values = headers.get(name.trim());
			if (isOverride || CollUtil.isEmpty(values)) {
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
	 * 获取headers
	 *
	 * @return Headers Map
	 */
	@Override
	public Map<String, List<String>> headers() {
		return Collections.unmodifiableMap(headers);
	}

	/**
	 * 清除所有头信息，包括全局头信息
	 *
	 * @return this
	 * @since 5.7.13
	 */
	public SoapClient clearHeaders() {
		this.headers.clear();
		return this;
	}

	/**
	 * 增加SOAP头信息，方法返回{@link SOAPHeaderElement}可以设置具体属性和子节点
	 *
	 * @param name           头信息标签名
	 * @param actorURI       中间的消息接收者
	 * @param roleUri        Role的URI
	 * @param mustUnderstand 标题项对于要对其进行处理的接收者来说是强制的还是可选的
	 * @param relay          relay属性
	 * @return {@link SOAPHeaderElement}
	 * @since 5.4.4
	 */
	public SOAPHeaderElement addSOAPHeader(final QName name, final String actorURI, final String roleUri, final Boolean mustUnderstand, final Boolean relay) {
		final SOAPHeaderElement ele = addSOAPHeader(name);
		try {
			if (StrUtil.isNotBlank(roleUri)) {
				ele.setRole(roleUri);
			}
			if (null != relay) {
				ele.setRelay(relay);
			}
		} catch (final SOAPException e) {
			throw new SoapRuntimeException(e);
		}

		if (StrUtil.isNotBlank(actorURI)) {
			ele.setActor(actorURI);
		}
		if (null != mustUnderstand) {
			ele.setMustUnderstand(mustUnderstand);
		}

		return ele;
	}

	/**
	 * 增加SOAP头信息，方法返回{@link SOAPHeaderElement}可以设置具体属性和子节点
	 *
	 * @param localName 头节点名称
	 * @return {@link SOAPHeaderElement}
	 * @since 5.4.7
	 */
	public SOAPHeaderElement addSOAPHeader(final String localName) {
		return addSOAPHeader(new QName(localName));
	}

	/**
	 * 增加SOAP头信息，方法返回{@link SOAPHeaderElement}可以设置具体属性和子节点
	 *
	 * @param localName 头节点名称
	 * @param value     头节点的值
	 * @return {@link SOAPHeaderElement}
	 * @since 5.4.7
	 */
	public SOAPHeaderElement addSOAPHeader(final String localName, final String value) {
		final SOAPHeaderElement soapHeaderElement = addSOAPHeader(localName);
		soapHeaderElement.setTextContent(value);
		return soapHeaderElement;
	}

	/**
	 * 增加SOAP头信息，方法返回{@link SOAPHeaderElement}可以设置具体属性和子节点
	 *
	 * @param name 头节点名称
	 * @return {@link SOAPHeaderElement}
	 * @since 5.4.4
	 */
	public SOAPHeaderElement addSOAPHeader(final QName name) {
		final SOAPHeaderElement ele;
		try {
			ele = this.message.getSOAPHeader().addHeaderElement(name);
		} catch (final SOAPException e) {
			throw new SoapRuntimeException(e);
		}
		return ele;
	}

	/**
	 * 设置请求方法
	 *
	 * @param name            方法名及其命名空间
	 * @param params          参数
	 * @param useMethodPrefix 是否使用方法的命名空间前缀
	 * @return this
	 */
	public SoapClient setMethod(final Name name, final Map<String, Object> params, final boolean useMethodPrefix) {
		return setMethod(new QName(name.getURI(), name.getLocalName(), name.getPrefix()), params, useMethodPrefix);
	}

	/**
	 * 设置请求方法
	 *
	 * @param name            方法名及其命名空间
	 * @param params          参数
	 * @param useMethodPrefix 是否使用方法的命名空间前缀
	 * @return this
	 */
	public SoapClient setMethod(final QName name, final Map<String, Object> params, final boolean useMethodPrefix) {
		setMethod(name);
		final String prefix = useMethodPrefix ? name.getPrefix() : null;
		final SOAPBodyElement methodEle = this.methodEle;
		for (final Entry<String, Object> entry : MapUtil.wrap(params)) {
			setParam(methodEle, entry.getKey(), entry.getValue(), prefix);
		}

		return this;
	}

	/**
	 * 设置请求方法<br>
	 * 方法名自动识别前缀，前缀和方法名使用“:”分隔<br>
	 * 当识别到前缀后，自动添加xmlns属性，关联到默认的namespaceURI
	 *
	 * @param methodName 方法名
	 * @return this
	 */
	public SoapClient setMethod(final String methodName) {
		return setMethod(methodName, ObjUtil.defaultIfNull(this.namespaceURI, XMLConstants.NULL_NS_URI));
	}

	/**
	 * 设置请求方法<br>
	 * 方法名自动识别前缀，前缀和方法名使用“:”分隔<br>
	 * 当识别到前缀后，自动添加xmlns属性，关联到传入的namespaceURI
	 *
	 * @param methodName   方法名（可有前缀也可无）
	 * @param namespaceURI 命名空间URI
	 * @return this
	 */
	public SoapClient setMethod(final String methodName, final String namespaceURI) {
		final List<String> methodNameList = SplitUtil.split(methodName, StrUtil.COLON);
		final QName qName;
		if (2 == methodNameList.size()) {
			qName = new QName(namespaceURI, methodNameList.get(1), methodNameList.get(0));
		} else {
			qName = new QName(namespaceURI, methodName);
		}
		return setMethod(qName);
	}

	/**
	 * 设置请求方法
	 *
	 * @param name 方法名及其命名空间
	 * @return this
	 */
	public SoapClient setMethod(final QName name) {
		try {
			this.methodEle = this.message.getSOAPBody().addBodyElement(name);
		} catch (final SOAPException e) {
			throw new SoapRuntimeException(e);
		}

		return this;
	}

	/**
	 * 设置方法参数，使用方法的前缀
	 *
	 * @param name  参数名
	 * @param value 参数值，可以是字符串或Map或{@link SOAPElement}
	 * @return this
	 */
	public SoapClient setParam(final String name, final Object value) {
		return setParam(name, value, true);
	}

	/**
	 * 设置方法参数
	 *
	 * @param name            参数名
	 * @param value           参数值，可以是字符串或Map或{@link SOAPElement}
	 * @param useMethodPrefix 是否使用方法的命名空间前缀
	 * @return this
	 */
	public SoapClient setParam(final String name, final Object value, final boolean useMethodPrefix) {
		setParam(this.methodEle, name, value, useMethodPrefix ? this.methodEle.getPrefix() : null);
		return this;
	}

	/**
	 * 批量设置参数，使用方法的前缀
	 *
	 * @param params 参数列表
	 * @return this
	 * @since 4.5.6
	 */
	public SoapClient setParams(final Map<String, Object> params) {
		return setParams(params, true);
	}

	/**
	 * 批量设置参数
	 *
	 * @param params          参数列表
	 * @param useMethodPrefix 是否使用方法的命名空间前缀
	 * @return this
	 * @since 4.5.6
	 */
	public SoapClient setParams(final Map<String, Object> params, final boolean useMethodPrefix) {
		for (final Entry<String, Object> entry : MapUtil.wrap(params)) {
			setParam(entry.getKey(), entry.getValue(), useMethodPrefix);
		}
		return this;
	}

	/**
	 * 获取方法节点<br>
	 * 用于创建子节点等操作
	 *
	 * @return {@link SOAPBodyElement}
	 * @since 4.5.6
	 */
	public SOAPBodyElement getMethodEle() {
		return this.methodEle;
	}

	/**
	 * 获取SOAP消息对象 {@link SOAPMessage}
	 *
	 * @return {@link SOAPMessage}
	 * @since 4.5.6
	 */
	public SOAPMessage getMessage() {
		return this.message;
	}

	/**
	 * 获取SOAP请求消息
	 *
	 * @param pretty 是否格式化
	 * @return 消息字符串
	 */
	public String getMsgStr(final boolean pretty) {
		return SoapUtil.toString(this.message, pretty, this.charset);
	}

	/**
	 * 将SOAP消息的XML内容输出到流
	 *
	 * @param out 输出流
	 * @return this
	 * @since 4.5.6
	 */
	public SoapClient write(final OutputStream out) {
		try {
			this.message.writeTo(out);
		} catch (final SOAPException | IOException e) {
			throw new SoapRuntimeException(e);
		}
		return this;
	}

	/**
	 * 执行Webservice请求，即发送SOAP内容
	 *
	 * @return 返回结果
	 */
	public SOAPMessage sendForMessage() {
		final Response res = sendForResponse();
		final MimeHeaders headers = new MimeHeaders();
		for (final Entry<String, List<String>> entry : res.headers().entrySet()) {
			if (StrUtil.isNotEmpty(entry.getKey())) {
				headers.setHeader(entry.getKey(), CollUtil.get(entry.getValue(), 0));
			}
		}
		try {
			return this.factory.createMessage(headers, res.bodyStream());
		} catch (final IOException | SOAPException e) {
			throw new SoapRuntimeException(e);
		} finally {
			IoUtil.closeQuietly(res);
		}
	}

	/**
	 * 执行Webservice请求，即发送SOAP内容
	 *
	 * @return 返回结果
	 */
	public String send() {
		return send(false);
	}

	/**
	 * 执行Webservice请求，即发送SOAP内容
	 *
	 * @param pretty 是否格式化
	 * @return 返回结果
	 */
	@SuppressWarnings("resource")
	public String send(final boolean pretty) {
		final String body = sendForResponse().bodyStr();
		return pretty ? XmlUtil.format(body) : body;
	}

	// -------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 发送请求，获取异步响应
	 *
	 * @return 响应对象
	 */
	public Response sendForResponse() {
		final Request request = Request.of(this.url)
			.method(Method.POST)
			.setMaxRedirectCount(2)
			.contentType(getXmlContentType())
			.header(this.headers, false)
			.body(getMsgStr(false));
		return request.send();
	}

	/**
	 * 获取请求的Content-Type，附加编码信息
	 *
	 * @return 请求的Content-Type
	 */
	private String getXmlContentType() {
		switch (this.protocol) {
			case SOAP_1_1:
				return CONTENT_TYPE_SOAP11_TEXT_XML.concat(this.charset.toString());
			case SOAP_1_2:
				return CONTENT_TYPE_SOAP12_SOAP_XML.concat(this.charset.toString());
			default:
				throw new SoapRuntimeException("Unsupported protocol: {}", this.protocol);
		}
	}

	/**
	 * 设置方法参数
	 *
	 * @param ele    方法节点
	 * @param name   参数名
	 * @param value  参数值
	 * @param prefix 命名空间前缀， {@code null}表示不使用前缀
	 * @return {@link SOAPElement}子节点
	 */
	@SuppressWarnings("rawtypes")
	private static SOAPElement setParam(final SOAPElement ele, final String name, final Object value, final String prefix) {
		final SOAPElement childEle;
		try {
			if (StrUtil.isNotBlank(prefix)) {
				childEle = ele.addChildElement(name, prefix);
			} else {
				childEle = ele.addChildElement(name);
			}
		} catch (final SOAPException e) {
			throw new SoapRuntimeException(e);
		}

		if (null != value) {
			if (value instanceof SOAPElement) {
				// 单个子节点
				try {
					ele.addChildElement((SOAPElement) value);
				} catch (final SOAPException e) {
					throw new SoapRuntimeException(e);
				}
			} else if (value instanceof Map) {
				// 多个字节点
				Entry entry;
				for (final Object obj : ((Map) value).entrySet()) {
					entry = (Entry) obj;
					setParam(childEle, entry.getKey().toString(), entry.getValue(), prefix);
				}
			} else {
				// 单个值
				childEle.setValue(value.toString());
			}
		}

		return childEle;
	}
	// -------------------------------------------------------------------------------------------------------- Private method end
}
