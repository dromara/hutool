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
				.add(FileUtil.file("d:/Java/apache-maven-3.8.1"), (f) -> {
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
				.add(FileUtil.file("d:/Java/apache-maven-3.8.1"), (f) -> {
					Console.log("Add: {}", f.getPath());
					return true;
				})
				.finish().close();
	}
}
