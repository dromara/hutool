package cn.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONNullTest {

	@Test
	public void parseNullTest(){
		JSONObject bodyjson = JSONUtil.parseObj("{\n" +
				"            \"device_model\": null,\n" +
				"            \"device_status_date\": null,\n" +
				"            \"imsi\": null,\n" +
				"            \"act_date\": \"2021-07-23T06:23:26.000+00:00\"}");
		Assertions.assertEquals(JSONNull.class, bodyjson.get("device_model").getClass());
		Assertions.assertEquals(JSONNull.class, bodyjson.get("device_status_date").getClass());
		Assertions.assertEquals(JSONNull.class, bodyjson.get("imsi").getClass());

		bodyjson.getConfig().setIgnoreNullValue(true);
		Assertions.assertEquals("{\"act_date\":\"2021-07-23T06:23:26.000+00:00\"}", bodyjson.toString());
	}

	@Test
	public void parseNullTest2(){
		JSONObject bodyjson = JSONUtil.parseObj("{\n" +
				"            \"device_model\": null,\n" +
				"            \"device_status_date\": null,\n" +
				"            \"imsi\": null,\n" +
				"            \"act_date\": \"2021-07-23T06:23:26.000+00:00\"}", true, true);
		Assertions.assertFalse(bodyjson.containsKey("device_model"));
		Assertions.assertFalse(bodyjson.containsKey("device_status_date"));
		Assertions.assertFalse(bodyjson.containsKey("imsi"));
	}
}
