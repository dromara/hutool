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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueI5DDZXTest {
	@Test
	public void copyPropertiesTest() {
		// 对于final字段，private由于没有提供setter方法，是无法实现属性赋值的，如果设置为public即可
		final TeStudent student = new TeStudent("Hutool");
		final TePerson tePerson = BeanUtil.copyProperties(student, TePerson.class);
		assertEquals("Hutool", tePerson.getName());
	}

	@Data
	static class TeStudent {
		private final String name;
	}

	@Data
	static class TePerson {
		public final String name;
	}
}
