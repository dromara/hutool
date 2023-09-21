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

package org.dromara.hutool.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasicTypeTest {

	@Test
	public void wrapTest(){
		Assertions.assertEquals(Integer.class, BasicType.wrap(int.class));
		Assertions.assertEquals(Integer.class, BasicType.wrap(Integer.class));
		Assertions.assertEquals(String.class, BasicType.wrap(String.class));
		Assertions.assertNull(BasicType.wrap(null));
	}

	@Test
	public void unWrapTest(){
		Assertions.assertEquals(int.class, BasicType.unWrap(int.class));
		Assertions.assertEquals(int.class, BasicType.unWrap(Integer.class));
		Assertions.assertEquals(String.class, BasicType.unWrap(String.class));
		Assertions.assertNull(BasicType.unWrap(null));
	}

	@Test
	public void getPrimitiveSetTest(){
		Assertions.assertEquals(8, BasicType.getPrimitiveSet().size());
	}

	@Test
	public void getWrapperSetTest(){
		Assertions.assertEquals(8, BasicType.getWrapperSet().size());
	}
}
