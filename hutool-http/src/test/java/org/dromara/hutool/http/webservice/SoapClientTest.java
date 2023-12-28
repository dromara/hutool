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

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;

/**
 * SOAP相关单元测试
 *
 * @author looly
 *
 */
public class SoapClientTest {

	@Test
	@Disabled
	public void requestTest() {
		final SoapClient client = SoapClient.of("http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx")
		.setMethod("getCountryCityByIp", "http://WebXml.com.cn/")
		.charset(CharsetUtil.GBK)
		.setParam("theIpAddress", "218.21.240.106");

		Console.log(client.getMsgStr(true));

		Console.log(client.send(true));
	}

	@Test
	@Disabled
	public void requestForMessageTest() throws SOAPException {
		final SoapClient client = SoapClient.of("http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx")
				.setMethod("web:getCountryCityByIp", "http://WebXml.com.cn/")
				.setParam("theIpAddress", "218.21.240.106");

		final SOAPMessage message = client.sendForMessage();
		Console.log(message.getSOAPBody().getTextContent());
	}
}
