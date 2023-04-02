package org.dromara.hutool;

import org.dromara.hutool.serialize.JSONObjectSerializer;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class CustomSerializeTest {

	@BeforeEach
	public void init(){
		JSONUtil.putSerializer(CustomBean.class, (JSONObjectSerializer<CustomBean>) (json, bean) -> json.set("customName", bean.name));
	}

	@Test
	public void serializeTest() {
		final CustomBean customBean = new CustomBean();
		customBean.name = "testName";

		final JSONObject obj = JSONUtil.parseObj(customBean);
		Assertions.assertEquals("testName", obj.getStr("customName"));
	}

	@Test
	public void putTest() {
		final CustomBean customBean = new CustomBean();
		customBean.name = "testName";

		final JSONObject obj = JSONUtil.ofObj().set("customBean", customBean);
		Assertions.assertEquals("testName", obj.getJSONObject("customBean").getStr("customName"));
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
		Assertions.assertEquals("testName", bean.name);
	}

	@ToString
	public static class CustomBean {
		public String name;
		public String b;
		public Date date;
	}
}
