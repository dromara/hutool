package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class JSONBeanParserTest {

	@Test
	public void parseTest(){
		String jsonStr = "{\"customName\": \"customValue\", \"customAddress\": \"customAddressValue\"}";
		final TestBean testBean = JSONUtil.toBean(jsonStr, TestBean.class);
		Assert.assertNotNull(testBean);
		Assert.assertEquals("customValue", testBean.getName());
		Assert.assertEquals("customAddressValue", testBean.getAddress());
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
