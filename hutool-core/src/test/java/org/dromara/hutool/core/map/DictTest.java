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

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.lang.builder.GenericBuilder;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.map.Dict;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class DictTest {
	@Test
	public void dictTest(){
		final Dict dict = Dict.of()
				.set("key1", 1)//int
				.set("key2", 1000L)//long
				.set("key3", DateTime.now());//Date

		final Long v2 = dict.getLong("key2");
		Assertions.assertEquals(Long.valueOf(1000L), v2);
	}

	@Test
	public void dictTest2(){
		final Dict dict = new Dict(true);
		final Map<String, Object> map = new HashMap<>();
		map.put("A", 1);

		dict.putAll(map);

		Assertions.assertEquals(1, dict.get("A"));
		Assertions.assertEquals(1, dict.get("a"));
	}

	@Test
	public void ofTest(){
		final Dict dict = Dict.ofKvs(
				"RED", "#FF0000",
				"GREEN", "#00FF00",
				"BLUE", "#0000FF"
		);

		Assertions.assertEquals("#FF0000", dict.get("RED"));
		Assertions.assertEquals("#00FF00", dict.get("GREEN"));
		Assertions.assertEquals("#0000FF", dict.get("BLUE"));
	}

	@Test
	public void removeEqualTest(){
		final Dict dict = Dict.ofKvs(
			"key1", null
		);

		final Dict dict2 = Dict.ofKvs(
			"key1", null
		);

		dict.removeEqual(dict2);

		Assertions.assertTrue(dict.isEmpty());
	}

	@Test
	public void setFieldsTest() {
		final User user = GenericBuilder.of(User::new).with(User::setUsername, "hutool").build();
		final Dict dict = Dict.of();
		dict.setFields(user::getNickname, user::getUsername);
		Assertions.assertEquals("hutool", dict.get("username"));
		Assertions.assertNull(dict.get("nickname"));

		// get by lambda
		Assertions.assertEquals("hutool", dict.get(User::getUsername));
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class User {
		private String username;
		private String nickname;
	}

	@Test
	public void removeNewTest(){
		final Dict dict = Dict.of()
			.set("key1", 1)//int
			.set("key2", 1000L)//long
			.set("key3", DateTime.now());//Date

		final Dict dict2 = dict.removeNew("key1");
		Assertions.assertEquals(3, dict.size());
		Assertions.assertEquals(2, dict2.size());
	}
}
