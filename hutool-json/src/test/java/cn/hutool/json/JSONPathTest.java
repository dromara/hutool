package cn.hutool.json;

import cn.hutool.core.io.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * JSON路径单元测试
 *
 * @author looly
 *
 */
public class JSONPathTest {

	@Test
	public void getByPathTest() {
		String json = "[{\"id\":\"1\",\"name\":\"xingming\"},{\"id\":\"2\",\"name\":\"mingzi\"}]";
		Object value = JSONUtil.parseArray(json).getByPath("[0].name");
		Assert.assertEquals("xingming", value);
		value = JSONUtil.parseArray(json).getByPath("[1].name");
		Assert.assertEquals("mingzi", value);
	}

	@Test
	public void getByPathTest2() {
		String str = "{'accountId':111}";
		JSON json = JSONUtil.parse(str);
		Long accountId = JSONUtil.getByPath(json, "$.accountId", 0L);
		Assert.assertEquals(111L, accountId.longValue());
	}

	@Test
	public void getListByPathTest1() {
		String jsonContent = FileUtil.readUtf8String("test_json_path_001.json");
		JSON json = JSONUtil.parse(jsonContent);
		List<TestUser> resultList = json.getListByPath("testUserList[1].testArray", TestUser.class);
		Assert.assertNotNull(resultList);
		Assert.assertEquals(2, resultList.size());
		Assert.assertEquals("a", resultList.get(0).getUsername());
		Assert.assertEquals("a-password", resultList.get(0).getPassword());
		Assert.assertEquals("b", resultList.get(1).getUsername());
		Assert.assertEquals("b-password", resultList.get(1).getPassword());
	}

	public static class TestUser {
		private String username;
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
