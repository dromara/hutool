package org.dromara.hutool.extra.template;

import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.template.engine.TemplateEngine;
import org.dromara.hutool.extra.template.engine.jetbrick.JetbrickEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JetbrickTest {

	@Test
	public void jetbrickEngineTest() {
		//classpath模板
		final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH)
				.setCustomEngine(JetbrickEngine.class);
		final TemplateEngine engine = TemplateFactory.createEngine(config);
		final Template template = engine.getTemplate("jetbrick_test.jetx");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("你好,hutool", StrUtil.trim(result));
	}

	@Test
	public void jetbrickEngineWithStringTest() {
		// 字符串模板
		final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.STRING)
				.setCustomEngine(JetbrickEngine.class);
		final TemplateEngine engine = TemplateFactory.createEngine(config);
		final Template template = engine.getTemplate("hello,${name}");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", StrUtil.trim(result));
	}
}
