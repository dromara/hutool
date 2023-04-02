package org.dromara.hutool;

import org.dromara.hutool.serialize.JSONDeserializer;
import org.dromara.hutool.serialize.JSONObjectSerializer;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		Assertions.assertEquals("{\"myType\":{\"addr\":\"addrValue1\"}}", json);

		//MyDeserializer不会被调用
		final SimpleObj simpleObj2 = JSONUtil.toBean(json, SimpleObj.class);
		Assertions.assertEquals("addrValue1", simpleObj2.getMyType().getAddress());
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
		public void serialize(final JSONObject json, final MyType bean) {
			json.set("addr", bean.getAddress());
		}
	}

	public static class MyDeserializer implements JSONDeserializer<MyType> {
		@Override
		public MyType deserialize(final JSON json) {
			final MyType myType = new MyType();
			myType.setAddress(((JSONObject)json).getStr("addr"));
			return myType;
		}
	}
}
