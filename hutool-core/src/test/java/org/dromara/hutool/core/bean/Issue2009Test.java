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

/**
 * https://github.com/dromara/hutool/issues/2009
 */
public class Issue2009Test {

	@SuppressWarnings("InnerClassMayBeStatic")
	public class BaseA {
		private String paPss;

		public String getPaPss() {
			return paPss;
		}

		public void setPaPss(final String paPss) {
			this.paPss = paPss;
		}
	}


	public class A extends BaseA {
		private String papss;

		public String getPapss() {
			return papss;
		}

		public void setPapss(final String papss) {
			this.papss = papss;
		}

		@Override
		public String toString() {
			return "A{" +
					"papss='" + papss + '\'' +
					'}';
		}
	}


	public class B extends BaseA {
		private String papss;

		public B(final String papss) {
			this.papss = papss;
		}

		public String getPapss() {
			return papss;
		}

		public void setPapss(final String papss) {
			this.papss = papss;
		}

		@Override
		public String toString() {
			return "B{" +
					"papss='" + papss + '\'' +
					'}';
		}
	}

	@Test
	public void test() {
		final B b = new B("a string text");
		final A a = new A();
		BeanUtil.copyProperties(b, a);

		Assertions.assertEquals(b.getPapss(), a.getPapss());
	}
}
