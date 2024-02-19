package cn.hutool.extra.template;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import lombok.Data;
import org.junit.Test;

public class Issue3488Test {
	@Test
	public void freemarkerTest() {
		final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH);
		config.setCustomEngine(FreemarkerEngine.class);
		config.setCharset(CharsetUtil.CHARSET_UTF_8);

		final TemplateEngine engine = TemplateUtil.createEngine(config);
		Template template = engine.getTemplate("issue3488.ftl");

		final UserService userService = new UserService();
		userService.setOperator("hutool");
		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setUserService(userService);

		String result = template.render(Dict.create().set("paymentInfo", paymentInfo));
		//Console.log(result);
		//Assert.assertEquals("你好,hutool", result);
	}

	@Data
	static class PaymentInfo{
		private UserService userService;
	}

	@Data
	static class UserService{
		private String operator;
	}
}
