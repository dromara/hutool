package org.dromara.hutool.poi.excel.writer;

import org.dromara.hutool.core.comparator.IndexedComparator;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pr3807Test {
	@Test
	@Disabled
	public void writeWithComparatorTest() {
		// 生成测试数据, 10w行50列
		final List<Map<String, Object>> dataList = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			final Map<String, Object> map = new HashMap<>();
			map.put("test11", "test11_" + i);
			map.put("test12", "test12_" + i);
			map.put("test13", "test13_" + i);
			map.put("test14", "test14_" + i);
			map.put("test15", "test15_" + i);
			map.put("test16", "test16_" + i);
			map.put("test17", "test17_" + i);
			map.put("test18", "test18_" + i);
			map.put("test19", "test19_" + i);
			map.put("test1", "test1_" + i);
			map.put("test2", "test2_" + i);
			map.put("test3", "test3_" + i);
			map.put("test4", "test4_" + i);
			map.put("test5", "test5_" + i);
			map.put("test6", "test6_" + i);
			map.put("test7", "test7_" + i);
			map.put("test8", "test8_" + i);
			map.put("test9", "test9_" + i);
			map.put("test10", "test10_" + i);
			map.put("test20", "test20_" + i);
			map.put("test21", "test21_" + i);
			map.put("test22", "test22_" + i);
			map.put("test23", "test23_" + i);
			map.put("test24", "test24_" + i);
			map.put("test25", "test25_" + i);
			map.put("test26", "test26_" + i);
			map.put("test27", "test27_" + i);
			map.put("test28", "test28_" + i);
			map.put("test29", "test29_" + i);
			map.put("test30", "test30_" + i);
			map.put("test41", "test41_" + i);
			map.put("test42", "test42_" + i);
			map.put("test43", "test43_" + i);
			map.put("test44", "test44_" + i);
			map.put("test45", "test45_" + i);
			map.put("test46", "test46_" + i);
			map.put("test47", "test47_" + i);
			map.put("test48", "test48_" + i);
			map.put("test49", "test49_" + i);
			map.put("test50", "test50_" + i);
			map.put("test31", "test31_" + i);
			map.put("test32", "test32_" + i);
			map.put("test33", "test33_" + i);
			map.put("test34", "test34_" + i);
			map.put("test35", "test35_" + i);
			map.put("test36", "test36_" + i);
			map.put("test37", "test37_" + i);
			map.put("test38", "test38_" + i);
			map.put("test39", "test39_" + i);
			map.put("test40", "test40_" + i);
			dataList.add(map);
		}
		// 使用比较器写出
		try (final ExcelWriter excelWriter = ExcelUtil.getWriter("d:/test/poi/writeWithComparatorTest.xlsx")) {
			excelWriter.write(dataList, new IndexedComparator<>(
				"test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9", "test10",
				"test11", "test12", "test13", "test14", "test15", "test16", "test17", "test18", "test19", "test20",
				"test21", "test22", "test23", "test24", "test25", "test26", "test27", "test28", "test29", "test30",
				"test31", "test32", "test33", "test34", "test35", "test36", "test37", "test38", "test39", "test40",
				"test41", "test42", "test43", "test44", "test45", "test46", "test47", "test48", "test49", "test50"
			));
		}
	}
}
