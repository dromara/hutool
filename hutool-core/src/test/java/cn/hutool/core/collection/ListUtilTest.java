package cn.hutool.core.collection;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtilTest {

	@Test
	public void splitTest() {
		List<List<Object>> lists = ListUtil.split(null, 3);
		Assert.assertEquals(ListUtil.empty(), lists);

		lists = ListUtil.split(Arrays.asList(1, 2, 3, 4), 1);
		Assert.assertEquals("[[1], [2], [3], [4]]", lists.toString());
		lists = ListUtil.split(Arrays.asList(1, 2, 3, 4), 2);
		Assert.assertEquals("[[1, 2], [3, 4]]", lists.toString());
		lists = ListUtil.split(Arrays.asList(1, 2, 3, 4), 3);
		Assert.assertEquals("[[1, 2, 3], [4]]", lists.toString());
		lists = ListUtil.split(Arrays.asList(1, 2, 3, 4), 4);
		Assert.assertEquals("[[1, 2, 3, 4]]", lists.toString());
		lists = ListUtil.split(Arrays.asList(1, 2, 3, 4), 5);
		Assert.assertEquals("[[1, 2, 3, 4]]", lists.toString());
	}

	@Test
	@Ignore
	public void splitBenchTest() {
		final List<String> list = new ArrayList<>();
		CollUtil.padRight(list, RandomUtil.randomInt(1000_0000, 1_0000_0000), "test");

		final int size = RandomUtil.randomInt(10, 1000);
		Console.log("\nlist size: {}", list.size());
		Console.log("partition size: {}\n", size);
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("CollUtil#split");
		final List<List<String>> CollSplitResult = CollUtil.split(list, size);
		stopWatch.stop();

		stopWatch.start("ListUtil#split");
		final List<List<String>> ListSplitResult = ListUtil.split(list, size);
		stopWatch.stop();

		Assert.assertEquals(CollSplitResult, ListSplitResult);

		Console.log(stopWatch.prettyPrint());
	}

	@Test
	public void splitAvgTest() {
		List<List<Object>> lists = ListUtil.splitAvg(null, 3);
		Assert.assertEquals(ListUtil.empty(), lists);

		lists = ListUtil.splitAvg(Arrays.asList(1, 2, 3, 4), 1);
		Assert.assertEquals("[[1, 2, 3, 4]]", lists.toString());
		lists = ListUtil.splitAvg(Arrays.asList(1, 2, 3, 4), 2);
		Assert.assertEquals("[[1, 2], [3, 4]]", lists.toString());
		lists = ListUtil.splitAvg(Arrays.asList(1, 2, 3, 4), 3);
		Assert.assertEquals("[[1, 2], [3], [4]]", lists.toString());
		lists = ListUtil.splitAvg(Arrays.asList(1, 2, 3, 4), 4);
		Assert.assertEquals("[[1], [2], [3], [4]]", lists.toString());

		lists = ListUtil.splitAvg(Arrays.asList(1, 2, 3), 2);
		Assert.assertEquals("[[1, 2], [3]]", lists.toString());
	}

	@Test
	public void splitAvgTest2() {
		List<List<Object>> lists = ListUtil.splitAvg(Arrays.asList(1, 2, 3), 5);
		Assert.assertEquals("[[1], [2], [3], [], []]", lists.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void splitAvgNotZero() {
		// limit不能小于等于0
		ListUtil.splitAvg(Arrays.asList(1, 2, 3, 4), 0);
	}

	@Test
	public void editTest() {
		final List<String> a = ListUtil.toLinkedList("1", "2", "3");
		final List<String> filter = (List<String>) CollUtil.edit(a, str -> "edit" + str);
		Assert.assertEquals("edit1", filter.get(0));
		Assert.assertEquals("edit2", filter.get(1));
		Assert.assertEquals("edit3", filter.get(2));
	}

	@Test
	public void indexOfAll() {
		final List<String> a = ListUtil.toLinkedList("1", "2", "3", "4", "3", "2", "1");
		final int[] indexArray = ListUtil.indexOfAll(a, "2"::equals);
		Assert.assertArrayEquals(new int[]{1,5}, indexArray);
		final int[] indexArray2 = ListUtil.indexOfAll(a, "1"::equals);
		Assert.assertArrayEquals(new int[]{0,6}, indexArray2);
	}

	@Test
	public void pageTest() {
		final List<Integer> a = ListUtil.toLinkedList(1, 2, 3,4,5);

		PageUtil.setFirstPageNo(1);
		final int[] a_1 = ListUtil.page(1,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] a1 = ListUtil.page(1,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] a2 = ListUtil.page(2,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] a3 = ListUtil.page(3,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] a4 = ListUtil.page(4,2,a).stream().mapToInt(Integer::valueOf).toArray();
		Assert.assertArrayEquals(new int[]{1,2},a_1);
		Assert.assertArrayEquals(new int[]{1,2},a1);
		Assert.assertArrayEquals(new int[]{3,4},a2);
		Assert.assertArrayEquals(new int[]{5},a3);
		Assert.assertArrayEquals(new int[]{},a4);


		PageUtil.setFirstPageNo(2);
		final int[] b_1 = ListUtil.page(1,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] b1 = ListUtil.page(2,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] b2 = ListUtil.page(3,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] b3 = ListUtil.page(4,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] b4 = ListUtil.page(5,2,a).stream().mapToInt(Integer::valueOf).toArray();
		Assert.assertArrayEquals(new int[]{1,2},b_1);
		Assert.assertArrayEquals(new int[]{1,2},b1);
		Assert.assertArrayEquals(new int[]{3,4},b2);
		Assert.assertArrayEquals(new int[]{5},b3);
		Assert.assertArrayEquals(new int[]{},b4);

		PageUtil.setFirstPageNo(0);
		final int[] c_1 = ListUtil.page(-1,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] c1 = ListUtil.page(0,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] c2 = ListUtil.page(1,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] c3 = ListUtil.page(2,2,a).stream().mapToInt(Integer::valueOf).toArray();
		final int[] c4 = ListUtil.page(3,2,a).stream().mapToInt(Integer::valueOf).toArray();
		Assert.assertArrayEquals(new int[]{1,2},c_1);
		Assert.assertArrayEquals(new int[]{1,2},c1);
		Assert.assertArrayEquals(new int[]{3,4},c2);
		Assert.assertArrayEquals(new int[]{5},c3);
		Assert.assertArrayEquals(new int[]{},c4);


		PageUtil.setFirstPageNo(1);
		final int[] d1 = ListUtil.page(0,8,a).stream().mapToInt(Integer::valueOf).toArray();
		Assert.assertArrayEquals(new int[]{1,2,3,4,5},d1);

		// page with consumer
		final List<List<Integer>> pageListData = new ArrayList<>();
		ListUtil.page(a, 2, pageListData::add);
		Assert.assertArrayEquals(new int[]{1, 2}, pageListData.get(0).stream().mapToInt(Integer::valueOf).toArray());
		Assert.assertArrayEquals(new int[]{3, 4}, pageListData.get(1).stream().mapToInt(Integer::valueOf).toArray());
		Assert.assertArrayEquals(new int[]{5}, pageListData.get(2).stream().mapToInt(Integer::valueOf).toArray());


		pageListData.clear();
		ListUtil.page(a, 2, pageList -> {
			pageListData.add(pageList);
			if (pageList.get(0).equals(1)) {
				pageList.clear();
			}
		});
		Assert.assertArrayEquals(new int[]{}, pageListData.get(0).stream().mapToInt(Integer::valueOf).toArray());
		Assert.assertArrayEquals(new int[]{3, 4}, pageListData.get(1).stream().mapToInt(Integer::valueOf).toArray());
		Assert.assertArrayEquals(new int[]{5}, pageListData.get(2).stream().mapToInt(Integer::valueOf).toArray());

		// 恢复默认值，避免影响其他测试用例
		PageUtil.setFirstPageNo(0);
	}

	@Test
	public void subTest() {
		final List<Integer> of = ListUtil.of(1, 2, 3, 4);
		final List<Integer> sub = ListUtil.sub(of, 2, 4);
		sub.remove(0);

		// 对子列表操作不影响原列表
		Assert.assertEquals(4, of.size());
		Assert.assertEquals(1, sub.size());
	}

	@Test
	public void sortByPropertyTest() {
		@Data
		@AllArgsConstructor
		class TestBean {
			private int order;
			private String name;
		}

		final List<TestBean> beanList = ListUtil.toList(
				new TestBean(2, "test2"),
				new TestBean(1, "test1"),
				new TestBean(5, "test5"),
				new TestBean(4, "test4"),
				new TestBean(3, "test3")
				);

		final List<TestBean> order = ListUtil.sortByProperty(beanList, "order");
		Assert.assertEquals("test1", order.get(0).getName());
		Assert.assertEquals("test2", order.get(1).getName());
		Assert.assertEquals("test3", order.get(2).getName());
		Assert.assertEquals("test4", order.get(3).getName());
		Assert.assertEquals("test5", order.get(4).getName());
	}

	@Test
	public void swapIndex() {
		final List<Integer> list = Arrays.asList(7, 2, 8, 9);
		ListUtil.swapTo(list, 8, 1);
		Assert.assertEquals(8, (int) list.get(1));
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
		Assert.assertEquals("李四", map.get("2"));

		ListUtil.swapElement(list, map2, map1);
		map = list.get(0);
		Assert.assertEquals("李四", map.get("2"));
	}

	@Test
	public void setOrPaddingNullTest(){
		final List<String> list = new ArrayList<>();
		list.add("1");

		// 替换原值
		ListUtil.setOrPadding(list, 0, "a");
		Assert.assertEquals("[a]", list.toString());

		//append值
		ListUtil.setOrPadding(list, 1, "a");
		Assert.assertEquals("[a, a]", list.toString());

		// padding null 后加入值
		ListUtil.setOrPadding(list, 3, "a");
		Assert.assertEquals(4, list.size());
	}
}
