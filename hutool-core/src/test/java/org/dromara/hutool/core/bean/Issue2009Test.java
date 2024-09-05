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
