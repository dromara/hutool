package cn.hutool.json;

import cn.hutool.core.lang.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;

public class IssueI6SZYBTest {
	@Test
	public void pairTest() {
		Pair<Integer,Integer> pair = Pair.of(1, 2);
		String jsonStr = JSONUtil.toJsonStr(pair);
		Assert.assertTrue(jsonStr.equals("{\"key\":1,\"value\":2}") || jsonStr.equals("{\"value\":2,\"key\":1}"));

		final Pair bean = JSONUtil.toBean(jsonStr, Pair.class);
		Assert.assertEquals(pair, bean);
	}

	@Test
	public void entryTest() {
		Map.Entry<String,Integer> pair = new AbstractMap.SimpleEntry<>("1", 2);
		String jsonStr = JSONUtil.toJsonStr(pair);
		Assert.assertEquals("{\"1\":2}", jsonStr);

		final Map.Entry bean = JSONUtil.toBean(jsonStr, AbstractMap.SimpleEntry.class);
		Assert.assertEquals(pair, bean);
	}
}
