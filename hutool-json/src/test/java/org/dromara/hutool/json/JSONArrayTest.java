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
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.json.test.bean.Exam;
import org.dromara.hutool.json.test.bean.JsonNode;
import org.dromara.hutool.json.test.bean.KeyBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JSONArray单元测试
 *
 * @author Looly
 */
public class JSONArrayTest {

	@Test()
	public void createJSONArrayFromJSONObjectTest() {
		// JSONObject实现了Iterable接口，可以转换为JSONArray
		final JSONObject jsonObject = new JSONObject();

		JSONArray jsonArray = JSONUtil.parseArray(jsonObject, JSONConfig.of());
		assertEquals(new JSONArray(), jsonArray);

		jsonObject.set("key1", "value1");
		jsonArray = JSONUtil.parseArray(jsonObject, JSONConfig.of());
		assertEquals(1, jsonArray.size());
		assertEquals("[{\"key1\":\"value1\"}]", jsonArray.toString());
	}

	@Test
	public void addNullTest() {
		final List<String> aaa = ListUtil.view("aaa", null);
		final String jsonStr = JSONUtil.toJsonStr(JSONUtil.parse(aaa, JSONConfig.of().setIgnoreNullValue(false)));
		assertEquals("[\"aaa\",null]", jsonStr);
	}

	@Test
	public void addTest() {
		// 方法1
		final JSONArray array = JSONUtil.ofArray();
		// 方法2
		// JSONArray array = new JSONArray();
		array.set("value1");
		array.set("value2");
		array.set("value3");

		assertEquals(array.getObj(0), "value1");
	}

	@Test
	public void parseTest() {
		final String jsonStr = "[\"value1\", \"value2\", \"value3\"]";
		final JSONArray array = JSONUtil.parseArray(jsonStr);
		assertEquals(array.getObj(0), "value1");
	}

	@Test
	public void parseWithNullTest() {
		final String jsonStr = "[{\"grep\":\"4.8\",\"result\":\"右\"},{\"grep\":\"4.8\",\"result\":null}]";
		JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
		Assertions.assertFalse(jsonArray.getJSONObject(1).containsKey("result"));

		// 不忽略null，则null的键值对被保留
		jsonArray = JSONUtil.parseArray(jsonStr, JSONConfig.of().setIgnoreNullValue(false));
		assertTrue(jsonArray.getJSONObject(1).containsKey("result"));
	}

	@Test
	public void readJSONArrayFromFileTest() {
		final JSONArray array = JSONUtil.readJSONArray(FileUtil.file("exam_test.json"), CharsetUtil.UTF_8);

		final JSONObject obj0 = array.getJSONObject(0);
		final Exam exam = JSONUtil.toBean(obj0, Exam.class);
		assertEquals("0", exam.getAnswerArray()[0].getSeq());
	}

	@Test
	public void parseBeanListTest() {
		final KeyBean b1 = new KeyBean();
		b1.setAkey("aValue1");
		b1.setBkey("bValue1");
		final KeyBean b2 = new KeyBean();
		b2.setAkey("aValue2");
		b2.setBkey("bValue2");

		final ArrayList<KeyBean> list = ListUtil.of(b1, b2);

		final JSONArray jsonArray = JSONUtil.parseArray(list);
		assertEquals("aValue1", jsonArray.getJSONObject(0).getStr("akey"));
		assertEquals("bValue2", jsonArray.getJSONObject(1).getStr("bkey"));
	}

	@Test
	public void toListTest() {
		final String jsonStr = FileUtil.readString("exam_test.json", CharsetUtil.UTF_8);
		final JSONArray array = JSONUtil.parseArray(jsonStr);

		final List<Exam> list = array.toList(Exam.class);
		Assertions.assertFalse(list.isEmpty());
		Assertions.assertSame(Exam.class, list.get(0).getClass());
	}

	@Test
	public void toListTest2() {
		final String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";

		final JSONArray array = JSONUtil.parseArray(jsonArr);
		final List<User> userList = JSONUtil.toList(array, User.class);

		Assertions.assertFalse(userList.isEmpty());
		Assertions.assertSame(User.class, userList.get(0).getClass());

		assertEquals(Integer.valueOf(111), userList.get(0).getId());
		assertEquals(Integer.valueOf(112), userList.get(1).getId());

		assertEquals("test1", userList.get(0).getName());
		assertEquals("test2", userList.get(1).getName());
	}

	@Test
	public void toDictListTest() {
		final String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";

		final JSONArray array = JSONUtil.parseArray(jsonArr, JSONConfig.of().setIgnoreError(false));

		final List<Dict> list = JSONUtil.toList(array, Dict.class);

		Assertions.assertFalse(list.isEmpty());
		Assertions.assertSame(Dict.class, list.get(0).getClass());

		assertEquals(Integer.valueOf(111), list.get(0).getInt("id"));
		assertEquals(Integer.valueOf(112), list.get(1).getInt("id"));

		assertEquals("test1", list.get(0).getStr("name"));
		assertEquals("test2", list.get(1).getStr("name"));
	}

	@Test
	public void toArrayTest() {
		final String jsonStr = FileUtil.readString("exam_test.json", CharsetUtil.UTF_8);
		final JSONArray array = JSONUtil.parseArray(jsonStr);

		//noinspection SuspiciousToArrayCall
		final Exam[] list = array.toArray(new Exam[0]);
		Assertions.assertNotEquals(0, list.length);
		Assertions.assertSame(Exam.class, list[0].getClass());
	}

