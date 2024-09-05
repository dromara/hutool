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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Issue3645Test {
	@Test
	public void copyPropertiesTest() {
		final User p = new User();
		p.setUserId(123L);

		final Map<Long, User> map = new HashMap<>();
		map.put(123L,p);

		final Map<Long, User> m = new HashMap<>();
		BeanUtil.copyProperties(map, m);
		final User u = m.get(123L);
		assertNotNull(u);
	}

	@Data
	static class User{
		private Long userId;
	}
}
