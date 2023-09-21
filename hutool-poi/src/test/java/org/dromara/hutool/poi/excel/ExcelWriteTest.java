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
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.data.id.IdUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.poi.excel.cell.setters.EscapeStrCellSetter;
import org.dromara.hutool.poi.excel.style.StyleUtil;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

/**
 * 写出Excel单元测试
 *
 * @author looly
 */
public class ExcelWriteTest {

	@Test
	public void writeNoFlushTest() {
		final List<?> row1 = ListUtil.of("aaaaa", "bb", "cc", "dd", DateUtil.now(), 3.22676575765);
		final List<?> row2 = ListUtil.of("aa1", "bb1", "cc1", "dd1", DateUtil.now(), 250.7676);
		final List<?> row3 = ListUtil.of("aa2", "bb2", "cc2", "dd2", DateUtil.now(), 0.111);
		final List<?> row4 = ListUtil.of("aa3", "bb3", "cc3", "dd3", DateUtil.now(), 35);
		final List<?> row5 = ListUtil.of("aa4", "bb4", "cc4", "dd4", DateUtil.now(), 28.00);
		final List<List<?>> rows = ListUtil.of(row1, row2, row3, row4, row5);

		final ExcelWriter writer = ExcelUtil.getWriter();
		writer.write(rows);
		writer.close();
	}

	@Test
	@Disabled
	public void testRowOrColumnCellStyle() {
		final List<?> row1 = ListUtil.of("aaaaa", "bb", "cc", "dd", DateUtil.now(), 3.22676575765);
		final List<?> row2 = ListUtil.of("aa1", "bb1", "cc1", "dd1", DateUtil.now(), 250.7676);
		final List<?> row3 = ListUtil.of("aa2", "bb2", "cc2", "dd2", DateUtil.now(), 0.111);
		final List<?> row4 = ListUtil.of("aa3", "bb3", "cc3", "dd3", DateUtil.now(), 35);
		final List<?> row5 = ListUtil.of("aa4", "bb4", "cc4", "dd4", DateUtil.now(), 28.00);

		final List<List<?>> rows = ListUtil.of(row1, row2, row3, row4, row5);
		final BigExcelWriter overtimeWriter = ExcelUtil.getBigWriter("d:/test/style_line.xlsx");

		overtimeWriter.write(rows, true);

		final CellStyle cellStyle = overtimeWriter.getWorkbook().createCellStyle();
		StyleUtil.setBorder(cellStyle, BorderStyle.THIN, IndexedColors.BLACK);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor((short) 13);
		cellStyle.setDataFormat((short) 22);//时间格式
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		//原设置行、列样式的方法
//		overtimeWriter.setRowStyle(2,cellStyle);
//		overtimeWriter.setColumnStyle(1,cellStyle);

		//现增加的设置行、列样式的方法
		//给第三行加背景色
		overtimeWriter.setRowStyleIfHasData(2, cellStyle);
		//给第二列加背景色 从第一行开始加（用于控制有表头时）
		overtimeWriter.setColumnStyleIfHasData(1, 0, cellStyle);

		final CellStyle cellStyle1 = overtimeWriter.getWorkbook().createCellStyle();
		StyleUtil.setBorder(cellStyle1, BorderStyle.THIN, IndexedColors.BLACK);
		cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle1.setFillForegroundColor((short) 13);
		cellStyle1.setDataFormat((short) 2);//小数保留两位
		cellStyle1.setAlignment(HorizontalAlignment.CENTER);
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		overtimeWriter.setStyle(cellStyle1, 5, 2);//由于第6列是数字 上面应用了日期格式会错乱，这里单独设置下第六列的格式

		overtimeWriter.close();
	}

	@Test
	@Disabled
	public void writeTest2() {
		final List<String> row = ListUtil.of("姓名", "加班日期", "下班时间", "加班时长", "餐补", "车补次数", "车补", "总计");
		final ExcelWriter overtimeWriter = ExcelUtil.getWriter("d:/test/excel/single_line.xlsx");
		overtimeWriter.writeRow(row);
		overtimeWriter.close();
	}

