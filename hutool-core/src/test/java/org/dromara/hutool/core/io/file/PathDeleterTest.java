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

import java.nio.file.Paths;

public class PathDeleterTest {
	@Test
	@Disabled
	public void delFileTest() {
		FileUtil.touch("d:/test/exist.txt");
		PathUtil.del(Paths.get("d:/test/exist.txt"));
	}

	@Test
	@Disabled
	public void delDirTest() {
		PathUtil.del(Paths.get("d:/test/dir1"));
	}

	@Test
	@Disabled
	public void cleanDirTest() {
		PathUtil.clean(Paths.get("d:/test/dir1"));
	}
}
