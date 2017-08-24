package com.xiaoleilu.hutool.poi.excel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.xiaoleilu.hutool.collection.IterUtil;
import com.xiaoleilu.hutool.poi.excel.editors.NumericToLongEditor;
import com.xiaoleilu.hutool.poi.excel.editors.TrimEditor;
import com.xiaoleilu.hutool.util.BeanUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Excel读取器<br>
 * 读取Excel工作簿
 * 
 * @author Looly
 * @since 3.1.0
 */
public class ExcelReader {

	/** Excel中对应的Sheet */
	private Sheet sheet;

	/** 是否忽略空行 */
	private boolean ignoreEmptyRow;
	/** 单元格值处理接口 */
	private CellEditor cellEditor;
	/** 标题别名 */
	private Map<String, String> headerAlias = new HashMap<>();

	// ------------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * 
	 * @param bookFile Excel文件
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 */
	public ExcelReader(File bookFile, int sheetIndex) {
		this(ExcelUtil.loadBook(bookFile), sheetIndex);
	}

	/**
	 * 构造
	 * 
	 * @param bookFile Excel文件
	 * @param sheetName sheet名，第一个默认是sheet1
	 */
	public ExcelReader(File bookFile, String sheetName) {
		this(ExcelUtil.loadBook(bookFile), sheetName);
	}

	/**
	 * 构造
	 * 
	 * @param bookStream Excel文件的流
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 */
	public ExcelReader(InputStream bookStream, int sheetIndex) {
		this(ExcelUtil.loadBook(bookStream), sheetIndex);
	}

	/**
	 * 构造
	 * 
	 * @param bookStream Excel文件的流
	 * @param sheetName sheet名，第一个默认是sheet1
	 */
	public ExcelReader(InputStream bookStream, String sheetName) {
		this(ExcelUtil.loadBook(bookStream), sheetName);
	}

	/**
	 * 构造
	 * 
	 * @param book {@link Workbook} 表示一个Excel文件
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 */
	public ExcelReader(Workbook book, int sheetIndex) {
		this(book.getSheetAt(sheetIndex));
	}

	/**
	 * 构造
	 * 
	 * @param book {@link Workbook} 表示一个Excel文件
	 * @param sheetName sheet名，第一个默认是sheet1
	 */
	public ExcelReader(Workbook book, String sheetName) {
		this(book.getSheet(sheetName));
	}

	/**
	 * 构造
	 * 
	 * @param sheet Excel中的sheet
	 */
	public ExcelReader(Sheet sheet) {
		this.sheet = sheet;
	}
	// ------------------------------------------------------------------------------------------------------- Constructor end

	// ------------------------------------------------------------------------------------------------------- Getters and Setters start
	/**
	 * 是否忽略空行
	 * 
	 * @return 是否忽略空行
	 */
	public boolean isIgnoreEmptyRow() {
		return ignoreEmptyRow;
	}

	/**
	 * 设置是否忽略空行
	 * 
	 * @param ignoreEmptyRow 是否忽略空行
	 * @return this
	 */
	public ExcelReader setIgnoreEmptyRow(boolean ignoreEmptyRow) {
		this.ignoreEmptyRow = ignoreEmptyRow;
		return this;
	}

	/**
	 * 设置单元格值处理逻辑<br>
	 * 当Excel中的值并不能满足我们的读取要求时，通过传入一个编辑接口，可以对单元格值自定义，例如对数字和日期类型值转换为字符串等
	 * 
	 * @param cellEditor 单元格值处理接口
	 * @return this
	 * @see TrimEditor
	 * @see NumericToLongEditor
	 */
	public ExcelReader setCellEditor(CellEditor cellEditor) {
		this.cellEditor = cellEditor;
		return this;
	}

	/**
	 * 获得标题行的别名Map
	 * 
	 * @return 别名Map
	 */
	public Map<String, String> getHeaderAlias() {
		return headerAlias;
	}

	/**
	 * 设置标题行的别名Map
	 * 
	 * @param headerAlias 别名Map
	 */
	public ExcelReader setHeaderAlias(Map<String, String> headerAlias) {
		this.headerAlias = headerAlias;
		return this;
	}

	/**
	 * 增加标题别名
	 * 
	 * @param header 标题
	 * @param alias 别名
	 * @return this
	 */
	public ExcelReader addHeaderAlias(String header, String alias) {
		this.headerAlias.put(header, alias);
		return this;
	}

	/**
	 * 去除标题别名
	 * 
	 * @param header 标题
	 * @return this
	 */
	public ExcelReader removeHeaderAlias(String header) {
		this.headerAlias.remove(header);
		return this;
	}
	// ------------------------------------------------------------------------------------------------------- Getters and Setters end

	/**
	 * 读取工作簿中指定的Sheet的所有行列数据
	 * 
	 * @return 行的集合，一行使用List表示
	 */
	public List<List<Object>> read() {
		return read(0, Integer.MAX_VALUE);
	}

