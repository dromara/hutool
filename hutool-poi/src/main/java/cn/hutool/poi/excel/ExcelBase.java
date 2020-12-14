package cn.hutool.poi.excel;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.poi.excel.cell.CellLocation;
import cn.hutool.poi.excel.cell.CellUtil;
import cn.hutool.poi.excel.style.StyleUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel基础类，用于抽象ExcelWriter和ExcelReader中共用部分的对象和方法
 *
 * @param <T> 子类类型，用于返回this
 * @author looly
 * @since 4.1.4
 */
public class ExcelBase<T extends ExcelBase<T>> implements Closeable {
	/**
	 * 是否被关闭
	 */
	protected boolean isClosed;
	/**
	 * 工作簿
	 */
	protected Workbook workbook;
	/**
	 * Excel中对应的Sheet
	 */
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
	public T setSheet(String sheetName) {
		return setSheet(WorkbookUtil.getOrCreateSheet(this.workbook, sheetName));
	}

	/**
	 * 自定义需要读取或写出的Sheet，如果给定的sheet不存在，创建之（命名为默认）<br>
	 * 在读取中，此方法用于切换读取的sheet，在写出时，此方法用于新建或者切换sheet
	 *
	 * @param sheetIndex sheet序号，从0开始计数
	 * @return this
	 * @since 4.0.10
	 */
	public T setSheet(int sheetIndex) {
		return setSheet(WorkbookUtil.getOrCreateSheet(this.workbook, sheetIndex));
	}

	/**
	 * 设置自定义Sheet
	 *
	 * @param sheet 自定义sheet，可以通过{@link WorkbookUtil#getOrCreateSheet(Workbook, String)} 创建
	 * @return this
	 * @since 5.2.1
	 */
	@SuppressWarnings("unchecked")
	public T setSheet(Sheet sheet) {
		this.sheet = sheet;
		return (T) this;
	}

	/**
	 * 获取指定坐标单元格，单元格不存在时返回<code>null</code>
	 *
	 * @param locationRef 单元格地址标识符，例如A11，B5
	 * @return {@link Cell}
	 * @since 5.1.4
	 */
	public Cell getCell(String locationRef) {
		final CellLocation cellLocation = ExcelUtil.toLocation(locationRef);
		return getCell(cellLocation.getX(), cellLocation.getY());
	}

	/**
	 * 获取指定坐标单元格，单元格不存在时返回<code>null</code>
	 *
	 * @param x X坐标，从0计数，即列号
	 * @param y Y坐标，从0计数，即行号
	 * @return {@link Cell}
	 * @since 4.0.5
	 */
	public Cell getCell(int x, int y) {
		return getCell(x, y, false);
	}

	/**
	 * 获取或创建指定坐标单元格
	 *
	 * @param locationRef 单元格地址标识符，例如A11，B5
	 * @return {@link Cell}
	 * @since 5.1.4
	 */
	public Cell getOrCreateCell(String locationRef) {
		final CellLocation cellLocation = ExcelUtil.toLocation(locationRef);
		return getOrCreateCell(cellLocation.getX(), cellLocation.getY());
	}

	/**
	 * 获取或创建指定坐标单元格
	 *
	 * @param x X坐标，从0计数，即列号
	 * @param y Y坐标，从0计数，即行号
	 * @return {@link Cell}
	 * @since 4.0.6
	 */
	public Cell getOrCreateCell(int x, int y) {
		return getCell(x, y, true);
	}

	/**
	 * 获取指定坐标单元格，如果isCreateIfNotExist为false，则在单元格不存在时返回<code>null</code>
	 *
	 * @param locationRef        单元格地址标识符，例如A11，B5
	 * @param isCreateIfNotExist 单元格不存在时是否创建
	 * @return {@link Cell}
	 * @since 5.1.4
	 */
	public Cell getCell(String locationRef, boolean isCreateIfNotExist) {
		final CellLocation cellLocation = ExcelUtil.toLocation(locationRef);
		return getCell(cellLocation.getX(), cellLocation.getY(), isCreateIfNotExist);
	}

	/**
	 * 获取指定坐标单元格，如果isCreateIfNotExist为false，则在单元格不存在时返回<code>null</code>
	 *
	 * @param x                  X坐标，从0计数，即列号
	 * @param y                  Y坐标，从0计数，即行号
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
	 * @param y Y坐标，从0计数，即行号
	 * @return {@link Row}
	 * @since 4.1.4
	 */
	public Row getOrCreateRow(int y) {
		return RowUtil.getOrCreateRow(this.sheet, y);
	}

	/**
	 * 为指定单元格获取或者创建样式，返回样式后可以设置样式内容
	 *
	 * @param locationRef 单元格地址标识符，例如A11，B5
	 * @return {@link CellStyle}
	 * @since 5.1.4
	 */
	public CellStyle getOrCreateCellStyle(String locationRef) {
		final CellLocation cellLocation = ExcelUtil.toLocation(locationRef);
		return getOrCreateCellStyle(cellLocation.getX(), cellLocation.getY());
	}

	/**
	 * 为指定单元格获取或者创建样式，返回样式后可以设置样式内容
	 *
	 * @param x X坐标，从0计数，即列号
	 * @param y Y坐标，从0计数，即行号
	 * @return {@link CellStyle}
	 * @since 4.1.4
	 */
	public CellStyle getOrCreateCellStyle(int x, int y) {
		final CellStyle cellStyle = getOrCreateCell(x, y).getCellStyle();
		return StyleUtil.isNullOrDefaultStyle(this.workbook, cellStyle) ? createCellStyle(x, y) : cellStyle;
	}

