package cn.hutool.core.bean;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.map.MapUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class BeanCopyMappingTest {

	/**
	 * https://gitee.com/dromara/hutool/issues/I4C48U <br>
	 * 传递复制不要用注解别名，应该用动态映射
	 */
	@Test
	public void copyPropertiesTest() {
		final CopyOptions copyOptions = CopyOptions.create()
				.setFieldMapping(MapUtil.of("car", "carNo"));

		B b = B.builder().car("12312312").build();
		A a = A.builder().build();
		C c = C.builder().build();
		BeanUtil.copyProperties(b, a, copyOptions);
		BeanUtil.copyProperties(a, c);

		Assert.assertEquals("12312312", c.getCarNo());
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
