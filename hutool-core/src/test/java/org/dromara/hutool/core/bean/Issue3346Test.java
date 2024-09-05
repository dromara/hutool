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
