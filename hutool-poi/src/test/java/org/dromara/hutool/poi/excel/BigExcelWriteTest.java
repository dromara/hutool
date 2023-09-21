/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.poi.excel.style.StyleUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * 写出Excel单元测试
 *
 * @author looly
 */
public class BigExcelWriteTest {

	@Test
	@Disabled
	public void writeTest2() {
		final List<String> row = ListUtil.of("姓名", "加班日期", "下班时间", "加班时长", "餐补", "车补次数", "车补", "总计");
		final BigExcelWriter overtimeWriter = ExcelUtil.getBigWriter("e:/excel/single_line.xlsx");
		overtimeWriter.write(row);
		overtimeWriter.close();
	}

	@Test
	@Disabled
	public void writeTest() {
		final List<?> row1 = ListUtil.of("aaaaa", "bb", "cc", "dd", DateUtil.now(), 3.22676575765);
		final List<?> row2 = ListUtil.of("aa1", "bb1", "cc1", "dd1", DateUtil.now(), 250.7676);
		final List<?> row3 = ListUtil.of("aa2", "bb2", "cc2", "dd2", DateUtil.now(), 0.111);
		final List<?> row4 = ListUtil.of("aa3", "bb3", "cc3", "dd3", DateUtil.now(), 35);
		final List<?> row5 = ListUtil.of("aa4", "bb4", "cc4", "dd4", DateUtil.now(), 28.00);

		final List<List<?>> rows = ListUtil.of(row1, row2, row3, row4, row5);
		for(int i=0; i < 400000; i++) {
			//超大列表写出测试
			rows.add(ObjUtil.clone(row1));
		}

		final String filePath = "e:/bigWriteTest.xlsx";
		FileUtil.del(FileUtil.file(filePath));
		// 通过工具类创建writer
		final BigExcelWriter writer = ExcelUtil.getBigWriter(filePath);

//		// 跳过当前行，即第一行，非必须，在此演示用
//		writer.passCurrentRow();
//		// 合并单元格后的标题行，使用默认标题样式
//		writer.merge(row1.size() - 1, "大数据测试标题");
		// 一次性写出内容，使用默认样式
		writer.write(rows);
//		writer.autoSizeColumn(0, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void mergeTest() {
		final List<?> row1 = ListUtil.of("aa", "bb", "cc", "dd", DateUtil.now(), 3.22676575765);
		final List<?> row2 = ListUtil.of("aa1", "bb1", "cc1", "dd1", DateUtil.now(), 250.7676);
		final List<?> row3 = ListUtil.of("aa2", "bb2", "cc2", "dd2", DateUtil.now(), 0.111);
		final List<?> row4 = ListUtil.of("aa3", "bb3", "cc3", "dd3", DateUtil.now(), 35);
		final List<?> row5 = ListUtil.of("aa4", "bb4", "cc4", "dd4", DateUtil.now(), 28.00);

		final List<List<?>> rows = ListUtil.of(row1, row2, row3, row4, row5);

		// 通过工具类创建writer
		final BigExcelWriter writer = ExcelUtil.getBigWriter("e:/mergeTest.xlsx");
		final CellStyle style = writer.getStyleSet().getHeadCellStyle();
		StyleUtil.setColor(style, IndexedColors.RED, FillPatternType.SOLID_FOREGROUND);

		// 跳过当前行，即第一行，非必须，在此演示用
		writer.passCurrentRow();
		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(row1.size() - 1, "测试标题");
		// 一次性写出内容，使用默认样式
		writer.write(rows);

		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(7, 10, 4, 10, "测试Merge", false);

		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeMapTest() {
		final Map<String, Object> row1 = new LinkedHashMap<>();
		row1.put("姓名", "张三");
		row1.put("年龄", 23);
		row1.put("成绩", 88.32);
		row1.put("是否合格", true);
		row1.put("考试日期", DateUtil.now());

		final Map<String, Object> row2 = new LinkedHashMap<>();
		row2.put("姓名", "李四");
		row2.put("年龄", 33);
		row2.put("成绩", 59.50);
		row2.put("是否合格", false);
		row2.put("考试日期", DateUtil.now());

		final ArrayList<Map<String, Object>> rows = ListUtil.of(row1, row2);

		// 通过工具类创建writer
		final String path = "e:/bigWriteMapTest.xlsx";
		FileUtil.del(FileUtil.file(path));
		final BigExcelWriter writer = ExcelUtil.getBigWriter(path);

		//设置内容字体
		final Font font = writer.createFont();
		font.setBold(true);
		font.setColor(Font.COLOR_RED);
		font.setItalic(true);
		writer.getStyleSet().setFont(font, true);

		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(row1.size() - 1, "一班成绩单");
		// 一次性写出内容，使用默认样式
		writer.write(rows);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeMapTest2() {
		final Map<String, Object> row1 = MapUtil.newHashMap(true);
		row1.put("姓名", "张三");
		row1.put("年龄", 23);
		row1.put("成绩", 88.32);
		row1.put("是否合格", true);
		row1.put("考试日期", DateUtil.now());

		// 通过工具类创建writer
		final String path = "e:/bigWriteMapTest2.xlsx";
		FileUtil.del(FileUtil.file(path));
		final BigExcelWriter writer = ExcelUtil.getBigWriter(path);

		// 一次性写出内容，使用默认样式
		writer.writeRow(row1, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeBeanTest() {
		final TestBean bean1 = new TestBean();
		bean1.setName("张三");
		bean1.setAge(22);
		bean1.setPass(true);
		bean1.setScore(66.30);
		bean1.setExamDate(DateUtil.now());

		final TestBean bean2 = new TestBean();
		bean2.setName("李四");
		bean2.setAge(28);
		bean2.setPass(false);
		bean2.setScore(38.50);
		bean2.setExamDate(DateUtil.now());

		final List<TestBean> rows = ListUtil.of(bean1, bean2);
		// 通过工具类创建writer
		final String file = "e:/bigWriteBeanTest.xlsx";
		FileUtil.del(FileUtil.file(file));
		final BigExcelWriter writer = ExcelUtil.getBigWriter(file);
		//自定义标题
		writer.addHeaderAlias("name", "姓名");
		writer.addHeaderAlias("age", "年龄");
		writer.addHeaderAlias("score", "分数");
		writer.addHeaderAlias("isPass", "是否通过");
		writer.addHeaderAlias("examDate", "考试时间");
		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(4, "一班成绩单");
		// 一次性写出内容，使用默认样式
		writer.write(rows);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeCellValueTest() {
		final String path = "d:/test/cellValueTest.xlsx";
		FileUtil.del(FileUtil.file(path));
		final BigExcelWriter writer = new BigExcelWriter(path);
		writer.writeCellValue(3, 5, "aaa");
		writer.close();
	}

	@Test
	@Disabled
	public void closeTest() {
		final Map<String, ?> map1 = MapUtil.of("id", "123456");
		final Map<String, ?> map2 = MapUtil.of("id", "123457");
		final List<?> data = Arrays.asList(map1, map2);
		final String destFilePath = "d:/test/closeTest.xlsx";//略
		FileUtil.del(FileUtil.file(destFilePath));
		try (final ExcelWriter writer = ExcelUtil.getBigWriter(destFilePath)) {
			writer.write(data).flush();
		}
	}

	@Test
	@Disabled
	public void issue1210() {
		// 通过工具类创建writer
		final String path = "d:/test/issue1210.xlsx";
		FileUtil.del(FileUtil.file(path));
		final BigExcelWriter writer = ExcelUtil.getBigWriter(path);
		writer.addHeaderAlias("id", "SN");
		writer.addHeaderAlias("userName", "User Name");

		final List<Map<String, Object>> list = new ArrayList<>();
		list.add(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;

			{
			put("id", 1);
			put("userName", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		}});

		list.add(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;

			{
			put("id", 2);
			put("userName", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
		}});
		writer.write(list, true);
		writer.autoSizeColumnAll();
		writer.close();
	}
}
