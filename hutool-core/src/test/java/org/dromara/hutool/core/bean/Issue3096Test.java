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
