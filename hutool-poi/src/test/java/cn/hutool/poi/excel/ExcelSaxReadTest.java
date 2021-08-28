package cn.hutool.poi.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.cell.FormulaCellValue;
import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import cn.hutool.poi.exceptions.POIException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel sax方式读取
 *
 * @author looly
 */
public class ExcelSaxReadTest {

	@Test
	public void excel07Test() {
		// 工具化快速读取
		ExcelUtil.readBySax("aaa.xlsx", 0, createRowHandler());
	}

	@Test
	public void excel07ByNameTest() {
		// 工具化快速读取
		// sheet名称是区分大小写的
		ExcelUtil.readBySax("aaa.xlsx", "Sheet1", createRowHandler());
		// 纯数字名称也支持
		ExcelUtil.readBySax("aaa.xlsx", "12", createRowHandler());
		// 前缀支持
		ExcelUtil.readBySax("aaa.xlsx", "sheetName:12", createRowHandler());
	}

	@Test
	public void excel07FromStreamTest() {
		// issue#1225 非markSupport的流读取会错误
		ExcelUtil.readBySax(IoUtil.toStream(FileUtil.file("aaa.xlsx")), 0, createRowHandler());
	}

	@Test
	public void excel03Test() {
		Excel03SaxReader reader = new Excel03SaxReader(createRowHandler());
		reader.read("aaa.xls", 1);

		// Console.log("Sheet index: [{}], Sheet name: [{}]", reader.getSheetIndex(), reader.getSheetName());
		ExcelUtil.readBySax("aaa.xls", 1, createRowHandler());
	}

	@Test
	public void excel03ByNameTest() {
		Excel03SaxReader reader = new Excel03SaxReader(createRowHandler());
		reader.read("aaa.xls", "校园入学");
		reader.read("aaa.xls", "sheetName:校园入学");
	}

	@Test(expected = POIException.class)
	public void excel03ByNameErrorTest() {
		// sheet名称不存在则报错
		Excel03SaxReader reader = new Excel03SaxReader(createRowHandler());
		reader.read("aaa.xls", "校园入学1");
	}

	@Test
	@Ignore
	public void readBlankLineTest() {
		ExcelUtil.readBySax("e:/ExcelBlankLine.xlsx", 0, (sheetIndex, rowIndex, rowList) -> {
			if (StrUtil.isAllEmpty(Convert.toStrArray(rowList))) {
				return;
			}
			Console.log(rowList);
		});
	}

	@Test
	public void readBySaxTest() {
		ExcelUtil.readBySax("blankAndDateTest.xlsx", "0", createRowHandler());
	}

	@Test
	public void readBySaxByRidTest() {
		ExcelUtil.readBySax("blankAndDateTest.xlsx", 0, createRowHandler());
	}

	@Test
	public void readBySaxByNameTest() {
		ExcelUtil.readBySax("blankAndDateTest.xlsx", "Sheet1", createRowHandler());
	}

	@Test
	@Ignore
	public void readBySaxTest2() {
		ExcelUtil.readBySax("d:/test/456789.xlsx", "0", (sheetIndex, rowIndex, rowList) -> Console.log(rowList));
	}

	private RowHandler createRowHandler() {
		return (sheetIndex, rowIndex, rowlist) -> {
//			Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist);
			if (5 != rowIndex && 6 != rowIndex) {
				// 测试样例中除第五行、第六行都为非空行
				Assert.assertTrue(CollUtil.isNotEmpty(rowlist));
			}
		};
	}

	@Test
	@Ignore
	public void handle07CellTest() {
		ExcelUtil.readBySax("d:/test/test.xlsx", -1, new RowHandler() {

					@Override
					public void handleCell(int sheetIndex, long rowIndex, int cellIndex, Object value, CellStyle xssfCellStyle) {
						Console.log("{} {} {}", rowIndex, cellIndex, value);
					}

					@Override
					public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {

					}
				}
		);
	}

	@Test
	@Ignore
	public void handle03CellTest() {
		ExcelUtil.readBySax("d:/test/test.xls", -1, new RowHandler() {

					@Override
					public void handleCell(int sheetIndex, long rowIndex, int cellIndex, Object value, CellStyle xssfCellStyle) {
						Console.log("{} {} {}", rowIndex, cellIndex, value);
					}

					@Override
					public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
					}
				}
		);
	}

	@Test
	public void formulaRead03Test() {
		List<Object> rows = new ArrayList<>();
		ExcelUtil.readBySax("data_for_sax_test.xls", -1, (i, i1, list) -> {
			if(list.size() > 1){
				rows.add(list.get(1));
			} else{
				rows.add("");
			}
		});
		Assert.assertEquals(50L, rows.get(3));
	}

	@Test
	public void formulaRead07Test() {
		List<Object> rows = new ArrayList<>();
		ExcelUtil.readBySax("data_for_sax_test.xlsx", 0, (i, i1, list) ->
				rows.add(list.get(1)));

		final FormulaCellValue value = (FormulaCellValue) rows.get(3);
		Assert.assertEquals(50L, value.getResult());
	}

	@Test
	public void dateReadXlsTest() {
		List<String> rows = new ArrayList<>();
		ExcelUtil.readBySax("data_for_sax_test.xls", 0,
				(i, i1, list) -> rows.add(StrUtil.toString(list.get(0)))
		);

		Assert.assertEquals("2020-10-09 00:00:00", rows.get(1));
		// 非日期格式不做转换
		Assert.assertEquals("112233", rows.get(2));
		Assert.assertEquals("1000.0", rows.get(3));
		Assert.assertEquals("2012-12-21 00:00:00", rows.get(4));
	}

	@Test
	public void dateReadXlsxTest() {
		List<String> rows = new ArrayList<>();
		ExcelUtil.readBySax("data_for_sax_test.xlsx", 0,
				(i, i1, list) -> rows.add(StrUtil.toString(list.get(0)))
		);

		Assert.assertEquals("2020-10-09 00:00:00", rows.get(1));
		// 非日期格式不做转换
		Assert.assertEquals("112233", rows.get(2));
		Assert.assertEquals("1000.0", rows.get(3));
		Assert.assertEquals("2012-12-21 00:00:00", rows.get(4));
	}

	@Test
	@Ignore
	public void dateReadXlsxTest2() {
		ExcelUtil.readBySax("d:/test/custom_date_format2.xlsx", 0,
				(i, i1, list) -> Console.log(list)
		);
	}

	@Test
	@Ignore
	public void readBlankTest() {
		File file = new File("D:/test/b.xlsx");

		ExcelUtil.readBySax(file, 0, (sheetIndex, rowIndex, rowList) -> rowList.forEach(Console::log));

		ExcelUtil.getReader(file).read().forEach(Console::log);
	}

	@Test
	@Ignore
	public void readXlsmTest(){
		ExcelUtil.readBySax("d:/test/WhiteListTemplate.xlsm", -1,
				(sheetIndex, rowIndex, rowlist) -> Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist));
	}
}
