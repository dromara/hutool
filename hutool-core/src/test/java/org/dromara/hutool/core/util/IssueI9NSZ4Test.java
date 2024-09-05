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

package org.dromara.hutool.core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI9NSZ4Test {
	@Test
	public void getByTest() {
		// AnimalKindInZoo所有枚举结果的getMappedValue结果值中都无AnimalKind.DOG，返回null
		final AnimalKindInZoo by = EnumUtil.getBy(AnimalKindInZoo::getMappedValue, AnimalKind.DOG);
		Assertions.assertNull(by);
	}

	@Test
	public void getByTest2() {
		final AnimalKindInZoo by = EnumUtil.getBy(AnimalKindInZoo::getMappedValue, AnimalKind.BIRD);
		Assertions.assertEquals(AnimalKindInZoo.BIRD, by);
	}

	/**
	 * 动物类型
	 */
	@Getter
	@ToString
	@AllArgsConstructor
	public enum AnimalKind {

		/**
		 * 猫
		 */
		CAT("cat", "猫"),
		/**
		 * 狗
		 */
		DOG("dog", "狗"),
		/**
		 * 鸟
		 */
		BIRD("bird", "鸟");

		/**
		 * 键
		 */
		private final String key;
		/**
		 * 值
		 */
		private final String value;
	}

	/**
	 * 动物园里的动物类型
	 */
	@Getter
	@ToString
	@AllArgsConstructor
	public enum AnimalKindInZoo {

		/**
		 * 猫
		 */
		CAT("cat", "猫", AnimalKind.CAT),
		/**
		 * 蛇
		 */
		SNAKE("snake", "蛇", null),
		/**
		 * 鸟
		 */
		BIRD("bird", "鸟", AnimalKind.BIRD);

		/**
		 * 键
		 */
		private final String key;
		/**
		 * 值
		 */
		private final String value;
		/**
		 * 映射值
		 */
		private final AnimalKind mappedValue;
	}
}
