package cn.hutool.core.map;

import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.map.multi.Table;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class RowKeyTableTest {

	@Test
	public void putGetTest(){
		final Table<Integer, Integer, Integer> table = new RowKeyTable<>();
		table.put(1, 2, 3);
		table.put(1, 6, 4);

		Assert.assertEquals(new Integer(3), table.get(1, 2));
		Assert.assertNull(table.get(1, 3));

		//判断row和column确定的二维点是否存在
		Assert.assertTrue(table.contains(1, 2));
		Assert.assertFalse(table.contains(1, 3));

		//判断列
		Assert.assertTrue(table.containsColumn(2));
		Assert.assertFalse(table.containsColumn(3));

		// 判断行
		Assert.assertTrue(table.containsRow(1));
		Assert.assertFalse(table.containsRow(2));


		// 获取列
		Map<Integer, Integer> column = table.getColumn(6);
		Assert.assertEquals(1, column.size());
		Assert.assertEquals(new Integer(4), column.get(1));
	}
}
