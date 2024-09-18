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

package org.dromara.hutool.json;

import org.dromara.hutool.core.collection.ListUtil;
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
