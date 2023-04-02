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

package org.dromara.hutool.compress;

import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.util.CharsetUtil;
import org.junit.jupiter.api.Test;

public class Issue3018Test {
	@Test
	void unzipTest() {
		ZipUtil.unzip(FileUtil.getInputStream("d:/test/default.zip"),
			FileUtil.file("d:/test/"),
			CharsetUtil.UTF_8
			);
	}

	@Test
	void unzipFromFileTest() {
		ZipUtil.unzip("d:/test/default.zip");
	}
}
