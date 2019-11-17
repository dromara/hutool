package cn.hutool.http.webservice;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HtmlUtil;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;

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
		.setCharset(CharsetUtil.CHARSET_GBK)
		.setParam("theIpAddress", "218.21.240.106");
		
		Console.log(client.getMsgStr(true));
		
		Console.log(client.send(true));
	}
	
	@Test
	@Ignore
	public void requestForMessageTest() throws SOAPException {
		SoapClient client = SoapClient.create("http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx")
				.setMethod("web:getCountryCityByIp", "http://WebXml.com.cn/")
				.setParam("theIpAddress", "218.21.240.106");
		
		SOAPMessage message = client.sendForMessage();
		Console.log(message.getSOAPBody().getTextContent());
	}


	@Test
	public void test(){
		final SoapClient soapClient = SoapClient.create("http://117.132.161.47:6403/services/JybgService")
				.setMethod("ser:zzjRequest", "http://service.jfsoft.com/")
				.setParam("arg0", "<![CDATA[\n" +
						"<Request>\n" +
						"<CardType>3</CardType>\n" +
						"<CardNo>C00002347</CardNo>\n" +
						"<BeginDate>20191101</BeginDate>\n" +
						"<EndDate>20191115</EndDate>\n" +
						"<TerminalNo>JKGCOnline000001</TerminalNo>\n" +
						"<BusinessCode>GetLisReport</BusinessCode>\n" +
						"<OperName>自助机01</OperName>\n" +
						"<OperCode>zzj01</OperCode>\n" +
						"<OperTime>20191116153748</OperTime>\n" +
						"</Request>\n" +
						"]]>", false);

		final String send = soapClient.send();
		Console.log(HtmlUtil.unescape(send));
	}
}
