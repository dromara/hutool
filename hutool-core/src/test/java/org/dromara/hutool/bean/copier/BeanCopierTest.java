package org.dromara.hutool.bean.copier;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class BeanCopierTest {

	@Test
	public void beanToMapIgnoreNullTest() {
		final A a = new A();

		HashMap<Object, Object> map = BeanCopier.of(a, new HashMap<>(), CopyOptions.of()).copy();
		Assertions.assertEquals(1, map.size());
		Assertions.assertTrue(map.containsKey("value"));
		Assertions.assertNull(map.get("value"));

		// 忽略null的情况下，空字段不写入map
		map = BeanCopier.of(a, new HashMap<>(), CopyOptions.of().ignoreNullValue()).copy();
		Assertions.assertFalse(map.containsKey("value"));
		Assertions.assertEquals(0, map.size());
	}

	/**
	 * 测试在非覆盖模式下，目标对象有值则不覆盖
	 */
	@Test
	public void beanToBeanNotOverrideTest() {
		final A a = new A();
		a.setValue("123");
		final B b = new B();
		b.setValue("abc");

		final BeanCopier<B> copier = BeanCopier.of(a, b, CopyOptions.of().setOverride(false));
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

		final BeanCopier<B> copier = BeanCopier.of(a, b, CopyOptions.of());
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

	/**
	 * 为{@code null}则写，否则忽略。如果覆盖，则不判断直接写
	 */
	@Test
	public void issues2484Test() {
		final A a = new A();
		a.setValue("abc");
		final B b = new B();
		b.setValue("123");

		BeanCopier<B> copier = BeanCopier.of(a, b, CopyOptions.of().setOverride(false));
		copier.copy();
		Assertions.assertEquals("123", b.getValue());

		b.setValue(null);
		copier = BeanCopier.of(a, b, CopyOptions.of().setOverride(false));
		copier.copy();
		Assertions.assertEquals("abc", b.getValue());
	}

}
