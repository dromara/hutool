package cn.hutool.json;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.test.bean.Exam;
import cn.hutool.json.test.bean.JsonNode;
import cn.hutool.json.test.bean.KeyBean;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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
	public void addTest() {
		// 方法1
		JSONArray array = JSONUtil.createArray();
		// 方法2
		// JSONArray array = new JSONArray();
		array.add("value1");
		array.add("value2");
		array.add("value3");

		Assert.assertEquals(array.get(0), "value1");
	}

	@Test
	public void parseTest() {
		String jsonStr = "[\"value1\", \"value2\", \"value3\"]";
		JSONArray array = JSONUtil.parseArray(jsonStr);
		Assert.assertEquals(array.get(0), "value1");
	}

	@Test
	public void parseWithNullTest() {
		String jsonStr = "[{\"grep\":\"4.8\",\"result\":\"右\"},{\"grep\":\"4.8\",\"result\":null}]";
		JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
		Assert.assertFalse(jsonArray.getJSONObject(1).containsKey("result"));

		// 不忽略null，则null的键值对被保留
		jsonArray = new JSONArray(jsonStr, false);
		Assert.assertTrue(jsonArray.getJSONObject(1).containsKey("result"));
	}

	@Test
	public void parseFileTest() {
		JSONArray array = JSONUtil.readJSONArray(FileUtil.file("exam_test.json"), CharsetUtil.CHARSET_UTF_8);

		JSONObject obj0 = array.getJSONObject(0);
		Exam exam = JSONUtil.toBean(obj0, Exam.class);
		Assert.assertEquals("0", exam.getAnswerArray()[0].getSeq());
	}

	@Test
	@Ignore
	public void parseBeanListTest() {
		KeyBean b1 = new KeyBean();
		b1.setAkey("aValue1");
		b1.setBkey("bValue1");
		KeyBean b2 = new KeyBean();
		b2.setAkey("aValue2");
		b2.setBkey("bValue2");

		ArrayList<KeyBean> list = CollUtil.newArrayList(b1, b2);

		JSONArray jsonArray = JSONUtil.parseArray(list);
		Console.log(jsonArray);
	}

	@Test
	public void toListTest() {
		String jsonStr = FileUtil.readString("exam_test.json", CharsetUtil.CHARSET_UTF_8);
		JSONArray array = JSONUtil.parseArray(jsonStr);

		List<Exam> list = array.toList(Exam.class);
		Assert.assertFalse(list.isEmpty());
		Assert.assertEquals(Exam.class, list.get(0).getClass());
	}

	@Test
	public void toListTest2() {
		String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";
		
		JSONArray array = JSONUtil.parseArray(jsonArr);
		List<User> userList = JSONUtil.toList(array, User.class);
		
		Assert.assertFalse(userList.isEmpty());
		Assert.assertEquals(User.class, userList.get(0).getClass());
		
		Assert.assertEquals(Integer.valueOf(111), userList.get(0).getId());
		Assert.assertEquals(Integer.valueOf(112), userList.get(1).getId());
		
		Assert.assertEquals("test1", userList.get(0).getName());
		Assert.assertEquals("test2", userList.get(1).getName());
	}
	
	@Test
	public void toDictListTest() {
		String jsonArr = "[{\"id\":111,\"name\":\"test1\"},{\"id\":112,\"name\":\"test2\"}]";
		
		JSONArray array = JSONUtil.parseArray(jsonArr);
		
		List<Dict> list = JSONUtil.toList(array, Dict.class);
		
		Assert.assertFalse(list.isEmpty());
		Assert.assertEquals(Dict.class, list.get(0).getClass());
		
		Assert.assertEquals(Integer.valueOf(111), list.get(0).getInt("id"));
		Assert.assertEquals(Integer.valueOf(112), list.get(1).getInt("id"));
		
		Assert.assertEquals("test1", list.get(0).getStr("name"));
		Assert.assertEquals("test2", list.get(1).getStr("name"));
	}

	@Test
	public void toArrayTest() {
		String jsonStr = FileUtil.readString("exam_test.json", CharsetUtil.CHARSET_UTF_8);
		JSONArray array = JSONUtil.parseArray(jsonStr);

		//noinspection SuspiciousToArrayCall
		Exam[] list = array.toArray(new Exam[0]);
		Assert.assertNotEquals(0, list.length);
		Assert.assertEquals(Exam.class, list[0].getClass());
	}

	/**
	 * 单元测试用于测试在列表元素中有null时的情况下是否出错
	 */
	@Test
	public void toListWithNullTest() {
		String json = "[null,{'akey':'avalue','bkey':'bvalue'}]";
		JSONArray ja = JSONUtil.parseArray(json);

		List<KeyBean> list = ja.toList(KeyBean.class);
		Assert.assertNull(list.get(0));
		Assert.assertEquals("avalue", list.get(1).getAkey());
		Assert.assertEquals("bvalue", list.get(1).getBkey());
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

		Assert.assertEquals(Long.valueOf(0L), nodeList.get(0).getId());
		Assert.assertEquals(Long.valueOf(1L), nodeList.get(1).getId());
		Assert.assertEquals(Long.valueOf(0L), nodeList.get(2).getId());
		Assert.assertEquals(Long.valueOf(0L), nodeList.get(3).getId());

		Assert.assertEquals(Integer.valueOf(0), nodeList.get(0).getParentId());
		Assert.assertEquals(Integer.valueOf(1), nodeList.get(1).getParentId());
		Assert.assertEquals(Integer.valueOf(0), nodeList.get(2).getParentId());
		Assert.assertEquals(Integer.valueOf(0), nodeList.get(3).getParentId());

		Assert.assertEquals("0", nodeList.get(0).getName());
		Assert.assertEquals("1", nodeList.get(1).getName());
		Assert.assertEquals("+0", nodeList.get(2).getName());
		Assert.assertEquals("-0", nodeList.get(3).getName());
	}

	private static Map<String, String> buildMap(String id, String parentId, String name) {
		Map<String, String> map = new HashMap<>();
		map.put("id", id);
		map.put("parentId", parentId);
		map.put("name", name);
		return map;
	}
	
	static class User {
		private Integer id;
		private String name;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + "]";
		}
	}
}
