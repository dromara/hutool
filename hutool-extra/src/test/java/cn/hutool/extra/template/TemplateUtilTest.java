package cn.hutool.extra.template;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.TemplateConfig.ResourceMode;
import cn.hutool.extra.template.engine.beetl.BeetlEngine;
import cn.hutool.extra.template.engine.enjoy.EnjoyEngine;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import cn.hutool.extra.template.engine.rythm.RythmEngine;
import cn.hutool.extra.template.engine.thymeleaf.ThymeleafEngine;
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
		// 默认模板引擎，此处为Beetl
		TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
		Template template = engine.getTemplate("hello,${name}");
		String result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result);
	}

	@Test
	public void beetlEngineTest() {
		// 字符串模板
		TemplateEngine engine = new BeetlEngine(new TemplateConfig("templates"));
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
		TemplateEngine engine = new RythmEngine(new TemplateConfig("templates"));
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
		TemplateEngine engine = new FreemarkerEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH));
		Template template = engine.getTemplate("freemarker_test.ftl");
		String result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result);
		
		engine = new FreemarkerEngine(new TemplateConfig("templates", ResourceMode.STRING));
		template = engine.getTemplate("hello,${name}");
		result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", result);
	}
	
	@Test
	public void velocityEngineTest() {
		TemplateEngine engine = new VelocityEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH));
		Template template = engine.getTemplate("templates/velocity_test.vtl");
		String result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("你好,hutool", result);
		
		engine = new VelocityEngine(new TemplateConfig("templates", ResourceMode.STRING));
		template = engine.getTemplate("你好,$name");
		result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("你好,hutool", result);
	}

	@Test
	public void enjoyEngineTest() {
		// 字符串模板
		TemplateEngine engine = new EnjoyEngine(new TemplateConfig("templates"));
		Template template = engine.getTemplate("#(x + 123)");
		String result = template.render(Dict.create().set("x", 1));
		Assert.assertEquals("124", result);
	}

	@Test
	public void thymeleafEngineTest() {
		// 字符串模板
		TemplateEngine engine = new ThymeleafEngine(new TemplateConfig("templates"));
		Template template = engine.getTemplate("<h3 th:text=\"${message}\"></h3>");
		String result = template.render(Dict.create().set("message", "Hutool"));
		Assert.assertEquals("<h3>Hutool</h3>", result);
	}
	
	@Test
	@Ignore
	public void renderToFileTest() {
		TemplateEngine engine = new BeetlEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH));
		Template template = engine.getTemplate("freemarker_test.ftl");

		final Map<String, Object> bindingMap = new HashMap<>();
		bindingMap.put("name", "aa");
		File outputFile = new File("e:/test.txt");
		template.render(bindingMap, outputFile);
	}
}
