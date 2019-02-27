package cn.hutool.http.webservice;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.XmlUtil;

/**
 * SOAP相关单元测试
 * 
 * @author looly
 *
 */
public class SoapRequestTest {

	@Test
	@Ignore
	public void requestTest() {
		SoapRequest request = new SoapRequest(//
				"http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx", //
				"http://WebXml.com.cn/"//
		);
		request.setXmlns("soapenv");
		request.setMethod("getCountryCityByIp");
		request.addParam("theIpAddress", "218.21.240.106");
		
		String body = request.executeBody();
		Console.log(body);
		Console.log(XmlUtil.format(body));
	}
}
