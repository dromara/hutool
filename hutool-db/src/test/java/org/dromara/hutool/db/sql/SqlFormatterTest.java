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

package org.dromara.hutool.db.sql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SqlFormatterTest {

	@Test
	public void formatTest(){
		// issue#I3XS44@Gitee
		// 测试是否空指针错误
		final String sql = "(select 1 from dual) union all (select 1 from dual)";
		SqlFormatter.format(sql);
	}

	@Test
	public void issue3246Test() {
		final String sql = "select * from `order`";
		final String format = SqlFormatter.format(sql);
		Assertions.assertEquals(
			"select\n" +
			"        * \n" +
			"    from\n" +
			"        `order`", format);
	}
}
