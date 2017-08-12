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
	/** 是否去除单元格元素两边空格 */
	private boolean trimCellValue;
	/** 标题别名 */
//	private Map<String, String> headerAlias;
	private Sheet sheet;

	// ------------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param bookStream Excel文件的流
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 */
	public ExcelReader(InputStream bookStream, int sheetIndex) {
		this(ExcelUtil.loadBook(bookStream), sheetIndex);
	}

	/**
	 * 构造
	 * @param bookStream Excel文件的流
	 * @param sheetName sheet名，第一个默认是sheet1
	 */
	public ExcelReader(InputStream bookStream, String sheetName) {
		this(ExcelUtil.loadBook(bookStream), sheetName);
	}

	/**
	 * 构造
	 * @param book {@link Workbook} 表示一个Excel文件
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 */
	public ExcelReader(Workbook book, int sheetIndex) {
		this(book.getSheetAt(sheetIndex));
	}

	/**
	 * 构造
	 * @param book {@link Workbook} 表示一个Excel文件
	 * @param sheetName sheet名，第一个默认是sheet1
	 */
	public ExcelReader(Workbook book, String sheetName) {
		this(book.getSheet(sheetName));
	}

	/**
	 * 构造
	 * @param sheet Excel中的sheet
	 */
	public ExcelReader(Sheet sheet) {
		this.sheet = sheet;
	}
	// ------------------------------------------------------------------------------------------------------- Constructor end
	
	// ------------------------------------------------------------------------------------------------------- Getters and Setters start
	/**
	 * 是否忽略空行
	 * @return 是否忽略空行
	 */
	public boolean isIgnoreEmptyRow() {
		return ignoreEmptyRow;
	}

	/**
	 * 设置是否忽略空行
	 * @param ignoreEmptyRow 是否忽略空行
	 * @return this
	 */
	public ExcelReader setIgnoreEmptyRow(boolean ignoreEmptyRow) {
		this.ignoreEmptyRow = ignoreEmptyRow;
		return this;
	}

	/**
	 * 是否去掉单元格值两边空格
	 * @return 是否去掉单元格值两边空格
	 */
	public boolean isTrimCellValue() {
		return trimCellValue;
	}

	/**
	 * 设置是否去掉单元格值两边空格
	 * @param trimCellValue 是否去掉单元格值两边空格
	 * @return this
	 */
	public ExcelReader setTrimCellValue(boolean trimCellValue) {
		this.trimCellValue = trimCellValue;
		return this;
	}
	// ------------------------------------------------------------------------------------------------------- Getters and Setters end

	/**
	 * 读取工作簿中指定的Sheet的所有行列数据
	 * 
	 * @param startRowIndex 起始行
	 * @return 行的集合，一行使用List表示
	 */
	public List<List<Object>> read() {
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
				cellValues.add(ExcelUtil.getCellValue(cell, this.trimCellValue));
			}
		}
		return cellValues;
	}
}
