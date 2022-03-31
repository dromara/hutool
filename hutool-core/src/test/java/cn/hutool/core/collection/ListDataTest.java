package cn.hutool.core.collection;


import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

/**
 * {@link ListData} 单元测试
 *
 * @author luozongle
 */
public class ListDataTest {

	private final List<String> nameList = ListUtil.toList("小明", "小刚", "小红", "张三", "李四", "王五", "赵六");


	@Test
	public void foreachDataTest() {
		ListDataIterator<String> iterator = ListData.createIncrementDataOffset(this::dataGenerate, 0, 2).iterator();

		iterator.forEachRemaining(Console::log);
		Console.log("count: ", iterator.iteratorCount());
		Assert.assertEquals(7, iterator.iteratorCount());
	}

	@Test
	public void foreachPageTest() {
		ListDataIterator<String> iterator = ListData.createIncrementPageOffset(this::dataGeneratePage, 0, 2).iterator();

		iterator.forEachRemaining(Console::log);
		Console.log("count: ", iterator.iteratorCount());
		Assert.assertEquals(7, iterator.iteratorCount());
	}


	/**
	 * 模拟数据, 根据偏移量获取数据
	 *
	 * @param offset   偏移量
	 * @param pageSize 分页大小
	 * @return List
	 */
	private List<String> dataGenerate(long offset, long pageSize) {
		long toIndex = offset + pageSize;
		if (toIndex >= nameList.size()) {
			toIndex = nameList.size();
		}

		return nameList.subList((int) offset, (int) toIndex);
	}


	/**
	 * 模拟数据, 根据分页获取数据(类似于JPA分页)
	 *
	 * @param page     分页
	 * @param pageSize 分页大小
	 * @return List
	 */
	private List<String> dataGeneratePage(long page, long pageSize) {
		long fromIndex = page * pageSize;
		if (fromIndex >= nameList.size()) {
			fromIndex = nameList.size();
		}

		long toIndex = fromIndex + pageSize;
		if (toIndex >= nameList.size()) {
			toIndex = nameList.size();
		}

		return nameList.subList((int) fromIndex, (int) toIndex);
	}


}
