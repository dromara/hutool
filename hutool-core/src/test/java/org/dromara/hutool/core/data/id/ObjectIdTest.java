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

import org.dromara.hutool.core.data.id.ObjectId;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

/**
 * ObjectId单元测试
 *
 * @author looly
 *
 */
public class ObjectIdTest {

	@Test
	public void distinctTest() {
		//生成10000个id测试是否重复
		final HashSet<String> set = new HashSet<>();
		for(int i = 0; i < 10000; i++) {
			set.add(ObjectId.next());
		}

		Assertions.assertEquals(10000, set.size());
	}

	@Test
	@Disabled
	public void nextTest() {
		Console.log(ObjectId.next());
	}
}
