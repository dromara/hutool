/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io.file.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 删除操作的FileVisitor实现，用于递归遍历删除文件夹
 *
 * @author looly
 * @since 5.5.1
 */
public class DelVisitor extends SimpleFileVisitor<Path> {

	/**
	 * 单例对象
	 */
	public static DelVisitor INSTANCE = new DelVisitor();

	@Override
	public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
		Files.delete(file);
		return FileVisitResult.CONTINUE;
	}

	/**
	 * 访问目录结束后删除目录，当执行此方法时，子文件或目录都已访问（删除）完毕<br>
	 * 理论上当执行到此方法时，目录下已经被清空了
	 *
	 * @param dir 目录
	 * @param e   异常
	 * @return {@link FileVisitResult}
	 * @throws IOException IO异常
	 */
	@Override
	public FileVisitResult postVisitDirectory(final Path dir, final IOException e) throws IOException {
		if (e == null) {
			Files.delete(dir);
			return FileVisitResult.CONTINUE;
		} else {
			throw e;
		}
	}
}
