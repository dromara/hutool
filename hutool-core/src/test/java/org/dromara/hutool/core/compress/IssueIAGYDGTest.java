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
