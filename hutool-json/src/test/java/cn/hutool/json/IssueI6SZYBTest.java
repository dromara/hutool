package cn.hutool.json;

import cn.hutool.core.lang.Pair;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Map;

public class IssueI6SZYBTest {
	@Test
	public void pairTest() {
		Pair<Integer,Integer> pair = Pair.of(1, 2);
		String jsonStr = JSONUtil.toJsonStr(pair);
		assertEquals("{\"key\":1,\"value\":2}", jsonStr);

		final Pair bean = JSONUtil.toBean(jsonStr, Pair.class);
		assertEquals(pair, bean);
	}

	@Test
	public void entryTest() {
		Map.Entry<String,Integer> pair = new AbstractMap.SimpleEntry<>("1", 2);
		String jsonStr = JSONUtil.toJsonStr(pair);
		assertEquals("{\"1\":2}", jsonStr);

		final Map.Entry bean = JSONUtil.toBean(jsonStr, AbstractMap.SimpleEntry.class);
		assertEquals(pair, bean);
	}
}
