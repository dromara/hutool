package cn.hutool.core.bean.copier;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BeanCopierTest {

	/**
	 * 测试在非覆盖模式下，目标对象有值则不覆盖
	 */
	@Test
	public void beanToBeanNotOverrideTest() {
		final A a = new A();
		a.setValue("123");
		final B b = new B();
		b.setValue("abc");

		final BeanCopier<B> copier = BeanCopier.create(a, b, CopyOptions.create().setOverride(false));
		copier.copy();

		Assertions.assertEquals("abc", b.getValue());
	}

	/**
	 * 测试在覆盖模式下，目标对象值被覆盖
	 */
	@Test
	public void beanToBeanOverrideTest() {
		final A a = new A();
		a.setValue("123");
		final B b = new B();
		b.setValue("abc");

		final BeanCopier<B> copier = BeanCopier.create(a, b, CopyOptions.create());
		copier.copy();

		Assertions.assertEquals("123", b.getValue());
	}

	@Data
	private static class A {
		private String value;
	}

	@Data
	private static class B {
		private String value;
	}
}
