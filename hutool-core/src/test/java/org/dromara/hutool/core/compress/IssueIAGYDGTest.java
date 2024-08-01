/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.compress;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * https://gitee.com/dromara/hutool/issues/IAGYDG
 */
public class IssueIAGYDGTest {
	@Test
	@Disabled
	void zipTest() {
		// 第一次压缩后，IssueIAGYDG.zip也会作为文件压缩到IssueIAGYDG.zip中，导致死循环
		final File filea = new File("d:/test/");
		final File fileb = new File("d:/test/IssueIAGYDG.zip");
		ZipUtil.zip(fileb, false, filea.listFiles());
	}
}
