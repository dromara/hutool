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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * https://github.com/dromara/hutool/issues/2082<br>
 * 当setXXX有重载方法的时候，BeanDesc中会匹配到重载方法，增加类型检查来规避之
 */
public class Issue2082Test {

	@Test
	public void toBeanTest() {
		final TestBean2 testBean2 = new TestBean2();
		final TestBean test = BeanUtil.toBean(testBean2, TestBean.class);
		Assertions.assertNull(test.getId());
	}

	@Data
	static class TestBean {
		private Long id;

		public void setId(final String id) {
			this.id = Long.valueOf(id);
		}
	}

	@Data
	static class TestBean2 {
		private String id;
	}
}
