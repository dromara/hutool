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

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.io.stream.EmptyOutputStream;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NioUtilTest {
	@Test
	public void copyByNIOTest() {
		final File file = FileUtil.file("hutool.jpg");
		final long size = NioUtil.copyByNIO(ResourceUtil.getStream("hutool.jpg"), EmptyOutputStream.INSTANCE, NioUtil.DEFAULT_MIDDLE_BUFFER_SIZE, null);

		// 确认写出
		Assertions.assertEquals(file.length(), size);
		Assertions.assertEquals(22807, size);
	}

	@Test
	@Disabled
	public void copyByNIOTest2() {
		final File file = FileUtil.file("d:/test/logo.jpg");
		final BufferedInputStream in = FileUtil.getInputStream(file);
		final BufferedOutputStream out = FileUtil.getOutputStream("d:/test/2logo.jpg");

		final long copySize = IoUtil.copyByNIO(in, out, NioUtil.DEFAULT_BUFFER_SIZE, new StreamProgress() {
			@Override
			public void start() {
				Console.log("start");
			}

			@Override
			public void progress(final long total, final long progressSize) {
				Console.log("{} {}", total, progressSize);
			}

			@Override
			public void finish() {
				Console.log("finish");
			}
		});
		Assertions.assertEquals(file.length(), copySize);
	}

	@Test
	public void readUtf8Test() throws IOException {
		final String s = NioUtil.readUtf8(FileChannel.open(FileUtil.file("text.txt").toPath()));
		Assertions.assertTrue(StrUtil.isNotBlank(s));
	}
}
