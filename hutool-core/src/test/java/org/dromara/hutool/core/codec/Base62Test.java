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
