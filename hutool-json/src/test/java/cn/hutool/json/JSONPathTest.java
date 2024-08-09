package cn.hutool.json;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
		assertEquals("xingming", value);
		value = JSONUtil.parseArray(json).getByPath("[1].name");
		assertEquals("mingzi", value);
	}

	@Test
	public void getByPathTest2(){
		String str = "{'accountId':111}";
		JSON json = JSONUtil.parse(str);
		Long accountId = JSONUtil.getByPath(json, "$.accountId", 0L);
		assertEquals(111L, accountId.longValue());
	}
}
