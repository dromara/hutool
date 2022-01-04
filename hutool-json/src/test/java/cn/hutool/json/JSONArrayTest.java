package cn.hutool.json;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.test.bean.Exam;
import cn.hutool.json.test.bean.JsonNode;
import cn.hutool.json.test.bean.KeyBean;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSONArray单元测试
 *
 * @author Looly
 *
 */
public class JSONArrayTest {

	@Test
	public void createJSONArrayTest(){
		Assertions.assertThrows(JSONException.class, () -> {
			// 集合类不支持转为JSONObject
			new JSONArray(new JSONObject(), JSONConfig.create());
		});
	}

	@Test
	public void addNullTest(){
		final List<String> aaa = ListUtil.of("aaa", null);
		String jsonStr = JSONUtil.toJsonStr(JSONUtil.parse(aaa,
				JSONConfig.create().setIgnoreNullValue(false)));
		Assertions.assertEquals("[\"aaa\",null]", jsonStr);
	}

	@Test
	public void addTest() {
		// 方法1
		JSONArray array = JSONUtil.createArray();
		// 方法2
		// JSONArray array = new JSONArray();
		array.add("value1");
		array.add("value2");
		array.add("value3");

		Assertions.assertEquals(array.get(0), "value1");
	}

	@Test
	public void parseTest() {
		String jsonStr = "[\"value1\", \"value2\", \"value3\"]";
		JSONArray array = JSONUtil.parseArray(jsonStr);
		Assertions.assertEquals(array.get(0), "value1");
	}

	@Test
	public void parseWithNullTest() {
		String jsonStr = "[{\"grep\":\"4.8\",\"result\":\"右\"},{\"grep\":\"4.8\",\"result\":null}]";
		JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
		Assertions.assertFalse(jsonArray.getJSONObject(1).containsKey("result"));

		// 不忽略null，则null的键值对被保留
		jsonArray = new JSONArray(jsonStr, false);
		Assertions.assertTrue(jsonArray.getJSONObject(1).containsKey("result"));
	}

	@Test
	public void parseFileTest() {
		JSONArray array = JSONUtil.readJSONArray(FileUtil.file("exam_test.json"), CharsetUtil.CHARSET_UTF_8);

		JSONObject obj0 = array.getJSONObject(0);
		Exam exam = JSONUtil.toBean(obj0, Exam.class);
		Assertions.assertEquals("0", exam.getAnswerArray()[0].getSeq());
	}

	@Test
	public void parseBeanListTest() {
		KeyBean b1 = new KeyBean();
		b1.setAkey("aValue1");
		b1.setBkey("bValue1");
		KeyBean b2 = new KeyBean();
		b2.setAkey("aValue2");
		b2.setBkey("bValue2");

		ArrayList<KeyBean> list = CollUtil.newArrayList(b1, b2);

		JSONArray jsonArray = JSONUtil.parseArray(list);
		Assertions.assertEquals("aValue1", jsonArray.getJSONObject(0).getStr("akey"));
		Assertions.assertEquals("bValue2", jsonArray.getJSONObject(1).getStr("bkey"));
	}

	@Test
	public void toListTest() {
		String jsonStr = FileUtil.readString("exam_test.json", CharsetUtil.CHARSET_UTF_8);
		JSONArray array = JSONUtil.parseArray(jsonStr);

		List<Exam> list = array.toList(Exam.class);
		Assertions.assertFalse(list.isEmpty());
		Assertions.assertSame(Exam.class, list.get(0).getClass());
	}

	@Test
	public void toListTest2() {
		String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";

		JSONArray array = JSONUtil.parseArray(jsonArr);
		List<User> userList = JSONUtil.toList(array, User.class);

		Assertions.assertFalse(userList.isEmpty());
		Assertions.assertSame(User.class, userList.get(0).getClass());

		Assertions.assertEquals(Integer.valueOf(111), userList.get(0).getId());
		Assertions.assertEquals(Integer.valueOf(112), userList.get(1).getId());

		Assertions.assertEquals("test1", userList.get(0).getName());
		Assertions.assertEquals("test2", userList.get(1).getName());
	}

	@Test
	public void toDictListTest() {
		String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";

		JSONArray array = JSONUtil.parseArray(jsonArr);

		List<Dict> list = JSONUtil.toList(array, Dict.class);

		Assertions.assertFalse(list.isEmpty());
		Assertions.assertSame(Dict.class, list.get(0).getClass());

		Assertions.assertEquals(Integer.valueOf(111), list.get(0).getInt("id"));
		Assertions.assertEquals(Integer.valueOf(112), list.get(1).getInt("id"));

		Assertions.assertEquals("test1", list.get(0).getStr("name"));
		Assertions.assertEquals("test2", list.get(1).getStr("name"));
	}

