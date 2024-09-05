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
