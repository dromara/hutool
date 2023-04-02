package org.dromara.hutool.webservice;

import org.dromara.hutool.lang.Console;
import org.dromara.hutool.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

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
		.setMethod("web:getCountryCityByIp", "http://WebXml.com.cn/")
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
