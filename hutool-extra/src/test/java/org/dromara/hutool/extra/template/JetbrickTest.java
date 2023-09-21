/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.template;

import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.template.engine.TemplateEngine;
import org.dromara.hutool.extra.template.engine.TemplateEngineFactory;
import org.dromara.hutool.extra.template.engine.jetbrick.JetbrickEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JetbrickTest {

	@Test
	public void jetbrickEngineTest() {
		//classpath模板
		final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH)
				.setCustomEngine(JetbrickEngine.class);
		final TemplateEngine engine = TemplateEngineFactory.createEngine(config);
		final Template template = engine.getTemplate("jetbrick_test.jetx");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("你好,hutool", StrUtil.trim(result));
	}

	@Test
	public void jetbrickEngineWithStringTest() {
		// 字符串模板
		final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.STRING)
				.setCustomEngine(JetbrickEngine.class);
		final TemplateEngine engine = TemplateEngineFactory.createEngine(config);
		final Template template = engine.getTemplate("hello,${name}");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("hello,hutool", StrUtil.trim(result));
	}
}
