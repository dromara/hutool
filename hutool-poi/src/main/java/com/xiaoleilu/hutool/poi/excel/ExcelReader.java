package com.xiaoleilu.hutool.poi.excel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel读取器<br>
 * 读取Excel工作簿
 * 
 * @author Looly
 *
 */
public class ExcelReader {
	
	/** 是否忽略空行 */
	private boolean ignoreEmptyRow;
	/** 是否忽略空行 */
//	private Map<String, String> headerAlias;
	private Sheet sheet;

	// ------------------------------------------------------------------------------------------------------- Constructor start
	public ExcelReader(InputStream bookStream, int sheetIndex) {
		this(ExcelUtil.loadBook(bookStream), sheetIndex);
	}

	public ExcelReader(InputStream bookStream, String sheetName) {
		this(ExcelUtil.loadBook(bookStream), sheetName);
	}

	public ExcelReader(Workbook book, int sheetIndex) {
		this(book.getSheetAt(sheetIndex));
	}

	public ExcelReader(Workbook book, String sheetName) {
		this(book.getSheet(sheetName));
	}

	public ExcelReader(Sheet sheet) {
		this.sheet = sheet;
	}
	// ------------------------------------------------------------------------------------------------------- Constructor end

	/**
	 * 读取工作簿中指定的Sheet的所有行列数据
	 * 
	 * @param startRowIndex 起始行
	 * @return 行的集合，一行使用List表示
	 */
	public List<List<Object>> readAll() {
		return read(0, Integer.MAX_VALUE);
	}

	/**
	 * 读取工作簿中指定的Sheet
	 * 
	 * @param startRowIndex 起始行（包含）
	 * @param endRowIndex 结束行（包含）
	 * @return 行的集合，一行使用List表示
	 */
	public List<List<Object>> read(int startRowIndex, int endRowIndex) {
		List<List<Object>> resultList = new ArrayList<>();

		final int firstRowIndex = Math.max(startRowIndex, sheet.getFirstRowNum());// 读取起始行（包含）
		final int lastRowIndex = Math.min(endRowIndex, sheet.getLastRowNum());// 读取结束行（包含）
		List<Object> rowList;
		for (int i = firstRowIndex; i <= lastRowIndex; i++) {
			rowList = readRow(sheet.getRow(i));
			if(false == rowList.isEmpty() || false == ignoreEmptyRow) {
				resultList.add(rowList);
			}
		}
		return resultList;
	}

	/**
	 * 读取一行
	 * 
	 * @param row 行
	 * @return 单元格值列表
	 */
	private List<Object> readRow(Row row) {
		final List<Object> cellValues = new ArrayList<>();
		if(null != row) {
			final Iterator<Cell> celIter = row.cellIterator();
			Cell cell;
			while (celIter.hasNext()) {
				cell = celIter.next();
				cellValues.add(ExcelUtil.getCellValue(cell));
			}
		}
		return cellValues;
	}
}
