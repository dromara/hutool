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

import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI71BE6Test {

	@Test
	void toArrayTest() {
		final String jsonStr = "[50.0,50.0,50.0,50.0]";

		final Double[] doubles = (Double[]) JSONUtil.parseArray(jsonStr).toArray(Double[].class);
		Assertions.assertArrayEquals(new Double[]{50.0,50.0,50.0,50.0}, doubles);
	}

	@Test
	void toBeanTest() {
		final String jsonStr = "[50.0,50.0,50.0,50.0]";

		final Double[] doubles = JSONUtil.toBean(jsonStr, Double[].class);
		Assertions.assertArrayEquals(new Double[]{50.0,50.0,50.0,50.0}, doubles);
	}
}
