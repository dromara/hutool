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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MorseTest {

	private final Morse morseCoder = new Morse();

	@Test
	public void test0() {
		final String text = "Hello World!";
		final String morse = "...././.-../.-../---/-...../.--/---/.-./.-../-../-.-.--/";
		Assertions.assertEquals(morse, morseCoder.encode(text));
		Assertions.assertEquals(morseCoder.decode(morse), text.toUpperCase());
	}

	@Test
	public void test1() {
		final String text = "你好，世界！";
		final String morse = "-..----.--...../-.--..-.-----.-/--------....--../-..---....-.--./---.-.-.-..--../--------.......-/";
		Assertions.assertEquals(morseCoder.encode(text), morse);
		Assertions.assertEquals(morseCoder.decode(morse), text);
	}

	@Test
	public void test2() {
		final String text = "こんにちは";
		final String morse = "--.....-.-..--/--....-..-..--/--.....--.-.--/--.....--....-/--.....--.----/";
		Assertions.assertEquals(morseCoder.encode(text), morse);
		Assertions.assertEquals(morseCoder.decode(morse), text);
	}
}
