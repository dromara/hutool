package org.dromara.hutool.core.bean;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI5DDZXTest {
	@Test
	public void copyPropertiesTest() {
		// 对于final字段，private由于没有提供setter方法，是无法实现属性赋值的，如果设置为public即可
		final TeStudent student = new TeStudent("Hutool");
		final TePerson tePerson = BeanUtil.copyProperties(student, TePerson.class);
		Assertions.assertEquals("Hutool", tePerson.getName());
	}

	@Data
	static class TeStudent {
		private final String name;
	}

	@Data
	static class TePerson {
		public final String name;
	}
}
