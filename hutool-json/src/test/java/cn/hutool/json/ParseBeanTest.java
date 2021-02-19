package cn.hutool.json;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.collection.CollUtil;

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

		C c1 = new C();
		c1.setTest("test1");
		C c2 = new C();
		c2.setTest("test2");
		
		B b1 = new B();
		b1.setCs(CollUtil.newArrayList(c1, c2));
		B b2 = new B();
		b2.setCs(CollUtil.newArrayList(c1, c2));

		A a = new A();
		a.setBs(CollUtil.newArrayList(b1, b2));

		JSONObject json = JSONUtil.parseObj(a);
		A a1 = JSONUtil.toBean(json, A.class);
		Assert.assertEquals(json.toString(), JSONUtil.toJsonStr(a1));
	}

}

class A {

	private List<B> bs;

	public List<B> getBs() {
		return bs;
	}

	public void setBs(List<B> bs) {
		this.bs = bs;
	}
}

class B {

	private List<C> cs;

	public List<C> getCs() {
		return cs;
	}

	public void setCs(List<C> cs) {
		this.cs = cs;
	}
}

class C {
	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}
