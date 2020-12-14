package cn.hutool.extra.cglib;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class CglibUtilTest {

	@Test
	public void copyTest() {
		SampleBean bean = new SampleBean();
		bean.setValue("Hello world");

		OtherSampleBean otherBean = new OtherSampleBean();
		CglibUtil.copy(bean, otherBean);
		Assert.assertEquals("Hello world", otherBean.getValue());

		OtherSampleBean otherBean2 = CglibUtil.copy(bean, OtherSampleBean.class);
		Assert.assertEquals("Hello world", otherBean2.getValue());
	}

	@Data
	public static class SampleBean {
		private String value;
	}

	@Data
	public static class OtherSampleBean {
		private String value;
	}
}
