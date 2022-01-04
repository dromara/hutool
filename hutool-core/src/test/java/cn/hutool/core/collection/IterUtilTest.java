package cn.hutool.core.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link IterUtil} 单元测试
 * @author looly
 *
 */
public class IterUtilTest {

	@Test
	public void getFirstNonNullTest(){
		final ArrayList<String> strings = CollUtil.newArrayList(null, null, "123", "456", null);
		Assertions.assertEquals("123", IterUtil.getFirstNoneNull(strings));
	}

	@Test
	public void fieldValueMapTest() {
		ArrayList<Car> carList = CollUtil.newArrayList(new Car("123", "大众"), new Car("345", "奔驰"), new Car("567", "路虎"));
		Map<String, Car> carNameMap = IterUtil.fieldValueMap(carList.iterator(), "carNumber");

		Assertions.assertEquals("大众", carNameMap.get("123").getCarName());
		Assertions.assertEquals("奔驰", carNameMap.get("345").getCarName());
		Assertions.assertEquals("路虎", carNameMap.get("567").getCarName());
	}

	@Test
	public void joinTest() {
		ArrayList<String> list = CollUtil.newArrayList("1", "2", "3", "4");
		String join = IterUtil.join(list.iterator(), ":");
		Assertions.assertEquals("1:2:3:4", join);

		ArrayList<Integer> list1 = CollUtil.newArrayList(1, 2, 3, 4);
		String join1 = IterUtil.join(list1.iterator(), ":");
		Assertions.assertEquals("1:2:3:4", join1);

		// 包装每个节点
		ArrayList<String> list2 = CollUtil.newArrayList("1", "2", "3", "4");
		String join2 = IterUtil.join(list2.iterator(), ":", "\"", "\"");
		Assertions.assertEquals("\"1\":\"2\":\"3\":\"4\"", join2);
	}

	@Test
	public void joinWithFuncTest() {
		ArrayList<String> list = CollUtil.newArrayList("1", "2", "3", "4");
		String join = IterUtil.join(list.iterator(), ":", String::valueOf);
		Assertions.assertEquals("1:2:3:4", join);
	}

	@Test
	public void joinWithNullTest() {
		ArrayList<String> list = CollUtil.newArrayList("1", null, "3", "4");
		String join = IterUtil.join(list.iterator(), ":", String::valueOf);
		Assertions.assertEquals("1:null:3:4", join);
	}

	@Test
	public void testToListMap() {
		Map<String, List<String>> expectedMap = new HashMap<>();
		expectedMap.put("a", Collections.singletonList("and"));
		expectedMap.put("b", Arrays.asList("brave", "back"));

		Map<String, List<String>> testMap = IterUtil.toListMap(Arrays.asList("and", "brave", "back"),
				v -> v.substring(0, 1));
		Assertions.assertEquals(testMap, expectedMap);
	}

	@Test
	public void testToMap() {
		Map<String, Car> expectedMap = new HashMap<>();

		Car bmw = new Car("123", "bmw");
		expectedMap.put("123", bmw);

		Car benz = new Car("456", "benz");
		expectedMap.put("456", benz);

		Map<String, Car> testMap = IterUtil.toMap(Arrays.asList(bmw, benz), Car::getCarNumber);
		Assertions.assertEquals(expectedMap, testMap);
	}

	@Data
	@AllArgsConstructor
	public static class Car {
		private String carNumber;
		private String carName;
	}
}