	@Test
	public void toArrayTest() {
		String jsonStr = FileUtil.readString("exam_test.json", CharsetUtil.CHARSET_UTF_8);
		JSONArray array = JSONUtil.parseArray(jsonStr);

		//noinspection SuspiciousToArrayCall
		Exam[] list = array.toArray(new Exam[0]);
		Assertions.assertNotEquals(0, list.length);
		Assertions.assertSame(Exam.class, list[0].getClass());
	}

	/**
	 * 单元测试用于测试在列表元素中有null时的情况下是否出错
	 */
	@Test
	public void toListWithNullTest() {
		String json = "[null,{'akey':'avalue','bkey':'bvalue'}]";
		JSONArray ja = JSONUtil.parseArray(json);

		List<KeyBean> list = ja.toList(KeyBean.class);
		Assertions.assertNull(list.get(0));
		Assertions.assertEquals("avalue", list.get(1).getAkey());
		Assertions.assertEquals("bvalue", list.get(1).getBkey());
	}

	@Test
	public void toListWithErrorTest(){
		Assertions.assertThrows(ConvertException.class, () -> {
			String json = "[['aaa',{'akey':'avalue','bkey':'bvalue'}]]";
			JSONArray ja = JSONUtil.parseArray(json);

			ja.toBean(new TypeReference<List<List<KeyBean>>>() {});
		});
	}

	@Test
	public void toBeanListTest() {
		List<Map<String, String>> mapList = new ArrayList<>();
		mapList.add(buildMap("0", "0", "0"));
		mapList.add(buildMap("1", "1", "1"));
		mapList.add(buildMap("+0", "+0", "+0"));
		mapList.add(buildMap("-0", "-0", "-0"));
		JSONArray jsonArray = JSONUtil.parseArray(mapList);
		List<JsonNode> nodeList = jsonArray.toList(JsonNode.class);

		Assertions.assertEquals(Long.valueOf(0L), nodeList.get(0).getId());
		Assertions.assertEquals(Long.valueOf(1L), nodeList.get(1).getId());
		Assertions.assertEquals(Long.valueOf(0L), nodeList.get(2).getId());
		Assertions.assertEquals(Long.valueOf(0L), nodeList.get(3).getId());

		Assertions.assertEquals(Integer.valueOf(0), nodeList.get(0).getParentId());
		Assertions.assertEquals(Integer.valueOf(1), nodeList.get(1).getParentId());
		Assertions.assertEquals(Integer.valueOf(0), nodeList.get(2).getParentId());
		Assertions.assertEquals(Integer.valueOf(0), nodeList.get(3).getParentId());

		Assertions.assertEquals("0", nodeList.get(0).getName());
		Assertions.assertEquals("1", nodeList.get(1).getName());
		Assertions.assertEquals("+0", nodeList.get(2).getName());
		Assertions.assertEquals("-0", nodeList.get(3).getName());
	}

	@Test
	public void getByPathTest(){
		String jsonStr = "[{\"id\": \"1\",\"name\": \"a\"},{\"id\": \"2\",\"name\": \"b\"}]";
		final JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
		Assertions.assertEquals("b", jsonArray.getByPath("[1].name"));
		Assertions.assertEquals("b", JSONUtil.getByPath(jsonArray, "[1].name"));
	}

	@Test
	public void putTest(){
		final JSONArray jsonArray = new JSONArray();
		jsonArray.put(3, "test");
		// 第三个位置插入值，0~2都是null
		Assertions.assertEquals(4, jsonArray.size());
	}

	// https://github.com/dromara/hutool/issues/1858
	@Test
	public void putTest2(){
		final JSONArray jsonArray = new JSONArray();
		jsonArray.put(0, 1);
		Assertions.assertEquals(1, jsonArray.size());
		Assertions.assertEquals(1, jsonArray.get(0));
	}

	private static Map<String, String> buildMap(String id, String parentId, String name) {
		Map<String, String> map = new HashMap<>();
		map.put("id", id);
		map.put("parentId", parentId);
		map.put("name", name);
		return map;
	}

	@Data
	static class User {
		private Integer id;
		private String name;
	}

	@Test
	public void filterIncludeTest(){
		JSONArray json1 = JSONUtil.createArray()
				.set("value1")
				.set("value2")
				.set("value3")
				.set(true);

		final String s = json1.toJSONString(0, (pair) -> pair.getValue().equals("value2"));
		Assertions.assertEquals("[\"value2\"]", s);
	}

	@Test
	public void filterExcludeTest(){
		JSONArray json1 = JSONUtil.createArray()
				.set("value1")
				.set("value2")
				.set("value3")
				.set(true);

		final String s = json1.toJSONString(0, (pair) -> false == pair.getValue().equals("value2"));
		Assertions.assertEquals("[\"value1\",\"value3\",true]", s);
	}

	@Test
	public void putNullTest(){
		final JSONArray array = JSONUtil.createArray(JSONConfig.create().setIgnoreNullValue(false));
		array.set(null);

		Assertions.assertEquals("[null]", array.toString());
	}
}
