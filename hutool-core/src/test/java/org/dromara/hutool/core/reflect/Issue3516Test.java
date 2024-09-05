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
import java.util.function.Function;

public class Issue3516Test {

	@Test
	public void getTypeArgumentTest() {
		final Type typeArgument = TypeUtil.getTypeArgument(Demo.class, 0);
		Assertions.assertEquals(B.class, typeArgument);
	}

	static class Demo implements A2B{
		@Override
		public A apply(final B b) {
			return new A();
		}
	}

	static class A {
		private String name;
	}

	static class B {
		private String name;
	}

	interface A2B extends Function<B, A> {
	}
}
