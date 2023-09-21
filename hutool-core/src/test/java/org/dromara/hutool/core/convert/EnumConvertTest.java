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

package org.dromara.hutool.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Enum转换单元测试
 */
public class EnumConvertTest {

	@Test
	public void convertTest(){
		TestEnum bbb = Convert.convert(TestEnum.class, "BBB");
		Assertions.assertEquals(TestEnum.B, bbb);

		bbb = Convert.convert(TestEnum.class, 22);
		Assertions.assertEquals(TestEnum.B, bbb);
	}

	@Test
	public void toEnumTest(){
		TestEnum ccc = Convert.toEnum(TestEnum.class, "CCC");
		Assertions.assertEquals(TestEnum.C, ccc);

		ccc = Convert.toEnum(TestEnum.class, 33);
		Assertions.assertEquals(TestEnum.C, ccc);
	}

	enum TestEnum {
		A, B, C;

		public static TestEnum parse(final String str) {
			switch (str) {
				case "AAA":
					return A;
				case "BBB":
					return B;
				case "CCC":
					return C;
			}
			return null;
		}

		public static TestEnum parseByNumber(final int i) {
			switch (i) {
				case 11:
					return A;
				case 22:
					return B;
				case 33:
					return C;
			}
			return null;
		}
	}
}
