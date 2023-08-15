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

package org.dromara.hutool.core.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

public class IssueI7CRIWTest {

	@Test
	void getTypeArgumentsTest() {
		// 无法从继承获取泛型，则从接口获取
		Type type = TypeUtil.getTypeArgument(C.class);
		Assertions.assertEquals(type, String.class);

		// 继承和第一个接口都非泛型接口，则从找到的第一个泛型接口获取
		type = TypeUtil.getTypeArgument(D.class);
		Assertions.assertEquals(type, String.class);
	}

	static class A{

	}

	static class AT<T>{

	}

	interface Face1<T>{

	}

	interface Face2{

	}

	static class B extends A{

	}

	static class C extends A implements Face1<String>{

	}

	static class D extends A implements Face2, Face1<String>{

	}
}
