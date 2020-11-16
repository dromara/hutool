package cn.hutool.json;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.annotation.PropIgnore;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.test.bean.JSONBean;
import cn.hutool.json.test.bean.ResultDto;
import cn.hutool.json.test.bean.Seq;
import cn.hutool.json.test.bean.TokenAuthResponse;
import cn.hutool.json.test.bean.TokenAuthWarp2;
import cn.hutool.json.test.bean.UserA;
import cn.hutool.json.test.bean.UserB;
import cn.hutool.json.test.bean.UserWithMap;
import cn.hutool.json.test.bean.report.CaseReport;
import cn.hutool.json.test.bean.report.StepReport;
import cn.hutool.json.test.bean.report.SuiteReport;
import lombok.Data;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * JSONObject单元测试
 *
 * @author Looly
 */
public class JSONObjectTest {

	@Test
	@Ignore
	public void toStringTest() {
		String str = "{\"code\": 500, \"data\":null}";
		JSONObject jsonObject = new JSONObject(str);
		Console.log(jsonObject);
		jsonObject.getConfig().setIgnoreNullValue(true);
		Console.log(jsonObject.toStringPretty());
	}

	@Test
	public void toStringTest2() {
		String str = "{\"test\":\"关于开展2018年度“文明集体”、“文明职工”评选表彰活动的通知\"}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		JSONObject json = new JSONObject(str);
		Assert.assertEquals(str, json.toString());
	}

	/**
	 * 测试JSON中自定义日期格式输出正确性
	 */
	@Test
	public void toStringTest3() {
		JSONObject json = Objects.requireNonNull(JSONUtil.createObj()//
				.set("dateTime", DateUtil.parse("2019-05-02 22:12:01")))//
				.setDateFormat(DatePattern.NORM_DATE_PATTERN);
		Assert.assertEquals("{\"dateTime\":\"2019-05-02\"}", json.toString());
	}

	@Test
	public void toStringWithDateTest() {
		JSONObject json = JSONUtil.createObj().set("date", DateUtil.parse("2019-05-08 19:18:21"));
		assert json != null;
		Assert.assertEquals("{\"date\":1557314301000}", json.toString());

		json = Objects.requireNonNull(JSONUtil.createObj().set("date", DateUtil.parse("2019-05-08 19:18:21"))).setDateFormat(DatePattern.NORM_DATE_PATTERN);
		Assert.assertEquals("{\"date\":\"2019-05-08\"}", json.toString());
	}


	@Test
	public void putAllTest() {
		JSONObject json1 = JSONUtil.createObj()
		.set("a", "value1")
		.set("b", "value2")
		.set("c", "value3")
		.set("d", true);

		JSONObject json2 = JSONUtil.createObj()
		.set("a", "value21")
		.set("b", "value22");

		// putAll操作会覆盖相同key的值，因此a,b两个key的值改变，c的值不变
		json1.putAll(json2);

		Assert.assertEquals(json1.get("a"), "value21");
		Assert.assertEquals(json1.get("b"), "value22");
		Assert.assertEquals(json1.get("c"), "value3");
	}

	@Test
	public void parseStringTest() {
		String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\", \"d\": true, \"e\": null}";
		JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
		Assert.assertEquals(jsonObject.get("a"), "value1");
		Assert.assertEquals(jsonObject.get("b"), "value2");
		Assert.assertEquals(jsonObject.get("c"), "value3");
		Assert.assertEquals(jsonObject.get("d"), true);

		Assert.assertTrue(jsonObject.containsKey("e"));
		Assert.assertEquals(jsonObject.get("e"), JSONNull.NULL);
	}

	@Test
	public void parseStringTest2() {
		String jsonStr = "{\"file_name\":\"RMM20180127009_731.000\",\"error_data\":\"201121151350701001252500000032 18973908335 18973908335 13601893517 201711211700152017112115135420171121 6594000000010100000000000000000000000043190101701001910072 100001100 \",\"error_code\":\"F140\",\"error_info\":\"最早发送时间格式错误，该字段可以为空，当不为空时正确填写格式为“YYYYMMDDHHMISS”\",\"app_name\":\"inter-pre-check\"}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		JSONObject json = new JSONObject(jsonStr);
		Assert.assertEquals("F140", json.getStr("error_code"));
		Assert.assertEquals("最早发送时间格式错误，该字段可以为空，当不为空时正确填写格式为“YYYYMMDDHHMISS”", json.getStr("error_info"));
	}

	@Test
	public void parseStringTest3() {
		String jsonStr = "{\"test\":\"体”、“文\"}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		JSONObject json = new JSONObject(jsonStr);
		Assert.assertEquals("体”、“文", json.getStr("test"));
	}

