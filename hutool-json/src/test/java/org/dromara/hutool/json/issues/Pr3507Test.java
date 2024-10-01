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

package org.dromara.hutool.json.issues;

import org.dromara.hutool.json.JSONFactory;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.serializer.JSONSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Pr3507Test {
	@Test
	void writeClassTest() {
		// 考虑安全问题，不提供默认的Class的序列化器，此处局部自定义
		final JSONFactory factory = JSONFactory.of(null, null);
		factory.register(Class.class, (JSONSerializer<Class<?>>) (bean, context) -> context.getOrCreatePrimitive(bean.getName()));

		final JSONObject set = factory.ofObj().putValue("name", Pr3507Test.class);
		Assertions.assertEquals("{\"name\":\"org.dromara.hutool.json.issues.Pr3507Test\"}", set.toString());
	}
}