	@Test
	@Disabled
	public void writeWithSheetTest() {
		final ExcelWriter writer = ExcelUtil.getWriterWithSheet("表格1");

		// 写出第一张表
		final List<String> row = ListUtil.of("姓名", "加班日期", "下班时间", "加班时长", "餐补", "车补次数", "车补", "总计");
		writer.writeRow(row);

		// 写出第二张表
		writer.setSheet("表格2");
		final List<String> row2 = ListUtil.of("姓名2", "加班日期2", "下班时间2", "加班时长2", "餐补2", "车补次数2", "车补2", "总计2");
		writer.writeRow(row2);

		// 生成文件或导出Excel
		writer.flush(FileUtil.file("d:/test/writeWithSheetTest.xlsx"));

		writer.close();
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
		for (int i = 0; i < 400; i++) {
			// 超大列表写出测试
			rows.add(ObjUtil.clone(row1));
		}

		final String filePath = "d:/test/writeTest.xlsx";
		FileUtil.del(FileUtil.file(filePath));
		// 通过工具类创建writer
		final ExcelWriter writer = ExcelUtil.getWriter(filePath);
		// 通过构造方法创建writer
		// ExcelWriter writer = new ExcelWriter("d:/writeTest.xls");

		// 跳过当前行，即第一行，非必须，在此演示用
		writer.passCurrentRow();
		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(row1.size() - 1, "测试标题");
		// 一次性写出内容，使用默认样式
		writer.write(rows);
		writer.autoSizeColumn(0, true);
		//冻结前两行
		writer.setFreezePane(0, 2);
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
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/mergeTest.xlsx");
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
	public void mergeTest2() {
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
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/writeMapTest.xlsx");
		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(row1.size() - 1, "一班成绩单");

		// 一次性写出内容，使用默认样式，强制输出标题
		writer.write(rows, true);
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
		final ExcelWriter writer = ExcelUtil.getWriter("e:/excel/writeMapTest.xlsx");

		// 设置内容字体
		final Font font = writer.createFont();
		font.setBold(true);
		font.setColor(Font.COLOR_RED);
		font.setItalic(true);
		writer.getStyleSet().setFont(font, true);

		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(row1.size() - 1, "一班成绩单");
		// 一次性写出内容，使用默认样式
		writer.write(rows, true);
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
		final ExcelWriter writer = ExcelUtil.getWriter("e:/writeMapTest2.xlsx");

		// 一次性写出内容，使用默认样式
		writer.writeRow(row1, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeMapWithStyleTest() {
		final Map<String, Object> row1 = MapUtil.newHashMap(true);
		row1.put("姓名", "张三");
		row1.put("年龄", 23);
		row1.put("成绩", 88.32);
		row1.put("是否合格", true);
		row1.put("考试日期", DateUtil.now());

		// 通过工具类创建writer
		final String path = "f:/test/writeMapWithStyleTest.xlsx";
		FileUtil.del(FileUtil.file(path));
		final ExcelWriter writer = ExcelUtil.getWriter(path);
		writer.setStyleSet(null);

		// 一次性写出内容，使用默认样式
		writer.writeRow(row1, true);

		// 设置某个单元格样式
		final CellStyle orCreateRowStyle = writer.getOrCreateCellStyle(0, 1);
		StyleUtil.setColor(orCreateRowStyle, IndexedColors.RED.getIndex(), FillPatternType.SOLID_FOREGROUND);

		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeMapAliasTest() {
		final Map<Object, Object> row1 = new LinkedHashMap<>();
		row1.put("name", "张三");
		row1.put("age", 22);
		row1.put("isPass", true);
		row1.put("score", 66.30);
		row1.put("examDate", DateUtil.now());
		final Map<Object, Object> row2 = new LinkedHashMap<>();
		row2.put("name", "李四");
		row2.put("age", 233);
		row2.put("isPass", false);
		row2.put("score", 32.30);
		row2.put("examDate", DateUtil.now());

		final List<Map<Object, Object>> rows = ListUtil.of(row1, row2);
		// 通过工具类创建writer
		final String file = "d:/test/writeMapAlias.xlsx";
		FileUtil.del(FileUtil.file(file));
		final ExcelWriter writer = ExcelUtil.getWriter(file);
		// 自定义标题
		writer.addHeaderAlias("name", "姓名");
		writer.addHeaderAlias("age", "年龄");
		writer.addHeaderAlias("score", "分数");
		writer.addHeaderAlias("isPass", "是否通过");
		writer.addHeaderAlias("examDate", "考试时间");
		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(4, "一班成绩单");
		// 一次性写出内容，使用默认样式
		writer.write(rows, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeMapOnlyAliasTest() {
		final Map<Object, Object> row1 = new LinkedHashMap<>();
		row1.put("name", "张三");
		row1.put("age", 22);
		row1.put("isPass", true);
		row1.put("score", 66.30);
		row1.put("examDate", DateUtil.now());
		final Map<Object, Object> row2 = new LinkedHashMap<>();
		row2.put("name", "李四");
		row2.put("age", 233);
		row2.put("isPass", false);
		row2.put("score", 32.30);
		row2.put("examDate", DateUtil.now());

		final List<Map<Object, Object>> rows = ListUtil.of(row1, row2);
		// 通过工具类创建writer
		final String file = "f:/test/test_alias.xlsx";
		FileUtil.del(FileUtil.file(file));
		final ExcelWriter writer = ExcelUtil.getWriter(file);
		writer.setOnlyAlias(true);
		// 自定义标题
		writer.addHeaderAlias("name", "姓名");
		writer.addHeaderAlias("age", "年龄");
		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(4, "一班成绩单");
		// 一次性写出内容，使用默认样式
		writer.write(rows, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeMapOnlyAliasTest2() {
		final Map<Object, Object> row1 = new LinkedHashMap<>();
		row1.put("name", "张三");
		row1.put("age", 22);
		row1.put("isPass", true);
		row1.put("score", 66.30);
		row1.put("examDate", DateUtil.now());
		final Map<Object, Object> row2 = new LinkedHashMap<>();
		row2.put("name", "李四");
		row2.put("age", 233);
		row2.put("isPass", false);
		row2.put("score", 32.30);
		row2.put("examDate", DateUtil.now());

		final List<Map<Object, Object>> rows = ListUtil.of(row1, row2);
		// 通过工具类创建writer
		final String file = "d:/test/test_alias.xls";
		final ExcelWriter writer = ExcelUtil.getWriter(file, "test1");
//		writer.setOnlyAlias(true);
		// 自定义标题
		writer.addHeaderAlias("name", "姓名");
		writer.addHeaderAlias("age", "年龄");
		// 一次性写出内容，使用默认样式
		writer.write(rows, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeMapOnlyAliasTest3() {
		final Map<Object, Object> row1 = new LinkedHashMap<>();
		row1.put("name", "张三");
		row1.put("age", 22);
		row1.put("isPass", true);
		row1.put("score", 66.30);
		row1.put("examDate", DateUtil.now());

		final Map<Object, Object> row2 = new LinkedHashMap<>();
		row2.put("name", "李四");
//		row2.put("age", 233);
		row2.put("isPass", false);
		row2.put("score", 32.30);
		row2.put("examDate", DateUtil.now());

		final List<Map<Object, Object>> rows = ListUtil.of(row1, row2);
		// 通过工具类创建writer
		final String file = "d:/test/test_alias.xls";
		final ExcelWriter writer = ExcelUtil.getWriter(file, "test1");
		writer.setOnlyAlias(true);

		// 自定义标题
		writer.addHeaderAlias("name", "姓名");
		writer.addHeaderAlias("age", "年龄");
		writer.addHeaderAlias("examDate", "考试时间");

		// 一次性写出内容，使用默认样式
		writer.write(rows, true);
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
		final String file = "e:/writeBeanTest.xlsx";
		FileUtil.del(FileUtil.file(file));
		final ExcelWriter writer = ExcelUtil.getWriter(file);
		// 自定义标题
		writer.addHeaderAlias("name", "姓名");
		writer.addHeaderAlias("age", "年龄");
		writer.addHeaderAlias("score", "分数");
		writer.addHeaderAlias("isPass", "是否通过");
		writer.addHeaderAlias("examDate", "考试时间");
		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(4, "一班成绩单");
		// 一次性写出内容，使用默认样式
		writer.write(rows, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeBeanTest2() {
		final OrderExcel order1 = new OrderExcel();
		order1.setId("1");
		order1.setNum("123");
		order1.setBody("body1");

		final OrderExcel order2 = new OrderExcel();
		order1.setId("2");
		order1.setNum("456");
		order1.setBody("body2");

		final List<OrderExcel> rows = ListUtil.of(order1, order2);
		// 通过工具类创建writer
		final String file = "f:/test/writeBeanTest2.xlsx";
		FileUtil.del(FileUtil.file(file));
		final ExcelWriter writer = ExcelUtil.getWriter(file);
		// 自定义标题
		writer.addHeaderAlias("id", "编号");
		writer.addHeaderAlias("num", "序号");
		writer.addHeaderAlias("body", "内容");
		// 一次性写出内容，使用默认样式
		writer.write(rows, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Disabled
	public void writeCellValueTest() {
		final ExcelWriter writer = new ExcelWriter("d:/cellValueTest.xls");
		writer.writeCellValue(3, 5, "aaa");
		writer.writeCellValue(3, 5, "aaa");
		writer.close();
	}

	@Test
	@Disabled
	public void addSelectTest() {
		final List<String> row = ListUtil.of("姓名", "加班日期", "下班时间", "加班时长", "餐补", "车补次数", "车补", "总计");
		final ExcelWriter overtimeWriter = ExcelUtil.getWriter("d:/test/single_line.xlsx");
		overtimeWriter.writeCellValue(3, 4, "AAAA");
		overtimeWriter.addSelect(3, 4, row.toArray(new String[0]));
		overtimeWriter.close();
	}

	@Test
	@Disabled
	public void addSelectTest2() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/select.xls");
		writer.writeCellValue(0, 0, "请选择科目");
		final int firstRow = 0;
		final int lastRow = 0;
		final int firstCol = 0;
		final int lastCol = 0;
		final CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		writer.addSelect(addressList, "1001", "1002", "1003");

		final List<?> rows = new ArrayList<>();
		writer.write(rows, true);

		writer.close();
	}

	@Test
	@Disabled
	public void writeMultiSheetTest() {
		final List<Map<String, Object>> rows = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			final Map<String, Object> tempList = new TreeMap<>();
			for (int j = 0; j < 10; j++) {
				tempList.put(String.valueOf(j), IdUtil.randomUUID());
			}
			rows.add(tempList);
		}
		final ExcelWriter writer = ExcelUtil.getWriter("D:\\test\\multiSheet.xlsx", "正常数据");
		writer.addHeaderAlias("1", "row1");
		writer.addHeaderAlias("3", "row2");
		writer.setOnlyAlias(true);

		writer.write(rows, true);
		writer.autoSizeColumnAll();

		//表2
		writer.setSheet("当前重复数据");
		writer.clearHeaderAlias();
		writer.addHeaderAlias("3", "行3");
		writer.addHeaderAlias("1", "行1");
		writer.write(rows, true);
		writer.autoSizeColumnAll();

		//表3
		writer.setSheet("历史重复数据");
		writer.write(rows, true);
		writer.autoSizeColumnAll();

		writer.close();
	}

	@Test
	@Disabled
	public void writeMultiSheetTest2() {
		final List<Map<String, Object>> rows = new LinkedList<>();
		final HashMap<String, Object> map = MapUtil.newHashMap();
		map.put("k1", "v1");
		map.put("k2", "v2");
		map.put("k3", "v3");
		rows.add(map);

		final ExcelWriter writer = ExcelUtil.getWriter("D:\\test\\multiSheet2.xlsx", "正常数据");
		writer.write(rows);

		//表2
		writer.setSheet("表2");
		final List<Map<String, Object>> rows2 = new LinkedList<>();
		final HashMap<String, Object> map2 = MapUtil.newHashMap();
		map2.put("x1", "v1");
		rows2.add(map2);
		writer.write(rows2);

		writer.close();
	}

	@Test
	@Disabled
	public void writeMultiSheetWithStyleTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("D:\\test\\multiSheetWithStyle.xlsx", "表格1");

		// 表1
		final List<Map<String, Object>> rows = new LinkedList<>();
		final HashMap<String, Object> map = MapUtil.newHashMap();
		map.put("k1", "v1");
		map.put("k2", "v2");
		map.put("k3", "v3");
		rows.add(map);
		writer.write(rows);

		final Font headFont = writer.createFont();
		headFont.setBold(true);
		headFont.setFontHeightInPoints((short) 50);
		headFont.setFontName("Microsoft YaHei");
		writer.getStyleSet().getHeadCellStyle().setFont(headFont);

		//表2
		writer.setSheet("表2");
		final List<Map<String, Object>> rows2 = new LinkedList<>();
		final HashMap<String, Object> map2 = MapUtil.newHashMap();
		map2.put("x1", "v1");
		rows2.add(map2);
		writer.write(rows2);

		writer.close();
	}

	@Test
	@Disabled
	public void writeMapsTest() {
		final List<Map<String, Object>> rows = new ArrayList<>();

		final Map<String, Object> map1 = new HashMap<>();
		map1.put("a", 1);
		map1.put("b", 2);
		map1.put("c", 3);
		map1.put("d", 4);
		map1.put("e", 5);
		final Map<String, Object> map2 = new HashMap<>();
		map2.put("c", 3);
		map2.put("d", 4);
		map2.put("e", 5);
		final Map<String, Object> map3 = new HashMap<>();
		map3.put("d", 4);
		map3.put("e", 5);

		rows.add(map1);
		rows.add(map2);
		rows.add(map3);

		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/rows.xlsx");
		writer.write(rows);
		writer.close();
	}

	@Test
	@Disabled
	public void formatTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/formatTest.xlsx");
		final CellStyle cellStyle = writer.createCellStyle(0, 0);
		cellStyle.setDataFormat(writer.getWorkbook().createDataFormat().getFormat("yyyy-mm-dd"));
		writer.close();
	}

	@Test
	@Disabled
	public void writeNumberFormatTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/formatTest.xlsx");
		writer.disableDefaultStyle();
		writer.writeRow(ListUtil.of(51.33333333, 90.111111111));
		final CellStyle columnStyle = writer.createCellStyle(0, 0);
		columnStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("0.00"));
		writer.close();
	}

	@Test
	@Disabled
	public void writeSecHeadRowTest() {
		final List<?> row1 = ListUtil.of(1, "aa", "bb", "cc", "dd", "ee");
		final List<?> row2 = ListUtil.of(2, "aa1", "bb1", "cc1", "dd1", "ee1");
		final List<?> row3 = ListUtil.of(3, "aa2", "bb2", "cc2", "dd2", "ee2");
		final List<?> row4 = ListUtil.of(4, "aa3", "bb3", "cc3", "dd3", "ee3");
		final List<?> row5 = ListUtil.of(5, "aa4", "bb4", "cc4", "dd4", "ee4");

		final List<List<?>> rows = ListUtil.of(row1, row2, row3, row4, row5);

		// 通过工具类创建writer
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/writeSecHeadRowTest.xlsx");

		final CellStyle cellStyle = writer.getWorkbook().createCellStyle();
		cellStyle.setWrapText(false);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		//设置标题内容字体
		final Font font = writer.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 15);
		font.setFontName("Arial");
		//设置边框样式
		StyleUtil.setBorder(cellStyle, BorderStyle.THICK, IndexedColors.RED);
		cellStyle.setFont(font);

		// 合并单元格后的标题行，使用设置好的样式
		writer.merge(0, 1, 0, row1.size() - 1, "标题XXXXXXXX", cellStyle);
		Console.log(writer.getCurrentRow());

		//设置复杂表头
		writer.merge(2, 3, 0, 0, "序号", true);
		writer.merge(2, 2, 1, 2, "AABB", true);
		writer.merge(2, 3, 3, 3, "CCCC", true);
		writer.merge(2, 2, 4, 5, "DDEE", true);
		writer.setCurrentRow(3);

		final List<String> sechead = ListUtil.of("AA", "BB", "DD", "EE");
		writer.writeSecHeadRow(sechead);
		// 一次性写出内容，使用默认样式
		writer.write(rows);
		// 关闭writer，释放内存
		writer.close();
	}

	/**
	 * issue#1659@Github
	 * 测试使用BigWriter写出，ExcelWriter修改失败
	 */
	@Test
	@Disabled
	public void editTest() {
		// 生成文件
		final File file = new File("d:/test/100_.xlsx");
		FileUtil.del(file);

		final BigExcelWriter writer = ExcelUtil.getBigWriter(file);
		writer.disableDefaultStyle();
		final List<List<String>> rows = Collections.singletonList(Arrays.asList("哈哈", "嘿嘿"));
		writer.write(rows);
		writer.close();

		// 修改文件
		final ExcelWriter writer2 = ExcelUtil.getWriter(file);
		writer2.disableDefaultStyle();
		writer2.writeCellValue(0, 0, "a");
		writer2.close();

		final ExcelReader reader = ExcelUtil.getReader(file);
		Console.log(reader.read());
	}

	@Test
	@Disabled
	public void mergeTest3() {
		// https://github.com/dromara/hutool/issues/1696

		final List<Map<String, Object>> list = new ArrayList<>();
		final Map<String, Object> map = new HashMap<>();
		map.put("xmnf", "2021");
		list.add(map);

		final Map<String, Object> map1 = new HashMap<>();
		map1.put("xmnf", new XSSFRichTextString("9999"));
		list.add(map1);

		final Map<String, Object> map2 = new HashMap<>();
		map2.put("xmnf", "2019");
		list.add(map2);

		//通过工具类创建writer
		FileUtil.del(FileUtil.file("d:/test/writeTest2123.xlsx"));
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/writeTest2123.xlsx");
		writer.addHeaderAlias("xmnf", "项目年份");//1

		//合并单元格后的标题行，使用默认标题样式
		writer.merge(7, "测试标题");
		writer.merge(3, 4, 0, 0, new XSSFRichTextString("9999"), true);
		writer.write(list, true);
		writer.close();
	}

	@Test
	@Disabled
	public void mergeForDateTest() {
		// https://github.com/dromara/hutool/issues/1911

		//通过工具类创建writer
		final String path = "d:/test/mergeForDate.xlsx";
		FileUtil.del(FileUtil.file(path));
		final ExcelWriter writer = ExcelUtil.getWriter(path);
		writer.merge(0, 3, 0, 2, DateUtil.now(), false);
		writer.close();
	}

	@Test
	@Disabled
	public void changeHeaderStyleTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/headerStyle.xlsx");
		writer.writeHeadRow(ListUtil.view("姓名", "性别", "年龄"));
		final CellStyle headCellStyle = writer.getStyleSet().getHeadCellStyle();
		headCellStyle.setFillForegroundColor(IndexedColors.YELLOW1.index);
		headCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		writer.close();
	}

	@Test
	@Disabled
	public void writeFloatTest() {
		//issue https://gitee.com/dromara/hutool/issues/I43U9G
		final String path = "d:/test/floatTest.xlsx";
		FileUtil.del(FileUtil.file(path));

		final ExcelWriter writer = ExcelUtil.getWriter(path);
		writer.writeRow(ListUtil.view(22.9f));
		writer.close();
	}

	@Test
	@Disabled
	public void writeDoubleTest() {
		// https://gitee.com/dromara/hutool/issues/I5PI5C
		final String path = "d:/test/doubleTest.xlsx";
		FileUtil.del(FileUtil.file(path));

		final ExcelWriter writer = ExcelUtil.getWriter(path);
		writer.disableDefaultStyle();
		writer.writeRow(ListUtil.view(0.427d));
		writer.close();
	}

	@Test
	@Disabled
	public void issueI466ZZTest() {
		// https://gitee.com/dromara/hutool/issues/I466ZZ
		// 需要输出S_20000314_x5116_0004
		// 此处加入一个转义前缀：_x005F
		final List<Object> row = ListUtil.view(new EscapeStrCellSetter("S_20000314_x5116_0004"));

		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/_x.xlsx");
		writer.writeRow(row);
		writer.close();
	}

	@Test
	@Disabled
	public void writeLongTest() {
		//https://gitee.com/dromara/hutool/issues/I49R6U
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/long.xlsx");
		writer.write(ListUtil.view(1427545395336093698L));
		writer.close();
	}

	@Test
	@Disabled
	public void writeHyperlinkTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/hyperlink.xlsx");

		final Hyperlink hyperlink = writer.createHyperlink(HyperlinkType.URL, "https://hutool.cn");

		writer.write(ListUtil.view(hyperlink));
		writer.close();
	}

	@Test
	@Disabled
	public void mergeNumberTest() {
		final File tempFile = new File("d:/test/mergeNumber.xlsx");
		FileUtil.del(tempFile);

		final BigExcelWriter writer = new BigExcelWriter(tempFile);
		writer.merge(0, 1, 2, 2, 3.99, false);
		writer.close();
	}

	@Test
	@Disabled
	public void writeImgTest() {
		final ExcelWriter writer = ExcelUtil.getWriter(true);

		final File file = new File("C:\\Users\\zsz\\Desktop\\1.jpg");

		writer.writeImg(file, 0, 0, 5, 10);

		writer.flush(new File("C:\\Users\\zsz\\Desktop\\2.xlsx"));

		writer.close();
	}

	@Test
	public void getDispositionTest() {
		final ExcelWriter writer = ExcelUtil.getWriter(true);
		final String disposition = writer.getDisposition("测试A12.xlsx", CharsetUtil.UTF_8);
		Assertions.assertEquals("attachment; filename=\"%E6%B5%8B%E8%AF%95A12.xlsx\"", disposition);
	}
}
