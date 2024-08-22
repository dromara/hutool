/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

import jakarta.xml.soap.SOAPConstants;

/**
 * SOAP协议版本枚举
 *
 * @author looly
 *
 */
public enum SoapProtocol {
	/** SOAP 1.1协议 */
	SOAP_1_1(SOAPConstants.SOAP_1_1_PROTOCOL),
	/** SOAP 1.2协议 */
	SOAP_1_2(SOAPConstants.SOAP_1_2_PROTOCOL);

	/**
	 * 构造
	 *
	 * @param value {@link SOAPConstants} 中的协议版本值
	 */
	SoapProtocol(final String value) {
		this.value = value;
	}

	private final String value;

	/**
	 * 获取版本值信息
	 *
	 * @return 版本值信息
	 */
	public String getValue() {
		return this.value;
	}
}
