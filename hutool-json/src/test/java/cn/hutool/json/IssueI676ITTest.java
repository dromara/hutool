package cn.hutool.json;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.xml.JSONXMLSerializer;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.xpath.XPathConstants;

public class IssueI676ITTest {
	@Test
	public void parseXMLTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(ResourceUtil.readUtf8Str("issueI676IT.json"));
		final String xmlStr = JSONXMLSerializer.toXml(jsonObject, null, (String) null);
		final String content = String.valueOf(XmlUtil.getByXPath("/page/orderItems[1]/content", XmlUtil.readXML(xmlStr), XPathConstants.STRING));
		Assert.assertEquals(content, "bar1");
	}
}
