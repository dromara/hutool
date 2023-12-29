/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.json.serialize.JSONStringer;
import org.dromara.hutool.json.test.bean.Price;
import org.dromara.hutool.json.test.bean.UserA;
import org.dromara.hutool.json.test.bean.UserC;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

public class JSONUtilTest {

	@Test
	public void parseInvalid() {
		Assertions.assertThrows(JSONException.class, ()->{
			JSONUtil.parse("abc");
		});
	}

	@Test
	public void parseInvalid2() {
		Assertions.assertThrows(JSONException.class, ()->{
			JSONUtil.parse("'abc");
		});
	}

	@Test
	public void parseInvalid3() {
		Assertions.assertThrows(JSONException.class, ()->{
			JSONUtil.parse("\"abc");
		});
	}

	@Test
	public void parseValueTest() {
		Object parse = JSONUtil.parse(123);
		Assertions.assertEquals(123, parse);

		parse = JSONUtil.parse("\"abc\"");
		Assertions.assertEquals("abc", parse);

		parse = JSONUtil.parse("true");
		Assertions.assertEquals(true, parse);

		parse = JSONUtil.parse("False");
		Assertions.assertEquals(false, parse);

		parse = JSONUtil.parse("null");
		Assertions.assertNull(parse);

		parse = JSONUtil.parse("");
		Assertions.assertNull(parse);
	}

	/**
	 * 出现语法错误时报错，检查解析\x字符时是否会导致死循环异常
	 */
	@Test
	public void parseTest() {
		Assertions.assertThrows(JSONException.class, ()->{
			JSONUtil.parseArray("[{\"a\":\"a\\x]");
		});
	}

	/**
	 * 数字解析为JSONArray报错
	 */
	@Test
	public void parseNumberToJSONArrayTest() {
		Assertions.assertThrows(JSONException.class, ()->{
			final JSONArray json = JSONUtil.parseArray(123L);
			Assertions.assertNotNull(json);
		});
	}

	/**
	 * 数字解析为JSONArray报错
	 */
	@Test
	public void parseNumberToJSONArrayTest2() {
		final JSONArray json = JSONUtil.parseArray(123L,
				JSONConfig.of().setIgnoreError(true));
		Assertions.assertNotNull(json);
	}

	/**
	 * 数字解析为JSONArray报错
	 */
	@Test
	public void parseNumberToJSONObjectTest() {
		Assertions.assertThrows(JSONException.class, ()->{
			final JSONObject json = JSONUtil.parseObj(123L);
			Assertions.assertNotNull(json);
		});
	}

	/**
	 * 数字解析为JSONObject，忽略错误
	 */
	@Test
	public void parseNumberToJSONObjectTest2() {
		final JSONObject json = JSONUtil.parseObj(123L, JSONConfig.of().setIgnoreError(true));
		Assertions.assertEquals(new JSONObject(), json);
	}

	@Test
	public void toJsonStrTest() {
		final UserA a1 = new UserA();
		a1.setA("aaaa");
		a1.setDate(DateUtil.now());
		a1.setName("AAAAName");
		final UserA a2 = new UserA();
		a2.setA("aaaa222");
		a2.setDate(DateUtil.now());
		a2.setName("AAAA222Name");

		final ArrayList<UserA> list = ListUtil.of(a1, a2);
		final HashMap<String, Object> map = MapUtil.newHashMap();
		map.put("total", 13);
		map.put("rows", list);

		final String str = JSONUtil.toJsonPrettyStr(map);
		JSONUtil.parse(str);
		Assertions.assertNotNull(str);
	}

	@Test
	public void toJsonStrTest2() {
		final Map<String, Object> model = new HashMap<>();
		model.put("mobile", "17610836523");
		model.put("type", 1);

		final Map<String, Object> data = new HashMap<>();
		data.put("model", model);
		data.put("model2", model);

		final JSONObject jsonObject = JSONUtil.parseObj(data);

		Assertions.assertTrue(jsonObject.containsKey("model"));
		Assertions.assertEquals(1, jsonObject.getJSONObject("model").getInt("type").intValue());
		Assertions.assertEquals("17610836523", jsonObject.getJSONObject("model").getStr("mobile"));
		// Assertions.assertEquals("{\"model\":{\"type\":1,\"mobile\":\"17610836523\"}}", jsonObject.toString());
	}

