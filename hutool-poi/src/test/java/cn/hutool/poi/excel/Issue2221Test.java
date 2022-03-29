package cn.hutool.poi.excel;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class Issue2221Test {

	@Test
	//@Ignore
	public void writeDuplicateHeaderAliasTest(){
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/duplicateAlias.xlsx");
		// 设置别名
		writer.addHeaderAlias("androidLc", "安卓");
		writer.addHeaderAlias("androidAc", "安卓");
		writer.setOnlyAlias(true);

		// 写入数据
		List<Map<Object, Object>> data = ListUtil.of(
				MapUtil.ofEntries(MapUtil.entry("androidLc", "1次"), MapUtil.entry("androidAc", "3人")),
				MapUtil.ofEntries(MapUtil.entry("androidLc", "1次"), MapUtil.entry("androidAc", "3人"))
		);

		writer.write(data, true);
		writer.close();
	}
}
