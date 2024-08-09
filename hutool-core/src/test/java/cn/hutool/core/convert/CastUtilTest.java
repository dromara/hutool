package cn.hutool.core.convert;

import cn.hutool.core.collection.CollUtil;
import static org.junit.jupiter.api.Assertions.*;
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
		Collection<Integer> collection= CollUtil.newLinkedList(1,2,3);
		List<Integer> list = CollUtil.newArrayList(1, 2, 3);
		Set<Integer> set = CollUtil.newHashSet(1, 2, 3);
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);

		Collection<Number> collection2 = CastUtil.castUp(collection);
		assertSame(collection, collection2);

		Collection<Integer> collection3 = CastUtil.castDown(collection2);
		assertSame(collection2, collection3);

		List<Number> list2 = CastUtil.castUp(list);
		assertSame(list, list2);
		List<Integer> list3 = CastUtil.castDown(list2);
		assertSame(list2, list3);

		Set<Number> set2 = CastUtil.castUp(set);
		assertSame(set, set2);
		Set<Integer> set3 = CastUtil.castDown(set2);
		assertSame(set2, set3);

		Map<Number, Serializable> map2 = CastUtil.castUp(map);
		assertSame(map, map2);
		Map<Integer, Number> map3 = CastUtil.castDown(map2);
		assertSame(map2, map3);
	}
}
