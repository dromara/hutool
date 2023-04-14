package org.dromara.hutool.core.collection;

import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.lang.page.PageInfo;
import org.dromara.hutool.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListUtilTest {

	@Test
	public void partitionTest() {
		List<List<Object>> lists = ListUtil.partition(null, 3);
		Assertions.assertEquals(ListUtil.empty(), lists);

		lists = ListUtil.partition(Arrays.asList(1, 2, 3, 4), 1);
		Assertions.assertEquals("[[1], [2], [3], [4]]", lists.toString());
		lists = ListUtil.partition(Arrays.asList(1, 2, 3, 4), 2);
		Assertions.assertEquals("[[1, 2], [3, 4]]", lists.toString());
		lists = ListUtil.partition(Arrays.asList(1, 2, 3, 4), 3);
		Assertions.assertEquals("[[1, 2, 3], [4]]", lists.toString());
		lists = ListUtil.partition(Arrays.asList(1, 2, 3, 4), 4);
		Assertions.assertEquals("[[1, 2, 3, 4]]", lists.toString());
		lists = ListUtil.partition(Arrays.asList(1, 2, 3, 4), 5);
		Assertions.assertEquals("[[1, 2, 3, 4]]", lists.toString());
	}

	@Test
	@Disabled
	public void partitionBenchTest() {
		final List<String> list = new ArrayList<>();
		CollUtil.padRight(list, RandomUtil.randomInt(1000_0000, 1_0000_0000), "test");

		final int size = RandomUtil.randomInt(10, 1000);
		Console.log("\nlist size: {}", list.size());
		Console.log("partition size: {}\n", size);
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("CollUtil#split");
		final List<List<String>> CollSplitResult = CollUtil.partition(list, size);
		stopWatch.stop();

		stopWatch.start("ListUtil#split");
		final List<List<String>> ListSplitResult = ListUtil.partition(list, size);
		stopWatch.stop();

		Assertions.assertEquals(CollSplitResult, ListSplitResult);

		Console.log(stopWatch.prettyPrint());
	}

	@Test
	public void splitAvgTest() {
		List<List<Object>> lists = ListUtil.avgPartition(null, 3);
		Assertions.assertEquals(ListUtil.empty(), lists);

		lists = ListUtil.avgPartition(Arrays.asList(1, 2, 3, 4), 1);
		Assertions.assertEquals("[[1, 2, 3, 4]]", lists.toString());
		lists = ListUtil.avgPartition(Arrays.asList(1, 2, 3, 4), 2);
		Assertions.assertEquals("[[1, 2], [3, 4]]", lists.toString());
		lists = ListUtil.avgPartition(Arrays.asList(1, 2, 3, 4), 3);
		Assertions.assertEquals("[[1, 2], [3], [4]]", lists.toString());
		lists = ListUtil.avgPartition(Arrays.asList(1, 2, 3, 4), 4);
		Assertions.assertEquals("[[1], [2], [3], [4]]", lists.toString());

		lists = ListUtil.avgPartition(Arrays.asList(1, 2, 3), 5);
		Assertions.assertEquals("[[1], [2], [3], [], []]", lists.toString());
		lists = ListUtil.avgPartition(Arrays.asList(1, 2, 3), 2);
		Assertions.assertEquals("[[1, 2], [3]]", lists.toString());
	}

	@Test
	public void splitAvgNotZero() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			// limit不能小于等于0
			ListUtil.avgPartition(Arrays.asList(1, 2, 3, 4), 0);
		});
	}

	@Test
	public void editTest() {
		final List<String> a = ListUtil.ofLinked("1", "2", "3");
		final List<String> filter = CollUtil.edit(a, str -> "edit" + str);
		Assertions.assertEquals("edit1", filter.get(0));
		Assertions.assertEquals("edit2", filter.get(1));
		Assertions.assertEquals("edit3", filter.get(2));
	}

	@Test
	public void indexOfAll() {
		final List<String> a = ListUtil.ofLinked("1", "2", "3", "4", "3", "2", "1");
		final int[] indexArray = CollUtil.indexOfAll(a, "2"::equals);
		Assertions.assertArrayEquals(new int[]{1, 5}, indexArray);
		final int[] indexArray2 = CollUtil.indexOfAll(a, "1"::equals);
		Assertions.assertArrayEquals(new int[]{0, 6}, indexArray2);
	}

	@Test
	public void pageTest1() {
		final List<Integer> a = ListUtil.ofLinked(1, 2, 3, 4, 5);

		final int[] a_1 = ListUtil.page(a, 0, 2).stream().mapToInt(Integer::valueOf).toArray();
		final int[] a1 = ListUtil.page(a, 0, 2).stream().mapToInt(Integer::valueOf).toArray();
		final int[] a2 = ListUtil.page(a, 1, 2).stream().mapToInt(Integer::valueOf).toArray();
		final int[] a3 = ListUtil.page(a, 2, 2).stream().mapToInt(Integer::valueOf).toArray();
		final int[] a4 = ListUtil.page(a, 3, 2).stream().mapToInt(Integer::valueOf).toArray();
		Assertions.assertArrayEquals(new int[]{1, 2}, a_1);
		Assertions.assertArrayEquals(new int[]{1, 2}, a1);
		Assertions.assertArrayEquals(new int[]{3, 4}, a2);
		Assertions.assertArrayEquals(new int[]{5}, a3);
		Assertions.assertArrayEquals(new int[]{}, a4);
	}

	@Test
	public void pageTest2() {
		final List<Integer> a = ListUtil.ofLinked(1, 2, 3, 4, 5);
		final int[] d1 = ListUtil.page(a, PageInfo.of(a.size(), 8).setFirstPageNo(0).setPageNo(0))
				.stream().mapToInt(Integer::valueOf).toArray();
		Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, d1);
	}

	@Test
	public void pageTest3() {
		final List<Integer> a = ListUtil.ofLinked(1, 2, 3, 4, 5);
		// page with consumer
		final List<List<Integer>> pageListData = new ArrayList<>();
		ListUtil.page(a, 2, pageListData::add);
		Assertions.assertArrayEquals(new int[]{1, 2}, pageListData.get(0).stream().mapToInt(Integer::valueOf).toArray());
		Assertions.assertArrayEquals(new int[]{3, 4}, pageListData.get(1).stream().mapToInt(Integer::valueOf).toArray());
		Assertions.assertArrayEquals(new int[]{5}, pageListData.get(2).stream().mapToInt(Integer::valueOf).toArray());


		pageListData.clear();
		ListUtil.page(a, 2, pageList -> {
			pageListData.add(pageList);
			if (pageList.get(0).equals(1)) {
				pageList.clear();
			}
		});
		Assertions.assertArrayEquals(new int[]{}, pageListData.get(0).stream().mapToInt(Integer::valueOf).toArray());
		Assertions.assertArrayEquals(new int[]{3, 4}, pageListData.get(1).stream().mapToInt(Integer::valueOf).toArray());
		Assertions.assertArrayEquals(new int[]{5}, pageListData.get(2).stream().mapToInt(Integer::valueOf).toArray());
	}

	@Test
	public void subTest() {
		final List<Integer> of = ListUtil.view(1, 2, 3, 4);
		final List<Integer> sub = ListUtil.sub(of, 2, 4);
		sub.remove(0);

		// 对子列表操作不影响原列表
		Assertions.assertEquals(4, of.size());
		Assertions.assertEquals(1, sub.size());
	}

	@Test
	public void sortByPropertyTest() {
		@Data
		@AllArgsConstructor
		class TestBean {
			private int order;
			private String name;
		}

		final List<TestBean> beanList = ListUtil.of(
				new TestBean(2, "test2"),
				new TestBean(1, "test1"),
				new TestBean(5, "test5"),
				new TestBean(4, "test4"),
				new TestBean(3, "test3")
		);

		final List<TestBean> order = ListUtil.sortByProperty(beanList, "order");
		Assertions.assertEquals("test1", order.get(0).getName());
		Assertions.assertEquals("test2", order.get(1).getName());
		Assertions.assertEquals("test3", order.get(2).getName());
		Assertions.assertEquals("test4", order.get(3).getName());
		Assertions.assertEquals("test5", order.get(4).getName());
	}

	@Test
	public void swapIndex() {
		final List<Integer> list = Arrays.asList(7, 2, 8, 9);
		ListUtil.swapTo(list, 8, 1);
		Assertions.assertEquals(8, (int) list.get(1));
	}

	@Test
	public void swapElement() {
		final Map<String, String> map1 = new HashMap<>();
		map1.put("1", "张三");
		final Map<String, String> map2 = new HashMap<>();
		map2.put("2", "李四");
		final Map<String, String> map3 = new HashMap<>();
		map3.put("3", "王五");
		final List<Map<String, String>> list = Arrays.asList(map1, map2, map3);
		ListUtil.swapElement(list, map2, map3);
		Map<String, String> map = list.get(2);
		Assertions.assertEquals("李四", map.get("2"));

		ListUtil.swapElement(list, map2, map1);
		map = list.get(0);
		Assertions.assertEquals("李四", map.get("2"));
	}

	@Test
	public void addAllIfNotContainsTest() {
		final ArrayList<String> list1 = new ArrayList<>();
		list1.add("1");
		list1.add("2");
		final ArrayList<String> list2 = new ArrayList<>();
		list2.add("2");
		list2.add("3");
		ListUtil.addAllIfNotContains(list1, list2);

		Assertions.assertEquals(3, list1.size());
		Assertions.assertEquals("1", list1.get(0));
		Assertions.assertEquals("2", list1.get(1));
		Assertions.assertEquals("3", list1.get(2));
	}

	@Test
	public void setOrPaddingNullTest() {
		final List<String> list = new ArrayList<>();
		list.add("1");

		// 替换原值
		ListUtil.setOrPadding(list, 0, "a");
		Assertions.assertEquals("[a]", list.toString());

		//append值
		ListUtil.setOrPadding(list, 1, "a");
		Assertions.assertEquals("[a, a]", list.toString());

		// padding null 后加入值
		ListUtil.setOrPadding(list, 3, "a");
		Assertions.assertEquals(4, list.size());
	}

	@Test
	public void ofCopyOnWriteTest(){
		final CopyOnWriteArrayList<String> strings = ListUtil.ofCopyOnWrite(ListUtil.of("a", "b"));
		Assertions.assertEquals(2, strings.size());
	}

	@Test
	public void ofCopyOnWriteTest2(){
		final CopyOnWriteArrayList<String> strings = ListUtil.ofCopyOnWrite("a", "b");
		Assertions.assertEquals(2, strings.size());
	}

	@Test
	void reverseNewTest() {
		final List<Integer> view = ListUtil.view(1, 2, 3);
		final List<Integer> reverse = ListUtil.reverseNew(view);
		Assertions.assertEquals("[3, 2, 1]", reverse.toString());
	}
}
