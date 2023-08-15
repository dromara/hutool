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

		final TestKBean testKBean = Convert.convert(TestKBean.class, map);

		Assertions.assertEquals("VampireAchao", testKBean.getId());
		Assertions.assertEquals("中国", testKBean.getCountry());
		Assertions.assertNull(testKBean.getName());
	}
}
