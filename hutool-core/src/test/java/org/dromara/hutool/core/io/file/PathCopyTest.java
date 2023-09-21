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

package org.dromara.hutool.core.io.file;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件或目录拷贝测试
 */
public class PathCopyTest {

	@Test
	@Disabled
	public void copySameFileTest() {
		final Path path = Paths.get("d:/test/dir1/test1.txt");
		//src路径和target路径相同时，不执行操作
		PathUtil.copy(
				path,
				path);
	}

	@Test
	@Disabled
	public void copySameDirTest() {
		final Path path = Paths.get("d:/test/dir1");
		//src路径和target路径相同时，不执行操作
		PathUtil.copyContent(
				path,
				path);
	}

	@Test
	@Disabled
	public void copyFileToDirTest() {
		// src为文件，target为已存在目录，则拷贝到目录下，文件名不变。
		PathUtil.copy(
				Paths.get("d:/test/dir1/test1.txt"),
				Paths.get("d:/test/dir2"));
	}

	@Test
	@Disabled
	public void copyFileToPathNotExistTest() {
		// src为文件，target为不存在路径，则目标以文件对待（自动创建父级目录）
		// 相当于拷贝后重命名
		PathUtil.copy(
				Paths.get("d:/test/dir1/test1.txt"),
				Paths.get("d:/test/test2"));
	}

	@Test
	@Disabled
	public void copyFileToFileTest() {
		//src为文件，target是一个已存在的文件，则当{@link CopyOption}设为覆盖时会被覆盖，默认不覆盖。
		PathUtil.copy(
				Paths.get("d:/test/dir1/test1.txt"),
				Paths.get("d:/test/test2"));
	}

	@Test
	@Disabled
	public void copyDirToDirTest() {
		//src为目录，target为已存在目录，整个src目录连同其目录拷贝到目标目录中
		PathUtil.copy(
				Paths.get("d:/test/dir1/"),
				Paths.get("d:/test/dir2"));
	}

	@Test
	@Disabled
	public void copyDirToPathNotExistTest() {
		//src为目录，target为不存在路径，则自动创建目标为新目录，整个src目录连同其目录拷贝到目标目录中
		PathUtil.copy(
				Paths.get("d:/test/dir1/"),
				Paths.get("d:/test/dir3"));
	}

	@Test
	@Disabled
	public void copyDirToFileTest() {
		//src为目录，target为文件，抛出IllegalArgumentException
		PathUtil.copy(
				Paths.get("d:/test/dir1/"),
				Paths.get("d:/test/exist.txt"));
	}
}
