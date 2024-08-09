package cn.hutool.extra.template;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.engine.jetbrick.JetbrickEngine;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JetbrickTest {

	@Test
	public void jetbrickEngineTest() {
		//classpath模板
		TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH)
				.setCustomEngine(JetbrickEngine.class);
		TemplateEngine engine = TemplateUtil.createEngine(config);
		Template template = engine.getTemplate("jetbrick_test.jetx");
		String result = template.render(Dict.create().set("name", "hutool"));
		assertEquals("你好,hutool", StrUtil.trim(result));
	}

	@Test
	public void jetbrickEngineWithStringTest() {
		// 字符串模板
		TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.STRING)
				.setCustomEngine(JetbrickEngine.class);
		TemplateEngine engine = TemplateUtil.createEngine(config);
		Template template = engine.getTemplate("hello,${name}");
		String result = template.render(Dict.create().set("name", "hutool"));
		assertEquals("hello,hutool", StrUtil.trim(result));
	}
}
