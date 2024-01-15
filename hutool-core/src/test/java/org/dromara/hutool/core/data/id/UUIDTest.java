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

import org.dromara.hutool.core.collection.ConcurrentHashSet;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class UUIDTest {

	/**
	 * 测试UUID是否存在重复问题
	 */
	@Test
	public void fastUUIDTest(){
		final Set<String> set = new ConcurrentHashSet<>(100);
		ThreadUtil.concurrencyTest(100, ()-> set.add(UUID.fastUUID().toString()));
		Assertions.assertEquals(100, set.size());
		//Console.log(set);
	}


}
