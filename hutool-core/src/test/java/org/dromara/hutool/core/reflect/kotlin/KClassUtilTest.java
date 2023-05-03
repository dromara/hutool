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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class KClassUtilTest {

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
}
