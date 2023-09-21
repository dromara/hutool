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

package org.dromara.hutool.extra.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.NotBlank;

/**
 * java bean 校验工具类单元测试
 *
 * @author chengqiang
 */
public class BeanValidatorUtilTest {

	public static class TestClass {

		@NotBlank(message = "姓名不能为空")
		private String name;

		@NotBlank(message = "地址不能为空")
		private String address;

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(final String address) {
			this.address = address;
		}
	}

	@Test
	public void beanValidatorTest() {
		final BeanValidationResult result = ValidationUtil.warpValidate(new TestClass());
		Assertions.assertFalse(result.isSuccess());
		Assertions.assertEquals(2, result.getErrorMessages().size());
	}

	@Test
	public void propertyValidatorTest() {
		final BeanValidationResult result = ValidationUtil.warpValidateProperty(new TestClass(), "name");
		Assertions.assertFalse(result.isSuccess());
		Assertions.assertEquals(1, result.getErrorMessages().size());
	}
}
