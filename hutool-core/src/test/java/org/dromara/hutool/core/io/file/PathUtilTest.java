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

import org.dromara.hutool.core.array.ArrayUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PathUtilTest {

	@SuppressWarnings("DuplicateExpressions")
	@Test
	void ofTest() {
		// 绝对路径测试
		Path path = PathUtil.of(Paths.get("d:/test/hutool"), Paths.get("data1"), Paths.get("data2"));
		Assertions.assertEquals("d:/test/hutool/data1/data2", path.toString().replace('\\', '/'));

		// 相对路径测试
		path = PathUtil.of(Paths.get("hutool"), Paths.get("data1"), Paths.get("data2"));
		Assertions.assertEquals("hutool/data1/data2", path.toString().replace('\\', '/'));

		path = PathUtil.of(Paths.get("hutool"));
		Assertions.assertEquals("hutool", path.toString().replace('\\', '/'));

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
		Assertions.assertEquals("image/jpeg", mimeType);

		mimeType = PathUtil.getMimeType(Paths.get("d:/test/test.mov"));
		Assertions.assertEquals("video/quicktime", mimeType);
	}

	@Test
	public void getMimeOfRarTest(){
		final String contentType = FileUtil.getMimeType("a001.rar");
		Assertions.assertTrue(
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
		Assertions.assertEquals("application/x-7z-compressed", contentType);
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
		Assertions.assertEquals("image/jpeg", mimeType);
	}
}
