/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.bean;

import lombok.Data;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 循环引用测试<br>
 * Hutool未对循环引用的对象做处理
 */
public class Issue3346Test {
	@Test
	@Disabled
	public void jsonWrapTest() {

		// 构建循环引用
		final TestBean1 testBean1 = new TestBean1();
		final TestBean2 testBean2 = new TestBean2();
		testBean1.testBean2 = testBean2;
		testBean2.testBean1 = testBean1;

		Console.log(BeanUtil.beanToMap(testBean1));
	}

	@Data
	public class TestBean1 {
		private TestBean2 testBean2;
	}

	@Data
	public class TestBean2 {
		private TestBean1 testBean1;
	}
}
