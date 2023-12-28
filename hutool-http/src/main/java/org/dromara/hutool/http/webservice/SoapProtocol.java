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
