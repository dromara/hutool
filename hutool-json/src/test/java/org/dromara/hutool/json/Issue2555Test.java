/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.json;

import org.dromara.hutool.json.serialize.JSONDeserializer;
import org.dromara.hutool.json.serialize.JSONObjectSerializer;
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