	@Test
	public void toJsonStrTest3() {
		// 验证某个字段为JSON字符串时转义是否规范
		final JSONObject object = new JSONObject(JSONConfig.of().setIgnoreError(true));
		object.set("name", "123123");
		object.set("value", "\\");
		object.set("value2", "</");

		final HashMap<String, String> map = MapUtil.newHashMap();
		map.put("user", object.toString());

		final JSONObject json = JSONUtil.parseObj(map);
		Assertions.assertEquals("{\"name\":\"123123\",\"value\":\"\\\\\",\"value2\":\"</\"}", json.get("user"));
		Assertions.assertEquals("{\"user\":\"{\\\"name\\\":\\\"123123\\\",\\\"value\\\":\\\"\\\\\\\\\\\",\\\"value2\\\":\\\"</\\\"}\"}", json.toString());

		final JSONObject json2 = JSONUtil.parseObj(json.toString());
		Assertions.assertEquals("{\"name\":\"123123\",\"value\":\"\\\\\",\"value2\":\"</\"}", json2.get("user"));
	}

	@Test
	public void toJsonStrFromSortedTest() {
		//noinspection SerializableInnerClassWithNonSerializableOuterClass
		final SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>() {
			private static final long serialVersionUID = 1L;

			{
			put("attributes", "a");
			put("b", "b");
			put("c", "c");
		}};

		Assertions.assertEquals("{\"attributes\":\"a\",\"b\":\"b\",\"c\":\"c\"}", JSONUtil.toJsonStr(sortedMap));
	}

	/**
	 * 泛型多层嵌套测试
	 */
	@Test
	public void toBeanTest() {
		final String json = "{\"ADT\":[[{\"BookingCode\":[\"N\",\"N\"]}]]}";

		final Price price = JSONUtil.toBean(json, Price.class);
		Assertions.assertEquals("N", price.getADT().get(0).get(0).getBookingCode().get(0));
	}

	@Test
	public void toBeanTest2() {
		// 测试JSONObject转为Bean中字符串字段的情况
		final String json = "{\"id\":123,\"name\":\"张三\",\"prop\":{\"gender\":\"男\", \"age\":18}}";
		final UserC user = JSONUtil.toBean(json, UserC.class);
		Assertions.assertNotNull(user.getProp());
		final String prop = user.getProp();
		final JSONObject propJson = JSONUtil.parseObj(prop);
		Assertions.assertEquals("男", propJson.getStr("gender"));
		Assertions.assertEquals(18, propJson.getInt("age").intValue());
		// Assertions.assertEquals("{\"age\":18,\"gender\":\"男\"}", user.getProp());
	}

	@Test
	public void getStrTest() {
		final String html = "{\"name\":\"Something must have been changed since you leave\"}";
		final JSONObject jsonObject = JSONUtil.parseObj(html);
		Assertions.assertEquals("Something must have been changed since you leave", jsonObject.getStr("name"));
	}

	@Test
	public void getStrTest2() {
		final String html = "{\"name\":\"Something\\u00a0must have been changed since you leave\"}";
		final JSONObject jsonObject = JSONUtil.parseObj(html);
		Assertions.assertEquals("Something\\u00a0must\\u00a0have\\u00a0been\\u00a0changed\\u00a0since\\u00a0you\\u00a0leave", jsonObject.getStrEscaped("name"));
	}

	@Test
	public void parseFromXmlTest() {
		final String s = "<sfzh>640102197312070614</sfzh><sfz>640102197312070614X</sfz><name>aa</name><gender>1</gender>";
		final JSONObject json = JSONUtil.parseFromXml(s);
		Assertions.assertEquals(640102197312070614L, json.get("sfzh"));
		Assertions.assertEquals("640102197312070614X", json.get("sfz"));
		Assertions.assertEquals("aa", json.get("name"));
		Assertions.assertEquals(1, json.get("gender"));
	}

