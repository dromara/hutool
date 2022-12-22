package cn.hutool.poi.excel;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Issue2706Test {

	@Test
	@Ignore
	public void writeTest() {
		String path = "d:/test/issue2706.xlsx";
		BigExcelWriter writer = ExcelUtil.getBigWriter(path, "表格名称");

		Set<String> headSet1 = new HashSet<>(Arrays.asList("A", "B", "C", "D", "E"));

		List<Map<String, String>> datas = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Map<String, String> map = new HashMap<>();
			for (String s : headSet1) {
				map.put(s, "h" + i);
			}
			datas.add(map);
		}
		writer.write(datas);
		writer.close();
	}
}
