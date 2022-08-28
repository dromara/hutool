package cn.hutool.core.comparator;

import cn.hutool.core.collection.ListUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CompareUtilTest {

	@Test
	public void compareTest(){
		int compare = CompareUtil.compare(null, "a", true);
		Assert.assertTrue(compare > 0);

		compare = CompareUtil.compare(null, "a", false);
		Assert.assertTrue(compare < 0);
	}

	@Test
	public void comparingPinyin() {
		final List<String> list = ListUtil.of("成都", "北京", "上海", "深圳");

		final List<String> ascendingOrderResult = ListUtil.view("北京", "成都", "上海", "深圳");
		final List<String> descendingOrderResult = ListUtil.view("深圳", "上海", "成都", "北京");

		// 正序
		list.sort(CompareUtil.comparingPinyin(e -> e));
		Assert.assertEquals(list, ascendingOrderResult);

		// 反序
		list.sort(CompareUtil.comparingPinyin(e -> e, true));
		Assert.assertEquals(list, descendingOrderResult);
	}

	@Test
	public void comparingIndexedTest() {
		List<String> data = ListUtil.of("1", "2", "3", "4", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
		List<String> index = ListUtil.view("2", "1", "3", "4");

		data.sort(CompareUtil.comparingIndexed(e -> e, index));
		//[1, 2, 3, 4, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
		Assert.assertEquals(ListUtil.view("5", "6", "7", "8", "9", "10", "2", "2", "1", "1", "3", "3", "4", "4"), data);
	}

	@Test
	public void comparingIndexedTest2() {
		List<String> data = ListUtil.of("1", "2", "3", "4", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
		List<String> index = ListUtil.view("2", "1", "3", "4");

		//正确排序，index.toArray()
		data.sort(CompareUtil.comparingIndexed(e -> e, index.toArray()));
		//[5, 6, 7, 8, 9, 10, 2, 2, 1, 1, 3, 3, 4, 4]
		Assert.assertEquals(ListUtil.view("5", "6", "7", "8", "9", "10", "2", "2", "1", "1", "3", "3", "4", "4"), data);
	}
	@Test
	public void comparingIndexedTest3() {
		List<String> data = ListUtil.of("1", "2", "3", "4", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
		String[] indexArray = new String[] {"2", "1", "3", "4"};

		//正确排序，array
		data.sort(CompareUtil.comparingIndexed(e -> e, indexArray));
		//[5, 6, 7, 8, 9, 10, 2, 2, 1, 1, 3, 3, 4, 4]
		Assert.assertEquals(data, ListUtil.view("5", "6", "7", "8", "9", "10", "2", "2", "1", "1", "3", "3", "4", "4"));
	}
}
