package cn.hutool.extra.template;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;

public class TemplateUtilTest {

	@Test
	public void createEngineTest() {
		Engine engine = TemplateUtil.createEngine(new TemplateConfig());
		Template template = engine.getTemplate("hello,${name}");
		String result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result);
	}
	
	@Test
	public void createVelocityEngineTest() {
		Engine engine = new VelocityEngine();
		Template template = engine.getTemplate("hello,$name");
		String result = template.render(Dict.create().set("name", "hutool"));
		Console.log(result);
	}
}