	@Test
	public void parseStringTest4() {
		String jsonStr = "{'msg':'这里还没有内容','data':{'cards':[]},'ok':0}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		JSONObject json = new JSONObject(jsonStr);
		Assert.assertEquals(new Integer(0), json.getInt("ok"));
		Assert.assertEquals(new JSONArray(), json.getJSONObject("data").getJSONArray("cards"));
	}

	@Test
	@Ignore
	public void parseStringWithBomTest() {
		String jsonStr = FileUtil.readUtf8String("f:/test/jsontest.txt");
		JSONObject json = new JSONObject(jsonStr);
		JSONObject json2 = JSONUtil.parseObj(json);
		Console.log(json);
		Console.log(json2);
	}

	@Test
	public void parseStringWithSlashTest() {
		//在5.3.2之前，</div>中的/会被转义，修复此bug的单元测试
		String jsonStr = "{\"a\":\"<div>aaa</div>\"}";
		//noinspection MismatchedQueryAndUpdateOfCollection
		JSONObject json = new JSONObject(jsonStr);
		Assert.assertEquals("<div>aaa</div>", json.get("a"));
		Assert.assertEquals(jsonStr, json.toString());
	}

	@Test
	public void toBeanTest() {
		JSONObject subJson = JSONUtil.createObj().set("value1", "strValue1").set("value2", "234");
		JSONObject json = JSONUtil.createObj().set("strValue", "strTest").set("intValue", 123)
				// 测试空字符串转对象
				.set("doubleValue", "")
				.set("beanValue", subJson)
				.set("list", JSONUtil.createArray().set("a").set("b")).set("testEnum", "TYPE_A");

		TestBean bean = json.toBean(TestBean.class);
		Assert.assertEquals("a", bean.getList().get(0));
		Assert.assertEquals("b", bean.getList().get(1));

		Assert.assertEquals("strValue1", bean.getBeanValue().getValue1());
		// BigDecimal转换检查
		Assert.assertEquals(new BigDecimal("234"), bean.getBeanValue().getValue2());
		// 枚举转换检查
		Assert.assertEquals(TestEnum.TYPE_A, bean.getTestEnum());
	}

	@Test
	public void toBeanNullStrTest() {
		JSONObject json = JSONUtil.createObj()//
				.set("strValue", "null")//
				.set("intValue", 123)//
				.set("beanValue", "null")//
				.set("list", JSONUtil.createArray().set("a").set("b"));

		TestBean bean = json.toBean(TestBean.class);
		// 当JSON中为字符串"null"时应被当作字符串处理
		Assert.assertEquals("null", bean.getStrValue());
		// 当JSON中为字符串"null"时Bean中的字段类型不匹配应在ignoreError模式下忽略注入
		Assert.assertNull(bean.getBeanValue());
	}

	@Test
	public void toBeanTest2() {
		UserA userA = new UserA();
		userA.setA("A user");
		userA.setName("{\n\t\"body\":{\n\t\t\"loginId\":\"id\",\n\t\t\"password\":\"pwd\"\n\t}\n}");
		userA.setDate(new Date());
		userA.setSqs(CollectionUtil.newArrayList(new Seq("seq1"), new Seq("seq2")));

		JSONObject json = JSONUtil.parseObj(userA);
		UserA userA2 = json.toBean(UserA.class);
		// 测试数组
		Assert.assertEquals("seq1", userA2.getSqs().get(0).getSeq());
		// 测试带换行符等特殊字符转换是否成功
		Assert.assertTrue(StrUtil.isNotBlank(userA2.getName()));
	}

	@Test
	public void toBeanWithNullTest() {
		String jsonStr = "{'data':{'userName':'ak','password': null}}";
		Console.log(JSONUtil.parseObj(jsonStr));
		UserWithMap user = JSONUtil.toBean(JSONUtil.parseObj(jsonStr), UserWithMap.class);
		Assert.assertTrue(user.getData().containsKey("password"));
	}

	@Test
	public void toBeanTest4() {
		String json = "{\"data\":{\"b\": \"c\"}}";

		UserWithMap map = JSONUtil.toBean(json, UserWithMap.class);
		Assert.assertEquals("c", map.getData().get("b"));
	}

	@Test
	public void toBeanTest5() {
		String readUtf8Str = ResourceUtil.readUtf8Str("suiteReport.json");
		JSONObject json = JSONUtil.parseObj(readUtf8Str);
		SuiteReport bean = json.toBean(SuiteReport.class);

		// 第一层
		List<CaseReport> caseReports = bean.getCaseReports();
		CaseReport caseReport = caseReports.get(0);
		Assert.assertNotNull(caseReport);

		// 第二层
		List<StepReport> stepReports = caseReports.get(0).getStepReports();
		StepReport stepReport = stepReports.get(0);
		Assert.assertNotNull(stepReport);
	}

