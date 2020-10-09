package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.xpath.XPathConstants;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * {@link XmlUtil} 工具类
 *
 * @author Looly
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
	public void xpathTest2() {
		String result = ResourceUtil.readUtf8Str("test.xml");
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
				+ "<newNode><sub>subText</sub></newNode>"//
				+ "</returnsms>";
		Map<String, Object> map = XmlUtil.xmlToMap(xml);

		Assert.assertEquals(6, map.size());
		Assert.assertEquals("Success", map.get("returnstatus"));
		Assert.assertEquals("ok", map.get("message"));
		Assert.assertEquals("1490", map.get("remainpoint"));
		Assert.assertEquals("885", map.get("taskID"));
		Assert.assertEquals("1", map.get("successCounts"));
		Assert.assertEquals("subText", ((Map<?, ?>) map.get("newNode")).get("sub"));
	}

	@Test
	public void xmlToMapTest2() {
		String xml = "<root><name>张三</name><name>李四</name></root>";
		Map<String, Object> map = XmlUtil.xmlToMap(xml);

		Assert.assertEquals(1, map.size());
		Assert.assertEquals(CollUtil.newArrayList("张三", "李四"), map.get("name"));
	}

	@Test
	public void mapToXmlTest() {
		Map<String, Object> map = MapBuilder.create(new LinkedHashMap<String, Object>())//
				.put("name", "张三")//
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
	public void mapToXmlTest2() {
		// 测试List
		Map<String, Object> map = MapBuilder.create(new LinkedHashMap<String, Object>())
				.put("Town", CollUtil.newArrayList("town1", "town2"))
				.build();

		Document doc = XmlUtil.mapToXml(map, "City");
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
						"<City>" +
						"<Town>town1</Town>" +
						"<Town>town2</Town>" +
						"</City>",
				XmlUtil.toStr(doc));
	}

	@Test
	public void readTest() {
		Document doc = XmlUtil.readXML("test.xml");
		Assert.assertNotNull(doc);
	}

	@Test
	public void readBySaxTest(){
		final Set<String> eles = CollUtil.newHashSet(
				"returnsms", "returnstatus", "message", "remainpoint", "taskID", "successCounts");
		XmlUtil.readBySax(ResourceUtil.getStream("test.xml"), new DefaultHandler(){
			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes) {
				Assert.assertTrue(eles.contains(localName));
			}
		});
	}

	@Test
	public void mapToXmlTestWithOmitXmlDeclaration() {

		Map<String, Object> map = MapBuilder.create(new LinkedHashMap<String, Object>())
				.put("name", "ddatsh")
				.build();
		String xml = XmlUtil.mapToXmlStr(map, true);
		Assert.assertEquals("<xml><name>ddatsh</name></xml>", xml);
	}

	@Test
	public void getByPathTest() {
		String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
				"  <soap:Body>\n" +
				"    <ns2:testResponse xmlns:ns2=\"http://ws.xxx.com/\">\n" +
				"      <return>2020/04/15 21:01:21</return>\n" +
				"    </ns2:testResponse>\n" +
				"  </soap:Body>\n" +
				"</soap:Envelope>\n";

		Document document = XmlUtil.readXML(xmlStr);
		Object value = XmlUtil.getByXPath(
				"//soap:Envelope/soap:Body/ns2:testResponse/return",
				document, XPathConstants.STRING);//
		Assert.assertEquals("2020/04/15 21:01:21", value);
	}

	@Test
	public void xmlToBeanTest() {
		final TestBean testBean = new TestBean();
		testBean.setReqCode("1111");
		testBean.setAccountName("账户名称");
		testBean.setOperator("cz");
		testBean.setProjectCode("123");
		testBean.setBankCode("00001");

		final Document doc = XmlUtil.beanToXml(testBean);
		Assert.assertEquals(TestBean.class.getSimpleName(), doc.getDocumentElement().getTagName());

		final TestBean testBean2 = XmlUtil.xmlToBean(doc, TestBean.class);
		Assert.assertEquals(testBean.getReqCode(), testBean2.getReqCode());
		Assert.assertEquals(testBean.getAccountName(), testBean2.getAccountName());
		Assert.assertEquals(testBean.getOperator(), testBean2.getOperator());
		Assert.assertEquals(testBean.getProjectCode(), testBean2.getProjectCode());
		Assert.assertEquals(testBean.getBankCode(), testBean2.getBankCode());
	}

	@Test
	public void cleanCommentTest() {
		final String xmlContent = "<info><title>hutool</title><!-- 这是注释 --><lang>java</lang></info>";
		final String ret = XmlUtil.cleanComment(xmlContent);
		Assert.assertEquals("<info><title>hutool</title><lang>java</lang></info>", ret);
	}

	@Data
	public static class TestBean {
		private String ReqCode;
		private String AccountName;
		private String Operator;
		private String ProjectCode;
		private String BankCode;
	}
}
