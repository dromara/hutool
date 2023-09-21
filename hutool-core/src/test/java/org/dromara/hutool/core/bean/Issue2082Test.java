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

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * https://github.com/dromara/hutool/issues/2082<br>
 * 当setXXX有重载方法的时候，BeanDesc中会匹配到重载方法，增加类型检查来规避之
 */
public class Issue2082Test {

	@Test
	public void toBeanTest() {
		final TestBean2 testBean2 = new TestBean2();
		final TestBean test = BeanUtil.toBean(testBean2, TestBean.class);
		Assertions.assertNull(test.getId());
	}

	@Data
	static class TestBean {
		private Long id;

		public void setId(final String id) {
			this.id = Long.valueOf(id);
		}
	}

	@Data
	static class TestBean2 {
		private String id;
	}
}