	/**
	 * 单元测试用于测试在列表元素中有null时的情况下是否出错
	 */
	@Test
	public void toListWithNullTest() {
		final String json = "[null,{'akey':'avalue','bkey':'bvalue'}]";
		final JSONArray ja = JSONUtil.parseArray(json, JSONConfig.of().setIgnoreNullValue(false));

		final List<KeyBean> list = ja.toList(KeyBean.class);
		Assertions.assertNull(list.get(0));
		assertEquals("avalue", list.get(1).getAkey());
		assertEquals("bvalue", list.get(1).getBkey());
	}

	@Test
	public void toListWithErrorTest() {
//		Assertions.assertThrows(JSONException.class, ()->{
//		});
		final String json = "[['aaa',{'akey':'avalue','bkey':'bvalue'}]]";
		final JSONArray ja = JSONUtil.parseArray(json);

		final Object bean = ja.toBean(new TypeReference<List<List<KeyBean>>>() {});

		Console.log(bean);
	}

	@Test
	public void toBeanListTest() {
		final List<Map<String, String>> mapList = new ArrayList<>();
		mapList.add(buildMap("0", "0", "0"));
		mapList.add(buildMap("1", "1", "1"));
		mapList.add(buildMap("+0", "+0", "+0"));
		mapList.add(buildMap("-0", "-0", "-0"));
		final JSONArray jsonArray = JSONUtil.parseArray(mapList);
		final List<JsonNode> nodeList = jsonArray.toList(JsonNode.class);

		assertEquals(Long.valueOf(0L), nodeList.get(0).getId());
		assertEquals(Long.valueOf(1L), nodeList.get(1).getId());
		assertEquals(Long.valueOf(0L), nodeList.get(2).getId());
		assertEquals(Long.valueOf(0L), nodeList.get(3).getId());

		assertEquals(Integer.valueOf(0), nodeList.get(0).getParentId());
		assertEquals(Integer.valueOf(1), nodeList.get(1).getParentId());
		assertEquals(Integer.valueOf(0), nodeList.get(2).getParentId());
		assertEquals(Integer.valueOf(0), nodeList.get(3).getParentId());

		assertEquals("0", nodeList.get(0).getName());
		assertEquals("1", nodeList.get(1).getName());
		assertEquals("+0", nodeList.get(2).getName());
		assertEquals("-0", nodeList.get(3).getName());
	}

	@Test
	public void getByPathTest() {
		final String jsonStr = "[{\"id\": \"1\",\"name\": \"a\"},{\"id\": \"2\",\"name\": \"b\"}]";
		final JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
		assertEquals("b", jsonArray.getByPath("[1].name"));
		assertEquals("b", JSONUtil.getByPath(jsonArray, "[1].name"));
	}

	@Test
	public void putToIndexTest() {
		JSONArray jsonArray = new JSONArray();
		jsonArray.setValue(3, "test");
		// 默认忽略null值，因此空位无值，只有一个值
		assertEquals(1, jsonArray.size());

		jsonArray = new JSONArray(JSONConfig.of().setIgnoreNullValue(false));
		jsonArray.setValue(2, "test");
		// 第三个位置插入值，0~2都是null
		assertEquals(3, jsonArray.size());
	}

	// https://github.com/dromara/hutool/issues/1858
	@Test
	public void putTest2() {
		final JSONArray jsonArray = new JSONArray();
		jsonArray.setValue(0, 1);
		assertEquals(1, jsonArray.size());
		assertEquals(1, jsonArray.get(0));
	}

	private static Map<String, String> buildMap(final String id, final String parentId, final String name) {
		final Map<String, String> map = new HashMap<>();
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
	public void filterIncludeTest() {
		final JSONArray json1 = JSONUtil.ofArray()
				.set("value1")
				.set("value2")
				.set("value3")
				.set(true);

		final String s = json1.toJSONString(0, (pair) -> pair.getValue().equals("value2"));
		assertEquals("[\"value2\"]", s);
	}

	@Test
	public void filterExcludeTest() {
		final JSONArray json1 = JSONUtil.ofArray()
				.set("value1")
				.set("value2")
				.set("value3")
				.set(true);

		final String s = json1.toJSONString(0, (pair) -> !pair.getValue().equals("value2"));
		assertEquals("[\"value1\",\"value3\",true]", s);
	}

	@Test
	public void putNullTest() {
		final JSONArray array = JSONUtil.ofArray(JSONConfig.of().setIgnoreNullValue(false));
		array.set(null);

		assertEquals("[null]", array.toString());
	}

	@Test
	public void parseFilterTest() {
		final String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONArray array = JSONUtil.parseArray(jsonArr, null, (mutable) -> mutable.get().toString().contains("111"));
		assertEquals(1, array.size());
		assertTrue(array.getJSONObject(0).containsKey("id"));
	}

	@Test
	public void parseFilterEditTest() {
		final String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";
		//noinspection MismatchedQueryAndUpdateOfCollection
		final JSONArray array = JSONUtil.parseArray(jsonArr, null, (mutable) -> {
			if(mutable.getKey() instanceof Integer){
				final JSONObject o = (JSONObject) mutable.getValue();
				if ("111".equals(o.getStr("id"))) {
					o.set("name", "test1_edit");
				}
			}
			return true;
		});
		assertEquals(2, array.size());
		assertTrue(array.getJSONObject(0).containsKey("id"));
		assertEquals("test1_edit", array.getJSONObject(0).getObj("name"));
	}

	@Test
	void jsonIterTest() {
		final JSONArray array = JSONUtil.ofArray();
		array.add(JSONUtil.ofObj().set("name", "aaa"));
		array.add(JSONUtil.ofObj().set("name", "bbb"));
		array.add(JSONUtil.ofObj().set("name", "ccc"));

		final StringBuilder result = new StringBuilder();
		array.jsonIter(JSONObject.class).forEach(result::append);
		assertEquals("{\"name\":\"aaa\"}{\"name\":\"bbb\"}{\"name\":\"ccc\"}", result.toString());
	}
}
