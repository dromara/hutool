package cn.hutool.extra.template;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.engine.beetl.BeetlUtil;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * BeetlUtil单元测试
 * 
 * @author looly
 *
 */
@SuppressWarnings("deprecation")
public class BeetlUtilTest {

	@Test
	public void renderStrTest() throws IOException {
		GroupTemplate groupTemplate = BeetlUtil.createGroupTemplate(new StringTemplateResourceLoader(), Configuration.defaultConfiguration());
		Template template = BeetlUtil.getTemplate(groupTemplate, "hello,${name}");
		String result = BeetlUtil.render(template, Dict.create().set("name", "hutool"));

		Assert.assertEquals("hello,hutool", result);

		String renderFromStr = BeetlUtil.renderFromStr("hello,${name}", Dict.create().set("name", "hutool"));
		Assert.assertEquals("hello,hutool", renderFromStr);

	}
}
