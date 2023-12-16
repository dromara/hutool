/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.data.id;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SeataSnowflakeTest {
	@Test
	public void nextTest() {
		//构建Snowflake，提供终端ID和数据中心ID
		final SeataSnowflake idWorker = new SeataSnowflake();
		final long nextId = idWorker.next();
		Assertions.assertTrue(nextId > 0);
	}

	@Test
	void testNextId() {
		final SeataSnowflake worker = new SeataSnowflake();
		final long id1 = worker.next();
		final long id2 = worker.next();
		Assertions.assertEquals(1L, id2 - id1, "increment step should be 1");
	}
}
