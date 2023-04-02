package org.dromara.hutool.xml;

import org.dromara.hutool.JSONObject;
import org.dromara.hutool.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class XMLTest {

	@Test
	public void toXmlTest(){
		final JSONObject put = JSONUtil.ofObj()
				.set("aaa", "你好")
				.set("键2", "test");
		final String s = JSONUtil.toXmlStr(put);
		Assertions.assertEquals("<aaa>你好</aaa><键2>test</键2>", s);
	}

	@Test
	public void escapeTest(){
		final String xml = "<a>•</a>";
		final JSONObject jsonObject = JSONXMLUtil.toJSONObject(xml);

		Assertions.assertEquals("{\"a\":\"•\"}", jsonObject.toString());

		final String xml2 = JSONXMLUtil.toXml(JSONUtil.parseObj(jsonObject));
		Assertions.assertEquals(xml, xml2);
	}

	@Test
	public void xmlContentTest(){
		final JSONObject jsonObject = JSONUtil.ofObj().set("content","123456");

		String xml = JSONXMLUtil.toXml(jsonObject);
		Assertions.assertEquals("123456", xml);

		xml = JSONXMLUtil.toXml(jsonObject, null, new String[0]);
		Assertions.assertEquals("<content>123456</content>", xml);
	}
}
