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

package org.dromara.hutool.core.bean;

import lombok.Data;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Issue3452Test {

	@Test
	void fillBeanWithMapTest() {
		final Map<String, Object> properties = new HashMap<>();
		properties.put("name", "JohnDoe");
		properties.put("user_age", 25);
		final User user = BeanUtil.fillBeanWithMap(
			properties, new User(), CopyOptions.of());
		Assertions.assertEquals("JohnDoe", user.getName());
		Assertions.assertEquals(25, user.getUserAge());
	}

	@Data
	static class User {
		private String name;
		private int userAge;
	}
}
