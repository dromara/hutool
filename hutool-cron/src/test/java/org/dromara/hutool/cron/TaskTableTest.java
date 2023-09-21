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

package org.dromara.hutool.cron;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.data.id.IdUtil;
import org.dromara.hutool.cron.pattern.CronPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TaskTableTest {

	@Test
	public void taskTableTest(){
		final TaskTable taskTable = new TaskTable();
		taskTable.add(IdUtil.fastUUID(), new CronPattern("*/10 * * * * *"), ()-> Console.log("Task 1"));
		taskTable.add(IdUtil.fastUUID(), new CronPattern("*/20 * * * * *"), ()-> Console.log("Task 2"));
		taskTable.add(IdUtil.fastUUID(), new CronPattern("*/30 * * * * *"), ()-> Console.log("Task 3"));

		Assertions.assertEquals(3, taskTable.size());
		final List<String> ids = taskTable.getIds();
		Assertions.assertEquals(3, ids.size());

		// getById
		Assertions.assertEquals(new CronPattern("*/10 * * * * *"), taskTable.getPattern(ids.get(0)));
		Assertions.assertEquals(new CronPattern("*/20 * * * * *"), taskTable.getPattern(ids.get(1)));
		Assertions.assertEquals(new CronPattern("*/30 * * * * *"), taskTable.getPattern(ids.get(2)));

		// set test
		taskTable.updatePattern(ids.get(2), new CronPattern("*/40 * * * * *"));
		Assertions.assertEquals(new CronPattern("*/40 * * * * *"), taskTable.getPattern(ids.get(2)));

		// getTask
		Assertions.assertEquals(
			taskTable.getTask(1),
			taskTable.getTask(ids.get(1))
		);
	}
}
