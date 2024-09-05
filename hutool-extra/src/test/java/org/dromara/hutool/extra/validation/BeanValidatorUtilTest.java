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
