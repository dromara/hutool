package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CastUtilTest {
	@Test
	public void testCastToSuper() {
		Collection<Integer> collection= ListUtil.of(1,2,3);
		List<Integer> list = ListUtil.of(1, 2, 3);
		Set<Integer> set = SetUtil.of(1, 2, 3);
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);

		Collection<Number> collection2 = CastUtil.castUp(collection);
		collection2.add(new Double("123.1"));
		Assertions.assertSame(collection, collection2);

		Collection<Integer> collection3 = CastUtil.castDown(collection2);
		Assertions.assertSame(collection2, collection3);

		List<Number> list2 = CastUtil.castUp(list);
		Assertions.assertSame(list, list2);
		List<Integer> list3 = CastUtil.castDown(list2);
		Assertions.assertSame(list2, list3);

		Set<Number> set2 = CastUtil.castUp(set);
		Assertions.assertSame(set, set2);
		Set<Integer> set3 = CastUtil.castDown(set2);
		Assertions.assertSame(set2, set3);

		Map<Number, Serializable> map2 = CastUtil.castUp(map);
		Assertions.assertSame(map, map2);
		Map<Integer, Number> map3 = CastUtil.castDown(map2);
		Assertions.assertSame(map2, map3);
	}
}
