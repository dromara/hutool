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

package org.dromara.hutool.db.ds;

import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.ds.simple.SimpleDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataSourceWrapperTest {

	@Test
	public void cloneTest(){
		final DbConfig dbConfig = DbConfig
			.of("jdbc:sqlite:test.db", "", "")
			.setDriver("test.driver");

		final SimpleDataSource simpleDataSource = new SimpleDataSource(dbConfig);
		final DSWrapper wrapper = new DSWrapper(simpleDataSource, dbConfig);

		final DSWrapper clone = wrapper.clone();
		Assertions.assertEquals("test.driver", clone.getDriver());
		Assertions.assertEquals(simpleDataSource, clone.getRaw());
	}
}
