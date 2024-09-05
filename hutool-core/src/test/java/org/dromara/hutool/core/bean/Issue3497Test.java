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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Issue3497Test {

	@Test
	public void setFieldEditorTest() {
		final Map<String, String> aB = MapUtil.builder("a_b", "1").build();
		final Map<?, ?> bean = BeanUtil.toBean(aB, Map.class, CopyOptions.of().setFieldEditor((entry)->{
			entry.setKey(StrUtil.toCamelCase(entry.getKey().toString()));
			return entry;
		}));
		Assertions.assertEquals(bean.toString(), "{aB=1}");
	}

}
