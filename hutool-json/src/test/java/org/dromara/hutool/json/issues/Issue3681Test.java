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

package org.dromara.hutool.json.issues;

import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class Issue3681Test {
	@Test
	void toJsonStrOfOptionalTest() {
		String abc = JSONUtil.toJsonStr(Optional.of("abc"));
		Assertions.assertEquals("\"abc\"", abc);

		abc = JSONUtil.toJsonStr(Optional.of("123"));
		Assertions.assertEquals("123", abc);
	}

	@Test
	void toJsonStrOfOptionalTest2() {
		final String abc = JSONUtil.toJsonStr(Optional.of(MapUtil.of("a", 1)));
		Assertions.assertEquals("{\"a\":1}", abc);
	}

	@Test
	void toJsonStrOfOptTest() {
		String abc = JSONUtil.toJsonStr(Opt.of("abc"));
		Assertions.assertEquals("\"abc\"", abc);

		abc = JSONUtil.toJsonStr(Opt.of("123"));
		Assertions.assertEquals("123", abc);

	}
}
