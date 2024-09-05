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

public class Issue3096Test {

	@Test
	void beanDescTest() {
		final BeanDesc desc = BeanUtil.getBeanDesc(User.class);

		// https://github.com/dromara/hutool/issues/3096
		// 新修改的规则中，isLastPage字段优先匹配setIsLastPage，这个顺序固定。
		// 只有setIsLastPage不存在时，才匹配setLastPage
		Assertions.assertEquals("setLastPage", desc.getSetter("lastPage").getName());
		Assertions.assertEquals("setIsLastPage", desc.getSetter("isLastPage").getName());
	}

	public static class User {
		private Boolean lastPage;
		private Boolean isLastPage;

		public Boolean getIsLastPage() {
			return this.isLastPage;
		}
		public void setIsLastPage(final Boolean isLastPage) {
			this.isLastPage = isLastPage;
		}

		public Boolean getLastPage() {
			return this.lastPage;
		}
		public void setLastPage(final Boolean lastPage) {
			this.lastPage = lastPage;
		}
	}
}
