/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json;

import lombok.Data;
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
