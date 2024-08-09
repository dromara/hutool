package cn.hutool.json;

import lombok.Getter;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class IssueI90ADXTest {

	/**
	 * 测试只有getter方法时是否可以解析
	 */
	@Test
	public void parseTest() {
		final TestBean testBean = new TestBean();
		testBean.name = "aaaa";

		final JSONObject json = JSONUtil.parseObj(testBean);
		assertEquals("{\"name\":\"aaaa\"}", json.toString());
	}

	@Getter
	static class TestBean{
		private String name;
	}

}
