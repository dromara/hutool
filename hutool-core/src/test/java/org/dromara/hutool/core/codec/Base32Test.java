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

import org.dromara.hutool.core.codec.binary.Base32;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Base32Test {

	@Test
	public void encodeAndDecodeTest(){
		final String a = "伦家是一个非常长的字符串";
		final String encode = Base32.encode(a);
		Assertions.assertEquals("4S6KNZNOW3TJRL7EXCAOJOFK5GOZ5ZNYXDUZLP7HTKCOLLMX46WKNZFYWI======", encode);

		String decodeStr = Base32.decodeStr(encode);
		Assertions.assertEquals(a, decodeStr);

		// 支持小写模式解码
		decodeStr = Base32.decodeStr(encode.toLowerCase());
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void hexEncodeAndDecodeTest(){
		final String a = "伦家是一个非常长的字符串";
		final String encode = Base32.encodeHex(ByteUtil.toUtf8Bytes(a));
		Assertions.assertEquals("SIUADPDEMRJ9HBV4N20E9E5AT6EPTPDON3KPBFV7JA2EBBCNSUMADP5OM8======", encode);

		String decodeStr = Base32.decodeStrHex(encode);
		Assertions.assertEquals(a, decodeStr);

		// 支持小写模式解码
		decodeStr = Base32.decodeStrHex(encode.toLowerCase());
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void encodeAndDecodeRandomTest(){
		final String a = RandomUtil.randomStringLower(RandomUtil.randomInt(1000));
		final String encode = Base32.encode(a);
		final String decodeStr = Base32.decodeStr(encode);
		Assertions.assertEquals(a, decodeStr);
	}

	@Test
	public void decodeTest(){
		final String a = "伦家是一个非常长的字符串";
		final String decodeStr = Base32.decodeStr("4S6KNZNOW3TJRL7EXCAOJOFK5GOZ5ZNYXDUZLP7HTKCOLLMX46WKNZFYWI");
		Assertions.assertEquals(a, decodeStr);
	}
}
