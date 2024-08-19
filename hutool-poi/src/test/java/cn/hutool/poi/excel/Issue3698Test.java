package cn.hutool.poi.excel;

import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue3698Test {
	@Test
	@Disabled
	void readTest() {
		ExcelReader reader = ExcelUtil.getReader("d:/test/default.xlsx", 0);
		List<List<Object>> list = reader.read(0, Integer.MAX_VALUE, false);
		for (List<Object> row : list) {
			Console.log(row.get(1).getClass());
		}
		reader.close();
	}
}
