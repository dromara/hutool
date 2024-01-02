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

package org.dromara.hutool.core.codec;

import org.dromara.hutool.core.codec.binary.Base62;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Base62单元测试
 *
 * @author looly
 *
 */
public class Base62Test {

	@Test
	public void encodeAndDecodeTest() {
		final String a = "伦家是一个非常长的字符串66";
		final String encode = Base62.encode(a);
		Assertions.assertEquals("17vKU8W4JMG8dQF8lk9VNnkdMOeWn4rJMva6F0XsLrrT53iKBnqo", encode);

		final String decodeStr = Base62.decodeStr(encode);
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeInvertedTest() {
		final String a = "伦家是一个非常长的字符串66";
		final String encode = Base62.encodeInverted(a);
		Assertions.assertEquals("17Vku8w4jmg8Dqf8LK9vnNKDmoEwN4RjmVA6f0xSlRRt53IkbNQO", encode);

		final String decodeStr = Base62.decodeStrInverted(encode);
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeRandomTest() {
		final String a = RandomUtil.randomStringLower(RandomUtil.randomInt(1000));
		final String encode = Base62.encode(a);
		final String decodeStr = Base62.decodeStr(encode);
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeInvertedRandomTest() {
		final String a = RandomUtil.randomStringLower(RandomUtil.randomInt(1000));
		final String encode = Base62.encodeInverted(a);
		final String decodeStr = Base62.decodeStrInverted(encode);
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	void encodeNumbersTest() {
		final String encode = Base62.encode("181338494");
		Assertions.assertEquals("HRmWh8NiFvYi", encode);
	}
}
