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

package org.dromara.hutool.extra.compress;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.extra.compress.archiver.StreamArchiver;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

@SuppressWarnings("resource")
public class ArchiverTest {

	@Test
	@Disabled
	public void zipTest() {
		final File file = FileUtil.file("d:/test/compress/test.zip");
		StreamArchiver.of(CharsetUtil.UTF_8, ArchiveStreamFactory.ZIP, file)
				.add(FileUtil.file("d:/Java"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Disabled
	public void tarTest() {
		final File file = FileUtil.file("d:/test/compress/test.tar");
		StreamArchiver.of(CharsetUtil.UTF_8, ArchiveStreamFactory.TAR, file)
				.add(FileUtil.file("d:/Java"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Disabled
	public void cpioTest() {
		final File file = FileUtil.file("d:/test/compress/test.cpio");
		StreamArchiver.of(CharsetUtil.UTF_8, ArchiveStreamFactory.CPIO, file)
				.add(FileUtil.file("d:/Java"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Disabled
	public void sevenZTest() {
		final File file = FileUtil.file("d:/test/compress/test.7z");
		CompressUtil.createArchiver(CharsetUtil.UTF_8, ArchiveStreamFactory.SEVEN_Z, file)
				.add(FileUtil.file("d:/Java/mina-maven-3.8.1"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}

	@Test
	@Disabled
	public void tgzTest() {
		final File file = FileUtil.file("d:/test/compress/test.tgz");
		CompressUtil.createArchiver(CharsetUtil.UTF_8, "tgz", file)
				.add(FileUtil.file("d:/Java/mina-maven-3.8.1"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}
}
