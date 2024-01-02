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

package org.dromara.hutool.core.xml;

import lombok.Data;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.MapBuilder;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.xpath.XPathConstants;
import java.util.LinkedHashMap;
import java.util.List;
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
		final String result = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
			+ "<returnsms>"//
			+ "<returnstatus>Success</returnstatus>"//
			+ "<message>ok</message>"//
			+ "<remainpoint>1490</remainpoint>"//
			+ "<taskID>885</taskID>"//
			+ "<successCounts>1</successCounts>"//
			+ "</returnsms>";
		final Document docResult = XmlUtil.parseXml(result);
		final String elementText = XmlUtil.elementText(docResult.getDocumentElement(), "returnstatus");
		Assertions.assertEquals("Success", elementText);
	}

	@Test
	@Disabled
	public void writeTest() {
		final String result = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
			+ "<returnsms>"//
			+ "<returnstatus>Success（成功）</returnstatus>"//
			+ "<message>ok</message>"//
			+ "<remainpoint>1490</remainpoint>"//
			+ "<taskID>885</taskID>"//
			+ "<successCounts>1</successCounts>"//
			+ "</returnsms>";
		final Document docResult = XmlUtil.parseXml(result);
		XmlUtil.write(docResult, FileUtil.file("d:/test/aaa.xml"), CharsetUtil.UTF_8);
	}

	@Test
	public void xpathTest() {
		final String result = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
			+ "<returnsms>"//
			+ "<returnstatus>Success（成功）</returnstatus>"//
			+ "<message>ok</message>"//
			+ "<remainpoint>1490</remainpoint>"//
			+ "<taskID>885</taskID>"//
			+ "<successCounts>1</successCounts>"//
			+ "</returnsms>";
		final Document docResult = XmlUtil.parseXml(result);
		final Object value = XPathUtil.getByXPath("//returnsms/message", docResult, XPathConstants.STRING);
		Assertions.assertEquals("ok", value);
	}

	@Test
	public void xpathTest2() {
		final String result = ResourceUtil.readUtf8Str("test.xml");
		final Document docResult = XmlUtil.parseXml(result);
		final Object value = XPathUtil.getByXPath("//returnsms/message", docResult, XPathConstants.STRING);
		Assertions.assertEquals("ok", value);
	}

	@Test
	public void xmlToMapTest() {
		final String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
			+ "<returnsms>"//
			+ "<returnstatus>Success</returnstatus>"//
			+ "<message>ok</message>"//
			+ "<remainpoint>1490</remainpoint>"//
			+ "<taskID>885</taskID>"//
			+ "<successCounts>1</successCounts>"//
			+ "<newNode><sub>subText</sub></newNode>"//
			+ "</returnsms>";
		final Map<String, Object> map = XmlUtil.xmlToMap(xml);

		Assertions.assertEquals(6, map.size());
		Assertions.assertEquals("Success", map.get("returnstatus"));
		Assertions.assertEquals("ok", map.get("message"));
		Assertions.assertEquals("1490", map.get("remainpoint"));
		Assertions.assertEquals("885", map.get("taskID"));
		Assertions.assertEquals("1", map.get("successCounts"));
		Assertions.assertEquals("subText", ((Map<?, ?>) map.get("newNode")).get("sub"));
	}

	@Test
	public void xmlToMapTest2() {
		final String xml = "<root><name>张三</name><name>李四</name></root>";
		final Map<String, Object> map = XmlUtil.xmlToMap(xml);

		Assertions.assertEquals(1, map.size());
		Assertions.assertEquals(ListUtil.of("张三", "李四"), map.get("name"));
	}

	@Test
	public void mapToXmlTest() {
		final Map<String, Object> map = MapBuilder.of(new LinkedHashMap<String, Object>())//
			.put("name", "张三")//
			.put("age", 12)//
			.put("game", MapUtil.builder(new LinkedHashMap<String, Object>()).put("昵称", "Looly").put("level", 14).build())//
			.build();
		final Document doc = XmlUtil.mapToXml(map, "user");
		// Console.log(XmlUtil.toStr(doc, false));
		Assertions.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"//
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
		final Map<String, Object> map = MapBuilder.of(new LinkedHashMap<String, Object>())
			.put("Town", ListUtil.of("town1", "town2"))
			.build();

		final Document doc = XmlUtil.mapToXml(map, "City");
		Assertions.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
				"<City>" +
				"<Town>town1</Town>" +
				"<Town>town2</Town>" +
				"</City>",
			XmlUtil.toStr(doc));
	}

	@Test
	public void readTest() {
		final Document doc = XmlUtil.readXml("test.xml");
		Assertions.assertNotNull(doc);
	}

	@Test
	public void readBySaxTest() {
		final Set<String> eles = SetUtil.of(
			"returnsms", "returnstatus", "message", "remainpoint", "taskID", "successCounts");
		XmlUtil.readBySax(ResourceUtil.getStream("test.xml"), new DefaultHandler() {
			@Override
			public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) {
				Assertions.assertTrue(eles.contains(localName));
			}
		});
	}

	@Test
	public void mapToXmlTestWithOmitXmlDeclaration() {

		final Map<String, Object> map = MapBuilder.of(new LinkedHashMap<String, Object>())
			.put("name", "ddatsh")
			.build();
		final String xml = XmlUtil.mapToXmlStr(map, true);
		Assertions.assertEquals("<xml><name>ddatsh</name></xml>", xml);
	}

	@Test
	public void getByPathTest() {
		final String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
			"  <soap:Body>\n" +
			"    <ns2:testResponse xmlns:ns2=\"http://ws.xxx.com/\">\n" +
			"      <return>2020/04/15 21:01:21</return>\n" +
			"    </ns2:testResponse>\n" +
			"  </soap:Body>\n" +
			"</soap:Envelope>\n";

		final Document document = XmlUtil.readXml(xmlStr);
		final Object value = XPathUtil.getByXPath(
			"//soap:Envelope/soap:Body/ns2:testResponse/return",
			document, XPathConstants.STRING);//
		Assertions.assertEquals("2020/04/15 21:01:21", value);
	}

	@Test
	public void beanToXmlIgnoreNullTest() {
		@Data
		class TestBean {
			private String ReqCode;
			private String AccountName;
			private String Operator;
			private String ProjectCode;
			private String BankCode;
		}

		final TestBean testBean = new TestBean();
		testBean.setReqCode("1111");
		testBean.setAccountName("账户名称");
		testBean.setOperator("cz");
		testBean.setProjectCode(null);
		testBean.setBankCode("00001");

		// 不忽略空字段情况下保留自闭标签
		Document doc = XmlUtil.beanToXml(testBean, null, false);
		Assertions.assertNotNull(XmlUtil.getElement(doc.getDocumentElement(), "ProjectCode"));

		// 忽略空字段情况下无自闭标签
		doc = XmlUtil.beanToXml(testBean, null, true);
		Assertions.assertNull(XmlUtil.getElement(doc.getDocumentElement(), "ProjectCode"));
	}

	@Test
	public void xmlToBeanTest() {
		@Data
		class TestBean {
			private String ReqCode;
			private String AccountName;
			private String Operator;
			private String ProjectCode;
			private String BankCode;
		}

		final TestBean testBean = new TestBean();
		testBean.setReqCode("1111");
		testBean.setAccountName("账户名称");
		testBean.setOperator("cz");
		testBean.setProjectCode("123");
		testBean.setBankCode("00001");

		final Document doc = XmlUtil.beanToXml(testBean);
		Assertions.assertEquals(TestBean.class.getSimpleName(), doc.getDocumentElement().getTagName());

		final TestBean testBean2 = XmlUtil.xmlToBean(doc, TestBean.class);
		Assertions.assertEquals(testBean.getReqCode(), testBean2.getReqCode());
		Assertions.assertEquals(testBean.getAccountName(), testBean2.getAccountName());
		Assertions.assertEquals(testBean.getOperator(), testBean2.getOperator());
		Assertions.assertEquals(testBean.getProjectCode(), testBean2.getProjectCode());
		Assertions.assertEquals(testBean.getBankCode(), testBean2.getBankCode());
	}

	@Test
	public void xmlToBeanTest2() {
		@Data
		class SmsRes {
			private String code;
		}

		//issue#1663@Github
		final String xmlStr = "<?xml version=\"1.0\" encoding=\"gbk\" ?><response><code>02</code></response>";

		final Document doc = XmlUtil.parseXml(xmlStr);

		// 标准方式
		final Map<String, Object> map = XmlUtil.xmlToMap(doc.getFirstChild());
		final SmsRes res = new SmsRes();
		BeanUtil.fillBeanWithMap(map, res, CopyOptions.of().setIgnoreError(true));

		// toBean方式
		final SmsRes res1 = XmlUtil.xmlToBean(doc.getFirstChild(), SmsRes.class);

		Assertions.assertEquals(res.toString(), res1.toString());
	}

	@Test
	public void cleanCommentTest() {
		final String xmlContent = "<info><title>hutool</title><!-- 这是注释 --><lang>java</lang></info>";
		final String ret = XmlUtil.cleanComment(xmlContent);
		Assertions.assertEquals("<info><title>hutool</title><lang>java</lang></info>", ret);
	}

	@Test
	@Disabled
	public void formatTest() {
		// https://github.com/looly/hutool/pull/1234
		final Document xml = XmlUtil.createXml("NODES");
		xml.setXmlStandalone(true);

		final NodeList parentNode = xml.getElementsByTagName("NODES");

		final Element parent1Node = xml.createElement("NODE");

		final Element node1 = xml.createElement("NODENAME");
		node1.setTextContent("走位");
		final Element node2 = xml.createElement("STEP");
		node2.setTextContent("1");
		final Element node3 = xml.createElement("STATE");
		node3.setTextContent("2");
		final Element node4 = xml.createElement("TIMELIMIT");
		node4.setTextContent("");
		final Element node5 = xml.createElement("STARTTIME");

		parent1Node.appendChild(node1);
		parent1Node.appendChild(node2);
		parent1Node.appendChild(node3);
		parent1Node.appendChild(node4);
		parent1Node.appendChild(node5);

		parentNode.item(0).appendChild(parent1Node);

		final String format = XmlUtil.toStr(xml, CharsetUtil.GBK, true);
		Console.log(format);
	}

	@Test
	public void getParamTest() {
		final String xml = "<Config name=\"aaaa\">\n" +
			"    <url>222222</url>\n" +
			"</Config>";

		final Document doc = XmlUtil.parseXml(xml);
		final String name = doc.getDocumentElement().getAttribute("name");
		Assertions.assertEquals("aaaa", name);
	}

	@Test
	@Disabled
	public void issueI5DO8ETest() {
		// 增加子节点后，格式会错乱，JDK的bug
		final String xmlStr = ResourceUtil.readUtf8Str("issueI5DO8E.xml");
		final Document doc = XmlUtil.readXml(xmlStr);

		final Element item = doc.createElement("item");
		item.setAttribute("id", "cover-image");
		final Element manifestEl = XPathUtil.getElementByXPath("//package/manifest", doc);
		manifestEl.appendChild(item);

		Console.log(XmlUtil.format(doc));
	}

	@Test
	public void xmlStrToBeanTest() {
		final String xml = "<userInfo><name>张三</name><age>20</age><email>zhangsan@example.com</email></userInfo>";
		final Document document = XmlUtil.readXml(xml);
		final UserInfo userInfo = XmlUtil.xmlToBean(document, UserInfo.class);
		Assertions.assertEquals("张三", userInfo.getName());
		Assertions.assertEquals("20", userInfo.getAge());
		Assertions.assertEquals("zhangsan@example.com", userInfo.getEmail());
	}

	@Data
	static class UserInfo {

		private String id;
		private String name;
		private String age;
		private String email;
	}

	@Test
	public void issue3139Test() {
		final String xml = "<r>\n" +
			"  <c>\n" +
			"     <s>1</s>\n" +
			"     <p>str</p>\n" +
			"  </c>\n" +
			"</r>";

		final R r = XmlUtil.xmlToBean(XmlUtil.parseXml(xml), R.class);
		Assertions.assertEquals("1", r.getC().get(0).getS());
		Assertions.assertEquals("str", r.getC().get(0).getP());
	}

	@Data
	static class C {
		String s;
		String p;
	}

	@Data
	static class R {
		List<C> c;
	}
}
