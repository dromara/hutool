package cn.hutool.http.webservice;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;

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
		request.setMethod("getCountryCityByIp");
		request.addParam("theIpAddress", "218.21.240.106");

		Console.log(request.execute());
	}
}
