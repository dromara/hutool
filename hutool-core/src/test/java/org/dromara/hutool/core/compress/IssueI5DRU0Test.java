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
