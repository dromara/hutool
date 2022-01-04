package cn.hutool.extra.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotBlank;

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

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
	}

	@Test
	public void beanValidatorTest() {
		BeanValidationResult result = ValidationUtil.warpValidate(new TestClass());
		Assertions.assertFalse(result.isSuccess());
		Assertions.assertEquals(2, result.getErrorMessages().size());
	}

	@Test
	public void propertyValidatorTest() {
		BeanValidationResult result = ValidationUtil.warpValidateProperty(new TestClass(), "name");
		Assertions.assertFalse(result.isSuccess());
		Assertions.assertEquals(1, result.getErrorMessages().size());
	}
}
