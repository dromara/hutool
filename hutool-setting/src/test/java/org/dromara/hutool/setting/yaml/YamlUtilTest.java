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
