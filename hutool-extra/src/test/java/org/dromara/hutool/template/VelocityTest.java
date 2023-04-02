package org.dromara.hutool.template;

import org.dromara.hutool.map.Dict;
import org.dromara.hutool.util.CharsetUtil;
import org.dromara.hutool.template.engine.velocity.VelocityEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VelocityTest {

	@Test
	public void charsetTest(){
		final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH);
		config.setCustomEngine(VelocityEngine.class);
		config.setCharset(CharsetUtil.GBK);
		final TemplateEngine engine = TemplateUtil.createEngine(config);
		final Template template = engine.getTemplate("velocity_test_gbk.vtl");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("你好,hutool", result);
	}
}
