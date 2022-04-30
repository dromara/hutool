package cn.hutool.json;

import cn.hutool.json.serialize.JSONObjectSerializer;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class CustomSerializeTest {

	@Test
	public void serializeTest() {
		JSONUtil.putSerializer(CustomBean.class, (JSONObjectSerializer<CustomBean>) (json, bean) -> json.set("customName", bean.name));

		final CustomBean customBean = new CustomBean();
		customBean.name = "testName";

		final JSONObject obj = JSONUtil.parseObj(customBean);
		Assert.assertEquals("testName", obj.getStr("customName"));
	}

	@Test
	public void deserializeTest() {
		JSONUtil.putDeserializer(CustomBean.class, json -> {
			final CustomBean customBean = new CustomBean();
			customBean.name = ((JSONObject)json).getStr("customName");
			return customBean;
		});

		final String jsonStr = "{\"customName\":\"testName\"}";
		final CustomBean bean = JSONUtil.parseObj(jsonStr).toBean(CustomBean.class);
		Assert.assertEquals("testName", bean.name);
	}

	@ToString
	public static class CustomBean {
		public String name;
		public String b;
		public Date date;
	}
}
