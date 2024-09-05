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

import org.dromara.hutool.core.array.ArrayUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

public class PathUtilTest {

	@SuppressWarnings("DuplicateExpressions")
	@Test
	void ofTest() {
		// 绝对路径测试
		Path path = PathUtil.of(Paths.get("d:/test/hutool"), Paths.get("data1"), Paths.get("data2"));
		assertEquals("d:/test/hutool/data1/data2", path.toString().replace('\\', '/'));

		// 相对路径测试
		path = PathUtil.of(Paths.get("hutool"), Paths.get("data1"), Paths.get("data2"));
		assertEquals("hutool/data1/data2", path.toString().replace('\\', '/'));

		path = PathUtil.of(Paths.get("hutool"));
		assertEquals("hutool", path.toString().replace('\\', '/'));

		path = PathUtil.of((Path) null);
		Assertions.assertNull(path);
	}

	@Test
	@Disabled
	public void copyFileTest(){
		PathUtil.copy(
				Paths.get("d:/test/1595232240113.jpg"),
				Paths.get("d:/test/1595232240113_copy.jpg"),
				StandardCopyOption.COPY_ATTRIBUTES,
				StandardCopyOption.REPLACE_EXISTING
				);
	}

	@Test
	@Disabled
	public void copyTest(){
		PathUtil.copy(
				Paths.get("d:/Red2_LYY"),
				Paths.get("d:/test/aaa/aaa.txt")
		);
	}

	@Test
	@Disabled
	public void copyContentTest(){
		PathUtil.copyContent(
				Paths.get("d:/Red2_LYY"),
				Paths.get("d:/test/aaa/")
		);
	}

	@Test
	@Disabled
	public void moveTest(){
		PathUtil.move(Paths.get("d:/lombok.jar"), Paths.get("d:/test/"), false);
	}

	@Test
	@Disabled
	public void moveDirTest(){
		PathUtil.move(Paths.get("c:\\aaa"), Paths.get("d:/test/looly"), false);
	}

	@Test
	@Disabled
	public void delDirTest(){
		PathUtil.del(Paths.get("d:/test/looly"));
	}

	@Test
	@Disabled
	public void getMimeTypeTest(){
		String mimeType = PathUtil.getMimeType(Paths.get("d:/test/test.jpg"));
		assertEquals("image/jpeg", mimeType);

		mimeType = PathUtil.getMimeType(Paths.get("d:/test/test.mov"));
		assertEquals("video/quicktime", mimeType);
	}

	@Test
	public void getMimeOfRarTest(){
		final String contentType = FileUtil.getMimeType("a001.rar");
		assertTrue(
			ArrayUtil.contains(
				new String[]{
					"application/x-rar-compressed",
					// JDK9+修改为此
					"application/vnd.rar"},
				contentType));
	}

	@Test
	public void getMimeOf7zTest(){
		final String contentType = FileUtil.getMimeType("a001.7z");
		assertEquals("application/x-7z-compressed", contentType);
	}

	/**
	 * issue#2893 target不存在空导致异常
	 */
	@Test
	@Disabled
	public void moveTest2(){
		PathUtil.move(Paths.get("D:\\project\\test1.txt"), Paths.get("D:\\project\\test2.txt"), false);
	}

	@Test
	public void issue3179Test() {
		final String mimeType = PathUtil.getMimeType(Paths.get("xxxx.jpg"));
		assertEquals("image/jpeg", mimeType);
	}

	@Test
	public void equalsTest() {
		// 源文件和目标文件都不存在
		final File srcFile = FileUtil.file("d:/hutool.jpg");
		final File destFile = FileUtil.file("d:/hutool.jpg");

		final boolean equals = PathUtil.equals(srcFile.toPath(), destFile.toPath());
		assertTrue(equals);

		// 源文件存在，目标文件不存在
		final File srcFile1 = FileUtil.file("hutool.jpg");
		final File destFile1 = FileUtil.file("d:/hutool.jpg");

		final boolean notEquals = PathUtil.equals(srcFile1.toPath(), destFile1.toPath());
		assertFalse(notEquals);
	}

	@Test
	@Disabled
	void isSameFileTest() {
		// 源文件和目标文件都不存在
		final File srcFile = FileUtil.file("f:/hutool.jpg");
		final File destFile = FileUtil.file("f:/hutool.jpg");

		assertTrue(PathUtil.isSameFile(srcFile.toPath(), destFile.toPath()));
	}
}
