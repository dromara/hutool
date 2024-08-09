package cn.hutool.json.xml;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.XML;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLTest {

	@Test
	public void toXmlTest() {
		final JSONObject put = JSONUtil.createObj()
			.set("aaa", "你好")
			.set("键2", "test");
		final String s = JSONUtil.toXmlStr(put);
		Assertions.assertEquals("<aaa>你好</aaa><键2>test</键2>", s);
	}

	@Test
	public void escapeTest() {
		String xml = "<a>•</a>";
		JSONObject jsonObject = XML.toJSONObject(xml);

		assertEquals("{\"a\":\"•\"}", jsonObject.toString());

		String xml2 = XML.toXml(JSONUtil.parseObj(jsonObject));
		assertEquals(xml, xml2);
	}

	@Test
	public void xmlContentTest() {
		JSONObject jsonObject = JSONUtil.createObj().set("content", "123456");

		String xml = XML.toXml(jsonObject);
		assertEquals("123456", xml);

		xml = XML.toXml(jsonObject, null, new String[0]);
		assertEquals("<content>123456</content>", xml);
	}
}
