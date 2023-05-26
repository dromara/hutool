package org.dromara.hutool.poi.excel;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.poi.excel.cell.values.FormulaCellValue;
import org.dromara.hutool.poi.excel.sax.Excel03SaxReader;
import org.dromara.hutool.poi.excel.sax.handler.RowHandler;
import org.dromara.hutool.poi.exceptions.POIException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
		final Excel03SaxReader reader = new Excel03SaxReader(createRowHandler());
		reader.read("aaa.xls", 1);

		// Console.log("Sheet index: [{}], Sheet name: [{}]", reader.getSheetIndex(), reader.getSheetName());
		ExcelUtil.readBySax("aaa.xls", 1, createRowHandler());
	}

	@Test
	public void excel03ByNameTest() {
		final Excel03SaxReader reader = new Excel03SaxReader(createRowHandler());
		reader.read("aaa.xls", "校园入学");
		reader.read("aaa.xls", "sheetName:校园入学");
	}

	@Test
	public void excel03ByNameErrorTest() {
		Assertions.assertThrows(POIException.class, ()->{
			// sheet名称不存在则报错
			final Excel03SaxReader reader = new Excel03SaxReader(createRowHandler());
			reader.read("aaa.xls", "校园入学1");
		});
	}

	@Test
	@Disabled
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
	@Disabled
	public void readBySaxTest2() {
		ExcelUtil.readBySax("d:/test/456789.xlsx", "0", (sheetIndex, rowIndex, rowList) -> Console.log(rowList));
	}

	private RowHandler createRowHandler() {
		return (sheetIndex, rowIndex, rowlist) -> {
//			Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist);
			if (5 != rowIndex && 6 != rowIndex) {
				// 测试样例中除第五行、第六行都为非空行
				Assertions.assertTrue(CollUtil.isNotEmpty(rowlist));
			}
		};
	}

	@Test
	@Disabled
	public void handle07CellTest() {
		ExcelUtil.readBySax("d:/test/test.xlsx", -1, new RowHandler() {

					@Override
					public void handleCell(final int sheetIndex, final long rowIndex, final int cellIndex, final Object value, final CellStyle xssfCellStyle) {
						Console.log("{} {} {}", rowIndex, cellIndex, value);
					}

					@Override
					public void handle(final int sheetIndex, final long rowIndex, final List<Object> rowCells) {

					}
				}
		);
	}

	@Test
	public void handle03CellTest() {
		ExcelUtil.readBySax("test.xls", -1, new RowHandler() {

					@Override
					public void handleCell(final int sheetIndex, final long rowIndex, final int cellIndex, final Object value, final CellStyle xssfCellStyle) {
						//Console.log("{} {} {}", rowIndex, cellIndex, value);
					}

					@Override
					public void handle(final int sheetIndex, final long rowIndex, final List<Object> rowCells) {
					}
				}
		);
	}

	@Test
	public void formulaRead03Test() {
		final List<Object> rows = new ArrayList<>();
		ExcelUtil.readBySax("data_for_sax_test.xls", -1, (i, i1, list) -> {
			if (list.size() > 1) {
				rows.add(list.get(1));
			} else {
				rows.add("");
			}
		});
		Assertions.assertEquals(50L, rows.get(3));
	}

	@Test
	public void formulaRead07Test() {
		// since 6.0.0修改
		// 默认不在行尾对齐单元格，因此只读取了有第二个值的行
		final List<Object> rows = new ArrayList<>();
		ExcelUtil.readBySax("data_for_sax_test.xlsx", 0, (i, i1, list) -> {
			if(list.size() > 1){
				rows.add(list.get(1));
			}
		});

		final FormulaCellValue value = (FormulaCellValue) rows.get(1);
		Assertions.assertEquals(50L, value.getResult());
	}

	@Test
	public void dateReadXlsTest() {
		final List<String> rows = new ArrayList<>();
		ExcelUtil.readBySax("data_for_sax_test.xls", 0,
				(i, i1, list) -> rows.add(StrUtil.toString(list.get(0)))
		);

		Assertions.assertEquals("2020-10-09 00:00:00", rows.get(1));
		// 非日期格式不做转换
		Assertions.assertEquals("112233", rows.get(2));
		Assertions.assertEquals("1000.0", rows.get(3));
		Assertions.assertEquals("2012-12-21 00:00:00", rows.get(4));
	}

	@Test
	public void dateReadXlsxTest() {
		final List<String> rows = new ArrayList<>();
		ExcelUtil.readBySax("data_for_sax_test.xlsx", 0,
				(i, i1, list) -> rows.add(StrUtil.toString(list.get(0)))
		);

		Assertions.assertEquals("2020-10-09 00:00:00", rows.get(1));
		// 非日期格式不做转换
		Assertions.assertEquals("112233", rows.get(2));
		// 读取实际值，而非带有格式处理过的值
		Assertions.assertEquals("1000.0", rows.get(3));
		Assertions.assertEquals("2012-12-21 00:00:00", rows.get(4));
	}

	@Test
	@Disabled
	public void dateReadXlsxTest2() {
		ExcelUtil.readBySax("d:/test/custom_date_format2.xlsx", 0,
				(i, i1, list) -> Console.log(list)
		);
	}

	@Test
	public void readBlankTest() {
		final File file = FileUtil.file("aaa.xlsx");

		final List<List<Object>> list = ListUtil.of();
		ExcelUtil.readBySax(file, 0, (sheetIndex, rowIndex, rowList) -> list.add(rowList));

		Assertions.assertEquals("[, 女, , 43.22]", list.get(3).toString());
	}

	@Test
	@Disabled
	public void readXlsmTest() {
		ExcelUtil.readBySax("d:/test/WhiteListTemplate.xlsm", -1,
				(sheetIndex, rowIndex, rowlist) -> Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist));
	}

	@Test
	public void doAfterAllAnalysedTest() {
		final String path = "readBySax.xls";
		final AtomicInteger doAfterAllAnalysedTime = new AtomicInteger(0);
		ExcelUtil.readBySax(path, -1, new RowHandler() {
			@Override
			public void handle(final int sheetIndex, final long rowIndex, final List<Object> rowCells) {
				//Console.log("sheetIndex={};rowIndex={},rowCells={}",sheetIndex,rowIndex,rowCells);
			}

			@Override
			public void doAfterAllAnalysed() {
				doAfterAllAnalysedTime.addAndGet(1);
			}
		});
		//总共2个sheet页，读取所有sheet时，一共执行doAfterAllAnalysed2次。
		Assertions.assertEquals(2, doAfterAllAnalysedTime.intValue());
	}
}
