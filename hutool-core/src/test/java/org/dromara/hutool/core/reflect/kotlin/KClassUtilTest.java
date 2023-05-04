/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.reflect.kotlin;

import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.core.bean.copier.provider.MapValueProvider;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.map.MapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class KClassUtilTest {

	@Test
	void isKotlinClassTest() {
		boolean kotlinClass = KClassUtil.isKotlinClass(TestKBean.class);
		Assertions.assertTrue(kotlinClass);

		kotlinClass = KClassUtil.isKotlinClass(KClassUtilTest.class);
		Assertions.assertFalse(kotlinClass);
	}

	@Test
	void getConstructorTest() {
		final List<?> constructors = KClassUtil.getConstructors(TestKBean.class);
		Assertions.assertEquals(1, constructors.size());

		Assertions.assertEquals("kotlin.reflect.jvm.internal.KFunctionImpl",
			constructors.get(0).getClass().getName());
	}

	@Test
	void getParametersTest() {
		final List<?> constructors = KClassUtil.getConstructors(TestKBean.class);

		final List<KParameter> parameters = KClassUtil.getParameters(constructors.get(0));
		Assertions.assertEquals(3, parameters.size());

		Assertions.assertEquals("id", parameters.get(0).getName());
		Assertions.assertEquals("name", parameters.get(1).getName());
		Assertions.assertEquals("country", parameters.get(2).getName());
	}

	@Test
	void newInstanceTest() {
		final HashMap<String, Object> argsMap = new HashMap<>();
		argsMap.put("country", "中国");
		argsMap.put("age", 18);
		argsMap.put("id", "VampireAchao");

		final TestKBean testKBean = KClassUtil.newInstance(TestKBean.class, argsMap);

		Assertions.assertEquals("VampireAchao", testKBean.getId());
		Assertions.assertEquals("中国", testKBean.getCountry());
		Assertions.assertNull(testKBean.getName());
	}
}
