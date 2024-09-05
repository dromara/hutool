/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
