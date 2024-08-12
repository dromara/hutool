/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.io.file.visitor;

import org.dromara.hutool.core.io.file.PathUtil;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 文件移动操作的FileVisitor实现，用于递归遍历移动目录和文件，此类非线程安全<br>
 * 此类在遍历源目录并移动过程中会自动创建目标目录中不存在的上级目录。
 *
 * @author looly
 * @since 5.7.7
 */
public class MoveVisitor extends SimpleFileVisitor<Path> {

	private final Path source;
	private final Path target;
	private boolean isTargetCreated;
	private final CopyOption[] copyOptions;

	/**
	 * 构造
	 *
	 * @param source 源Path
	 * @param target 目标Path
	 * @param copyOptions 拷贝（移动）选项
	 */
	public MoveVisitor(final Path source, final Path target, final CopyOption... copyOptions) {
		if(PathUtil.exists(target, false) && !PathUtil.isDirectory(target)){
			throw new IllegalArgumentException("Target must be a directory");
		}
		this.source = source;
		this.target = target;
		this.copyOptions = copyOptions;
	}

	@Override
	public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
			throws IOException {
		initTarget();
		// 将当前目录相对于源路径转换为相对于目标路径
		final Path targetDir = target.resolve(source.relativize(dir));
		if(!Files.exists(targetDir)){
			Files.createDirectories(targetDir);
		} else if(!Files.isDirectory(targetDir)){
			throw new FileAlreadyExistsException(targetDir.toString());
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
			throws IOException {
		initTarget();
		Files.move(file, target.resolve(source.relativize(file)), copyOptions);
		return FileVisitResult.CONTINUE;
	}

	/**
	 * 初始化目标文件或目录
	 */
	private void initTarget(){
		if(!this.isTargetCreated){
			PathUtil.mkdir(this.target);
			this.isTargetCreated = true;
		}
	}
}
