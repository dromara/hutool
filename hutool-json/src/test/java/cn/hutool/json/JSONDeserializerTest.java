package cn.hutool.json;

import cn.hutool.json.serialize.JSONDeserializer;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONDeserializerTest {

	@Test
	public void parseTest(){
		final String jsonStr = "{\"customName\": \"customValue\", \"customAddress\": \"customAddressValue\"}";
		final TestBean testBean = JSONUtil.toBean(jsonStr, TestBean.class);
		Assertions.assertNotNull(testBean);
		Assertions.assertEquals("customValue", testBean.getName());
		Assertions.assertEquals("customAddressValue", testBean.getAddress());
	}

	@Data
	static class TestBean implements JSONDeserializer<Object> {

		private String name;
		private String address;

		@Override
		public Object deserialize(final JSON value) {
			final JSONObject valueObj = (JSONObject) value;
			this.name = valueObj.getStr("customName");
			this.address = valueObj.getStr("customAddress");
			return this;
		}
	}
}
