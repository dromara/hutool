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

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeanDescFactoryTest {
	@Test
	void getBeanDescTest() {
		final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Food.class);
		final Collection<PropDesc> props = beanDesc.getProps();
		assertEquals(2, props.size());
	}

	@Test
	void getBeanDescWithoutCacheTest() {
		final BeanDesc beanDesc = BeanDescFactory.getBeanDescWithoutCache(Food.class);
		final Collection<PropDesc> props = beanDesc.getProps();
		assertEquals(2, props.size());
	}

	@Data
	public static class Food {
		private String bookID;
		private String code;
	}
}
