package cn.hutool.poi.excel.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 写出Excel单元测试
 * 
 * @author looly
 */
public class BigExcelWriteTest {
	
	@Test
	@Ignore
	public void writeTest2() {
		List<String> row = CollUtil.newArrayList("姓名", "加班日期", "下班时间", "加班时长", "餐补", "车补次数", "车补", "总计");
		BigExcelWriter overtimeWriter = ExcelUtil.getBigWriter("e:/excel/single_line.xlsx");
		overtimeWriter.write(row);
		overtimeWriter.close();
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
		for(int i=0; i < 400000; i++) {
			//超大列表写出测试
			rows.add(ObjectUtil.clone(row1));
		}
		
		String filePath = "e:/bigWriteTest.xlsx";
		FileUtil.del(filePath);
		// 通过工具类创建writer
		BigExcelWriter writer = ExcelUtil.getBigWriter(filePath);

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
	@Ignore
	public void mergeTest() {
		List<?> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd", DateUtil.date(), 3.22676575765);
		List<?> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1", DateUtil.date(), 250.7676);
		List<?> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2", DateUtil.date(), 0.111);
		List<?> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3", DateUtil.date(), 35);
		List<?> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4", DateUtil.date(), 28.00);

		List<List<?>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);

		// 通过工具类创建writer
		BigExcelWriter writer = ExcelUtil.getBigWriter("e:/mergeTest.xlsx");
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
		String path = "e:/bigWriteMapTest.xlsx";
		FileUtil.del(path);
		BigExcelWriter writer = ExcelUtil.getBigWriter(path);
		
		//设置内容字体
		Font font = writer.createFont();
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
	@Ignore
	public void writeMapTest2() {
		Map<String, Object> row1 = MapUtil.newHashMap(true);
		row1.put("姓名", "张三");
		row1.put("年龄", 23);
		row1.put("成绩", 88.32);
		row1.put("是否合格", true);
		row1.put("考试日期", DateUtil.date());
		
		// 通过工具类创建writer
		String path = "e:/bigWriteMapTest2.xlsx";
		FileUtil.del(path);
		BigExcelWriter writer = ExcelUtil.getBigWriter(path);
		
		// 一次性写出内容，使用默认样式
		writer.writeRow(row1, true);
		// 关闭writer，释放内存
		writer.close();
	}

	@Test
	@Ignore
	public void writeBeanTest() {
		TestBean bean1 = new TestBean();
		bean1.setName("张三");
		bean1.setAge(22);
		bean1.setPass(true);
		bean1.setScore(66.30);
		bean1.setExamDate(DateUtil.date());

		TestBean bean2 = new TestBean();
		bean2.setName("李四");
		bean2.setAge(28);
		bean2.setPass(false);
		bean2.setScore(38.50);
		bean2.setExamDate(DateUtil.date());

		List<TestBean> rows = CollUtil.newArrayList(bean1, bean2);
		// 通过工具类创建writer
		String file = "e:/bigWriteBeanTest.xlsx";
		FileUtil.del(file);
		BigExcelWriter writer = ExcelUtil.getBigWriter(file);
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
	@Ignore
	public void writeCellValueTest() {
		String path = "d:/test/cellValueTest.xlsx";
		FileUtil.del(path);
		BigExcelWriter writer = new BigExcelWriter(path);
		writer.writeCellValue(3, 5, "aaa");
		writer.close();
	}

	@Test
	@Ignore
	public void closeTest() {
		final Map<String, ?> map1 = MapUtil.of("id", "123456");
		final Map<String, ?> map2 = MapUtil.of("id", "123457");
		final List<?> data = Arrays.asList(map1, map2);
		final String destFilePath = "d:/test/closeTest.xlsx";//略
		FileUtil.del(destFilePath);
		try (ExcelWriter writer = ExcelUtil.getBigWriter(destFilePath)) {
			writer.write(data).flush();
		}
	}

	@Test
	@Ignore
	public void issue1210() {
		// 通过工具类创建writer
		String path = "d:/test/issue1210.xlsx";
		FileUtil.del(path);
		BigExcelWriter writer = ExcelUtil.getBigWriter(path);
		writer.addHeaderAlias("id", "SN");
		writer.addHeaderAlias("userName", "User Name");

		List<Map<String, Object>> list = new ArrayList<>();
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
