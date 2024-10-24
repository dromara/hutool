package org.dromara.hutool.core.bean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIAYGT0Test {
	@Test
	void setPropertyTest() {
		final Cat cat = new Cat();
		// 优先调用setter方法
		BeanUtil.setProperty(cat, "name", "Kitty");
		Assertions.assertEquals("RedKitty", cat.getName());
	}

	static class Cat {
		private String name;

		public void setName(final String name) {
			this.name = "Red" + name;
		}

		public String getName() {
			return name;
		}
	}
}
