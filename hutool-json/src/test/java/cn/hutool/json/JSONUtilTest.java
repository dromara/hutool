package cn.hutool.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.test.bean.Price;
import cn.hutool.json.test.bean.UserA;

public class JSONUtilTest {
	
	@Test
	public void toDateTest() {
		String x = JSONUtil.parse(new Date()).toString();
		Date date = JSONUtil.toBean(JSONUtil.parseObj(x), Date.class);
		Assert.assertNotNull(date);
	}

	@Test
	public void toJsonStrTest() {
		UserA a1 = new UserA();
		a1.setA("aaaa");
		a1.setDate(DateUtil.date());
		a1.setName("AAAAName");
		UserA a2 = new UserA();
		a2.setA("aaaa222");
		a2.setDate(DateUtil.date());
		a2.setName("AAAA222Name");

		ArrayList<UserA> list = CollectionUtil.newArrayList(a1, a2);
		HashMap<String, Object> map = CollectionUtil.newHashMap();
		map.put("total", 13);
		map.put("rows", list);

		String str = JSONUtil.toJsonStr(map);
		Assert.assertNotNull(str);
	}

	@Test
	public void toJsonStrTest2() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("mobile", "17610836523");
		model.put("type", 1);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("model", model);
		
		JSONObject jsonObject = JSONUtil.parseObj(data);
		Assert.assertEquals("{\"model\":{\"type\":1,\"mobile\":\"17610836523\"}}", jsonObject.toString());
	}
	
	@Test
	public void toJsonStrTest3() {
		//验证某个字段为JSON字符串时转义是否规范
		JSONObject object = new JSONObject(true);
		object.put("name", "123123");
		object.put("value", "\\");
		object.put("value2", "</");
		
		HashMap<String, String> map = MapUtil.newHashMap();
		map.put("user", object.toString());
		
		JSONObject json = JSONUtil.parseObj(map);
		Assert.assertEquals("{\"name\":\"123123\",\"value\":\"\\\\\",\"value2\":\"<\\/\"}", json.get("user"));
		Assert.assertEquals("{\"user\":\"{\\\"name\\\":\\\"123123\\\",\\\"value\\\":\\\"\\\\\\\\\\\",\\\"value2\\\":\\\"<\\\\/\\\"}\"}", json.toString());
		
		JSONObject json2 = JSONUtil.parseObj(json.toString());
		Assert.assertEquals("{\"name\":\"123123\",\"value\":\"\\\\\",\"value2\":\"<\\/\"}", json2.get("user"));
	}
	
	@Test
	public void toBeanTest() {
		String json = "{\"ADT\":[[{\"BookingCode\":[\"N\",\"N\"]}]]}";

		Price price = JSONUtil.toBean(json, Price.class);
		Assert.assertEquals("N", price.getADT().get(0).get(0).getBookingCode().get(0));
	}
}
