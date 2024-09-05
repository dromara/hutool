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

package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSystemUtilTest {

	@Test
	@Disabled
	public void listTest(){
		final FileSystem fileSystem = FileSystemUtil.createZip("d:/test/test.zip",
				CharsetUtil.GBK);
		final Path root = FileSystemUtil.getRoot(fileSystem);
		PathUtil.walkFiles(root, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) {
				Console.log(path);
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
