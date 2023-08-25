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

package cn.hutool.db;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

/**
 * https://gitee.com/dromara/hutool/issues/I73770
 */
public class IssueI73770Test {
	@Test
	public void pageTest() throws SQLException {
		final PageResult<User> result = Db.use()
			.page("select * from user where id = ?"
				, new Page(0, 10), User.class, 9);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Integer.valueOf(9), result.get(0).getId());
	}

	@Data
	static class User {
		private Integer id;
		private String name;
	}
}
