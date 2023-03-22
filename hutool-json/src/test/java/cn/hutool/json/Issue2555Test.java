package cn.hutool.json;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.serialize.JSONDeserializer;
import cn.hutool.json.serialize.JSONObjectSerializer;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class Issue2555Test {

	@Test
	public void serAndDeserTest(){
		JSONUtil.putSerializer(MyType.class, new MySerializer());
		JSONUtil.putDeserializer(MyType.class, new MyDeserializer());

		final SimpleObj simpleObj = new SimpleObj();
		final MyType child = new MyType();
		child.setAddress("addrValue1");
		simpleObj.setMyType(child);

		final String json = JSONUtil.toJsonStr(simpleObj);
		Assert.assertEquals("{\"myType\":{\"addr\":\"addrValue1\"}}", json);

		//MyDeserializer不会被调用
		final JSONObject jsonObject = JSONUtil.parseObj(json, JSONConfig.create().setIgnoreError(false));
		final SimpleObj simpleObj2 = jsonObject.toBean(SimpleObj.class);
		Assert.assertEquals("addrValue1", simpleObj2.getMyType().getAddress());
	}

	@Test
	public void deserTest(){
		JSONUtil.putDeserializer(MyType.class, new MyDeserializer());

		final String jsonStr = "{\"myType\":{\"addr\":\"addrValue1\"}}";

		//MyDeserializer不会被调用
		final JSONObject jsonObject = JSONUtil.parseObj(jsonStr, JSONConfig.create().setIgnoreError(false));
		final SimpleObj simpleObj2 = jsonObject.toBean(SimpleObj.class);
		Assert.assertEquals("addrValue1", simpleObj2.getMyType().getAddress());
	}

	@Data
	public static class MyType {
		private String address;
	}
	@Data
	public static class SimpleObj {
		private MyType myType;
	}

	public static class MySerializer implements JSONObjectSerializer<MyType> {
		@Override
		public void serialize(JSONObject json, MyType bean) {
			json.set("addr", bean.getAddress());
		}
	}

	public static class MyDeserializer implements JSONDeserializer<MyType>{
		@Override
		public MyType deserialize(JSON json) {
			final MyType myType = new MyType();
			myType.setAddress(((JSONObject)json).getStr("addr"));
			return myType;
		}
	}
}
