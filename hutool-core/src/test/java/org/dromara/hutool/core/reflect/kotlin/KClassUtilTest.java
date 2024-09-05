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

package org.dromara.hutool.core.reflect.kotlin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