	/**
	 * 在JSON转Bean过程中，Bean中字段如果为父类定义的泛型类型，则应正确转换，此方法用于测试这类情况
	 */
	@Test
	public void toBeanTest6() {
		JSONObject json = JSONUtil.createObj()
				.set("targetUrl", "http://test.com")
				.set("success", "true")
				.set("result", JSONUtil.createObj()
						.set("token", "tokenTest")
						.set("userId", "测试用户1"));

		TokenAuthWarp2 bean = json.toBean(TokenAuthWarp2.class);
		Assert.assertEquals("http://test.com", bean.getTargetUrl());
		Assert.assertEquals("true", bean.getSuccess());

		TokenAuthResponse result = bean.getResult();
		Assert.assertNotNull(result);
		Assert.assertEquals("tokenTest", result.getToken());
		Assert.assertEquals("测试用户1", result.getUserId());
	}

	/**
	 * 泛型对象中的泛型参数如果未定义具体类型，按照JSON处理<br>
	 * 此处用于测试获取泛型类型实际类型错误导致的空指针问题
	 */
	@Test
	public void toBeanTest7() {
		String jsonStr = " {\"result\":{\"phone\":\"15926297342\",\"appKey\":\"e1ie12e1ewsdqw1\"," +
				"\"secret\":\"dsadadqwdqs121d1e2\",\"message\":\"hello world\"},\"code\":100,\"" +
				"message\":\"validate message\"}";
		ResultDto<?> dto = JSONUtil.toBean(jsonStr, ResultDto.class);
		Assert.assertEquals("validate message", dto.getMessage());
	}

	@Test
	public void parseBeanTest() {
		UserA userA = new UserA();
		userA.setName("nameTest");
		userA.setDate(new Date());
		userA.setSqs(CollectionUtil.newArrayList(new Seq(null), new Seq("seq2")));

		JSONObject json = JSONUtil.parseObj(userA, false, true);

		Assert.assertTrue(json.containsKey("a"));
		Assert.assertTrue(json.getJSONArray("sqs").getJSONObject(0).containsKey("seq"));
	}

	@Test
	public void parseBeanTest2() {
		TestBean bean = new TestBean();
		bean.setDoubleValue(111.1);
		bean.setIntValue(123);
		bean.setList(CollUtil.newArrayList("a", "b", "c"));
		bean.setStrValue("strTest");
		bean.setTestEnum(TestEnum.TYPE_B);

		JSONObject json = JSONUtil.parseObj(bean, false);
		// 枚举转换检查
		Assert.assertEquals("TYPE_B", json.get("testEnum"));

		TestBean bean2 = json.toBean(TestBean.class);
		Assert.assertEquals(bean.toString(), bean2.toString());
	}

	@Test
	public void parseBeanTest3() {
		JSONObject json = JSONUtil.createObj()
				.set("code", 22)
				.set("data", "{\"jobId\": \"abc\", \"videoUrl\": \"http://a.com/a.mp4\"}");

		JSONBean bean = json.toBean(JSONBean.class);
		Assert.assertEquals(22, bean.getCode());
		Assert.assertEquals("abc", bean.getData().getObj("jobId"));
		Assert.assertEquals("http://a.com/a.mp4", bean.getData().getObj("videoUrl"));
	}

	@Test
	public void beanTransTest() {
		UserA userA = new UserA();
		userA.setA("A user");
		userA.setName("nameTest");
		userA.setDate(new Date());

		JSONObject userAJson = JSONUtil.parseObj(userA);
		UserB userB = JSONUtil.toBean(userAJson, UserB.class);

		Assert.assertEquals(userA.getName(), userB.getName());
		Assert.assertEquals(userA.getDate(), userB.getDate());
	}

	@Test
	public void beanTransTest2() {
		UserA userA = new UserA();
		userA.setA("A user");
		userA.setName("nameTest");
		userA.setDate(DateUtil.parse("2018-10-25"));

		JSONObject userAJson = JSONUtil.parseObj(userA);
		// 自定义日期格式
		userAJson.setDateFormat("yyyy-MM-dd");

		UserA bean = JSONUtil.toBean(userAJson.toString(), UserA.class);
		Assert.assertEquals(DateUtil.parse("2018-10-25"), bean.getDate());
	}

	@Test
	public void beanTransTest3() {
		JSONObject userAJson = JSONUtil.createObj()
				.set("a", "AValue")
				.set("name", "nameValue")
				.set("date", "08:00:00");
		UserA bean = JSONUtil.toBean(userAJson.toString(), UserA.class);
		Assert.assertEquals(DateUtil.today() + " 08:00:00", DateUtil.date(bean.getDate()).toString());
	}

