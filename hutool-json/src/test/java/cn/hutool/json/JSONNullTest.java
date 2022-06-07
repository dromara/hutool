package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

public class JSONNullTest {

	@Test
	public void parseNullTest(){
		final JSONObject bodyjson = JSONUtil.parseObj("{\n" +
				"            \"device_model\": null,\n" +
				"            \"device_status_date\": null,\n" +
				"            \"imsi\": null,\n" +
				"            \"act_date\": \"2021-07-23T06:23:26.000+00:00\"}");
		Assert.assertNull(bodyjson.get("device_model"));
		Assert.assertNull(bodyjson.get("device_status_date"));
		Assert.assertNull(bodyjson.get("imsi"));

		bodyjson.getConfig().setIgnoreNullValue(true);
		Assert.assertEquals("{\"act_date\":\"2021-07-23T06:23:26.000+00:00\"}", bodyjson.toString());
	}

	@Test
	public void parseNullTest2(){
		final JSONObject bodyjson = JSONUtil.parseObj("{\n" +
				"            \"device_model\": null,\n" +
				"            \"device_status_date\": null,\n" +
				"            \"imsi\": null,\n" +
				"            \"act_date\": \"2021-07-23T06:23:26.000+00:00\"}", true);
		Assert.assertFalse(bodyjson.containsKey("device_model"));
		Assert.assertFalse(bodyjson.containsKey("device_status_date"));
		Assert.assertFalse(bodyjson.containsKey("imsi"));
	}

	@Test
	public void setNullTest(){
		// 忽略null
		String json1 = JSONUtil.createObj().set("key1", null).toString();
		Assert.assertEquals("{}", json1);

		// 不忽略null
		json1 = JSONUtil.createObj(JSONConfig.of().setIgnoreNullValue(false)).set("key1", null).toString();
		Assert.assertEquals("{\"key1\":null}", json1);
	}

	@Test
	public void setNullOfJSONArrayTest(){
		// 忽略null
		String json1 = JSONUtil.createArray().set(null).toString();
		Assert.assertEquals("[]", json1);

		// 不忽略null
		json1 = JSONUtil.createArray(JSONConfig.of().setIgnoreNullValue(false)).set(null).toString();
		Assert.assertEquals("[null]", json1);
	}
}
