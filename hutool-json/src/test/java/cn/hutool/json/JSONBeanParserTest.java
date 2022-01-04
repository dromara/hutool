package cn.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONBeanParserTest {

	@Test
	public void parseTest(){
		String jsonStr = "{\"customName\": \"customValue\", \"customAddress\": \"customAddressValue\"}";
		final TestBean testBean = JSONUtil.toBean(jsonStr, TestBean.class);
		Assertions.assertNotNull(testBean);
		Assertions.assertEquals("customValue", testBean.getName());
		Assertions.assertEquals("customAddressValue", testBean.getAddress());
	}

	@Data
	static class TestBean implements JSONBeanParser<JSONObject>{

		private String name;
		private String address;

		@Override
		public void parse(JSONObject value) {
			this.name = value.getStr("customName");
			this.address = value.getStr("customAddress");
		}
	}
}
