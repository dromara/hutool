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

package org.dromara.hutool.setting.yaml;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class YamlUtilTest {

	@Test
	public void loadByPathTest() {
		final Dict result = YamlUtil.loadByPath("test.yaml");

		Assertions.assertEquals("John", result.getStr("firstName"));

		final List<Integer> numbers = result.getByPath("contactDetails.number");
		Assertions.assertEquals(123456789, (int) numbers.get(0));
		Assertions.assertEquals(456786868, (int) numbers.get(1));
	}

	@Test
	@Disabled
	public void dumpTest() {
		final Dict dict = Dict.of()
				.set("name", "hutool")
				.set("count", 1000);

		YamlUtil.dump(
				dict
				, FileUtil.getWriter("d:/test/dump.yaml", CharsetUtil.UTF_8, false));
	}
}
