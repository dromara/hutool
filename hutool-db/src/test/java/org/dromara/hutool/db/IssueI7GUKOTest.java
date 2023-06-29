/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI7GUKOTest {
	@Test
	void entityTest() {
		final Entity entity = Entity.of("data")
			.set("sort", "test")
			.set("pcode", "test")
			.set("code", "test")
			.set("define", "test")
			.set("icon", "test")
			.set("label", "test")
			.set("stu", "test");

		Assertions.assertEquals("[sort, pcode, code, define, icon, label, stu]", entity.keySet().toString());
	}
}
