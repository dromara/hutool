package cn.hutool.core.bean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIAYGT0Test {

	/**
	 * BeanUtil.setProperty默认调用的是BeanPath方法，在设置值时，使用BeanUtil.setFieldValue这个方法。
	 * 此方法默认直接给字段赋值。
	 * 这里确实存在一定的歧义性，但是考虑到兼容性，不做处理。
	 */
	@Test
	void setPropertyTest() {
		Cat cat = new Cat();
		BeanUtil.setProperty(cat, "name", "Kitty");
		Assertions.assertEquals("Kitty", cat.getName());
	}

	static class Cat {
		private String name;

		public void setName(String name) {
			this.name = "Red" + name;
		}

		public String getName() {
			return name;
		}
	}

}