	/**
	 * 读取工作簿中指定的Sheet
	 * 
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex 结束行（包含，从0开始计数）
	 * @return 行的集合，一行使用List表示
	 */
	public List<List<Object>> read(int startRowIndex, int endRowIndex) {
		List<List<Object>> resultList = new ArrayList<>();

		startRowIndex = Math.max(startRowIndex, sheet.getFirstRowNum());// 读取起始行（包含）
		endRowIndex = Math.min(endRowIndex, sheet.getLastRowNum());// 读取结束行（包含）
		List<Object> rowList;
		for (int i = startRowIndex; i <= endRowIndex; i++) {
			rowList = readRow(sheet.getRow(i));
			if (false == rowList.isEmpty() || false == ignoreEmptyRow) {
				resultList.add(rowList);
			}
		}
		return resultList;
	}

	/**
	 * 读取Excel为Map的列表，读取所有行，默认第一行做为标题，数据从第二行开始<br>
	 * Map表示一行，标题为key，单元格内容为value
	 * 
	 * @return Map的列表
	 */
	public List<Map<String, Object>> readAll() {
		return read(0, 1, Integer.MAX_VALUE);
	}

	/**
	 * 读取Excel为Map的列表<br>
	 * Map表示一行，标题为key，单元格内容为value
	 * 
	 * @param headerRowIndex 标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex 读取结束行（包含，从0开始计数）
	 * @return Map的列表
	 */
	public List<Map<String, Object>> read(int headerRowIndex, int startRowIndex, int endRowIndex) {
		// 边界判断
		final int firstRowNum = sheet.getFirstRowNum();
		final int lastRowNum = sheet.getLastRowNum();
		if (headerRowIndex < firstRowNum) {
			throw new IndexOutOfBoundsException(StrUtil.format("Header row index {} is lower than first row index {}.", headerRowIndex, firstRowNum));
		} else if (headerRowIndex > lastRowNum) {
			throw new IndexOutOfBoundsException(StrUtil.format("Header row index {} is greater than last row index {}.", headerRowIndex, firstRowNum));
		}
		startRowIndex = Math.max(startRowIndex, firstRowNum);// 读取起始行（包含）
		endRowIndex = Math.min(endRowIndex, lastRowNum);// 读取结束行（包含）

		// 读取header
		List<Object> headerList = readRow(sheet.getRow(headerRowIndex));

		final List<Map<String, Object>> result = new ArrayList<>(endRowIndex - startRowIndex + 1);
		List<Object> rowList;
		for (int i = startRowIndex; i <= endRowIndex; i++) {
			if (i != headerRowIndex) {
				// 跳过标题行
				rowList = readRow(sheet.getRow(i));
				if (false == rowList.isEmpty() || false == ignoreEmptyRow) {
					result.add(IterUtil.toMap(aliasHeader(headerList), rowList));
				}
			}
		}
		return result;
	}

	/**
	 * 读取Excel为Bean的列表，读取所有行，默认第一行做为标题，数据从第二行开始
	 * 
	 * @param <T> Bean类型
	 * @param beanType 每行对应Bean的类型
	 * @return Map的列表
	 */
	public <T> List<T> readAll(Class<T> beanType) {
		return read(0, 1, Integer.MAX_VALUE, beanType);
	}

	/**
	 * 读取Excel为Bean的列表
	 * 
	 * @param <T> Bean类型
	 * @param headerRowIndex 标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略，，从0开始计数
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex 读取结束行（包含，从0开始计数）
	 * @param beanType 每行对应Bean的类型
	 * @return Map的列表
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> read(int headerRowIndex, int startRowIndex, int endRowIndex, Class<T> beanType) {
		final List<Map<String, Object>> mapList = read(headerRowIndex, startRowIndex, endRowIndex);
		if (Map.class.isAssignableFrom(beanType)) {
			return (List<T>) mapList;
		}

		final List<T> beanList = new ArrayList<>(mapList.size());
		for (Map<String, Object> map : mapList) {
			beanList.add(BeanUtil.mapToBean(map, beanType, false));
		}
		return beanList;
	}

	// ------------------------------------------------------------------------------------------------------- Private methods start
	/**
	 * 读取一行
	 * 
	 * @param row 行
	 * @return 单元格值列表
	 */
	private List<Object> readRow(Row row) {
		final List<Object> cellValues = new ArrayList<>();
		
		short length = row.getLastCellNum();
		for (short i = 0; i < length; i++) {
			cellValues.add(ExcelUtil.getCellValue(row.getCell(i), cellEditor));
		}
		return cellValues;
	}

	/**
	 * 转换标题别名，如果没有别名则使用原标题
	 * 
	 * @param headerList 原标题列表
	 * @return 转换别名列表
	 */
	private List<String> aliasHeader(List<Object> headerList) {
		final ArrayList<String> result = new ArrayList<>();
		String header;
		String alias;
		for (Object headerObj : headerList) {
			header = headerObj.toString();
			alias = this.headerAlias.get(header);
			if (null == alias) {
				alias = header;
			}
			result.add(alias);
		}
		return result;
	}
	// ------------------------------------------------------------------------------------------------------- Private methods end
}
