package cn.hutool.json;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.json.serialize.JSONDeserializer;
import cn.hutool.json.serialize.JSONObjectSerializer;
import lombok.ToString;

public class CustomSerializeTest {

	@Test
	public void serializeTest() {
		JSONUtil.putSerializer(CustomBean.class, new JSONObjectSerializer<CustomBean>() {

			@Override
			public void serialize(JSONObject json, CustomBean bean) {
				json.put("customName", bean.name);
			}
		});
		
		CustomBean customBean = new CustomBean();
		customBean.name = "testName";
		
		JSONObject obj = JSONUtil.parseObj(customBean);
		Assert.assertEquals("testName", obj.getStr("customName"));
	}
	
	@Test
	public void deserializeTest() {
		JSONUtil.putDeserializer(CustomBean.class, new JSONDeserializer<CustomBean>() {

			@Override
			public CustomBean deserialize(JSON json) {
				CustomBean customBean = new CustomBean();
				customBean.name = ((JSONObject)json).getStr("customName");
				return customBean;
			}

		});
		
		String jsonStr = "{\"customName\":\"testName\"}";
		CustomBean bean = JSONUtil.parseObj(jsonStr).toBean(CustomBean.class);
		Assert.assertEquals("testName", bean.name);
	}

	@ToString
	public static class CustomBean {
		public String name;
		public String b;
		public Date date;
	}
}
