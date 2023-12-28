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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.xml.XmlUtil;

/**
 * SOAP相关工具类
 *
 * @author looly
 * @since 4.5.7
 */
public class SoapUtil {

	/**
	 * 创建SOAP客户端，默认使用soap1.1版本协议
	 *
	 * @param url WS的URL地址
	 * @return {@link SoapClient}
	 */
	public static SoapClient createClient(final String url) {
		return SoapClient.of(url);
	}

	/**
	 * 创建SOAP客户端
	 *
	 * @param url WS的URL地址
	 * @param protocol 协议，见{@link SoapProtocol}
	 * @return {@link SoapClient}
	 */
	public static SoapClient createClient(final String url, final SoapProtocol protocol) {
		return SoapClient.of(url, protocol);
	}

	/**
	 * 创建SOAP客户端
	 *
	 * @param url WS的URL地址
	 * @param protocol 协议，见{@link SoapProtocol}
	 * @param namespaceURI 方法上的命名空间URI
	 * @return {@link SoapClient}
	 * @since 4.5.6
	 */
	public static SoapClient createClient(final String url, final SoapProtocol protocol, final String namespaceURI) {
		return SoapClient.of(url, protocol, namespaceURI);
	}

	/**
	 * {@link SOAPMessage} 转为字符串
	 *
	 * @param message SOAP消息对象
	 * @param pretty 是否格式化
	 * @return SOAP XML字符串
	 */
	public static String toString(final SOAPMessage message, final boolean pretty) {
		return toString(message, pretty, CharsetUtil.UTF_8);
	}

	/**
	 * {@link SOAPMessage} 转为字符串
	 *
	 * @param message SOAP消息对象
	 * @param pretty 是否格式化
	 * @param charset 编码
	 * @return SOAP XML字符串
	 * @since 4.5.7
	 */
	public static String toString(final SOAPMessage message, final boolean pretty, final Charset charset) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			message.writeTo(out);
		} catch (final SOAPException | IOException e) {
			throw new SoapRuntimeException(e);
		}
		final String messageToString;
		try {
			messageToString = out.toString(charset.toString());
		} catch (final UnsupportedEncodingException e) {
			throw new HutoolException(e);
		}
		return pretty ? XmlUtil.format(messageToString) : messageToString;
	}
}
