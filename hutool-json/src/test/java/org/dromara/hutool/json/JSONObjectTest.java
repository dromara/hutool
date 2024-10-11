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

import lombok.Data;
import org.dromara.hutool.core.annotation.Alias;
import org.dromara.hutool.core.annotation.PropIgnore;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.date.DateFormatPool;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.format.DateFormatManager;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.test.bean.*;
import org.dromara.hutool.json.test.bean.report.CaseReport;
import org.dromara.hutool.json.test.bean.report.StepReport;
import org.dromara.hutool.json.test.bean.report.SuiteReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * JSONObject单元测试
 *
 * @author Looly
 */
public class JSONObjectTest {

	@Test
	public void toStringTest() {
		final String str = "{\"code\": 500, \"data\":null}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject jsonObject = JSONUtil.parseObj(str, JSONConfig.of().setIgnoreNullValue(false));
		assertEquals("{\"code\":500,\"data\":null}", jsonObject.toString());
		jsonObject.config().setIgnoreNullValue(true);
		assertEquals("{\"code\":500}", jsonObject.toString());
	}

	@Test
	public void toStringTest2() {
		final String str = "{\"test\":\"关于开展2018年度“文明集体”、“文明职工”评选表彰活动的通知\"}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject json = JSONUtil.parseObj(str);
		assertEquals(str, json.toString());
	}

	/**
	 * 测试JSON中自定义日期格式输出正确性
	 */
	@Test
	public void toStringTest3() {
		final JSONObject json = JSONUtil.ofObj(JSONConfig.of().setDateFormat(DateFormatPool.NORM_DATE_PATTERN))//
						.putValue("dateTime", DateUtil.parse("2019-05-02 22:12:01"));
		assertEquals("{\"dateTime\":\"2019-05-02\"}", json.toString());
	}

	@Test
	public void toStringWithDateTest() {
		JSONObject json = JSONUtil.ofObj().putValue("date", DateUtil.parse("2019-05-08 19:18:21"));
		assert json != null;
		assertEquals("{\"date\":1557314301000}", json.toString());

		json = JSONUtil.ofObj(JSONConfig.of().setDateFormat(DateFormatPool.NORM_DATE_PATTERN))
			.putValue("date", DateUtil.parse("2019-05-08 19:18:21"));
		assertEquals("{\"date\":\"2019-05-08\"}", json.toString());
	}


	@Test
	public void putAllTest() {
		final JSONObject json1 = JSONUtil.ofObj()
				.putValue("a", "value1")
				.putValue("b", "value2")
				.putValue("c", "value3")
				.putValue("d", true);

		final JSONObject json2 = JSONUtil.ofObj()
				.putValue("a", "value21")
				.putValue("b", "value22");

		// putAll操作会覆盖相同key的值，因此a,b两个key的值改变，c的值不变
		json1.putAll(json2);

		assertEquals(json1.getObj("a"), "value21");
		assertEquals(json1.getObj("b"), "value22");
		assertEquals(json1.getObj("c"), "value3");
	}

	@Test
	public void parseStringTest() {
		final String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\", \"d\": true, \"e\": null}";
		final JSONObject jsonObject = JSONUtil.parseObj(jsonStr, JSONConfig.of().setIgnoreNullValue(false));
		assertEquals(jsonObject.getObj("a"), "value1");
		assertEquals(jsonObject.getObj("b"), "value2");
		assertEquals(jsonObject.getObj("c"), "value3");
		assertEquals(jsonObject.getObj("d"), true);

		Assertions.assertTrue(jsonObject.containsKey("e"));
		assertNull(jsonObject.get("e"));
	}

	@Test
	public void parseStringTest2() {
		final String jsonStr = "{\"file_name\":\"RMM20180127009_731.000\",\"error_data\":\"201121151350701001252500000032 18973908335 18973908335 13601893517 201711211700152017112115135420171121 6594000000010100000000000000000000000043190101701001910072 100001100 \",\"error_code\":\"F140\",\"error_info\":\"最早发送时间格式错误，该字段可以为空，当不为空时正确填写格式为“YYYYMMDDHHMISS”\",\"app_name\":\"inter-pre-check\"}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject json = JSONUtil.parseObj(jsonStr);
		assertEquals("F140", json.getStr("error_code"));
		assertEquals("最早发送时间格式错误，该字段可以为空，当不为空时正确填写格式为“YYYYMMDDHHMISS”", json.getStr("error_info"));
	}

