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
