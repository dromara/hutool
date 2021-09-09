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
import java.util.List;

public class ListUtilTest {

	@Test
	public void splitTest(){
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
		List<String> list = new ArrayList<>();
		CollUtil.padRight(list, RandomUtil.randomInt(1000_0000, 1_0000_0000), "test");

		int size = RandomUtil.randomInt(10, 1000);
		Console.log("\nlist size: {}", list.size());
		Console.log("partition size: {}\n", size);
		StopWatch stopWatch = new StopWatch();

		stopWatch.start("CollUtil#split");
		List<List<String>> CollSplitResult = CollUtil.split(list, size);
		stopWatch.stop();

		stopWatch.start("ListUtil#split");
		List<List<String>> ListSplitResult = ListUtil.split(list, size);
		stopWatch.stop();

		Assert.assertEquals(CollSplitResult, ListSplitResult);

		Console.log(stopWatch.prettyPrint());
	}

	@Test
	public void splitAvgTest(){
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

		lists = ListUtil.splitAvg(Arrays.asList(1, 2, 3), 5);
		Assert.assertEquals("[[1], [2], [3], [], []]", lists.toString());
		lists = ListUtil.splitAvg(Arrays.asList(1, 2, 3), 2);
		Assert.assertEquals("[[1, 2], [3]]", lists.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void splitAvgNotZero(){
		// limit不能小于等于0
		ListUtil.splitAvg(Arrays.asList(1, 2, 3, 4), 0);
	}

	@Test
	public void editTest(){
		List<String> a = ListUtil.toLinkedList("1", "2", "3");
		final List<String> filter = (List<String>) CollUtil.edit(a, str -> "edit" + str);
		Assert.assertEquals("edit1", filter.get(0));
		Assert.assertEquals("edit2", filter.get(1));
		Assert.assertEquals("edit3", filter.get(2));
	}

	@Test
	public void indexOfAll() {
		List<String> a = ListUtil.toLinkedList("1", "2", "3", "4", "3", "2", "1");
		final int[] indexArray = ListUtil.indexOfAll(a, "2"::equals);
		Assert.assertArrayEquals(new int[]{1,5}, indexArray);
		final int[] indexArray2 = ListUtil.indexOfAll(a, "1"::equals);
		Assert.assertArrayEquals(new int[]{0,6}, indexArray2);
	}

	@Test
	public void pageTest(){
		List<Integer> a = ListUtil.toLinkedList(1, 2, 3,4,5);

		PageUtil.setFirstPageNo(1);
		int[] a_1 = ListUtil.page(1,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] a1 = ListUtil.page(1,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] a2 = ListUtil.page(2,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] a3 = ListUtil.page(3,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] a4 = ListUtil.page(4,2,a).stream().mapToInt(Integer::valueOf).toArray();
		Assert.assertArrayEquals(new int[]{1,2},a_1);
		Assert.assertArrayEquals(new int[]{1,2},a1);
		Assert.assertArrayEquals(new int[]{3,4},a2);
		Assert.assertArrayEquals(new int[]{5},a3);
		Assert.assertArrayEquals(new int[]{},a4);


		PageUtil.setFirstPageNo(2);
		int[] b_1 = ListUtil.page(1,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] b1 = ListUtil.page(2,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] b2 = ListUtil.page(3,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] b3 = ListUtil.page(4,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] b4 = ListUtil.page(5,2,a).stream().mapToInt(Integer::valueOf).toArray();
		Assert.assertArrayEquals(new int[]{1,2},b_1);
		Assert.assertArrayEquals(new int[]{1,2},b1);
		Assert.assertArrayEquals(new int[]{3,4},b2);
		Assert.assertArrayEquals(new int[]{5},b3);
		Assert.assertArrayEquals(new int[]{},b4);

		PageUtil.setFirstPageNo(0);
		int[] c_1 = ListUtil.page(-1,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] c1 = ListUtil.page(0,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] c2 = ListUtil.page(1,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] c3 = ListUtil.page(2,2,a).stream().mapToInt(Integer::valueOf).toArray();
		int[] c4 = ListUtil.page(3,2,a).stream().mapToInt(Integer::valueOf).toArray();
		Assert.assertArrayEquals(new int[]{1,2},c_1);
		Assert.assertArrayEquals(new int[]{1,2},c1);
		Assert.assertArrayEquals(new int[]{3,4},c2);
		Assert.assertArrayEquals(new int[]{5},c3);
		Assert.assertArrayEquals(new int[]{},c4);


		PageUtil.setFirstPageNo(1);
		int[] d1 = ListUtil.page(0,8,a).stream().mapToInt(Integer::valueOf).toArray();
		Assert.assertArrayEquals(new int[]{1,2,3,4,5},d1);

		// page with consumer
		List<List<Integer>> pageListData = new ArrayList<>();
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
	}

	@Test
	public void subTest(){
		final List<Integer> of = ListUtil.of(1, 2, 3, 4);
		final List<Integer> sub = ListUtil.sub(of, 2, 4);
		sub.remove(0);

		// 对子列表操作不影响原列表
		Assert.assertEquals(4, of.size());
		Assert.assertEquals(1, sub.size());
	}

	@Test
	public void sortByPropertyTest(){
		@Data
		@AllArgsConstructor
		class TestBean{
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
}
