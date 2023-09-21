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

package org.dromara.hutool.json;

import org.dromara.hutool.core.annotation.Alias;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gitee.com/dromara/hutool/issues/I4XFMW
 */
public class IssueI4XFMWTest {

	@Test
	public void test() {
		final List<TestEntity> entityList = new ArrayList<>();
		final TestEntity entityA = new TestEntity();
		entityA.setId("123");
		entityA.setPassword("456");
		entityList.add(entityA);

		final TestEntity entityB = new TestEntity();
		entityB.setId("789");
		entityB.setPassword("098");
		entityList.add(entityB);

		final String jsonStr = JSONUtil.toJsonStr(entityList);
		Assertions.assertEquals("[{\"uid\":\"123\",\"password\":\"456\"},{\"uid\":\"789\",\"password\":\"098\"}]", jsonStr);
		final List<TestEntity> testEntities = JSONUtil.toList(jsonStr, TestEntity.class);
		Assertions.assertEquals("123", testEntities.get(0).getId());
		Assertions.assertEquals("789", testEntities.get(1).getId());
	}

	@Data
	static class TestEntity {
		@Alias("uid")
		private String id;
		private String password;
	}
}
