/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class IssueI73770Test {

	private final String sql = "select * from user where id = ?";

	@Test
	void pageForEntityResultTest() {
		final PageResult<Entity> result = Db.of()
			.page(sql, new Page(0, 10), 9);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(9, result.get(0).get("id"));
	}

	@Test
	void pageForBeanResultTest() {
		final PageResult<User> result = Db.of()
				.pageForBeanResult(sql, new Page(0, 10), User.class, 9);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(9, result.get(0).getId());
	}

	@Test
	void pageForBeanListTest() {
		final List<User> result = Db.of()
			.pageForBeanList(sql, new Page(0, 10), User.class, 9);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(9, result.get(0).getId());
	}

	@Data
	static class User{
		private Integer id;
		private String name;
	}
}
