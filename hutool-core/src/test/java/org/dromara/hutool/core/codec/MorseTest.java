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
