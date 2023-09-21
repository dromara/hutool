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

import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.map.MapUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BeanCopyMappingTest {

	/**
	 * https://gitee.com/dromara/hutool/issues/I4C48U <br>
	 * 传递复制不要用注解别名，应该用动态映射
	 */
	@Test
	public void copyPropertiesTest() {
		final CopyOptions copyOptions = CopyOptions.of()
				.setFieldMapping(MapUtil.of("car", "carNo"));


		final B b = B.builder().car("12312312").build();
		final A a = A.builder().build();
		final C c = C.builder().build();
		BeanUtil.copyProperties(b, a, copyOptions);
		BeanUtil.copyProperties(a, c);

		Assertions.assertEquals("12312312", a.getCarNo());
		Assertions.assertEquals("12312312", c.getCarNo());
	}

	@Data
	@Builder
	public static class A {
		private String carNo;
	}

	@Data
	@Builder
	public static class B {
		private String car;
	}

	@Data
	@Builder
	public static class C {
		private String carNo;
	}
}
