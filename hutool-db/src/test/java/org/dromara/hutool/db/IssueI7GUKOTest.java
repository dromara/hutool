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

package org.dromara.hutool.db;

import org.dromara.hutool.core.collection.CollUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class IssueI7GUKOTest {

	@Test
	void entityTest() {
		final Entity entity = Entity.of("data")
			.set("sort", "")
			.set("pcode", "test")
			.set("code", "")
			.set("define", "test")
			.set("icon", "")
			.set("label", "test")
			.set("stu", "test");

		Assertions.assertEquals("[sort, pcode, code, define, icon, label, stu]", entity.keySet().toString());

		final Set<String> keys = CollUtil.removeEmpty(entity.keySet());
		Assertions.assertEquals("[sort, pcode, code, define, icon, label, stu]", keys.toString());
	}
}
