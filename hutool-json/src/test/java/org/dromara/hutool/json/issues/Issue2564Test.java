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

package org.dromara.hutool.json.issues;

import lombok.Data;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue2564Test {

	/**
	 * 实力类 没有get set方法，不能被认为是一个bean
	 */
	@Test()
	public void emptyToBeanTest() {
		// 空对象转为一个空Bean，返回默认空实例
		String x = "{}" ;
		A a = JSONUtil.toBean(x, JSONConfig.of().setIgnoreError(true), A.class);
		Assertions.assertEquals(new A(), a);

		// 非空对象转为一个空bean，转换失败
		x = "{\"a\": 1}" ;
		a = JSONUtil.toBean(x, JSONConfig.of().setIgnoreError(true), A.class);
		Assertions.assertNull(a);
	}

	@Data
	public static class A {
	}
}
