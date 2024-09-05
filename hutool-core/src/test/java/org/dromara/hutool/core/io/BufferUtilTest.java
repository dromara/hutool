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

package org.dromara.hutool.core.io;

import java.nio.ByteBuffer;

import org.dromara.hutool.core.io.buffer.BufferUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * BufferUtil单元测试
 *
 * @author looly
 *
 */
public class BufferUtilTest {

	@Test
	public void copyTest() {
		final byte[] bytes = "AAABBB".getBytes();
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);

		final ByteBuffer buffer2 = BufferUtil.copy(buffer, ByteBuffer.allocate(5));
		Assertions.assertEquals("AAABB", StrUtil.utf8Str(buffer2));
	}

	@Test
	public void readBytesTest() {
		final byte[] bytes = "AAABBB".getBytes();
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);

		final byte[] bs = BufferUtil.readBytes(buffer, 5);
		Assertions.assertEquals("AAABB", StrUtil.utf8Str(bs));
	}

	@Test
	public void readBytes2Test() {
		final byte[] bytes = "AAABBB".getBytes();
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);

		final byte[] bs = BufferUtil.readBytes(buffer, 5);
		Assertions.assertEquals("AAABB", StrUtil.utf8Str(bs));
	}

	@Test
	public void readLineTest() {
		final String text = "aa\r\nbbb\ncc";
		final ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());

		// 第一行
		String line = BufferUtil.readLine(buffer, CharsetUtil.UTF_8);
		Assertions.assertEquals("aa", line);

		// 第二行
		line = BufferUtil.readLine(buffer, CharsetUtil.UTF_8);
		Assertions.assertEquals("bbb", line);

		// 第三行因为没有行结束标志，因此返回null
		line = BufferUtil.readLine(buffer, CharsetUtil.UTF_8);
		Assertions.assertNull(line);

		// 读取剩余部分
		Assertions.assertEquals("cc", StrUtil.utf8Str(BufferUtil.readBytes(buffer)));
	}
}
