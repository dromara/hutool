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
