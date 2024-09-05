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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.reflect.JdkProxyUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ProxyBeanTest {

	@Test
	void noFieldBeanTest() {
		final NoFieldBean noFieldBean = new NoFieldBean();
		final Map<String, Object> map = BeanUtil.beanToMap(noFieldBean);
		Assertions.assertEquals(2, map.size());
		Assertions.assertEquals("hutool", map.get("name"));
		Assertions.assertEquals(1, map.get("age"));
	}

	static class NoFieldBean{
		public String getName(){
			return "hutool";
		}

		public int getAge(){
			return 1;
		}
	}

	@Test
	void proxyTest() {
		final IBean bean = JdkProxyUtil.newProxyInstance((proxy, method, args) -> {
			final String name = method.getName();
			switch (name){
				case "getName":
					return "hutool";
				case "setName":
				case "setAge":
					return null;
				case "getAge":
					return 1;
			}
			throw new HutoolException("No method name: " + name);
		}, IBean.class);

		// 测试代理类的Bean拷贝
		final Map<String, Object> map = BeanUtil.beanToMap(bean);
		Assertions.assertEquals(2, map.size());
		Assertions.assertEquals("hutool", map.get("name"));
		Assertions.assertEquals(1, map.get("age"));
	}

	interface IBean{
		String getName();
		IBean setName(String name);

		int getAge();
		IBean setAge(int age);
	}
}
