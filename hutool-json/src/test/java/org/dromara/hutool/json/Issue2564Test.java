/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
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

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue2564Test {

	/**
	 * 实力类 没有get set方法，不能被认为是一个bean
	 */
	@Test()
	public void emptyToBeanTest(){
		final String x = "{}";
		final A a = JSONUtil.toBean(x, JSONConfig.of().setIgnoreError(true), A.class);
		Assertions.assertNull(a);
	}

	@Getter
	@Setter
	public static class A{
	}
}
