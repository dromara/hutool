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

package org.dromara.hutool.core.compress;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class IssueI5DRU0Test {

	@Test
	@Disabled
	public void appendTest(){
		// https://gitee.com/dromara/hutool/issues/I5DRU0
		// 向zip中添加文件的时候，如果添加的文件的父目录已经存在，会报错。实际中目录存在忽略即可。
		ZipUtil.append(Paths.get("d:/test/zipTest.zip"), Paths.get("d:/test/zipTest"), StandardCopyOption.REPLACE_EXISTING);
	}
}
