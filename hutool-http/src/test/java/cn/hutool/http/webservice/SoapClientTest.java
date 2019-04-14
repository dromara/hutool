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
public class SoapClientTest {

	@Test
	@Ignore
	public void requestTest() {
		SoapClient client = SoapClient.create("http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx")
		.setMethod("web:getCountryCityByIp", "http://WebXml.com.cn/")
		.setParam("theIpAddress", "218.21.240.106");
		
		Console.log(client.getMsgStr(true));
		
		Console.log(client.send(true));
	}
	
	@Test
	public void requestTest2() {
		String send = SoapClient.create("http://222.85.138.39:6088/comm-watch-web-nmsf/SendComdService")
		.setMethod("unk:sayHi", "http://unknown.namespace/")
		.setParam("arg0", "aaa", false)
		.send();
		
		Console.log(XmlUtil.format(send));
	}
}
