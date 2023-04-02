package org.dromara.hutool.excel;

import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.excel.style.StyleUtil;
import org.dromara.hutool.map.MapUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class Issue2221Test {

	/**
	 * 设置重复别名的时候，通过原key获取写出位置
	 */
	@Test
	@Disabled
	public void writeDuplicateHeaderAliasTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/duplicateAlias.xlsx");
		// 设置别名
		writer.addHeaderAlias("androidLc", "安卓");
		writer.addHeaderAlias("androidAc", "安卓");
		writer.setOnlyAlias(true);

		// 写入数据
		final List<Map<Object, Object>> data = ListUtil.view(
				MapUtil.ofEntries(MapUtil.entry("androidLc", "1次"), MapUtil.entry("androidAc", "3人")),
				MapUtil.ofEntries(MapUtil.entry("androidLc", "1次"), MapUtil.entry("androidAc", "3人"))
		);

		writer.write(data, true);
		writer.close();
	}

	@Test
	@Disabled
	public void writeDuplicateHeaderAliasTest2(){
		// 获取写Excel的流
		final ExcelWriter writer = ExcelUtil.getBigWriter("d:/test/duplicateAlias2.xlsx");

		// 设置头部的背景颜色
		StyleUtil.setColor(writer.getHeadCellStyle(), IndexedColors.GREY_50_PERCENT, FillPatternType.SOLID_FOREGROUND);

		//设置全局字体
		final Font font = writer.createFont();
		font.setFontName("Microsoft YaHei");
		writer.getStyleSet().setFont(font, false);

		// 设置头部的字体为白颜色
		final Font headerFont = writer.createFont();
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		writer.getHeadCellStyle().setFont(headerFont);

		// 跳过多少行
		writer.passRows(1);

		// 冻结多少行
		writer.setFreezePane(2);

		// 设置别名
		writer.addHeaderAlias("date", "日期");
		writer.addHeaderAlias("androidLc", "安卓");
		writer.addHeaderAlias("iosLc", "iOS");
		writer.addHeaderAlias("androidAc", " 安卓");
		writer.addHeaderAlias("iosAc", " iOS");
		writer.setOnlyAlias(true);

		// 设置合并的单元格
		writer.merge(0, 1, 0, 0, "日期", true);
		writer.merge(0, 0, 1, 2, "运行次数", true);
		writer.merge(0, 0, 3, 4, "新增人数", true);

		// 写入数据
		final List<Map<Object, Object>> data = ListUtil.view(
				MapUtil.ofEntries(
						MapUtil.entry("date", "2022-01-01"),
						MapUtil.entry("androidLc", "1次"),
						MapUtil.entry("iosLc", "2次"),
						MapUtil.entry("androidAc", "3次"),
						MapUtil.entry("iosAc", "4人")),
				MapUtil.ofEntries(
						MapUtil.entry("date", "2022-01-02"),
						MapUtil.entry("androidLc", "5次"),
						MapUtil.entry("iosLc", "6次"),
						MapUtil.entry("androidAc", "7次"),
						MapUtil.entry("iosAc", "8人"))
		);

		// 自动尺寸
		writer.autoSizeColumnAll();

		writer.write(data, true);
		writer.close();
	}
}
