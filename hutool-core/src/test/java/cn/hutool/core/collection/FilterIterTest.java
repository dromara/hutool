package cn.hutool.core.collection;

import org.junit.Test;

import java.util.Iterator;

/**
 * {@link FilterIter} 单元测试
 * @author chao.wang
 */
public class FilterIterTest {

	@Test
	public void checkFilterIter() {
		Iterator<String> it = ListUtil.of("1", "2").iterator();
		// filter 为null
		FilterIter<String> filterIter = new FilterIter<>(it, null);
		while (filterIter.hasNext()) {
			System.out.println(filterIter.next());
		}

		System.out.println();
		it = ListUtil.of("1", "2").iterator();
		// filter 不为空
		filterIter = new FilterIter<>(it, (key) -> key.equals("1"));
		while (filterIter.hasNext()) {
			System.out.println(filterIter.next());
		}
	}

}
