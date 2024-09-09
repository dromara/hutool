/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.http.webservice;

import jakarta.xml.soap.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.xml.XmlUtil;
import org.dromara.hutool.http.client.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * SOAP响应对象
 *
 * @author Looly
 * @since 6.0.0
 */
public class SoapResponse {

	private final Response response;
	private final MessageFactory factory;

	private SOAPMessage message;
	private SOAPBody body;

	/**
	 * 构造
	 *
	 * @param response HTTP响应
	 * @param factory  {@link MessageFactory}
	 */
	public SoapResponse(final Response response, final MessageFactory factory) {
		this.response = response;
		this.factory = factory;
	}

	/**
	 * 获取原始HTTP响应
	 *
	 * @return HTTP响应
	 */
	public Response getHttpResponse() {
		return this.response;
	}

	/**
	 * 获取SOAP消息字符串，默认为XML格式
	 *
	 * @param pretty 是否格式化XML
	 * @return SOAP消息字符串
	 */
	public String getResponseStr(final boolean pretty) {
		final String messageStr = this.response.bodyStr();
		return pretty ? XmlUtil.format(messageStr) : messageStr;
	}

	/**
	 * 获取SOAP消息
	 *
	 * @return SOAP消息
	 */
	public SOAPMessage getMessage() {
		if (null == this.message) {
			final MimeHeaders headers = new MimeHeaders();
			for (final Map.Entry<String, List<String>> entry : response.headers().entrySet()) {
				if (StrUtil.isNotEmpty(entry.getKey())) {
					headers.setHeader(entry.getKey(), CollUtil.get(entry.getValue(), 0));
				}
			}
			try {
				this.message = this.factory.createMessage(headers, response.bodyStream());
			} catch (final IOException | SOAPException e) {
				throw new SoapRuntimeException(e);
			} finally {
				IoUtil.closeQuietly(response);
			}
		}

		return this.message;
	}

	/**
	 * 获取SOAP消息体
	 *
	 * @return SOAP消息体
	 */
	public SOAPBody getBody() {
		if (null == this.body) {
			try {
				this.body = getMessage().getSOAPBody();
			} catch (final SOAPException e) {
				throw new SoapRuntimeException(e);
			}
		}
		return this.body;
	}

	/**
	 * 获取SOAP消息体中的文本内容
	 *
	 * @return SOAP消息体中的文本内容
	 */
	public String getBodyText() {
		return getBody().getTextContent();
	}

	/**
	 * 是否有错误信息
	 *
	 * @return 是否有Fault信息
	 */
	public boolean hasFault() {
		return getBody().hasFault();
	}

	/**
	 * 获取SOAP消息体中的错误信息，{@code null}表示没有错误
	 *
	 * @return Fault信息
	 */
	public SOAPFault getFault() {
		return getBody().getFault();
	}
}
