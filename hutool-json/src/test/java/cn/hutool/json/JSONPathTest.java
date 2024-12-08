package cn.hutool.json;

import static org.junit.jupiter.api.Assertions.*;

import cn.hutool.core.lang.TypeReference;
import org.junit.jupiter.api.Test;

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

	@Test
	public void getByPathTest3(){
		String str = "[{'accountId':1},{'accountId':2},{'accountId':3}]";
		JSON json = JSONUtil.parse(str);
		// 返回指定泛型的对象 List<Long>
		List<Long> accountIds = json.getByPath("$.accountId", new TypeReference<List<Long>>() {
		});
		assertNotNull(accountIds);
		assertArrayEquals(new Long[]{1L, 2L, 3L}, accountIds.toArray());

		str = "{\"accountInfos\": [{\"accountId\":1},{\"accountId\":2},{\"accountId\":3}]}";
		json = JSONUtil.parse(str);
		// 返回指定泛型的对象 List<Long>
		accountIds = json.getByPath("$.accountInfos.accountId", new TypeReference<List<Long>>() {
		});
		assertNotNull(accountIds);
		assertArrayEquals(new Long[]{1L, 2L, 3L}, accountIds.toArray());
	}
}
