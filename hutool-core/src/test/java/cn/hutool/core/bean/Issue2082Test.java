package cn.hutool.core.bean;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 * https://github.com/dromara/hutool/issues/2082<br>
 * 当setXXX有重载方法的时候，BeanDesc中会匹配到重载方法，增加类型检查来规避之
 */
public class Issue2082Test {

	@Test
	public void toBeanTest() {
		TestBean2 testBean2 = new TestBean2();
		TestBean test = BeanUtil.toBean(testBean2, TestBean.class);
		Assert.assertNull(test.getId());
	}

	@Data
	static class TestBean {
		private Long id;

		public void setId(String id) {
			this.id = Long.valueOf(id);
		}
	}

	@Data
	static class TestBean2 {
		private String id;
	}
}