	/**
	 * 为指定单元格创建样式，返回样式后可以设置样式内容
	 *
	 * @param locationRef 单元格地址标识符，例如A11，B5
	 * @return {@link CellStyle}
	 * @since 5.1.4
	 */
	public CellStyle createCellStyle(String locationRef) {
		final CellLocation cellLocation = ExcelUtil.toLocation(locationRef);
		return createCellStyle(cellLocation.getX(), cellLocation.getY());
	}

	/**
	 * 为指定单元格创建样式，返回样式后可以设置样式内容
	 *
	 * @param x X坐标，从0计数，即列号
	 * @param y Y坐标，从0计数，即行号
	 * @return {@link CellStyle}
	 * @since 4.6.3
	 */
	public CellStyle createCellStyle(int x, int y) {
		final Cell cell = getOrCreateCell(x, y);
			final CellStyle cellStyle = this.workbook.createCellStyle();
		cell.setCellStyle(cellStyle);
		return cellStyle;
	}

	/**
	 * 创建单元格样式
	 *
	 * @return {@link CellStyle}
	 * @see Workbook#createCellStyle()
	 * @since 5.4.0
	 */
	public CellStyle createCellStyle(){
		return StyleUtil.createCellStyle(this.workbook);
	}

	/**
	 * 获取或创建某一行的样式，返回样式后可以设置样式内容<br>
	 * 需要注意，此方法返回行样式，设置背景色在单元格设置值后会被覆盖，需要单独设置其单元格的样式。
	 *
	 * @param y Y坐标，从0计数，即行号
	 * @return {@link CellStyle}
	 * @since 4.1.4
	 */
	public CellStyle getOrCreateRowStyle(int y) {
		CellStyle rowStyle = getOrCreateRow(y).getRowStyle();
		return StyleUtil.isNullOrDefaultStyle(this.workbook, rowStyle) ? createRowStyle(y) : rowStyle;
	}

	/**
	 * 创建某一行的样式，返回样式后可以设置样式内容
	 *
	 * @param y Y坐标，从0计数，即行号
	 * @return {@link CellStyle}
	 * @since 4.6.3
	 */
	public CellStyle createRowStyle(int y) {
		final CellStyle rowStyle = this.workbook.createCellStyle();
		getOrCreateRow(y).setRowStyle(rowStyle);
		return rowStyle;
	}

	/**
	 * 获取或创建某一行的样式，返回样式后可以设置样式内容<br>
	 * 需要注意，此方法返回行样式，设置背景色在单元格设置值后会被覆盖，需要单独设置其单元格的样式。
	 *
	 * @param x X坐标，从0计数，即列号
	 * @return {@link CellStyle}
	 * @since 4.1.4
	 */
	public CellStyle getOrCreateColumnStyle(int x) {
		final CellStyle columnStyle = this.sheet.getColumnStyle(x);
		return StyleUtil.isNullOrDefaultStyle(this.workbook, columnStyle) ? createColumnStyle(x) : columnStyle;
	}

	/**
	 * 创建某一行的样式，返回样式后可以设置样式内容
	 *
	 * @param x X坐标，从0计数，即列号
	 * @return {@link CellStyle}
	 * @since 4.6.3
	 */
	public CellStyle createColumnStyle(int x) {
		final CellStyle columnStyle = this.workbook.createCellStyle();
		this.sheet.setDefaultColumnStyle(x, columnStyle);
		return columnStyle;
	}

	/**
	 * 获取总行数，计算方法为：
	 *
	 * <pre>
	 * 最后一行序号 + 1
	 * </pre>
	 *
	 * @return 行数
	 * @since 4.5.4
	 */
	public int getRowCount() {
		return this.sheet.getLastRowNum() + 1;
	}

	/**
	 * 获取有记录的行数，计算方法为：
	 *
	 * <pre>
	 * 最后一行序号 - 第一行序号 + 1
	 * </pre>
	 *
	 * @return 行数
	 * @since 4.5.4
	 */
	public int getPhysicalRowCount() {
		return this.sheet.getPhysicalNumberOfRows();
	}

	/**
	 * 获取第一行总列数，计算方法为：
	 *
	 * <pre>
	 * 最后一列序号 + 1
	 * </pre>
	 *
	 * @return 列数
	 */
	public int getColumnCount() {
		return getColumnCount(0);
	}

	/**
	 * 获取总列数，计算方法为：
	 *
	 * <pre>
	 * 最后一列序号 + 1
	 * </pre>
	 *
	 * @param rowNum 行号
	 * @return 列数，-1表示获取失败
	 */
	public int getColumnCount(int rowNum) {
		final Row row = this.sheet.getRow(rowNum);
		if (null != row) {
			// getLastCellNum方法返回序号+1的值
			return row.getLastCellNum();
		}
		return -1;
	}

	/**
	 * 判断是否为xlsx格式的Excel表（Excel07格式）
	 *
	 * @return 是否为xlsx格式的Excel表（Excel07格式）
	 * @since 4.6.2
	 */
	public boolean isXlsx() {
		return this.sheet instanceof XSSFSheet || this.sheet instanceof SXSSFSheet;
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
