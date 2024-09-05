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

package org.dromara.hutool.core.text;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.hutool.core.text.placeholder.StrFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StrFormatterTest {

	@Test
	public void formatTest() {
		//通常使用
		final String result1 = StrFormatter.format("this is {} for {}", "a", "b");
		Assertions.assertEquals("this is a for b", result1);

		//转义{}
		final String result2 = StrFormatter.format("this is \\{} for {}", "a", "b");
		Assertions.assertEquals("this is {} for a", result2);

		//转义\
		final String result3 = StrFormatter.format("this is \\\\{} for {}", "a", "b");
		Assertions.assertEquals("this is \\a for b", result3);
	}

	@Test
	public void formatWithTest() {
		//通常使用
		final String result1 = StrFormatter.formatWith("this is ? for ?", "?", "a", "b");
		Assertions.assertEquals("this is a for b", result1);

		//转义?
		final String result2 = StrFormatter.formatWith("this is \\? for ?", "?", "a", "b");
		Assertions.assertEquals("this is ? for a", result2);

		//转义\
		final String result3 = StrFormatter.formatWith("this is \\\\? for ?", "?", "a", "b");
		Assertions.assertEquals("this is \\a for b", result3);
	}

	@Test
	public void formatWithTest2() {
		//通常使用
		final String result1 = StrFormatter.formatWith("this is $$$ for $$$", "$$$", "a", "b");
		Assertions.assertEquals("this is a for b", result1);

		//转义?
		final String result2 = StrFormatter.formatWith("this is \\$$$ for $$$", "$$$", "a", "b");
		Assertions.assertEquals("this is $$$ for a", result2);

		//转义\
		final String result3 = StrFormatter.formatWith("this is \\\\$$$ for $$$", "$$$", "a", "b");
		Assertions.assertEquals("this is \\a for b", result3);
	}

	@Test
	void formatByBeanTest() {
		final User user = new User("张三", 18, true);
		final String s = StrFormatter.formatByBean("User name: {name}, age: {age}, gender: {gender}", user, true);

		Assertions.assertEquals("User name: 张三, age: 18, gender: true", s);
	}

	@Data
	@AllArgsConstructor
	private static class User{
		private String name;
		private int age;
		private boolean gender;
	}
}