	@Test
	public void parseFromBeanTest() {
		UserA userA = new UserA();
		userA.setA(null);
		userA.setName("nameTest");
		userA.setDate(new Date());

		JSONObject userAJson = JSONUtil.parseObj(userA);
		Assert.assertFalse(userAJson.containsKey("a"));

		JSONObject userAJsonWithNullValue = JSONUtil.parseObj(userA, false);
		Assert.assertTrue(userAJsonWithNullValue.containsKey("a"));
		Assert.assertTrue(userAJsonWithNullValue.containsKey("sqs"));
	}

	@Test
	public void specialCharTest() {
		String json = "{\"pattern\": \"[abc]\b\u2001\", \"pattern2Json\": {\"patternText\": \"[ab]\\b\"}}";
		JSONObject obj = JSONUtil.parseObj(json);
		Assert.assertEquals("[abc]\\b\\u2001", obj.getStrEscaped("pattern"));
		Assert.assertEquals("{\"patternText\":\"[ab]\\b\"}", obj.getStrEscaped("pattern2Json"));
	}

	@Test
	public void getStrTest() {
		String json = "{\"name\": \"yyb\\nbbb\"}";
		JSONObject jsonObject = JSONUtil.parseObj(json);

		// 没有转义按照默认规则显示
		Assert.assertEquals("yyb\nbbb", jsonObject.getStr("name"));
		// 转义按照字符串显示
		Assert.assertEquals("yyb\\nbbb", jsonObject.getStrEscaped("name"));

		String bbb = jsonObject.getStr("bbb", "defaultBBB");
		Assert.assertEquals("defaultBBB", bbb);
	}

	@Test
	public void aliasTest(){
		final BeanWithAlias beanWithAlias = new BeanWithAlias();
		beanWithAlias.setValue1("张三");
		beanWithAlias.setValue2(35);

		final JSONObject jsonObject = JSONUtil.parseObj(beanWithAlias);
		Assert.assertEquals("张三", jsonObject.getStr("name"));
		Assert.assertEquals(new Integer(35), jsonObject.getInt("age"));

		JSONObject json = JSONUtil.createObj()
				.set("name", "张三")
				.set("age", 35);
		final BeanWithAlias bean = JSONUtil.toBean(Objects.requireNonNull(json).toString(), BeanWithAlias.class);
		Assert.assertEquals("张三", bean.getValue1());
		Assert.assertEquals(new Integer(35), bean.getValue2());
	}

	@Test
	public void setDateFormatTest(){
		JSONConfig jsonConfig = JSONConfig.create();
		jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
		jsonConfig.setOrder(true);

		JSONObject json = new JSONObject(jsonConfig);
		json.append("date", DateUtil.parse("2020-06-05 11:16:11"));
		json.append("bbb", "222");
		json.append("aaa", "123");
		Assert.assertEquals("{\"date\":[\"2020-06-05 11:16:11\"],\"bbb\":[\"222\"],\"aaa\":[\"123\"]}", json.toString());
	}

	@Test
	public void getTimestampTest(){
		String timeStr = "1970-01-01 00:00:00";
		final JSONObject jsonObject = JSONUtil.createObj().set("time", timeStr);
		final Timestamp time = jsonObject.get("time", Timestamp.class);
		Assert.assertEquals("1970-01-01 00:00:00.0", time.toString());
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
	public void parseBeanSameNameTest(){
		final SameNameBean sameNameBean = new SameNameBean();
		final JSONObject parse = JSONUtil.parseObj(sameNameBean);
		Assert.assertEquals("123", parse.getStr("username"));
		Assert.assertEquals("abc", parse.getStr("userName"));

		// 测试ignore注解是否有效
		Assert.assertNull(parse.getStr("fieldToIgnore"));
	}

	/**
	 * 测试子Bean
	 *
	 * @author Looly
	 */
	@SuppressWarnings("FieldCanBeLocal")
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

		public String getFieldToIgnore(){
			return this.fieldToIgnore;
		}
	}

	@Test
	public void setEntryTest(){
		final HashMap<String, String> of = MapUtil.of("test", "testValue");
		final Set<Map.Entry<String, String>> entries = of.entrySet();
		final Map.Entry<String, String> next = entries.iterator().next();

		final JSONObject jsonObject = JSONUtil.parseObj(next);
		Console.log(jsonObject);
	}

	@Test(expected = JSONException.class)
	public void createJSONObjectTest(){
		// 集合类不支持转为JSONObject
		new JSONObject(new JSONArray(), JSONConfig.create());
	}

	@Test
	public void floatTest(){
		Map<String, Object> map = new HashMap<>();
		map.put("c", 2.0F);

		final String s = JSONUtil.toJsonStr(map);
		Console.log(s);
	}
}
