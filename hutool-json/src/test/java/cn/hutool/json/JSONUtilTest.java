package cn.hutool.json;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.test.bean.Price;
import cn.hutool.json.test.bean.UserA;
import cn.hutool.json.test.bean.UserC;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSONUtilTest {

	/**
	 * 出现语法错误时报错，检查解析\x字符时是否会导致死循环异常
	 */
	@Test(expected = JSONException.class)
	public void parseTest(){
		JSONArray jsonArray = JSONUtil.parseArray("[{\"a\":\"a\\x]");
		Console.log(jsonArray);
	}

	/**
	 * 数字解析为JSONArray报错
	 */
	@Test(expected = JSONException.class)
	public void parseNumberTest(){
		JSONArray json = JSONUtil.parseArray(123L);
		Console.log(json);
	}

	/**
	 * 数字解析为JSONObject忽略
	 */
	@Test
	public void parseNumberTest2(){
		JSONObject json = JSONUtil.parseObj(123L);
		Assert.assertEquals(new JSONObject(), json);
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

		String str = JSONUtil.toJsonPrettyStr(map);
		Assert.assertNotNull(str);
	}

	@Test
	public void toJsonStrTest2() {
		Map<String, Object> model = new HashMap<>();
		model.put("mobile", "17610836523");
		model.put("type", 1);

		Map<String, Object> data = new HashMap<>();
		data.put("model", model);
		data.put("model2", model);

		JSONObject jsonObject = JSONUtil.parseObj(data);

		Assert.assertTrue(jsonObject.containsKey("model"));
		Assert.assertEquals(1, jsonObject.getJSONObject("model").getInt("type").intValue());
		Assert.assertEquals("17610836523", jsonObject.getJSONObject("model").getStr("mobile"));
		// Assert.assertEquals("{\"model\":{\"type\":1,\"mobile\":\"17610836523\"}}", jsonObject.toString());
	}

	@Test
	public void toJsonStrTest3() {
		// 验证某个字段为JSON字符串时转义是否规范
		JSONObject object = new JSONObject(true);
		object.set("name", "123123");
		object.set("value", "\\");
		object.set("value2", "</");

		HashMap<String, String> map = MapUtil.newHashMap();
		map.put("user", object.toString());

		JSONObject json = JSONUtil.parseObj(map);
		Assert.assertEquals("{\"name\":\"123123\",\"value\":\"\\\\\",\"value2\":\"</\"}", json.get("user"));
		Assert.assertEquals("{\"user\":\"{\\\"name\\\":\\\"123123\\\",\\\"value\\\":\\\"\\\\\\\\\\\",\\\"value2\\\":\\\"</\\\"}\"}", json.toString());

		JSONObject json2 = JSONUtil.parseObj(json.toString());
		Assert.assertEquals("{\"name\":\"123123\",\"value\":\"\\\\\",\"value2\":\"</\"}", json2.get("user"));
	}

	/**
	 * 泛型多层嵌套测试
	 */
	@Test
	public void toBeanTest() {
		String json = "{\"ADT\":[[{\"BookingCode\":[\"N\",\"N\"]}]]}";

		Price price = JSONUtil.toBean(json, Price.class);
		Assert.assertEquals("N", price.getADT().get(0).get(0).getBookingCode().get(0));
	}

	@Test
	public void toBeanTest2() {
		// 测试JSONObject转为Bean中字符串字段的情况
		String json = "{\"id\":123,\"name\":\"张三\",\"prop\":{\"gender\":\"男\", \"age\":18}}";
		UserC user = JSONUtil.toBean(json, UserC.class);
		Assert.assertNotNull(user.getProp());
		String prop = user.getProp();
		JSONObject propJson = JSONUtil.parseObj(prop);
		Assert.assertEquals("男", propJson.getStr("gender"));
		Assert.assertEquals(18, propJson.getInt("age").intValue());
		// Assert.assertEquals("{\"age\":18,\"gender\":\"男\"}", user.getProp());
	}

	@Test
	public void putByPathTest() {
		JSONObject json = new JSONObject();
		json.putByPath("aa.bb", "BB");
		Assert.assertEquals("{\"aa\":{\"bb\":\"BB\"}}", json.toString());
	}

	@Test
	public void getStrTest() {
		String html = "{\"name\":\"Something must have been changed since you leave\"}";
		JSONObject jsonObject = JSONUtil.parseObj(html);
		Assert.assertEquals("Something must have been changed since you leave", jsonObject.getStr("name"));
	}

	@Test
	public void getStrTest2() {
		String html = "{\"name\":\"Something\\u00a0must have been changed since you leave\"}";
		JSONObject jsonObject = JSONUtil.parseObj(html);
		Assert.assertEquals("Something\\u00a0must\\u00a0have\\u00a0been\\u00a0changed\\u00a0since\\u00a0you\\u00a0leave", jsonObject.getStrEscaped("name"));
	}

	@Test
	public void parseFromXmlTest() {
		String s = "<sfzh>640102197312070614</sfzh><sfz>640102197312070614X</sfz><name>aa</name><gender>1</gender>";
		JSONObject json = JSONUtil.parseFromXml(s);
		Assert.assertEquals(640102197312070614L, json.get("sfzh"));
		Assert.assertEquals("640102197312070614X", json.get("sfz"));
		Assert.assertEquals("aa", json.get("name"));
		Assert.assertEquals(1, json.get("gender"));
	}
}
