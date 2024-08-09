package cn.hutool.extra.cglib;

import cn.hutool.core.convert.Convert;
import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CglibUtilTest {

	@Test
	public void copyTest() {
		SampleBean bean = new SampleBean();
		OtherSampleBean otherBean = new OtherSampleBean();
		bean.setValue("Hello world");
		bean.setValue2("123");

		CglibUtil.copy(bean, otherBean);
		assertEquals("Hello world", otherBean.getValue());
		// 无定义转换器，转换失败
		assertEquals(0, otherBean.getValue2());

		OtherSampleBean otherBean2 = CglibUtil.copy(bean, OtherSampleBean.class);
		assertEquals("Hello world", otherBean2.getValue());
		// 无定义转换器，转换失败
		assertEquals(0, otherBean.getValue2());

		otherBean = new OtherSampleBean();
		//自定义转换器
		CglibUtil.copy(bean, otherBean, (value, target, context) -> Convert.convertQuietly(target, value));
		assertEquals("Hello world", otherBean.getValue());
		assertEquals(123, otherBean.getValue2());
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