	@Test
	public void doubleTest() {
		final String json = "{\"test\": 12.00}";
		final JSONObject jsonObject = JSONUtil.parseObj(json);
		//noinspection BigDecimalMethodWithoutRoundingCalled
		Assertions.assertEquals("12.00", jsonObject.getBigDecimal("test").setScale(2).toString());
	}

	@Test
	public void customValueTest() {
		final JSONObject jsonObject = JSONUtil.ofObj()
		.set("test2", (JSONStringer) () -> NumberUtil.format("#.0", 12.00D));

		Assertions.assertEquals("{\"test2\":12.0}", jsonObject.toString());
	}

	@Test
	public void setStripTrailingZerosTest() {
		// 默认去除多余的0
		final JSONObject jsonObjectDefault = JSONUtil.ofObj()
				.set("test2", 12.00D);
		Assertions.assertEquals("{\"test2\":12}", jsonObjectDefault.toString());

		// 不去除多余的0
		final JSONObject jsonObject = JSONUtil.ofObj(JSONConfig.of().setStripTrailingZeros(false))
				.set("test2", 12.00D);
		Assertions.assertEquals("{\"test2\":12.0}", jsonObject.toString());

		// 去除多余的0
		jsonObject.config().setStripTrailingZeros(true);
		Assertions.assertEquals("{\"test2\":12}", jsonObject.toString());
	}

	@Test
	public void parseObjTest() {
		// 测试转义
		final JSONObject jsonObject = JSONUtil.parseObj("{\n" +
				"    \"test\": \"\\\\地库地库\",\n" +
				"}");

		Assertions.assertEquals("\\地库地库", jsonObject.getObj("test"));
	}

	@Test
	public void sqlExceptionTest(){
		//https://github.com/dromara/hutool/issues/1399
		// SQLException实现了Iterable接口，默认是遍历之，会栈溢出，修正后只返回string
		final JSONObject set = JSONUtil.ofObj().set("test", new SQLException("test"));
		Assertions.assertEquals("{\"test\":\"java.sql.SQLException: test\"}", set.toString());
	}

	@Test
	public void parseBigNumberTest(){
		// 科学计数法使用BigDecimal处理，默认输出非科学计数形式
		final String str = "{\"test\":100000054128897953e4}";
		Assertions.assertEquals("{\"test\":1000000541288979530000}", JSONUtil.parseObj(str).toString());
	}

	@Test
	public void toXmlTest(){
		final JSONObject obj = JSONUtil.ofObj();
		obj.set("key1", "v1")
				.set("key2", ListUtil.view("a", "b", "c"));
		final String xmlStr = JSONUtil.toXmlStr(obj);
		Assertions.assertEquals("<key1>v1</key1><key2>a</key2><key2>b</key2><key2>c</key2>", xmlStr);
	}

	@Test
	public void toJsonStrOfStringTest(){
		Assertions.assertThrows(JSONException.class, ()->{
			final String a = "a";

			// 普通字符串不能解析为JSON字符串，必须由双引号或者单引号包裹
			final String s = JSONUtil.toJsonStr(a);
			Assertions.assertEquals(a, s);
		});
	}

	@Test
	public void toJsonStrOfNumberTest(){
		final int a = 1;
		final String s = JSONUtil.toJsonStr(a);
		Assertions.assertEquals("1", s);
	}

	/**
	 * 测试普通数组转JSONArray时是否异常, 尤其是byte[]数组, 可能是普通的byte[]数组, 也可能是二进制流
	 */
	@Test
	public void testArrayEntity() {
		final String jsonStr = JSONUtil.toJsonStr(new ArrayEntity());
		// a为空的bytes数组，按照空的流对待
		Assertions.assertEquals("{\"b\":[0],\"c\":[],\"d\":[],\"e\":[]}", jsonStr);
	}

	@Data
	static class ArrayEntity {
		private byte[] a = new byte[0];
		private byte[] b = new byte[1];
		private int[] c = new int[0];
		private Byte[] d = new Byte[0];
		private Byte[] e = new Byte[1];
	}

	@Test
	void toJsonStrOfBooleanTest() {
		final String jsonStr = JSONUtil.toJsonStr(true);
		Assertions.assertEquals("true", jsonStr);
	}
}
