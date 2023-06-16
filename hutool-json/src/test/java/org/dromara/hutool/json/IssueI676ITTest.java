package org.dromara.hutool.json;

import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.xml.XmlUtil;
import org.dromara.hutool.json.xml.JSONXMLSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.xpath.XPathConstants;

public class IssueI676ITTest {
	@Test
	public void parseXMLTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(ResourceUtil.readUtf8Str("issueI676IT.json"));
		final String xmlStr = JSONXMLSerializer.toXml(jsonObject, null, (String) null);
		final String content = String.valueOf(XmlUtil.getByXPath("/page/orderItems[1]/content", XmlUtil.readXML(xmlStr), XPathConstants.STRING));
		Assertions.assertEquals(content, "bar1");
	}
}
