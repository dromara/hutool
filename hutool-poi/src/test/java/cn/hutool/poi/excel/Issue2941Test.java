package cn.hutool.poi.excel;

import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class Issue2941Test {
	@Test
	@Ignore
	public void excelReadDateTest() {
		final ExcelReader reader = ExcelUtil.getReader("d:/test/1677649503673.xlsx");
		final List<Map<String, Object>> maps = reader.readAll();
		for (Map<String, Object> map : maps) {
			Console.log(map);
		}
	}
}
