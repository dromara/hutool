package org.dromara.hutool;

import org.dromara.hutool.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 测试Bean中嵌套List等对象时是否完整转换<br>
 * 同时测试私有class是否可以有效实例化
 *
 * @author looly
 *
 */
public class ParseBeanTest {

	@Test
	public void parseBeanTest() {

		final C c1 = new C();
		c1.setTest("test1");
		final C c2 = new C();
		c2.setTest("test2");

		final B b1 = new B();
		b1.setCs(ListUtil.of(c1, c2));
		final B b2 = new B();
		b2.setCs(ListUtil.of(c1, c2));

		final A a = new A();
		a.setBs(ListUtil.of(b1, b2));

		final JSONObject json = JSONUtil.parseObj(a);
		final A a1 = JSONUtil.toBean(json, A.class);
		Assertions.assertEquals(json.toString(), JSONUtil.toJsonStr(a1));
	}

}

class A {

	private List<B> bs;

	public List<B> getBs() {
		return bs;
	}

	public void setBs(final List<B> bs) {
		this.bs = bs;
	}
}

class B {

	private List<C> cs;

	public List<C> getCs() {
		return cs;
	}

	public void setCs(final List<C> cs) {
		this.cs = cs;
	}
}

class C {
	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(final String test) {
		this.test = test;
	}
}
