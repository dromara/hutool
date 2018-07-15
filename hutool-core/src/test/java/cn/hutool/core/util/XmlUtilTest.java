package cn.hutool.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.xpath.XPathConstants;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;

/**
 * {@link XmlUtil} 工具类
 * 
 * @author Looly
 *
 */
public class XmlUtilTest {

	@Test
	public void parseTest() {
		String result = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
				+ "<returnsms>"//
				+ "<returnstatus>Success</returnstatus>"//
				+ "<message>ok</message>"//
				+ "<remainpoint>1490</remainpoint>"//
				+ "<taskID>885</taskID>"//
				+ "<successCounts>1</successCounts>"//
				+ "</returnsms>";
		Document docResult = XmlUtil.parseXml(result);
		String elementText = XmlUtil.elementText(docResult.getDocumentElement(), "returnstatus");
		Assert.assertEquals("Success", elementText);
	}

	@Test
	@Ignore
	public void writeTest() {
		String result = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
				+ "<returnsms>"//
				+ "<returnstatus>Success（成功）</returnstatus>"//
				+ "<message>ok</message>"//
				+ "<remainpoint>1490</remainpoint>"//
				+ "<taskID>885</taskID>"//
				+ "<successCounts>1</successCounts>"//
				+ "</returnsms>";
		Document docResult = XmlUtil.parseXml(result);
		XmlUtil.toFile(docResult, "e:/aaa.xml", "utf-8");
	}

	@Test
	public void xpathTest() {
		String result = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
				+ "<returnsms>"//
				+ "<returnstatus>Success（成功）</returnstatus>"//
				+ "<message>ok</message>"//
				+ "<remainpoint>1490</remainpoint>"//
				+ "<taskID>885</taskID>"//
				+ "<successCounts>1</successCounts>"//
				+ "</returnsms>";
		Document docResult = XmlUtil.parseXml(result);
		Object value = XmlUtil.getByXPath("//returnsms/message", docResult, XPathConstants.STRING);
		Assert.assertEquals("ok", value);
	}
	
	@Test
	public void xmlToMapTest() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
				+ "<returnsms>"//
				+ "<returnstatus>Success</returnstatus>"//
				+ "<message>ok</message>"//
				+ "<remainpoint>1490</remainpoint>"//
				+ "<taskID>885</taskID>"//
				+ "<successCounts>1</successCounts>"//
				+ "</returnsms>";
		Map<String, Object> map = XmlUtil.xmlToMap(xml);
		
		Assert.assertEquals(5, map.size());
		Assert.assertEquals("Success", map.get("returnstatus"));
		Assert.assertEquals("ok", map.get("message"));
		Assert.assertEquals("1490", map.get("remainpoint"));
		Assert.assertEquals("885", map.get("taskID"));
		Assert.assertEquals("1", map.get("successCounts"));
	}

	@Test
	public void mapToXmlTest() {
		Map<String, Object> map = MapBuilder.create(new LinkedHashMap<String, Object>()).put("name", "张三")//
				.put("age", 12)//
				.put("game", MapUtil.builder(new LinkedHashMap<String, Object>()).put("昵称", "Looly").put("level", 14).build())//
				.build();
		Document doc = XmlUtil.mapToXml(map, "user");
		// Console.log(XmlUtil.toStr(doc, false));
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"//
				+ "<user>"//
				+ "<name>张三</name>"//
				+ "<age>12</age>"//
				+ "<game>"//
				+ "<昵称>Looly</昵称>"//
				+ "<level>14</level>"//
				+ "</game>"//
				+ "</user>", //
				XmlUtil.toStr(doc, false));
	}
	
	@Test
	public void readTest() {
		Document doc = XmlUtil.readXML("test.xml");
		Assert.assertNotNull(doc);
	}
}
