package cn.hutool.extra.validation;

import cn.hutool.core.lang.Assert;
import org.junit.Test;

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
		BeanValidationResult result = BeanValidationUtil.warpValidate(new TestClass());
		Assert.isTrue(result.getSuccess());
	}

	@Test
	public void propertyValidatorTest() {
		BeanValidationResult result = BeanValidationUtil.warpValidateProperty(new TestClass(), "name");
		Assert.isTrue(result.getSuccess());
	}
}
