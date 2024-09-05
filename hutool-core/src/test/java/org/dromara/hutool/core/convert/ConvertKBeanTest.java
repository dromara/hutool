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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.reflect.kotlin.TestKBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class ConvertKBeanTest {

	@Test
	void mapToBeanTest(){
		final HashMap<String, Object> map = new HashMap<>();
		map.put("country", "中国");
		map.put("age", 18);
		map.put("id", "VampireAchao");

		final TestKBean testKBean = ConvertUtil.convert(TestKBean.class, map);

		Assertions.assertEquals("VampireAchao", testKBean.getId());
		Assertions.assertEquals("中国", testKBean.getCountry());
		Assertions.assertNull(testKBean.getName());
	}
}
