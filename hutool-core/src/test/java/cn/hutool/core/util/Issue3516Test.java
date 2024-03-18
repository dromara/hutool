package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

/**
 * Description: https://github.com/dromara/hutool/issues/3516
 * Create by 张文涛 on 2024/3/18 15:51:59
 */
public class Issue3516Test {

	@Test
	public void getTypeArgumentTest() {
		// 获取继承接口泛型参数
		Class<?> typeArgument = ClassUtil.getTypeArgument(Demo.class, 0);
		Assert.assertEquals(typeArgument, B.class);
	}

	public class Demo implements A2B {
		@Override
		public A apply(B arg0) {
			A a = new A();
			return a;
		}
	}

	class A {
		private String name;
	}

	class B {
		private String name;
	}

	interface A2B extends Function<B, A> {
	}
}