	@Test
	public void parseStringTest3() {
		final String jsonStr = "{\"test\":\"体”、“文\"}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject json = JSONUtil.parseObj(jsonStr);
		assertEquals("体”、“文", json.getStr("test"));
	}

	@Test
	public void parseStringTest4() {
		final String jsonStr = "{'msg':'这里还没有内容','data':{'cards':[]},'ok':0}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject json = JSONUtil.parseObj(jsonStr);
		assertEquals(Integer.valueOf(0), json.getInt("ok"));
		assertEquals(new JSONArray(), json.getJSONObject("data").getJSONArray("cards"));
	}

	@Test
	public void parseBytesTest() {
		final String jsonStr = "{'msg':'这里还没有内容','data':{'cards':[]},'ok':0}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject json = JSONUtil.parseObj(jsonStr.getBytes(StandardCharsets.UTF_8));
		assertEquals(Integer.valueOf(0), json.getInt("ok"));
		assertEquals(new JSONArray(), json.getJSONObject("data").getJSONArray("cards"));
	}

	@Test
	public void parseReaderTest() {
		final String jsonStr = "{'msg':'这里还没有内容','data':{'cards':[]},'ok':0}";
		final StringReader stringReader = new StringReader(jsonStr);

		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject json = JSONUtil.parseObj(stringReader);
		assertEquals(Integer.valueOf(0), json.getInt("ok"));
		assertEquals(new JSONArray(), json.getJSONObject("data").getJSONArray("cards"));
	}

