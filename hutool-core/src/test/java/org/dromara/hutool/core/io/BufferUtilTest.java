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
