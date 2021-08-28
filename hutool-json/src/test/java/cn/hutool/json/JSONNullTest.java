package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

public class JSONNullTest {

	@Test
	public void parseNullTest(){
		JSONObject bodyjson = JSONUtil.parseObj("{\n" +
				"            \"device_model\": null,\n" +
				"            \"device_status_date\": null,\n" +
				"            \"imsi\": null,\n" +
				"            \"act_date\": \"2021-07-23T06:23:26.000+00:00\"}");
		Assert.assertEquals(JSONNull.class, bodyjson.get("device_model").getClass());
		Assert.assertEquals(JSONNull.class, bodyjson.get("device_status_date").getClass());
		Assert.assertEquals(JSONNull.class, bodyjson.get("imsi").getClass());

		bodyjson.getConfig().setIgnoreNullValue(true);
		Assert.assertEquals("{\"act_date\":\"2021-07-23T06:23:26.000+00:00\"}", bodyjson.toString());
	}

	@Test
	public void parseNullTest2(){
		JSONObject bodyjson = JSONUtil.parseObj("{\n" +
				"            \"device_model\": null,\n" +
				"            \"device_status_date\": null,\n" +
				"            \"imsi\": null,\n" +
				"            \"act_date\": \"2021-07-23T06:23:26.000+00:00\"}", true, true);
		Assert.assertFalse(bodyjson.containsKey("device_model"));
		Assert.assertFalse(bodyjson.containsKey("device_status_date"));
		Assert.assertFalse(bodyjson.containsKey("imsi"));
	}
}
