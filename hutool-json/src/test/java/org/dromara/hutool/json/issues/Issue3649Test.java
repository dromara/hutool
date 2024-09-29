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

import lombok.Data;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue3649Test {
	@Test
	void toEmptyBeanTest() {
		//issue#3649，对于空对象转目标对象，直接实例化一个空对象
		// 逻辑见：BeanTypeAdapter
		final Object bean = JSONUtil.toBean("{}", JSONConfig.of().setIgnoreError(false), EmptyBean.class);
		Assertions.assertEquals(new EmptyBean(), bean);
	}

	@Test
	void toEmptyListTest() {
		final List<?> bean = JSONUtil.toBean("[]", JSONConfig.of().setIgnoreError(false), List.class);
		Assertions.assertNotNull(bean);
		Assertions.assertTrue(bean.isEmpty());
	}

	@Data
	public static class EmptyBean {
	}
}
