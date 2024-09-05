/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.extra.template;

import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.template.TemplateConfig.ResourceMode;
import org.dromara.hutool.extra.template.engine.TemplateEngine;
import org.dromara.hutool.extra.template.engine.TemplateEngineFactory;
import org.dromara.hutool.extra.template.engine.beetl.BeetlEngine;
import org.dromara.hutool.extra.template.engine.enjoy.EnjoyEngine;
import org.dromara.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import org.dromara.hutool.extra.template.engine.jte.JteEngine;
import org.dromara.hutool.extra.template.engine.pebble.PebbleTemplateEngine;
import org.dromara.hutool.extra.template.engine.rythm.RythmEngine;
import org.dromara.hutool.extra.template.engine.thymeleaf.ThymeleafEngine;
import org.dromara.hutool.extra.template.engine.velocity.VelocityEngine;
import org.dromara.hutool.extra.template.engine.wit.WitEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

/**
 * 模板引擎单元测试
 *
 * @author looly
 *
 */
public class TemplateFactoryTest {

	@Test
	public void createEngineTest() {
		// 字符串模板, 默认模板引擎，此处为Beetl
		TemplateEngine engine = TemplateEngineFactory.createEngine(new TemplateConfig());
		final Template template = engine.getTemplate("hello,${name}");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", result);

		// classpath中获取模板
		engine = TemplateEngineFactory.createEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH));
		final Template template2 = engine.getTemplate("beetl_test.btl");
		final String result2 = template2.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", result2);
	}

	@Test
	public void beetlEngineTest() {
		// 字符串模板
		TemplateEngine engine = new BeetlEngine(new TemplateConfig("templates"));
		final Template template = engine.getTemplate("hello,${name}");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", result);

		// classpath中获取模板
		engine = new BeetlEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH));
		final Template template2 = engine.getTemplate("beetl_test.btl");
		final String result2 = template2.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", result2);
	}

	@Test
	public void rythmEngineTest() {
		// 字符串模板
		final TemplateEngine engine = TemplateEngineFactory.createEngine(
				new TemplateConfig("templates").setCustomEngine(RythmEngine.class));
		final Template template = engine.getTemplate("hello,@name");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", result);

		// classpath中获取模板
		final Template template2 = engine.getTemplate("rythm_test.tmpl");
		final String result2 = template2.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", result2);
	}

	@Test
	public void freemarkerEngineTest() {
		// 字符串模板
		TemplateEngine engine = TemplateEngineFactory.createEngine(
				new TemplateConfig("templates", ResourceMode.STRING).setCustomEngine(FreemarkerEngine.class));
		Template template = engine.getTemplate("hello,${name}");
		String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", result);

		//ClassPath模板
		engine = TemplateEngineFactory.createEngine(
				new TemplateConfig("templates", ResourceMode.CLASSPATH).setCustomEngine(FreemarkerEngine.class));
		template = engine.getTemplate("freemarker_test.ftl");
		result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", result);
	}

	@Test
	public void velocityEngineTest() {
		// 字符串模板
		TemplateEngine engine = TemplateEngineFactory.createEngine(
				new TemplateConfig("templates", ResourceMode.STRING).setCustomEngine(VelocityEngine.class));
		Template template = engine.getTemplate("你好,$name");
		String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("你好,hutool", result);

		//ClassPath模板
		engine = TemplateEngineFactory.createEngine(
				new TemplateConfig("templates", ResourceMode.CLASSPATH).setCustomEngine(VelocityEngine.class));
		template = engine.getTemplate("velocity_test.vtl");
		result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("你好,hutool", result);

		template = engine.getTemplate("templates/velocity_test.vtl");
		result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("你好,hutool", result);
	}

	@Test
	public void enjoyEngineTest() {
		// 字符串模板
		TemplateEngine engine = TemplateEngineFactory.createEngine(
				new TemplateConfig("templates").setCustomEngine(EnjoyEngine.class));
		Template template = engine.getTemplate("#(x + 123)");
		String result = template.render(Dict.of().set("x", 1));
		Assertions.assertEquals("124", result);

		//ClassPath模板
		engine = new EnjoyEngine(
				new TemplateConfig("templates", ResourceMode.CLASSPATH).setCustomEngine(EnjoyEngine.class));
		template = engine.getTemplate("enjoy_test.etl");
		result = template.render(Dict.of().set("x", 1));
		Assertions.assertEquals("124", result);
	}

	@Test
	public void thymeleafEngineTest() {
		// 字符串模板
		TemplateEngine engine = TemplateEngineFactory.createEngine(
				new TemplateConfig("templates").setCustomEngine(ThymeleafEngine.class));
		Template template = engine.getTemplate("<h3 th:text=\"${message}\"></h3>");
		String result = template.render(Dict.of().set("message", "Hutool"));
		Assertions.assertEquals("<h3>Hutool</h3>", result);

		//ClassPath模板
		engine = TemplateEngineFactory.createEngine(
				new TemplateConfig("templates", ResourceMode.CLASSPATH).setCustomEngine(ThymeleafEngine.class));
		template = engine.getTemplate("thymeleaf_test.ttl");
		result = template.render(Dict.of().set("message", "Hutool"));
		Assertions.assertEquals("<h3>Hutool</h3>", result);
	}

	@Test
	public void jteEngineTest() {
		// 字符串模板
		TemplateEngine engine = TemplateEngineFactory.createEngine(
				new TemplateConfig().setCustomEngine(JteEngine.class));
		Template template = engine.getTemplate("@param java.util.HashMap<String, String> map\n" +
			"<h3>${map.get(\"message\")}</h3>");
		final Map<String, String> model = new HashMap<>();
		model.put("message", "Hutool");
		String result = template.render(model);
		Assertions.assertEquals("<h3>Hutool</h3>", result);

		//ClassPath模板
		engine = TemplateEngineFactory.createEngine(
				new TemplateConfig("templates", ResourceMode.CLASSPATH).setCustomEngine(JteEngine.class));
		template = engine.getTemplate("jte_test.jte");
		result = template.render(model);
		Assertions.assertEquals("<h3>Hutool</h3>", result);
	}

	/**
	 * pebble template engine test
	 */
	@Test
	public void pebbleEngineTest() {
		// 字符串模板
		TemplateEngine engine = TemplateEngineFactory.createEngine(new TemplateConfig("templates").setCustomEngine(PebbleTemplateEngine.class));
		Template template = engine.getTemplate("<h3>{{ message }}</h3>");
		String result = template.render(Dict.of().set("message", "Hutool"));
		Assertions.assertEquals("<h3>Hutool</h3>", result);

		//ClassPath模板
		engine = TemplateEngineFactory.createEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH).setCustomEngine(PebbleTemplateEngine.class));
		template = engine.getTemplate("pebble_test.peb");
		result = template.render(Dict.of().set("name", "Hutool"));
		Assertions.assertEquals("hello, Hutool", result);
	}

	@Test
	@Disabled
	public void renderToFileTest() {
		final TemplateEngine engine = new BeetlEngine(new TemplateConfig("templates", ResourceMode.CLASSPATH));
		final Template template = engine.getTemplate("freemarker_test.ftl");

		final Map<String, Object> bindingMap = new HashMap<>();
		bindingMap.put("name", "aa");
		final File outputFile = new File("e:/test.txt");
		template.render(bindingMap, outputFile);
	}

	@Test
	public void witEngineTest() {
		//classpath模板
		TemplateConfig config = new TemplateConfig("templates", ResourceMode.CLASSPATH)
				.setCustomEngine(WitEngine.class);
		TemplateEngine engine = TemplateEngineFactory.createEngine(config);
		Template template = engine.getTemplate("/wit_test.wit");
		String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", StrUtil.trim(result));

		// 字符串模板
		config = new TemplateConfig("templates", ResourceMode.STRING)
				.setCustomEngine(WitEngine.class);
		engine = TemplateEngineFactory.createEngine(config);
		template = engine.getTemplate("<%var name;%>hello,${name}");
		result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", StrUtil.trim(result));
	}
}
