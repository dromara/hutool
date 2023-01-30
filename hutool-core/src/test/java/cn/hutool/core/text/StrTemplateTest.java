package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class StrTemplateTest {

	@Test
	public void parserTest(){
		StrTemplate strTemplate = StrTemplate.getInstance("{", "}");
		Map<String, String> parse1 = strTemplate.parse("siot/sys/{productKey}/{deviceKey}/property/{get}", "siot/sys/11/22/property/get");

		Assert.assertEquals(parse1.get("productKey"), "11");
		Assert.assertEquals(parse1.get("deviceKey"), "22");
		Assert.assertEquals(parse1.get("get"), "get");

		strTemplate = StrTemplate.getInstance("${", "}");
		Map<String, String> parse = strTemplate.parse("siot/sys/${productKey}/${deviceKey}/property/${get}", "siot/sys/11/22/property/get");

		Assert.assertEquals(parse.get("productKey"), "11");
		Assert.assertEquals(parse.get("deviceKey"), "22");
		Assert.assertEquals(parse.get("get"), "get");


		strTemplate = StrTemplate.getInstance("{{", "}}");
		Map<String, String> parse2 = strTemplate.parse("siot/sys/{{pKey}}/{{dKey}}/property/{{get}}", "siot/sys/111111/22222222/property/33333333");

		Assert.assertEquals(parse2.get("pKey"), "111111");
		Assert.assertEquals(parse2.get("dKey"), "22222222");
		Assert.assertEquals(parse2.get("get"), "33333333");
	}


	@Test
	public void format(){
		StrTemplate strTemplate = StrTemplate.getInstance("{", "}");
		Assert.assertEquals(strTemplate.format("I say {0} {1} to the {1}", (Object[]) new String[]{"hello", "world", "world"}), "I say hello world to the world");

		strTemplate = StrTemplate.getInstance("{{", "}}");
		Assert.assertEquals(strTemplate.format("I say {{0}} {{1}} to the {{1}}", (Object[]) new String[]{"hello", "world"}), "I say hello world to the world");
	}


	@Test
	public void format1(){
		StrTemplate strTemplate = StrTemplate.getInstance("{", "}");
		Map<String, String> map = new HashMap<>();
		map.put("param1", "hello");
		map.put("param2", "world");
		Assert.assertEquals(strTemplate.format("I/say/{param1}/{param2}/{param3}", map, true), "I/say/hello/world/");
		Assert.assertEquals(strTemplate.format("I/say/{param1}/{param2}/{param3}", map, false), "I/say/hello/world/{param3}");

		strTemplate = StrTemplate.getInstance("{{", "}}");
		Assert.assertEquals(strTemplate.format("I/say/{{param1}}/{{param2}}/{{param3}}", map, true), "I/say/hello/world/");
		Assert.assertEquals(strTemplate.format("I/say/{{param1}}/{{param2}}/{{param3}}", map, false), "I/say/hello/world/{{param3}}");

	}
}
