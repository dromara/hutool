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

package cn.hutool.extra.compress;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class IssueI7PMJ0Test {

	@Test
	@Ignore
	public void createArchiverTest() {
		final File tarFile = FileUtil.file("d:/test/issueI7PMJ0.zip");
		CompressUtil.createArchiver(StandardCharsets.UTF_8, ArchiveStreamFactory.ZIP, tarFile)
			.add(FileUtil.file("d:/test/aaa.xml"), (file) -> !file.getName().equals("aaa.xml"))
			.finish().close();
	}
}
