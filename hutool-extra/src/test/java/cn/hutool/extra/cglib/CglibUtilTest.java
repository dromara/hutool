package cn.hutool.extra.cglib;

import cn.hutool.core.convert.Convert;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CglibUtilTest {

	@Test
	public void copyTest() {
		final SampleBean bean = new SampleBean();
		OtherSampleBean otherBean = new OtherSampleBean();
		bean.setValue("Hello world");
		bean.setValue2("123");

		CglibUtil.copy(bean, otherBean);
		Assertions.assertEquals("Hello world", otherBean.getValue());
		// 无定义转换器，转换失败
		Assertions.assertEquals(0, otherBean.getValue2());

		final OtherSampleBean otherBean2 = CglibUtil.copy(bean, OtherSampleBean.class);
		Assertions.assertEquals("Hello world", otherBean2.getValue());
		// 无定义转换器，转换失败
		Assertions.assertEquals(0, otherBean.getValue2());

		otherBean = new OtherSampleBean();
		//自定义转换器
		CglibUtil.copy(bean, otherBean, (value, target, context) -> Convert.convertQuietly(target, value));
		Assertions.assertEquals("Hello world", otherBean.getValue());
		Assertions.assertEquals(123, otherBean.getValue2());
	}

	@Data
	public static class SampleBean {
		private String value;
		private String value2;
	}

	@Data
	public static class OtherSampleBean {
		private String value;
		private int value2;
	}
}
