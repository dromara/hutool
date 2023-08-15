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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.util.EnumUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * EnumUtil单元测试
 *
 * @author looly
 *
 */
public class EnumUtilTest {

	@Test
	public void getNamesTest() {
		final List<String> names = EnumUtil.getNames(TestEnum.class);
		Assertions.assertEquals(ListUtil.of("TEST1", "TEST2", "TEST3"), names);
	}

	@Test
	public void getFieldValuesTest() {
		final List<Object> types = EnumUtil.getFieldValues(TestEnum.class, "type");
		Assertions.assertEquals(ListUtil.of("type1", "type2", "type3"), types);
	}

	@Test
	public void getFieldNamesTest() {
		final List<String> names = EnumUtil.getFieldNames(TestEnum.class);
		Assertions.assertTrue(names.contains("type"));
		Assertions.assertTrue(names.contains("name"));
	}

	@Test
	public void getByTest() {
		// 枚举中字段互相映射使用
		final TestEnum testEnum = EnumUtil.getBy(TestEnum::ordinal, 1);
		Assertions.assertEquals("TEST2", testEnum.name());
	}

	@Test
	public void getFieldByTest() {
		// 枚举中字段互相映射使用
		final String type = EnumUtil.getFieldBy(TestEnum::getType, Enum::ordinal, 1);
		Assertions.assertEquals("type2", type);

		final int ordinal = EnumUtil.getFieldBy(TestEnum::ordinal, Enum::ordinal, 1);
		Assertions.assertEquals(1, ordinal);
	}

	@Test
	public void likeValueOfTest() {
		final TestEnum value = EnumUtil.likeValueOf(TestEnum.class, "type2");
		Assertions.assertEquals(TestEnum.TEST2, value);
	}

	@Test
	public void getEnumMapTest() {
		final Map<String,TestEnum> enumMap = EnumUtil.getEnumMap(TestEnum.class);
		Assertions.assertEquals(TestEnum.TEST1, enumMap.get("TEST1"));
	}

	@Test
	public void getNameFieldMapTest() {
		final Map<String, Object> enumMap = EnumUtil.getNameFieldMap(TestEnum.class, "type");
		assert enumMap != null;
		Assertions.assertEquals("type1", enumMap.get("TEST1"));
	}

	public enum TestEnum{
		TEST1("type1"), TEST2("type2"), TEST3("type3");

		TestEnum(final String type) {
			this.type = type;
		}

		private final String type;
		@SuppressWarnings("unused")
		private String name;

		public String getType() {
			return this.type;
		}

		public String getName() {
			return this.name;
		}
	}
}
