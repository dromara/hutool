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
