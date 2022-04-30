package cn.hutool.extra.validation;

import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(2, result.getErrorMessages().size());
	}

	@Test
	public void propertyValidatorTest() {
		final BeanValidationResult result = ValidationUtil.warpValidateProperty(new TestClass(), "name");
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(1, result.getErrorMessages().size());
	}
}
