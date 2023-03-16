package cn.hutool.core.bean;

import org.junit.Assert;
import org.junit.Test;

public class BeanWithReturnThisTest {

	@Test
	public void setValueTest() {
		final BeanWithRetuenThis bean = new BeanWithRetuenThis();
		final BeanDesc beanDesc = BeanUtil.getBeanDesc(BeanWithRetuenThis.class);
		final PropDesc prop = beanDesc.getProp("a");
		prop.setValue(bean, "123");

		Assert.assertEquals("123", bean.getA());
	}

	static class BeanWithRetuenThis{
		public String getA() {
			return a;
		}

		public BeanWithRetuenThis setA(final String a) {
			this.a = a;
			return this;
		}

		private String a;
	}
}
