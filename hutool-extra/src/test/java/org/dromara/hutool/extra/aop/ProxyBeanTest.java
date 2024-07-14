/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.aop;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.exception.HutoolException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ProxyBeanTest {

	@Test
	void proxyTest() {
		final IBean bean = ProxyUtil.newProxyInstance((proxy, method, args) -> {
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
