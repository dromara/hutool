package cn.hutool.poi.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.style.StyleUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 写出Excel单元测试
 *
 * @author looly
 */
public class ExcelWriteTest {

	@Test
	@Ignore
	public void testRowOrColumnCellStyle() {
		List<?> row1 = CollUtil.newArrayList("aaaaa", "bb", "cc", "dd", DateUtil.date(), 3.22676575765);
		List<?> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1", DateUtil.date(), 250.7676);
		List<?> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2", DateUtil.date(), 0.111);
		List<?> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3", DateUtil.date(), 35);
		List<?> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4", DateUtil.date(), 28.00);

		List<List<?>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);
		BigExcelWriter overtimeWriter = ExcelUtil.getBigWriter("d:/test/style_line.xlsx");

		overtimeWriter.write(rows, true);

		CellStyle cellStyle = overtimeWriter.getWorkbook().createCellStyle();
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

		CellStyle cellStyle1 = overtimeWriter.getWorkbook().createCellStyle();
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
	@Ignore
	public void writeTest2() {
		List<String> row = CollUtil.newArrayList("姓名", "加班日期", "下班时间", "加班时长", "餐补", "车补次数", "车补", "总计");
		ExcelWriter overtimeWriter = ExcelUtil.getWriter("e:/excel/single_line.xlsx");
		overtimeWriter.writeRow(row);
		overtimeWriter.close();
	}

	@Test
	@Ignore
	public void writeWithSheetTest() {
		ExcelWriter writer = ExcelUtil.getWriterWithSheet("表格1");

		// 写出第一张表
		List<String> row = CollUtil.newArrayList("姓名", "加班日期", "下班时间", "加班时长", "餐补", "车补次数", "车补", "总计");
		writer.writeRow(row);

		// 写出第二张表
		writer.setSheet("表格2");
		List<String> row2 = CollUtil.newArrayList("姓名2", "加班日期2", "下班时间2", "加班时长2", "餐补2", "车补次数2", "车补2", "总计2");
		writer.writeRow(row2);

		// 生成文件或导出Excel
		writer.flush(FileUtil.file("f:/test/writeWithSheetTest.xlsx"));

		writer.close();
	}

	@Test
	@Ignore
	public void writeTest() {
		List<?> row1 = CollUtil.newArrayList("aaaaa", "bb", "cc", "dd", DateUtil.date(), 3.22676575765);
		List<?> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1", DateUtil.date(), 250.7676);
		List<?> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2", DateUtil.date(), 0.111);
		List<?> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3", DateUtil.date(), 35);
		List<?> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4", DateUtil.date(), 28.00);

		List<List<?>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);
		for (int i = 0; i < 400; i++) {
			// 超大列表写出测试
			rows.add(ObjectUtil.clone(row1));
		}

		String filePath = "d:/test/writeTest.xlsx";
		FileUtil.del(filePath);
		// 通过工具类创建writer
		ExcelWriter writer = ExcelUtil.getWriter(filePath);
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
	@Ignore
	public void mergeTest() {
		List<?> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd", DateUtil.date(), 3.22676575765);
		List<?> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1", DateUtil.date(), 250.7676);
		List<?> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2", DateUtil.date(), 0.111);
		List<?> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3", DateUtil.date(), 35);
		List<?> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4", DateUtil.date(), 28.00);

		List<List<?>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);

		// 通过工具类创建writer
		ExcelWriter writer = ExcelUtil.getWriter("d:/test/mergeTest.xlsx");
		CellStyle style = writer.getStyleSet().getHeadCellStyle();
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
	@Ignore
	public void mergeTest2() {
		Map<String, Object> row1 = new LinkedHashMap<>();
		row1.put("姓名", "张三");
		row1.put("年龄", 23);
		row1.put("成绩", 88.32);
		row1.put("是否合格", true);
		row1.put("考试日期", DateUtil.date());

		Map<String, Object> row2 = new LinkedHashMap<>();
		row2.put("姓名", "李四");
		row2.put("年龄", 33);
		row2.put("成绩", 59.50);
		row2.put("是否合格", false);
		row2.put("考试日期", DateUtil.date());

		ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);

		// 通过工具类创建writer
		ExcelWriter writer = ExcelUtil.getWriter("d:/test/writeMapTest.xlsx");
		// 合并单元格后的标题行，使用默认标题样式
		writer.merge(row1.size() - 1, "一班成绩单");

