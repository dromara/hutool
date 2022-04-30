package cn.hutool.extra.template;

import cn.hutool.core.map.Dict;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;
import org.junit.Assert;
import org.junit.Test;

public class VelocityTest {

	@Test
	public void charsetTest(){
		final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH);
		config.setCustomEngine(VelocityEngine.class);
		config.setCharset(CharsetUtil.GBK);
		final TemplateEngine engine = TemplateUtil.createEngine(config);
		final Template template = engine.getTemplate("velocity_test_gbk.vtl");
		final String result = template.render(Dict.create().set("name", "hutool"));
		Assert.assertEquals("你好,hutool", result);
	}
}
