package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

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
	public void getByPathTest2(){
		String str = "{'accountId':111}";
		JSON json = JSONUtil.parse(str);
		Long accountId = JSONUtil.getByPath(json, "$.accountId", 0L);
		Assert.assertEquals(111L, accountId.longValue());
	}
}