		// 一次性写出内容，使用默认样式，强制输出标题
		writer.write(rows, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Ignore
	public void writeMapTest() {
		Map<String, Object> row1 = new LinkedHashMap<>();
		row1.put("姓名", "张三");
		row1.put("年龄", 23);
		row1.put("成绩", 88.32);
		row1.put("是否合格", true);
		row1.put("考试日期", DateUtil.date());

		Map<String, Object> row2 = new LinkedHashMap<>();
		row2.put("姓名", "李四");
		row2.put("年龄", 33);
		row2.put("成绩", 59.50);
		row2.put("是否合格", false);
		row2.put("考试日期", DateUtil.date());

		ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);

		// 通过工具类创建writer
		ExcelWriter writer = ExcelUtil.getWriter("e:/excel/writeMapTest.xlsx");

		// 设置内容字体
		Font font = writer.createFont();
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
	@Ignore
	public void writeMapTest2() {
		Map<String, Object> row1 = MapUtil.newHashMap(true);
		row1.put("姓名", "张三");
		row1.put("年龄", 23);
		row1.put("成绩", 88.32);
		row1.put("是否合格", true);
		row1.put("考试日期", DateUtil.date());

		// 通过工具类创建writer
		ExcelWriter writer = ExcelUtil.getWriter("e:/writeMapTest2.xlsx");

		// 一次性写出内容，使用默认样式
		writer.writeRow(row1, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Ignore
	public void writeMapWithStyleTest() {
		Map<String, Object> row1 = MapUtil.newHashMap(true);
		row1.put("姓名", "张三");
		row1.put("年龄", 23);
		row1.put("成绩", 88.32);
		row1.put("是否合格", true);
		row1.put("考试日期", DateUtil.date());

		// 通过工具类创建writer
		String path = "f:/test/writeMapWithStyleTest.xlsx";
		FileUtil.del(path);
		ExcelWriter writer = ExcelUtil.getWriter(path);
		writer.setStyleSet(null);

		// 一次性写出内容，使用默认样式
		writer.writeRow(row1, true);

		// 设置某个单元格样式
		CellStyle orCreateRowStyle = writer.getOrCreateCellStyle(0, 1);
		StyleUtil.setColor(orCreateRowStyle, IndexedColors.RED.getIndex(), FillPatternType.SOLID_FOREGROUND);

		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Ignore
	public void writeMapAliasTest() {
		Map<Object, Object> row1 = new LinkedHashMap<>();
		row1.put("name", "张三");
		row1.put("age", 22);
		row1.put("isPass", true);
		row1.put("score", 66.30);
		row1.put("examDate", DateUtil.date());
		Map<Object, Object> row2 = new LinkedHashMap<>();
		row2.put("name", "李四");
		row2.put("age", 233);
		row2.put("isPass", false);
		row2.put("score", 32.30);
		row2.put("examDate", DateUtil.date());

		List<Map<Object, Object>> rows = CollUtil.newArrayList(row1, row2);
		// 通过工具类创建writer
		String file = "d:/test/writeMapAlias.xlsx";
		FileUtil.del(file);
		ExcelWriter writer = ExcelUtil.getWriter(file);
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
	@Ignore
	public void writeMapOnlyAliasTest() {
		Map<Object, Object> row1 = new LinkedHashMap<>();
		row1.put("name", "张三");
		row1.put("age", 22);
		row1.put("isPass", true);
		row1.put("score", 66.30);
		row1.put("examDate", DateUtil.date());
		Map<Object, Object> row2 = new LinkedHashMap<>();
		row2.put("name", "李四");
		row2.put("age", 233);
		row2.put("isPass", false);
		row2.put("score", 32.30);
		row2.put("examDate", DateUtil.date());

		List<Map<Object, Object>> rows = CollUtil.newArrayList(row1, row2);
		// 通过工具类创建writer
		String file = "f:/test/test_alias.xlsx";
		FileUtil.del(file);
		ExcelWriter writer = ExcelUtil.getWriter(file);
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
	@Ignore
	public void writeMapOnlyAliasTest2() {
		Map<Object, Object> row1 = new LinkedHashMap<>();
		row1.put("name", "张三");
		row1.put("age", 22);
		row1.put("isPass", true);
		row1.put("score", 66.30);
		row1.put("examDate", DateUtil.date());
		Map<Object, Object> row2 = new LinkedHashMap<>();
		row2.put("name", "李四");
		row2.put("age", 233);
		row2.put("isPass", false);
		row2.put("score", 32.30);
		row2.put("examDate", DateUtil.date());

		List<Map<Object, Object>> rows = CollUtil.newArrayList(row1, row2);
		// 通过工具类创建writer
		String file = "d:/test/test_alias.xls";
		ExcelWriter writer = ExcelUtil.getWriter(file, "test1");
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
	@Ignore
	public void writeMapOnlyAliasTest3() {
		Map<Object, Object> row1 = new LinkedHashMap<>();
		row1.put("name", "张三");
		row1.put("age", 22);
		row1.put("isPass", true);
		row1.put("score", 66.30);
		row1.put("examDate", DateUtil.date());

		Map<Object, Object> row2 = new LinkedHashMap<>();
		row2.put("name", "李四");
//		row2.put("age", 233);
		row2.put("isPass", false);
		row2.put("score", 32.30);
		row2.put("examDate", DateUtil.date());

		List<Map<Object, Object>> rows = CollUtil.newArrayList(row1, row2);
		// 通过工具类创建writer
		String file = "d:/test/test_alias.xls";
		ExcelWriter writer = ExcelUtil.getWriter(file, "test1");
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
	@Ignore
	public void writeBeanTest() {
		cn.hutool.poi.excel.TestBean bean1 = new cn.hutool.poi.excel.TestBean();
		bean1.setName("张三");
		bean1.setAge(22);
		bean1.setPass(true);
		bean1.setScore(66.30);
		bean1.setExamDate(DateUtil.date());

		cn.hutool.poi.excel.TestBean bean2 = new cn.hutool.poi.excel.TestBean();
		bean2.setName("李四");
		bean2.setAge(28);
		bean2.setPass(false);
		bean2.setScore(38.50);
		bean2.setExamDate(DateUtil.date());

		List<cn.hutool.poi.excel.TestBean> rows = CollUtil.newArrayList(bean1, bean2);
		// 通过工具类创建writer
		String file = "e:/writeBeanTest.xlsx";
		FileUtil.del(file);
		ExcelWriter writer = ExcelUtil.getWriter(file);
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
	@Ignore
	public void writeBeanTest2() {
		cn.hutool.poi.excel.OrderExcel order1 = new cn.hutool.poi.excel.OrderExcel();
		order1.setId("1");
		order1.setNum("123");
		order1.setBody("body1");

		cn.hutool.poi.excel.OrderExcel order2 = new cn.hutool.poi.excel.OrderExcel();
		order1.setId("2");
		order1.setNum("456");
		order1.setBody("body2");

		List<cn.hutool.poi.excel.OrderExcel> rows = CollUtil.newArrayList(order1, order2);
		// 通过工具类创建writer
		String file = "f:/test/writeBeanTest2.xlsx";
		FileUtil.del(file);
		ExcelWriter writer = ExcelUtil.getWriter(file);
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
	@Ignore
	public void writeCellValueTest() {
		ExcelWriter writer = new ExcelWriter("d:/cellValueTest.xls");
		writer.writeCellValue(3, 5, "aaa");
		writer.writeCellValue(3, 5, "aaa");
		writer.close();
	}

	@Test
	@Ignore
	public void addSelectTest() {
		List<String> row = CollUtil.newArrayList("姓名", "加班日期", "下班时间", "加班时长", "餐补", "车补次数", "车补", "总计");
		ExcelWriter overtimeWriter = ExcelUtil.getWriter("d:/test/single_line.xlsx");
		overtimeWriter.writeCellValue(3, 4, "AAAA");
		overtimeWriter.addSelect(3, 4, row.toArray(new String[0]));
		overtimeWriter.close();
	}

	@Test
	@Ignore
	public void addSelectTest2() {
		ExcelWriter writer = ExcelUtil.getWriter("d:/test/select.xls");
		writer.writeCellValue(0, 0, "请选择科目");
		int firstRow = 0;
		int lastRow = 0;
		int firstCol = 0;
		int lastCol = 0;
		CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		writer.addSelect(addressList, "1001", "1002", "1003");

		List<?> rows = new ArrayList<>();
		writer.write(rows, true);

		writer.close();
	}

	@Test
	@Ignore
	public void writeMultiSheetTest() {
		List<Map<String, Object>> rows = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> tempList = new TreeMap<>();
			for (int j = 0; j < 10; j++) {
				tempList.put(j + "", IdUtil.randomUUID());
			}
			rows.add(tempList);
		}
		ExcelWriter writer = ExcelUtil.getWriter("D:\\test\\multiSheet.xlsx", "正常数据");
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
	@Ignore
	public void writeMapsTest() {
		List<Map<String, Object>> rows = new ArrayList<>();

		Map<String, Object> map1 = new HashMap<>();
		map1.put("a", 1);
		map1.put("b", 2);
		map1.put("c", 3);
		map1.put("d", 4);
		map1.put("e", 5);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("c", 3);
		map2.put("d", 4);
		map2.put("e", 5);
		Map<String, Object> map3 = new HashMap<>();
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
	@Ignore
	public void formatTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/formatTest.xlsx");
		final CellStyle cellStyle = writer.createCellStyle(0, 0);
		cellStyle.setDataFormat(writer.getWorkbook().createDataFormat().getFormat("yyyy-mm-dd"));
		writer.close();
	}

	@Test
	@Ignore
	public void writeNumberFormatTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/formatTest.xlsx");
		writer.disableDefaultStyle();
		writer.writeRow(ListUtil.toList(51.33333333, 90.111111111));
		final CellStyle columnStyle = writer.createCellStyle(0, 0);
		columnStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("0.00"));
		writer.close();
	}

	@Test
	@Ignore
	public void writeSecHeadRowTest() {
		List<?> row1 = CollUtil.newArrayList(1, "aa", "bb", "cc", "dd", "ee");
		List<?> row2 = CollUtil.newArrayList(2, "aa1", "bb1", "cc1", "dd1", "ee1");
		List<?> row3 = CollUtil.newArrayList(3, "aa2", "bb2", "cc2", "dd2", "ee2");
		List<?> row4 = CollUtil.newArrayList(4, "aa3", "bb3", "cc3", "dd3", "ee3");
		List<?> row5 = CollUtil.newArrayList(5, "aa4", "bb4", "cc4", "dd4", "ee4");

		List<List<?>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);

		// 通过工具类创建writer
		ExcelWriter writer = ExcelUtil.getWriter("d:/test/writeSecHeadRowTest.xlsx");

		CellStyle cellStyle = writer.getWorkbook().createCellStyle();
		cellStyle.setWrapText(false);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		//设置标题内容字体
		Font font = writer.createFont();
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

		List<String> sechead = CollUtil.newArrayList("AA", "BB", "DD", "EE");
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
	@Ignore
	public void editTest() {
		// 生成文件
		File file = new File("d:/test/100_.xlsx");
		FileUtil.del(file);

		BigExcelWriter writer = ExcelUtil.getBigWriter(file);
		writer.disableDefaultStyle();
		List<List<String>> rows = Collections.singletonList(Arrays.asList("哈哈", "嘿嘿"));
		writer.write(rows);
		writer.close();

		// 修改文件
		ExcelWriter writer2 = ExcelUtil.getWriter(file);
		writer2.disableDefaultStyle();
		writer2.writeCellValue(0, 0, "a");
		writer2.close();

		final ExcelReader reader = ExcelUtil.getReader(file);
		Console.log(reader.read());
	}

	@Test
	@Ignore
	public void mergeTest3(){
		// https://github.com/dromara/hutool/issues/1696

		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> map = new HashMap<>();
		map.put("xmnf","2021");
		list.add(map);

		Map<String,Object> map1 = new HashMap<>();
		map1.put("xmnf",new XSSFRichTextString("9999"));
		list.add(map1);

		Map<String,Object> map2 = new HashMap<>();
		map2.put("xmnf","2019");
		list.add(map2);

		//通过工具类创建writer
		FileUtil.del("d:/test/writeTest2123.xlsx");
		ExcelWriter writer = ExcelUtil.getWriter("d:/test/writeTest2123.xlsx");
		writer.addHeaderAlias("xmnf", "项目年份");//1

		//合并单元格后的标题行，使用默认标题样式
		writer.merge(7, "测试标题");
		writer.merge(3, 4, 0, 0, new XSSFRichTextString("9999"), true);
		writer.write(list, true);
		writer.close();
	}
}
