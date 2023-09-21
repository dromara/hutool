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

package org.dromara.hutool.core.bean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BeanWithReturnThisTest {

	@Test
	public void setValueTest() {
		final BeanWithRetuenThis bean = new BeanWithRetuenThis();
		final BeanDesc beanDesc = BeanUtil.getBeanDesc(BeanWithRetuenThis.class);
		final PropDesc prop = beanDesc.getProp("a");
		prop.setValue(bean, "123");

		Assertions.assertEquals("123", bean.getA());
	}

	static class BeanWithRetuenThis{
		public String getA() {
			return a;
		}

		public BeanWithRetuenThis setA(final String a) {
			this.a = a;
			return this;
		}

		private String a;
	}
}
