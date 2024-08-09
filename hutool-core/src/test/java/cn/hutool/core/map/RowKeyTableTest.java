package cn.hutool.core.map;

import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.map.multi.Table;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class RowKeyTableTest {

	@Test
	public void putGetTest(){
		final Table<Integer, Integer, Integer> table = new RowKeyTable<>();
		table.put(1, 2, 3);
		table.put(1, 6, 4);

		assertEquals(new Integer(3), table.get(1, 2));
		assertNull(table.get(1, 3));

		//判断row和column确定的二维点是否存在
		assertTrue(table.contains(1, 2));
		assertFalse(table.contains(1, 3));

		//判断列
		assertTrue(table.containsColumn(2));
		assertFalse(table.containsColumn(3));

		// 判断行
		assertTrue(table.containsRow(1));
		assertFalse(table.containsRow(2));


		// 获取列
		Map<Integer, Integer> column = table.getColumn(6);
		assertEquals(1, column.size());
		assertEquals(new Integer(4), column.get(1));
	}

	@Test
	public void issue3135Test() {
		final Table<Integer, Integer, Integer> table = new RowKeyTable<>();
		table.put(1, 2, 3);
		table.put(1, 6, 4);

		assertNull(table.getRow(2));
		assertFalse(table.contains(2, 3));
	}
}
