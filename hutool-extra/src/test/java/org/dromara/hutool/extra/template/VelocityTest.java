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
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.extra.template.engine.TemplateEngine;
import org.dromara.hutool.extra.template.engine.TemplateEngineFactory;
import org.dromara.hutool.extra.template.engine.velocity.VelocityEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VelocityTest {

	@Test
	public void charsetTest(){
		final TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH);
		config.setCustomEngine(VelocityEngine.class);
		config.setCharset(CharsetUtil.GBK);
		final TemplateEngine engine = TemplateEngineFactory.createEngine(config);
		final Template template = engine.getTemplate("velocity_test_gbk.vtl");
		final String result = template.render(Dict.of().set("name", "hutool"));
		Assertions.assertEquals("你好,hutool", result);
	}
}
