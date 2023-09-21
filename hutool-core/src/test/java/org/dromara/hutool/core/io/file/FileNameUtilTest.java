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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileNameUtilTest {
	@Test
	public void cleanInvalidTest(){
		String name = FileNameUtil.cleanInvalid("1\n2\n");
		Assertions.assertEquals("12", name);

		name = FileNameUtil.cleanInvalid("\r1\r\n2\n");
		Assertions.assertEquals("12", name);
	}

	@Test
	public void mainNameTest() {
		final String s = FileNameUtil.mainName("abc.tar.gz");
		Assertions.assertEquals("abc", s);
	}

	@Test
	public void normalizeTest() {
		Assertions.assertEquals("/foo/", FileNameUtil.normalize("/foo//"));
		Assertions.assertEquals("/foo/", FileNameUtil.normalize("/foo/./"));
		Assertions.assertEquals("/bar", FileNameUtil.normalize("/foo/../bar"));
		Assertions.assertEquals("/bar/", FileNameUtil.normalize("/foo/../bar/"));
		Assertions.assertEquals("/baz", FileNameUtil.normalize("/foo/../bar/../baz"));
		Assertions.assertEquals("/", FileNameUtil.normalize("/../"));
		Assertions.assertEquals("foo", FileNameUtil.normalize("foo/bar/.."));
		Assertions.assertEquals("../bar", FileNameUtil.normalize("foo/../../bar"));
		Assertions.assertEquals("bar", FileNameUtil.normalize("foo/../bar"));
		Assertions.assertEquals("/server/bar", FileNameUtil.normalize("//server/foo/../bar"));
		Assertions.assertEquals("/bar", FileNameUtil.normalize("//server/../bar"));
		Assertions.assertEquals("C:/bar", FileNameUtil.normalize("C:\\foo\\..\\bar"));
		//
		Assertions.assertEquals("C:/bar", FileNameUtil.normalize("C:\\..\\bar"));
		Assertions.assertEquals("../../bar", FileNameUtil.normalize("../../bar"));
		Assertions.assertEquals("C:/bar", FileNameUtil.normalize("/C:/bar"));
		Assertions.assertEquals("C:", FileNameUtil.normalize("C:"));

		// issue#3253，smb保留格式
		Assertions.assertEquals("\\\\192.168.1.1\\Share\\", FileNameUtil.normalize("\\\\192.168.1.1\\Share\\"));
	}

	@Test
	public void normalizeBlankTest() {
		Assertions.assertEquals("C:/aaa ", FileNameUtil.normalize("C:\\aaa "));
	}
}
