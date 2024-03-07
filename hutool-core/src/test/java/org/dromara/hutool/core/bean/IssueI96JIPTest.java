package org.dromara.hutool.core.bean;

import lombok.Data;
import org.junit.jupiter.api.Test;

public class IssueI96JIPTest {
	@Test
	void copyPropertiesTest() {
		BeanUtil.copyProperties(new TestBean(), new TestBean());
	}

	@Data
	static class TestBean {
		private String name;
	}
}
