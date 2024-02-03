package org.dromara.hutool.json;

import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI90ADXTest {

	/**
	 * 测试只有getter方法时是否可以解析
	 */
	@Test
	void parseTest() {
		final TestBean testBean = new TestBean();
		testBean.name = "aaaa";

		final JSONObject json = JSONUtil.parseObj(testBean);
		Assertions.assertEquals("{\"name\":\"aaaa\"}", json.toString());
	}

	@Getter
	static class TestBean{
		private String name;
	}
}
