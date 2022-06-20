package cn.hutool.json;

import cn.hutool.json.serialize.JSONDeserializer;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class JSONDeserializerTest {

	@Test
	public void parseTest(){
		final String jsonStr = "{\"customName\": \"customValue\", \"customAddress\": \"customAddressValue\"}";
		final TestBean testBean = JSONUtil.toBean(jsonStr, TestBean.class);
		Assert.assertNotNull(testBean);
		Assert.assertEquals("customValue", testBean.getName());
		Assert.assertEquals("customAddressValue", testBean.getAddress());
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
