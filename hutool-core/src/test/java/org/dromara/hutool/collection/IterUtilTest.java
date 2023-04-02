package org.dromara.hutool.collection;

import org.dromara.hutool.collection.iter.FilterIter;
import org.dromara.hutool.collection.iter.IterUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * {@link IterUtil} 单元测试
 *
 * @author looly
 */
public class IterUtilTest {

	@Test
	public void getFirstNonNullTest() {
		final List<String> strings = ListUtil.of(null, null, "123", "456", null);
		Assertions.assertEquals("123", CollUtil.getFirstNoneNull(strings));
	}

	@Test
	public void fieldValueMapTest() {
		final List<Car> carList = ListUtil.of(new Car("123", "大众"), new Car("345", "奔驰"), new Car("567", "路虎"));
		final Map<String, Car> carNameMap = IterUtil.fieldValueMap(carList.iterator(), "carNumber");

		Assertions.assertEquals("大众", carNameMap.get("123").getCarName());
		Assertions.assertEquals("奔驰", carNameMap.get("345").getCarName());
		Assertions.assertEquals("路虎", carNameMap.get("567").getCarName());
	}

	@Test
	public void joinTest() {
		final List<String> list = ListUtil.of("1", "2", "3", "4");
		final String join = IterUtil.join(list.iterator(), ":");
		Assertions.assertEquals("1:2:3:4", join);

		final List<Integer> list1 = ListUtil.of(1, 2, 3, 4);
		final String join1 = IterUtil.join(list1.iterator(), ":");
		Assertions.assertEquals("1:2:3:4", join1);

		// 包装每个节点
		final List<String> list2 = ListUtil.of("1", "2", "3", "4");
		final String join2 = IterUtil.join(list2.iterator(), ":", "\"", "\"");
		Assertions.assertEquals("\"1\":\"2\":\"3\":\"4\"", join2);
	}

	@Test
	public void joinWithFuncTest() {
		final List<String> list = ListUtil.of("1", "2", "3", "4");
		final String join = IterUtil.join(list.iterator(), ":", String::valueOf);
		Assertions.assertEquals("1:2:3:4", join);
	}

	@Test
	public void joinWithNullTest() {
		final List<String> list = ListUtil.of("1", null, "3", "4");
		final String join = IterUtil.join(list.iterator(), ":", String::valueOf);
		Assertions.assertEquals("1:null:3:4", join);
	}

	@Test
	public void testToListMap() {
		final Map<String, List<String>> expectedMap = new HashMap<>();
		expectedMap.put("a", Collections.singletonList("and"));
		expectedMap.put("b", Arrays.asList("brave", "back"));

		final Map<String, List<String>> testMap = IterUtil.toListMap(Arrays.asList("and", "brave", "back"),
				v -> v.substring(0, 1));
		Assertions.assertEquals(testMap, expectedMap);
	}

	@Test
	public void testToMap() {
		final Map<String, Car> expectedMap = new HashMap<>();

		final Car bmw = new Car("123", "bmw");
		expectedMap.put("123", bmw);

		final Car benz = new Car("456", "benz");
		expectedMap.put("456", benz);

		final Map<String, Car> testMap = IterUtil.toMap(Arrays.asList(bmw, benz), Car::getCarNumber);
		Assertions.assertEquals(expectedMap, testMap);
	}

	@Test
	public void getElementTypeTest() {
		final List<Integer> integers = Arrays.asList(null, 1);
		final Class<?> elementType = IterUtil.getElementType(integers);
		Assertions.assertEquals(Integer.class, elementType);
	}

	@Data
	@AllArgsConstructor
	public static class Car {
		private String carNumber;
		private String carName;
	}

	@Test
	public void removeTest() {
		final List<String> obj2 = ListUtil.of("3");
		final List<String> obj = ListUtil.of("1", "3");

		IterUtil.remove(obj.iterator(), (e)-> false == obj2.contains(e));

		Assertions.assertEquals(1, obj.size());
		Assertions.assertEquals("3", obj.get(0));
	}

	@Test
	public void filteredTest() {
		final List<String> obj2 = ListUtil.of("3");
		final List<String> obj = ListUtil.of("1", "3");

		final FilterIter<String> filtered = IterUtil.filtered(obj.iterator(), obj2::contains);

		Assertions.assertEquals("3", filtered.next());
		Assertions.assertFalse(filtered.hasNext());
	}

	@Test
	public void filterToListTest() {
		final List<String> obj2 = ListUtil.of("3");
		final List<String> obj = ListUtil.of("1", "3");

		final List<String> filtered = IterUtil.filterToList(obj.iterator(), obj2::contains);

		Assertions.assertEquals(1, filtered.size());
		Assertions.assertEquals("3", filtered.get(0));
	}

	@Test
	public void getTest() {
		final HashSet<String> set = SetUtil.ofLinked("A", "B", "C", "D");
		final String str = IterUtil.get(set.iterator(), 2);
		Assertions.assertEquals("C", str);
	}
}