	@Test
	public void parseInputStreamTest() {
		final String jsonStr = "{'msg':'这里还没有内容','data':{'cards':[]},'ok':0}";
		final ByteArrayInputStream in = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));

		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject json = JSONUtil.parseObj(in);
		assertEquals(Integer.valueOf(0), json.getInt("ok"));
		assertEquals(new JSONArray(), json.getJSONObject("data").getJSONArray("cards"));
	}

	@Test
	public void parseStringWithSlashTest() {
		//在5.3.2之前，</div>中的/会被转义，修复此bug的单元测试
		final String jsonStr = "{\"a\":\"<div>aaa</div>\"}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject json = JSONUtil.parseObj(jsonStr);
		assertEquals("<div>aaa</div>", json.getObj("a"));
		assertEquals(jsonStr, json.toString());
	}

	@Test
	public void toBeanTest() {
		final JSONObject subJson = JSONUtil.ofObj().putValue("value1", "strValue1").putValue("value2", "234");
		final JSONObject json = JSONUtil.ofObj(JSONConfig.of().setIgnoreError(true)).putValue("strValue", "strTest").putValue("intValue", 123)
				// 测试空字符串转对象
				.putValue("doubleValue", "")
				.putValue("beanValue", subJson)
				.putValue("list", JSONUtil.ofArray().addValue("a").addValue("b")).putValue("testEnum", "TYPE_A");

		final TestBean bean = json.toBean(TestBean.class);
		assertEquals("a", bean.getList().get(0));
		assertEquals("b", bean.getList().get(1));

		assertEquals("strValue1", bean.getBeanValue().getValue1());
		// BigDecimal转换检查
		assertEquals(new BigDecimal("234"), bean.getBeanValue().getValue2());
		// 枚举转换检查
		assertEquals(TestEnum.TYPE_A, bean.getTestEnum());
	}

	@Test
	public void toBeanNullStrTest() {
		final JSONObject json = JSONUtil.ofObj(JSONConfig.of().setIgnoreError(true))//
				.putValue("strValue", "null")//
				.putValue("intValue", 123)//
				// 子对象对应"null"字符串，如果忽略错误，跳过，否则抛出转换异常
				.putValue("beanValue", "null")//
				.putValue("list", JSONUtil.ofArray().addValue("a").addValue("b"));

		final TestBean bean = json.toBean(TestBean.class);
		// 当JSON中为字符串"null"时应被当作字符串处理
		assertEquals("null", bean.getStrValue());
		// 当JSON中为字符串"null"时Bean中的字段类型不匹配应在ignoreError模式下忽略注入
		assertNull(bean.getBeanValue());
	}

	@Test
	void addListTest(){
		final JSONObject json = JSONUtil.ofObj();
		json.putValue("list", ListUtil.of(1, 2, 3));
		Assertions.assertEquals("{\"list\":[1,2,3]}", json.toString());
	}

	@Test
	public void toBeanTest2() {
		final UserA userA = new UserA();
		userA.setA("A user");
		userA.setName("{\n\t\"body\":{\n\t\t\"loginId\":\"id\",\n\t\t\"password\":\"pwd\"\n\t}\n}");
		userA.setDate(new Date());
		userA.setSqs(ListUtil.of(new Seq("seq1"), new Seq("seq2")));

		final JSONObject json = JSONUtil.parseObj(userA);
		final UserA userA2 = json.toBean(UserA.class);
		// 测试数组
		assertEquals("seq1", userA2.getSqs().get(0).getSeq());
		// 测试带换行符等特殊字符转换是否成功
		Assertions.assertTrue(StrUtil.isNotBlank(userA2.getName()));
	}

	@Test
	public void toBeanWithNullTest() {
		final String jsonStr = "{'data':{'userName':'ak','password': null}}";
		final UserWithMap user = JSONUtil.toBean(JSONUtil.parseObj(jsonStr, JSONConfig.of().setIgnoreNullValue(false)), UserWithMap.class);
		Assertions.assertTrue(user.getData().containsKey("password"));
	}

	@Test
	public void toBeanTest4() {
		final String json = "{\"data\":{\"b\": \"c\"}}";

		final UserWithMap map = JSONUtil.toBean(json, UserWithMap.class);
		assertEquals("c", map.getData().get("b"));
	}

	@Test
	public void toBeanTest5() {
		final String readUtf8Str = ResourceUtil.readUtf8Str("suiteReport.json");
		final JSONObject json = JSONUtil.parseObj(readUtf8Str);
		final SuiteReport bean = json.toBean(SuiteReport.class);

		// 第一层
		final List<CaseReport> caseReports = bean.getCaseReports();
		final CaseReport caseReport = caseReports.get(0);
		Assertions.assertNotNull(caseReport);

		// 第二层
		final List<StepReport> stepReports = caseReports.get(0).getStepReports();
		final StepReport stepReport = stepReports.get(0);
		Assertions.assertNotNull(stepReport);
	}

	/**
	 * 在JSON转Bean过程中，Bean中字段如果为父类定义的泛型类型，则应正确转换，此方法用于测试这类情况
	 */
	@Test
	public void toBeanTest6() {
		final JSONObject json = JSONUtil.ofObj()
				.putValue("targetUrl", "http://test.com")
				.putValue("success", "true")
				.putValue("result", JSONUtil.ofObj()
						.putValue("token", "tokenTest")
						.putValue("userId", "测试用户1"));

		final TokenAuthWarp2 bean = json.toBean(TokenAuthWarp2.class);
		assertEquals("http://test.com", bean.getTargetUrl());
		assertEquals("true", bean.getSuccess());

		final TokenAuthResponse result = bean.getResult();
		Assertions.assertNotNull(result);
		assertEquals("tokenTest", result.getToken());
		assertEquals("测试用户1", result.getUserId());
	}

	/**
	 * 泛型对象中的泛型参数如果未定义具体类型，按照JSON处理<br>
	 * 此处用于测试获取泛型类型实际类型错误导致的空指针问题
	 */
	@Test
	public void toBeanTest7() {
		final String jsonStr = " {\"result\":{\"phone\":\"15926297342\",\"appKey\":\"e1ie12e1ewsdqw1\"," +
				"\"secret\":\"dsadadqwdqs121d1e2\",\"message\":\"hello world\"},\"code\":100,\"" +
				"message\":\"validate message\"}";
		final ResultDto<?> dto = JSONUtil.toBean(jsonStr, ResultDto.class);
		assertEquals("validate message", dto.getMessage());
	}

	@Test
	public void parseBeanTest() {
		final UserA userA = new UserA();
		userA.setName("nameTest");
		userA.setDate(new Date());
		userA.setSqs(ListUtil.of(new Seq(null), new Seq("seq2")));

		final JSONObject json = JSONUtil.parseObj(userA, JSONConfig.of().setIgnoreNullValue(false));

		Assertions.assertTrue(json.containsKey("a"));
		Assertions.assertTrue(json.getJSONArray("sqs").getJSONObject(0).containsKey("seq"));
	}

	@Test
	public void parseBeanWithNumberListEnumTest() {
		final TestBean bean = new TestBean();
		bean.setDoubleValue(111.1);
		bean.setIntValue(123);
		bean.setList(ListUtil.of("a", "b", "c"));
		bean.setStrValue("strTest");
		bean.setTestEnum(TestEnum.TYPE_B);

		final JSONObject json = JSONUtil.parseObj(bean,
			JSONConfig.of().setIgnoreNullValue(false));

		assertEquals(111.1, json.getObj("doubleValue"));
		// 枚举转换检查，默认序列化枚举为其name
		assertEquals(TestEnum.TYPE_B.name(), json.getObj("testEnum"));

		final TestBean bean2 = json.toBean(TestBean.class);
		assertEquals(bean.toString(), bean2.toString());
	}

	@Test
	public void parseBeanTest3() {
		final JSONObject json = JSONUtil.ofObj()
				.putValue("code", 22)
				.putValue("data", "{\"jobId\": \"abc\", \"videoUrl\": \"http://a.com/a.mp4\"}");

		final JSONBean bean = json.toBean(JSONBean.class);
		assertEquals(22, bean.getCode());
		assertEquals("abc", bean.getData().getObj("jobId"));
		assertEquals("http://a.com/a.mp4", bean.getData().getObj("videoUrl"));
	}

	@Test
	public void beanTransTest() {
		final UserA userA = new UserA();
		userA.setA("A user");
		userA.setName("nameTest");
		userA.setDate(new Date());

		final JSONObject userAJson = JSONUtil.parseObj(userA);
		final UserB userB = JSONUtil.toBean(userAJson, UserB.class);

		assertEquals(userA.getName(), userB.getName());
		assertEquals(userA.getDate(), userB.getDate());
	}

	@Test
	public void beanTransTest2() {
		final UserA userA = new UserA();
		userA.setA("A user");
		userA.setName("nameTest");
		userA.setDate(DateUtil.parse("2018-10-25"));

		// 自定义日期格式
		final JSONObject userAJson = JSONUtil.parseObj(userA, JSONConfig.of().setDateFormat("yyyy-MM-dd"));

		final UserA bean = JSONUtil.toBean(userAJson.toString(), UserA.class);
		assertEquals(DateUtil.parse("2018-10-25"), bean.getDate());
	}

	@Test
	public void beanTransTest3() {
		final JSONObject userAJson = JSONUtil.ofObj()
				.putValue("a", "AValue")
				.putValue("name", "nameValue")
				.putValue("date", "08:00:00");
		final UserA bean = JSONUtil.toBean(userAJson.toString(), UserA.class);
		assertEquals(DateUtil.formatToday() + " 08:00:00", DateUtil.date(bean.getDate()).toString());
	}

	@Test
	public void parseFromBeanTest() {
		final UserA userA = new UserA();
		userA.setA(null);
		userA.setName("nameTest");
		userA.setDate(new Date());

		final JSONObject userAJson = JSONUtil.parseObj(userA);
		Assertions.assertFalse(userAJson.containsKey("a"));

		final JSONObject userAJsonWithNullValue = JSONUtil.parseObj(userA,
			JSONConfig.of().setIgnoreNullValue(false));
		Assertions.assertTrue(userAJsonWithNullValue.containsKey("a"));
		Assertions.assertTrue(userAJsonWithNullValue.containsKey("sqs"));
	}

	@Test
	public void specialCharTest() {
		final String json = "{\"pattern\": \"[abc]\b\u2001\", \"pattern2Json\": {\"patternText\": \"[ab]\\b\"}}";
		final JSONObject obj = JSONUtil.parseObj(json);
		assertEquals("[abc]\\b\\u2001", obj.getStrEscaped("pattern"));
		assertEquals("{\"patternText\":\"[ab]\\b\"}", obj.getStrEscaped("pattern2Json"));
	}

	@Test
	public void getStrTest() {
		final String json = "{\"name\": \"yyb\\nbbb\"}";
		final JSONObject jsonObject = JSONUtil.parseObj(json);

		// 没有转义按照默认规则显示
		assertEquals("yyb\nbbb", jsonObject.getStr("name"));
		// 转义按照字符串显示
		assertEquals("yyb\\nbbb", jsonObject.getStrEscaped("name"));

		final String bbb = jsonObject.getStr("bbb", "defaultBBB");
		assertEquals("defaultBBB", bbb);
	}

	@Test
	public void aliasTest() {
		final BeanWithAlias beanWithAlias = new BeanWithAlias();
		beanWithAlias.setValue1("张三");
		beanWithAlias.setValue2(35);

		final JSONObject jsonObject = JSONUtil.parseObj(beanWithAlias);
		assertEquals("张三", jsonObject.getStr("name"));
		assertEquals(Integer.valueOf(35), jsonObject.getInt("age"));

		final JSONObject json = JSONUtil.ofObj()
				.putValue("name", "张三")
				.putValue("age", 35);
		final BeanWithAlias bean = JSONUtil.toBean(Objects.requireNonNull(json).toString(), BeanWithAlias.class);
		assertEquals("张三", bean.getValue1());
		assertEquals(Integer.valueOf(35), bean.getValue2());
	}

	@Test
	public void setDateFormatTest() {
		final JSONConfig jsonConfig = JSONConfig.of();
		jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

		final JSONObject json = new JSONObject(jsonConfig);
		json.append("date", DateUtil.parse("2020-06-05 11:16:11"));
		json.append("bbb", "222");
		json.append("aaa", "123");
		assertEquals("{\"date\":\"2020-06-05 11:16:11\",\"bbb\":\"222\",\"aaa\":\"123\"}", json.toString());
	}

	@Test
	public void setDateFormatTest2() {
		final JSONConfig jsonConfig = JSONConfig.of();
		jsonConfig.setDateFormat("yyyy#MM#dd");

		final Date date = DateUtil.parse("2020-06-05 11:16:11");
		final JSONObject json = new JSONObject(jsonConfig);
		json.putValue("date", date);
		json.putValue("bbb", "222");
		json.putValue("aaa", "123");

		final String jsonStr = "{\"date\":\"2020#06#05\",\"bbb\":\"222\",\"aaa\":\"123\"}";

		assertEquals(jsonStr, json.toString());

		// 解析测试
		final JSONObject parse = JSONUtil.parseObj(jsonStr, jsonConfig);
		assertEquals(DateUtil.beginOfDay(date), parse.getDate("date"));
	}

	@Test
	public void setDateFormatSecondsTest() {
		// 自定义格式为只有秒的时间戳，一般用于JWT
		final JSONConfig jsonConfig = JSONConfig.of().setDateFormat(DateFormatManager.FORMAT_SECONDS);

		final Date date = DateUtil.parse("2020-06-05 11:16:11");
		final JSONObject json = new JSONObject(jsonConfig);
		json.putValue("date", date);

		assertEquals("{\"date\":1591326971}", json.toString());

		// 解析测试
		final JSONObject parse = JSONUtil.parseObj(json.toString(), jsonConfig);
		assertEquals(date, DateUtil.date(parse.getDate("date")));
	}

	@Test
	public void setCustomDateFormatTest() {
		final JSONConfig jsonConfig = JSONConfig.of();
		jsonConfig.setDateFormat(DateFormatManager.FORMAT_SECONDS);

		final Date date = DateUtil.parse("2020-06-05 11:16:11");
		final JSONObject json = new JSONObject(jsonConfig);
		json.putValue("date", date);
		json.putValue("bbb", "222");
		json.putValue("aaa", "123");

		final String jsonStr = "{\"date\":1591326971,\"bbb\":\"222\",\"aaa\":\"123\"}";

		assertEquals(jsonStr, json.toString());

		// 解析测试
		final JSONObject parse = JSONUtil.parseObj(jsonStr, jsonConfig);
		assertEquals(date, parse.getDate("date"));
	}

	@Test
	public void getTimestampTest() {
		final String timeStr = "1970-01-01 00:00:00";
		final JSONObject jsonObject = JSONUtil.ofObj().putValue("time", timeStr);
		final Timestamp time = jsonObject.get("time", Timestamp.class);
		assertEquals("1970-01-01 00:00:00.0", time.toString());
	}

	public enum TestEnum {
		TYPE_A, TYPE_B
	}

	/**
	 * 测试Bean
	 *
	 * @author Looly
	 */
	@Data
	public static class TestBean {
		private String strValue;
		private int intValue;
		private Double doubleValue;
		private SubBean beanValue;
		private List<String> list;
		private TestEnum testEnum;
	}

	/**
	 * 测试子Bean
	 *
	 * @author Looly
	 */
	@Data
	public static class SubBean {
		private String value1;
		private BigDecimal value2;
	}

	@Data
	public static class BeanWithAlias {
		@Alias("name")
		private String value1;
		@Alias("age")
		private Integer value2;
	}

	@Test
	public void parseBeanSameNameTest() {
		final SameNameBean sameNameBean = new SameNameBean();
		final JSONObject parse = JSONUtil.parseObj(sameNameBean);
		assertEquals("123", parse.getStr("username"));
		assertEquals("abc", parse.getStr("userName"));

		// 测试ignore注解是否有效
		assertNull(parse.getStr("fieldToIgnore"));
	}

	/**
	 * 测试子Bean
	 *
	 * @author Looly
	 */
	@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeStatic"})
	public static class SameNameBean {
		private final String username = "123";
		private final String userName = "abc";

		public String getUsername() {
			return username;
		}

		@PropIgnore
		private final String fieldToIgnore = "sfdsdads";

		public String getUserName() {
			return userName;
		}

		public String getFieldToIgnore() {
			return this.fieldToIgnore;
		}
	}

	@Test
	public void setEntryTest() {
		final HashMap<String, String> of = MapUtil.of("test", "testValue");
		final Set<Map.Entry<String, String>> entries = of.entrySet();
		final Map.Entry<String, String> next = entries.iterator().next();

		final JSONObject jsonObject = JSONUtil.parseObj(next);
		assertEquals("{\"test\":\"testValue\"}", jsonObject.toString());
	}

	@Test
	public void createJSONObjectTest() {
		Assertions.assertThrows(JSONException.class, ()->{
			// 集合类不支持转为JSONObject
			JSONUtil.parseObj(new JSONArray(), JSONConfig.of());
		});
	}

	@Test
	public void floatTest() {
		final Map<String, Object> map = new HashMap<>();
		map.put("c", 2.0F);

		final String s = JSONUtil.toJsonStr(map);
		assertEquals("{\"c\":2}", s);
	}

	@Test
	public void appendTest() {
		final JSONObject jsonObject = JSONUtil.ofObj().append("key1", "value1");
		assertEquals("{\"key1\":\"value1\"}", jsonObject.toString());

		jsonObject.append("key1", "value2");
		assertEquals("{\"key1\":[\"value1\",\"value2\"]}", jsonObject.toString());

		jsonObject.append("key1", "value3");
		assertEquals("{\"key1\":[\"value1\",\"value2\",\"value3\"]}", jsonObject.toString());
	}

	@Test
	public void putByPathTest() {
		final JSONObject json = new JSONObject();
		json.putByPath("aa.bb", "BB");
		assertEquals("{\"aa\":{\"bb\":\"BB\"}}", json.toString());
	}


	@Test
	public void bigDecimalTest() {
		final String jsonStr = "{\"orderId\":\"1704747698891333662002277\"}";
		final BigDecimalBean bigDecimalBean = JSONUtil.toBean(jsonStr, BigDecimalBean.class);
		assertEquals("{\"orderId\":1704747698891333662002277}", JSONUtil.toJsonStr(bigDecimalBean));
	}

	@Data
	static
	class BigDecimalBean {
		private BigDecimal orderId;
	}

	@Test
	public void filterIncludeTest() {
		final JSONObject json1 = JSONUtil.ofObj(JSONConfig.of())
				.putValue("a", "value1")
				.putValue("b", "value2")
				.putValue("c", "value3")
				.putValue("d", true);

		final String s = json1.toJSONString(0, (pair) -> pair.getKey().equals("b"));
		assertEquals("{\"b\":\"value2\"}", s);
	}

	@Test
	public void filterExcludeTest() {
		final JSONObject json1 = JSONUtil.ofObj(JSONConfig.of())
				.putValue("a", "value1")
				.putValue("b", "value2")
				.putValue("c", "value3")
				.putValue("d", true);

		final String s = json1.toJSONString(0, (pair) -> !pair.getKey().equals("b"));
		assertEquals("{\"a\":\"value1\",\"c\":\"value3\",\"d\":true}", s);
	}

	@Test
	public void editTest() {
		final JSONObject json1 = JSONUtil.ofObj(JSONConfig.of())
				.putValue("a", "value1")
				.putValue("b", "value2")
				.putValue("c", "value3")
				.putValue("d", true);

		final String s = json1.toJSONString(0, (pair) -> {
			if ("b".equals(pair.getKey())) {
				// 修改值为新值
				final JSONPrimitive value = (JSONPrimitive) pair.getValue();
				pair.setValue(value.getValue() + "_edit");
				return true;
			}
			// 除了"b"，其他都去掉
			return false;
		});
		assertEquals("{\"b\":\"value2_edit\"}", s);
	}

	@Test
	public void toUnderLineCaseTest() {
		final JSONObject json1 = JSONUtil.ofObj(JSONConfig.of())
				.putValue("aKey", "value1")
				.putValue("bJob", "value2")
				.putValue("cGood", "value3")
				.putValue("d", true);

		final String s = json1.toJSONString(0, (pair) -> {
			pair.setKey(StrUtil.toUnderlineCase((String)pair.getKey()));
			return true;
		});
		assertEquals("{\"a_key\":\"value1\",\"b_job\":\"value2\",\"c_good\":\"value3\",\"d\":true}", s);
	}

	@Test
	public void nullToEmptyTest() {
		final JSONObject json1 = JSONUtil.ofObj(JSONConfig.of().setIgnoreNullValue(false))
				.putNull("a")
				.putValue("b", "value2");

		final String s = json1.toJSONString(0, (pair) -> {
			pair.setValue(ObjUtil.defaultIfNull(pair.getValue(), StrUtil.EMPTY));
			return true;
		});
		assertEquals("{\"a\":\"\",\"b\":\"value2\"}", s);
	}

	@Test
	public void parseFilterTest() {
		final String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\", \"d\": true, \"e\": null}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject jsonObject = JSONUtil.parseObj(jsonStr, null, (pair)-> "b".equals(pair.getKey()));
		assertEquals(1, jsonObject.size());
		assertEquals("value2", jsonObject.getObj("b"));
	}

	@Test
	public void parseFilterEditTest() {
		final String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\", \"d\": true, \"e\": null}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONObject jsonObject = JSONUtil.parseObj(jsonStr, null, (pair)-> {
			if("b".equals(pair.getKey())){
				final JSONPrimitive primitive = (JSONPrimitive) pair.getValue();
				pair.setValue(primitive.getValue() + "_edit");
			}
			return true;
		});
		assertEquals("value2_edit", jsonObject.getObj("b"));
	}
}
