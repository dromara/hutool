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

import lombok.Data;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Issue2223Test {

	@Test
	public void toStrOrderTest() {
		final Map<String, Long> m1 = new LinkedHashMap<>();
		for (long i = 0; i < 5; i++) {
			m1.put("2022/" + i, i);
		}

		Assertions.assertEquals("{\"2022/0\":0,\"2022/1\":1,\"2022/2\":2,\"2022/3\":3,\"2022/4\":4}", JSONUtil.toJsonStr(m1));

		final Map<String, Map<String, Long>> map1 = new HashMap<>();
		map1.put("m1", m1);

		Assertions.assertEquals("{\"m1\":{\"2022/0\":0,\"2022/1\":1,\"2022/2\":2,\"2022/3\":3,\"2022/4\":4}}",
				JSONUtil.toJsonStr(map1, JSONConfig.of()));

		final BeanDemo beanDemo = new BeanDemo();
		beanDemo.setMap1(map1);
		Assertions.assertEquals("{\"map1\":{\"m1\":{\"2022/0\":0,\"2022/1\":1,\"2022/2\":2,\"2022/3\":3,\"2022/4\":4}}}",
				JSONUtil.toJsonStr(beanDemo, JSONConfig.of()));
	}

	@Data
	static class BeanDemo {
		Map<String, Map<String, Long>> map1;
	}
}
