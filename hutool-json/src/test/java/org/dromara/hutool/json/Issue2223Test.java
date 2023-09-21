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

package org.dromara.hutool.json;

import lombok.Data;
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
