package cn.hutool.poi.excel;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.poi.excel.cell.CellUtil;

/**
 * Excel基础类，用于抽象ExcelWriter和ExcelReader中共用部分的对象和方法
 * 
 * @param <T> 子类类型，用于返回this
 * @author looly
 * @since 4.1.4
 */
public class ExcelBase<T extends ExcelBase<T>> implements Closeable {
	/** 是否被关闭 */
	protected boolean isClosed;
	/** 工作簿 */
	protected Workbook workbook;
	/** Excel中对应的Sheet */
	protected Sheet sheet;
	
	/**
	 * 构造
	 * 
	 * @param sheet Excel中的sheet
	 */
	public ExcelBase(Sheet sheet) {
		Assert.notNull(sheet, "No Sheet provided.");
		this.sheet = sheet;
		this.workbook = sheet.getWorkbook();
	}

	/**
	 * 获取Workbook
	 * 
	 * @return Workbook
	 */
	public Workbook getWorkbook() {
		return this.workbook;
	}
	
	/**
	 * 返回工作簿表格数
	 * 
	 * @return 工作簿表格数
	 * @since 4.0.10
	 */
	public int getSheetCount() {
		return this.workbook.getNumberOfSheets();
	}
	
	/**
	 * 获取此工作簿所有Sheet表
	 * 
	 * @return sheet表列表
	 * @since 4.0.3
	 */
	public List<Sheet> getSheets() {
		final int totalSheet = getSheetCount();
		final List<Sheet> result = new ArrayList<>(totalSheet);
		for (int i = 0; i < totalSheet; i++) {
			result.add(this.workbook.getSheetAt(i));
		}
		return result;
	}
	
	/**
	 * 获取表名列表
	 * 
	 * @return 表名列表
	 * @since 4.0.3
	 */
	public List<String> getSheetNames() {
		final int totalSheet = workbook.getNumberOfSheets();
		List<String> result = new ArrayList<>(totalSheet);
		for (int i = 0; i < totalSheet; i++) {
			result.add(this.workbook.getSheetAt(i).getSheetName());
		}
		return result;
	}

	/**
	 * 获取当前Sheet
	 * 
	 * @return {@link Sheet}
	 */
	public Sheet getSheet() {
		return this.sheet;
	}
	
	/**
	 * 自定义需要读取或写出的Sheet，如果给定的sheet不存在，创建之。<br>
	 * 在读取中，此方法用于切换读取的sheet，在写出时，此方法用于新建或者切换sheet。
	 * 
	 * @param sheetName sheet名
	 * @return this
	 * @since 4.0.10
	 */
	@SuppressWarnings("unchecked")
	public T setSheet(String sheetName) {
		this.sheet = this.workbook.getSheet(sheetName);
		if(null == this.sheet) {
			this.sheet = this.workbook.createSheet(sheetName);
		}
		return (T) this;
	}

	/**
	 * 自定义需要读取或写出的Sheet，如果给定的sheet不存在，创建之（命名为默认）<br>
	 * 在读取中，此方法用于切换读取的sheet，在写出时，此方法用于新建或者切换sheet
	 * 
	 * @param sheetIndex sheet序号，从0开始计数
	 * @return this
	 * @since 4.0.10
	 */
	@SuppressWarnings("unchecked")
	public T setSheet(int sheetIndex) {
		this.sheet = this.workbook.getSheetAt(sheetIndex);
		if(null == this.sheet) {
			this.sheet = this.workbook.createSheet();
		}
		return (T) this;
	}

	/**
	 * 获取指定坐标单元格，单元格不存在时返回<code>null</code>
	 * 
	 * @param x X坐标，从0计数，既列号
	 * @param y Y坐标，从0计数，既行号
	 * @return {@link Cell}
	 * @since 4.0.5
	 */
	public Cell getCell(int x, int y) {
		return getCell(x, y, false);
	}

	/**
	 * 获取或创建指定坐标单元格
	 * 
	 * @param x X坐标，从0计数，既列号
	 * @param y Y坐标，从0计数，既行号
	 * @return {@link Cell}
	 * @since 4.0.6
	 */
	public Cell getOrCreateCell(int x, int y) {
		return getCell(x, y, true);
	}

	/**
	 * 获取指定坐标单元格，如果isCreateIfNotExist为false，则在单元格不存在时返回<code>null</code>
	 * 
	 * @param x X坐标，从0计数，既列号
	 * @param y Y坐标，从0计数，既行号
	 * @param isCreateIfNotExist 单元格不存在时是否创建
	 * @return {@link Cell}
	 * @since 4.0.6
	 */
	public Cell getCell(int x, int y, boolean isCreateIfNotExist) {
		final Row row = isCreateIfNotExist ? RowUtil.getOrCreateRow(this.sheet, y) : this.sheet.getRow(y);
		if (null != row) {
			return isCreateIfNotExist ? CellUtil.getOrCreateCell(row, x) : row.getCell(x);
		}
		return null;
	}

	/**
	 * 获取或者创建行
	 * 
	 * @param y Y坐标，从0计数，既行号
	 * @return {@link Row}
	 * @since 4.1.4
	 */
	public Row getOrCreateRow(int y) {
		return RowUtil.getOrCreateRow(this.sheet, y);
	}

	/**
	 * 为指定单元格获取或者创建样式，返回样式后可以设置样式内容
	 * 
	 * @param x X坐标，从0计数，既列号
	 * @param y Y坐标，从0计数，既行号
	 * @return {@link CellStyle}
	 * @since 4.1.4
	 */
	public CellStyle getOrCreateCellStyle(int x, int y) {
		final Cell cell = getOrCreateCell(x, y);
		CellStyle cellStyle = cell.getCellStyle();
		if (null == cellStyle) {
			cellStyle = this.workbook.createCellStyle();
			cell.setCellStyle(cellStyle);
		}
		return cellStyle;
	}

	/**
	 * 获取或创建某一行的样式，返回样式后可以设置样式内容
	 * 
	 * @param y Y坐标，从0计数，既行号
	 * @return {@link CellStyle}
	 * @since 4.1.4
	 */
	public CellStyle getOrCreateRowStyle(int y) {
		final Row row = getOrCreateRow(y);
		CellStyle rowStyle = row.getRowStyle();
		if (null == rowStyle) {
			rowStyle = this.workbook.createCellStyle();
			row.setRowStyle(rowStyle);
		}
		return rowStyle;
	}

	/**
	 * 获取或创建某一行的样式，返回样式后可以设置样式内容
	 * 
	 * @param x X坐标，从0计数，既列号
	 * @return {@link CellStyle}
	 * @since 4.1.4
	 */
	public CellStyle getOrCreateColumnStyle(int x) {
		CellStyle columnStyle = this.sheet.getColumnStyle(x);
		if (null == columnStyle) {
			columnStyle = this.workbook.createCellStyle();
			this.sheet.setDefaultColumnStyle(x, columnStyle);
		}
		return columnStyle;
	}

	/**
	 * 关闭工作簿<br>
	 * 如果用户设定了目标文件，先写出目标文件后给关闭工作簿
	 */
	@Override
	public void close() {
		IoUtil.close(this.workbook);
		this.sheet = null;
		this.workbook = null;
		this.isClosed = true;
	}
}
