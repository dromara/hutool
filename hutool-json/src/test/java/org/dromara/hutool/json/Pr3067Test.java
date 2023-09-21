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

import lombok.Data;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Pr3067Test {

	@Test
	public void getListByPathTest1() {
		final JSONObject json = JSONUtil.parseObj(ResourceUtil.readUtf8Str("test_json_path_001.json"));
		final List<TestUser> resultList = json.getByPath("testUserList[1].testArray",
			new TypeReference<List<TestUser>>() {});

		Assertions.assertNotNull(resultList);
		Assertions.assertEquals(2, resultList.size());
		Assertions.assertEquals("a", resultList.get(0).getUsername());
		Assertions.assertEquals("a-password", resultList.get(0).getPassword());
		Assertions.assertEquals("b", resultList.get(1).getUsername());
		Assertions.assertEquals("b-password", resultList.get(1).getPassword());
	}

	@Data
	public static class TestUser {
		private String username;
		private String password;
	}
}
