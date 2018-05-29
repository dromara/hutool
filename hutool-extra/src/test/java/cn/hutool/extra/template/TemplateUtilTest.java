package cn.hutool.extra.template;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.TemplateConfig.ResourceMode;
import cn.hutool.extra.template.engine.beetl.BeetlEngine;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import cn.hutool.extra.template.engine.rythm.RythmEngine;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;

/**
 * 模板引擎单元测试
 * 
 * @author looly
 *
 */
public class TemplateUtilTest {

	@Test
	public void createEngineTest() {
		//默认模板引擎，此处为Beetl
		Engine engine = TemplateUtil.createEngine(new TemplateConfig());
		Template template = engine.getTemplate("hello,${name}");
		String result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result);
	}
	
	@Test
	public void beetlEngineTest() {
		// 字符串模板
		Engine engine = new BeetlEngine(new TemplateConfig("templates"));
		Template template = engine.getTemplate("hello,${name}");
		String result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result);

		// classpath中获取模板
		engine = new BeetlEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH));
		Template template2 = engine.getTemplate("beetl_test.btl");
		String result2 = template2.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result2);
	}
	
	@Test
	public void rythmEngineTest() {
		// 字符串模板
		Engine engine = new RythmEngine(new TemplateConfig("templates"));
		Template template = engine.getTemplate("hello,@name");
		String result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result);

		// classpath中获取模板
		Template template2 = engine.getTemplate("rythm_test.tmpl");
		String result2 = template2.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result2);
	}
	
	@Test
	public void freemarkerEngineTest() {
		Engine engine = new FreemarkerEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH));
		Template template = engine.getTemplate("freemarker_test.ftl");
		String result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result);
	}

	@Test
	public void velocityEngineTest() {
		Engine engine = new VelocityEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH));
		Template template = engine.getTemplate("templates/velocity_test.vtl");
		String result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result);
	}
}
