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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONNullTest {

	@Test
	public void parseNullTest(){
		final JSONObject bodyjson = JSONUtil.parseObj("{\n" +
				"            \"device_model\": null,\n" +
				"            \"device_status_date\": null,\n" +
				"            \"imsi\": null,\n" +
				"            \"act_date\": \"2021-07-23T06:23:26.000+00:00\"}");
		Assertions.assertNull(bodyjson.get("device_model"));
		Assertions.assertNull(bodyjson.get("device_status_date"));
		Assertions.assertNull(bodyjson.get("imsi"));

		bodyjson.config().setIgnoreNullValue(true);
		Assertions.assertEquals("{\"act_date\":\"2021-07-23T06:23:26.000+00:00\"}", bodyjson.toString());
	}

	@Test
	public void parseNullTest2(){
		final JSONObject bodyjson = JSONUtil.parseObj("{\n" +
				"            \"device_model\": null,\n" +
				"            \"device_status_date\": null,\n" +
				"            \"imsi\": null,\n" +
				"            \"act_date\": \"2021-07-23T06:23:26.000+00:00\"}");
		Assertions.assertFalse(bodyjson.containsKey("device_model"));
		Assertions.assertFalse(bodyjson.containsKey("device_status_date"));
		Assertions.assertFalse(bodyjson.containsKey("imsi"));
	}

	@Test
	public void setNullTest(){
		// 忽略null
		String json1 = JSONUtil.ofObj().putObj("key1", null).toString();
		Assertions.assertEquals("{}", json1);

		// 不忽略null
		json1 = JSONUtil.ofObj(JSONConfig.of().setIgnoreNullValue(false)).putObj("key1", null).toString();
		Assertions.assertEquals("{\"key1\":null}", json1);
	}

	@Test
	public void setNullOfJSONArrayTest(){
		// 忽略null
		String json1 = JSONUtil.ofArray().addObj(null).toString();
		Assertions.assertEquals("[]", json1);

		// 不忽略null
		json1 = JSONUtil.ofArray(JSONConfig.of().setIgnoreNullValue(false)).addObj(null).toString();
		Assertions.assertEquals("[null]", json1);
	}
}
