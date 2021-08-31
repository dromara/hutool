package cn.hutool.json.xml;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.XML;
import org.junit.Assert;
import org.junit.Test;

public class XMLTest {

	@Test
	public void toXmlTest(){
		final JSONObject put = JSONUtil.createObj()
				.set("aaa", "你好")
				.set("键2", "test");
		final String s = JSONUtil.toXmlStr(put);
		Assert.assertEquals("<aaa>你好</aaa><键2>test</键2>", s);
	}

	@Test
	public void escapeTest(){
		String xml = "<a>•</a>";
		JSONObject jsonObject = XML.toJSONObject(xml);

		Assert.assertEquals("{\"a\":\"•\"}", jsonObject.toString());

		String xml2 = XML.toXml(JSONUtil.parseObj(jsonObject));
		Assert.assertEquals(xml, xml2);
	}

	@Test
	public void xmlContentTest(){
		JSONObject jsonObject = JSONUtil.createObj().set("content","123456");

		String xml = XML.toXml(jsonObject);
		Assert.assertEquals("123456", xml);

		xml = XML.toXml(jsonObject, null, new String[0]);
		Assert.assertEquals("<content>123456</content>", xml);
	}
}
