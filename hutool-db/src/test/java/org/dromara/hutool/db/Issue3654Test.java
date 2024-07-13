/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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

import org.dromara.hutool.db.config.DbConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue3654Test {
	@Test
	@Disabled
	void insertSqliteTest() {
		final Db db = Db.of(DbConfig.of().setUrl("jdbc:sqlite:d:/test/test.db"));
		db.insert(Entity.of("user").set("age", "testStr2"));
	}
}
