package cn.hutool.poi.excel;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.cell.CellUtil;
import cn.hutool.poi.excel.style.Align;

/**
 * Excel 写入器<br>
 * 此工具用于通过POI将数据写出到Excel，此对象可完成以下两个功能
 * 
 * <pre>
 * 1. 编辑已存在的Excel，可写出原Excel文件，也可写出到其它地方（到文件或到流）
 * 2. 新建一个空的Excel工作簿，完成数据填充后写出（到文件或到流）
 * </pre>
 * 
 * @author Looly
 * @since 3.2.0
 */
public class ExcelWriter implements Closeable {

	/** 是否被关闭 */
	private boolean isClosed;
	/** 工作簿 */
	private Workbook workbook;
	/** Excel中对应的Sheet */
	private Sheet sheet;
	/** 目标文件 */
	private File destFile;
	/** 当前行 */
	private AtomicInteger currentRow = new AtomicInteger(0);
	/** 标题行别名 */
	private Map<String, String> headerAlias;
	/** 样式集，定义不同类型数据样式 */
	private StyleSet styleSet;

	// -------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，默认生成xls格式的Excel文件<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link #setDestFile(File)}方法自定义写出的文件，然后调用{@link #flush()}方法写出到文件
	 * 
	 * @since 3.2.1
	 */
	public ExcelWriter() {
		this(false);
	}

	/**
	 * 构造<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link #setDestFile(File)}方法自定义写出的文件，然后调用{@link #flush()}方法写出到文件
	 * 
	 * @param isXlsx 是否为xlsx格式
	 * @since 3.2.1
	 */
	public ExcelWriter(boolean isXlsx) {
		this(WorkbookUtil.createBook(isXlsx), null);
	}

	/**
	 * 构造，默认写出到第一个sheet，第一个sheet名为sheet1
	 * 
	 * @param destFilePath 目标文件路径，可以不存在
	 */
	public ExcelWriter(String destFilePath) {
		this(destFilePath, null);
	}

	/**
	 * 构造
	 * 
	 * @param destFilePath 目标文件路径，可以不存在
	 * @param sheetName sheet名，第一个sheet名并写出到此sheet，例如sheet1
	 */
	public ExcelWriter(String destFilePath, String sheetName) {
		this(FileUtil.file(destFilePath), sheetName);
	}

	/**
	 * 构造，默认写出到第一个sheet，第一个sheet名为sheet1
	 * 
	 * @param destFile 目标文件，可以不存在
	 */
	public ExcelWriter(File destFile) {
		this(destFile, null);
	}

	/**
	 * 构造
	 * 
	 * @param destFile 目标文件，可以不存在
	 * @param sheetName sheet名，做为第一个sheet名并写出到此sheet，例如sheet1
	 */
	public ExcelWriter(File destFile, String sheetName) {
		this(destFile.exists() ? WorkbookUtil.createBook(FileUtil.getInputStream(destFile), true) : WorkbookUtil.createBook(StrUtil.endWithIgnoreCase(destFile.getName(), ".xlsx")), sheetName);
		this.destFile = destFile;
	}

	/**
	 * 构造<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link #setDestFile(File)}方法自定义写出的文件，然后调用{@link #flush()}方法写出到文件
	 * 
	 * @param workbook {@link Workbook}
	 * @param sheetName sheet名，做为第一个sheet名并写出到此sheet，例如sheet1
	 */
	public ExcelWriter(Workbook workbook, String sheetName) {
		this(ExcelUtil.getOrCreateSheet(workbook, sheetName));
	}

	/**
	 * 构造<br>
	 * 此构造不传入写出的Excel文件路径，只能调用{@link #flush(OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link #setDestFile(File)}方法自定义写出的文件，然后调用{@link #flush()}方法写出到文件
	 * 
	 * @param sheet {@link Sheet}
	 * @since 4.0.6
	 */
	public ExcelWriter(Sheet sheet) {
		this.workbook = sheet.getWorkbook();
		this.sheet = sheet;
		this.styleSet = new StyleSet(workbook);
	}

	// -------------------------------------------------------------------------- Constructor end

	/**
	 * 获取Workbook
	 * 
	 * @return Workbook
	 */
	public Workbook getWorkbook() {
		return this.workbook;
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
	 * 设置某列为自动宽度，不考虑合并单元格<br>
	 * 此方法必须在指定列数据完全写出后调用才有效。
	 * 
	 * @param columnIndex 第几列，从0计数
	 * @return this
	 * @since 4.0.12
	 */
	public ExcelWriter autoSizeColumn(int columnIndex) {
		this.sheet.autoSizeColumn(columnIndex);
		return this;
	}

	/**
	 * 设置某列为自动宽度<br>
	 * 此方法必须在指定列数据完全写出后调用才有效。
	 * 
	 * @param columnIndex 第几列，从0计数
	 * @param useMergedCells 是否适用于合并单元格
	 * @return this
	 * @since 3.3.0
	 */
	public ExcelWriter autoSizeColumn(int columnIndex, boolean useMergedCells) {
		this.sheet.autoSizeColumn(columnIndex, useMergedCells);
		return this;
	}

	/**
	 * 获取样式集，样式集可以自定义包括：<br>
	 * 
	 * <pre>
	 * 1. 头部样式
	 * 2. 一般单元格样式
	 * 3. 默认数字样式
	 * 4. 默认日期样式
	 * </pre>
	 * 
	 * @return 样式集
	 * @since 4.0.0
	 */
	public StyleSet getStyleSet() {
		return this.styleSet;
	}

	/**
	 * 获取头部样式，获取样式后可自定义样式
	 * 
	 * @return 头部样式
	 */
	public CellStyle getHeadCellStyle() {
		return this.styleSet.headCellStyle;
	}

	/**
	 * 获取单元格样式，获取样式后可自定义样式
	 * 
	 * @return 单元格样式
	 */
	public CellStyle getCellStyle() {
		return this.styleSet.cellStyle;
	}

	/**
	 * 获得当前行
	 * 
	 * @return 当前行
	 */
	public int getCurrentRow() {
		return this.currentRow.get();
	}

	/**
	 * 设置当前所在行
	 * 
	 * @param rowIndex 行号
	 * @return this
	 */
	public ExcelWriter setCurrentRow(int rowIndex) {
		this.currentRow.set(rowIndex);
		return this;
	}

	/**
	 * 跳过当前行
	 * 
	 * @return this
	 */
	public ExcelWriter passCurrentRow() {
		this.currentRow.incrementAndGet();
		return this;
	}

	/**
	 * 跳过指定行数
	 * 
	 * @param rows 跳过的行数
	 * @return this
	 */
	public ExcelWriter passRows(int rows) {
		this.currentRow.addAndGet(rows);
		return this;
	}

	/**
	 * 重置当前行为0
	 * 
	 * @return this
	 */
	public ExcelWriter resetRow() {
		this.currentRow.set(0);
		return this;
	}

	/**
	 * 切换sheet，如果指定的sheet不存在，创建之
	 * 
	 * @param sheetName sheet名
	 * @return this
	 * @since 4.0.8
	 */
	public ExcelWriter setOrCreateSheet(String sheetName) {
		this.sheet = ExcelUtil.getOrCreateSheet(this.workbook, sheetName);
		return this;
	}

	/**
	 * 设置写出的目标文件
	 * 
	 * @param destFile 目标文件
	 * @return this
	 */
	public ExcelWriter setDestFile(File destFile) {
		this.destFile = destFile;
		return this;
	}

	/**
	 * 设置标题别名，key为Map中的key，value为别名
	 * 
	 * @param headerAlias 标题别名
	 * @return this
	 * @since 3.2.1
	 */
	public ExcelWriter setHeaderAlias(Map<String, String> headerAlias) {
		this.headerAlias = headerAlias;
		return this;
	}

	/**
	 * 设置列宽（单位为一个字符的宽度，例如传入width为10，表示10个字符的宽度）
	 * 
	 * @param columnIndex 列号（从0开始计数，-1表示所有列的默认宽度）
	 * @param width 宽度（单位1~256个字符宽度）
	 * @return this
	 * @since 4.0.8
	 */
	public ExcelWriter setColumnWidth(int columnIndex, int width) {
		if (columnIndex < 0) {
			this.sheet.setDefaultColumnWidth(width);
		} else {
			this.sheet.setColumnWidth(columnIndex, width * 256);
		}
		return this;
	}

	/**
	 * 设置行高，值为一个点的高度
	 * 
	 * @param rownum 行号（从0开始计数，-1表示所有行的默认高度）
	 * @param height 高度
	 * @return this
	 * @since 4.0.8
	 */
	public ExcelWriter setRowHeight(int rownum, int height) {
		if (rownum < 0) {
			this.sheet.setDefaultRowHeightInPoints(height);
		} else {
			this.sheet.getRow(rownum).setHeightInPoints(height);
		}
		return this;
	}

	/**
	 * 设置Excel页眉或页脚
	 * 
	 * @param text 页脚的文本
	 * @param align 对齐方式枚举 {@link Align}
	 * @param isFooter 是否为页脚，false表示页眉，true表示页脚
	 * @return this
	 * @since 4.1.0
	 */
	public ExcelWriter setHeaderOrFooter(String text, Align align, boolean isFooter) {
		final HeaderFooter headerFooter = isFooter ? this.sheet.getFooter() : this.sheet.getHeader();
		switch (align) {
		case LEFT:
			headerFooter.setLeft(text);
			break;
		case RIGHT:
			headerFooter.setRight(text);
			break;
		case CENTER:
			headerFooter.setCenter(text);
			break;
		default:
			break;
		}
		return this;
	}

	/**
	 * 合并当前行的单元格<br>
	 * 样式为默认标题样式，可使用{@link #getHeadCellStyle()}方法调用后自定义默认样式
	 * 
	 * @param lastColumn 合并到的最后一个列号
	 * @return this
	 */
	public ExcelWriter merge(int lastColumn) {
		return merge(lastColumn, null);
	}

	/**
	 * 合并当前行的单元格，并写入对象到单元格<br>
	 * 如果写到单元格中的内容非null，行号自动+1，否则当前行号不变<br>
	 * 样式为默认标题样式，可使用{@link #getHeadCellStyle()}方法调用后自定义默认样式
	 * 
	 * @param lastColumn 合并到的最后一个列号
	 * @param content 合并单元格后的内容
	 * @return this
	 */
	public ExcelWriter merge(int lastColumn, Object content) {
		return merge(lastColumn, content, true);
	}

	/**
	 * 合并某行的单元格，并写入对象到单元格<br>
	 * 如果写到单元格中的内容非null，行号自动+1，否则当前行号不变<br>
	 * 样式为默认标题样式，可使用{@link #getHeadCellStyle()}方法调用后自定义默认样式
	 * 
	 * @param lastColumn 合并到的最后一个列号
	 * @param content 合并单元格后的内容
	 * @param isSetHeaderStyle 是否为合并后的单元格设置默认标题样式
	 * @return this
	 * @since 4.0.10
	 */
	public ExcelWriter merge(int lastColumn, Object content, boolean isSetHeaderStyle) {
		Assert.isFalse(this.isClosed, "ExcelWriter has been closed!");

		final int rowIndex = this.currentRow.get();
		merge(rowIndex, rowIndex, 0, lastColumn, content, isSetHeaderStyle);

		// 设置内容后跳到下一行
		if (null != content) {
			this.currentRow.incrementAndGet();
		}
		return this;
	}

	/**
	 * 合并某行的单元格，并写入对象到单元格<br>
	 * 如果写到单元格中的内容非null，行号自动+1，否则当前行号不变<br>
	 * 样式为默认标题样式，可使用{@link #getHeadCellStyle()}方法调用后自定义默认样式
	 * 
	 * @param lastColumn 合并到的最后一个列号
	 * @param content 合并单元格后的内容
	 * @param isSetHeaderStyle 是否为合并后的单元格设置默认标题样式
	 * @return this
	 * @since 4.0.10
	 */
	public ExcelWriter merge(int firstRow, int lastRow, int firstColumn, int lastColumn, Object content, boolean isSetHeaderStyle) {
		Assert.isFalse(this.isClosed, "ExcelWriter has been closed!");

		final CellStyle style = (isSetHeaderStyle && null != this.styleSet.headCellStyle) ? this.styleSet.headCellStyle : this.styleSet.cellStyle;
		CellUtil.mergingCells(this.sheet, firstRow, lastRow, firstColumn, lastColumn, style);

		// 设置内容
		if (null != content) {
			final Cell cell = getOrCreateCell(firstColumn, firstRow);
			CellUtil.setCellValue(cell, content, this.styleSet, isSetHeaderStyle);
		}
		return this;
	}

	/**
	 * 写出数据，本方法只是将数据写入Workbook中的Sheet，并不写出到文件<br>
	 * 写出的起始行为当前行号，可使用{@link #getCurrentRow()}方法调用，根据写出的的行数，当前行号自动增加<br>
	 * 样式为默认样式，可使用{@link #getCellStyle()}方法调用后自定义默认样式<br>
	 * data中元素支持的类型有：
	 * 
	 * <p>
	 * 1. Iterable，既元素为一个集合，元素被当作一行，data表示多行<br>
	 * 2. Map，既元素为一个Map，第一个Map的keys作为首行，剩下的行为Map的values，data表示多行 <br>
	 * 3. Bean，既元素为一个Bean，第一个Bean的字段名列表会作为首行，剩下的行为Bean的字段值列表，data表示多行 <br>
	 * 4. 无法识别，不输出
	 * </p>
	 * 
	 * @param data 数据
	 * @return this
	 */
	public ExcelWriter write(Iterable<?> data) {
		Assert.isFalse(this.isClosed, "ExcelWriter has been closed!");
		int index = 0;
		for (Object object : data) {
			if (object instanceof Iterable) {
				// 普通多行数据
				writeRow((Iterable<?>) object);
			} else if (object instanceof Map) {
				// Map表示一行，第一条数据的key做为标题行
				writeRows((Map<?, ?>) object, 0 == index);
			} else if (BeanUtil.isBean(object.getClass())) {
				// 一个Bean对象表示一行
				writeRows(BeanUtil.beanToMap(object, new LinkedHashMap<String, Object>(), false, false), 0 == index);
			} else {
				break;
			}
			index++;
		}
		if (0 == index) {
			// 在无法识别元素类型的情况下，做为一行对待
			writeRow(data);
		}
		return this;
	}

	/**
	 * 写出数据，本方法只是将数据写入Workbook中的Sheet，并不写出到文件<br>
	 * 写出的起始行为当前行号，可使用{@link #getCurrentRow()}方法调用，根据写出的的行数，当前行号自动增加<br>
	 * 样式为默认样式，可使用{@link #getCellStyle()}方法调用后自定义默认样式<br>
	 * data中元素支持的类型有：
	 * 
	 * <p>
	 * 1. Map，既元素为一个Map，第一个Map的keys作为首行，剩下的行为Map的values，data表示多行 <br>
	 * 2. Bean，既元素为一个Bean，第一个Bean的字段名列表会作为首行，剩下的行为Bean的字段值列表，data表示多行 <br>
	 * </p>
	 * 
	 * @param data 数据
	 * @param comparator 比较器
	 * @return this
	 * @since 3.2.3
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> ExcelWriter write(Iterable<T> data, Comparator<String> comparator) {
		Assert.isFalse(this.isClosed, "ExcelWriter has been closed!");
		boolean isFirstRow = true;
		Map<?, ?> map;
		for (T t : data) {
			if (t instanceof Map) {
				map = new TreeMap<>(comparator);
				map.putAll((Map) t);
			} else {
				map = BeanUtil.beanToMap(t, new TreeMap<String, Object>(comparator), false, false);
			}
			writeRows(map, isFirstRow);
			if (isFirstRow) {
				isFirstRow = false;
			}
		}
		return this;
	}

	/**
	 * 写出一行标题数据<br>
	 * 本方法只是将数据写入Workbook中的Sheet，并不写出到文件<br>
	 * 写出的起始行为当前行号，可使用{@link #getCurrentRow()}方法调用，根据写出的的行数，当前行号自动+1<br>
	 * 样式为默认标题样式，可使用{@link #getHeadCellStyle()}方法调用后自定义默认样式
	 * 
	 * @param rowData 一行的数据
	 * @return this
	 */
	public ExcelWriter writeHeadRow(Iterable<?> rowData) {
		Assert.isFalse(this.isClosed, "ExcelWriter has been closed!");
		RowUtil.writeRow(this.sheet.createRow(this.currentRow.getAndIncrement()), rowData, this.styleSet, true);
		return this;
	}

	/**
	 * 写出一行数据<br>
	 * 本方法只是将数据写入Workbook中的Sheet，并不写出到文件<br>
	 * 写出的起始行为当前行号，可使用{@link #getCurrentRow()}方法调用，根据写出的的行数，当前行号自动+1<br>
	 * 样式为默认样式，可使用{@link #getCellStyle()}方法调用后自定义默认样式
	 * 
	 * @param rowData 一行的数据
	 * @return this
	 */
	public ExcelWriter writeRow(Iterable<?> rowData) {
		Assert.isFalse(this.isClosed, "ExcelWriter has been closed!");
		RowUtil.writeRow(this.sheet.createRow(this.currentRow.getAndIncrement()), rowData, this.styleSet, false);
		return this;
	}

	/**
	 * 将一个Map写入到Excel，isWriteKeys为true写出两行，Map的keys做为一行，values做为第二行，否则只写出一行values
	 * 
	 * @param rowMap 写出的Map
	 * @param isWriteKeys 为true写出两行，Map的keys做为一行，values做为第二行，否则只写出一行values
	 * @return this
	 */
	public ExcelWriter writeRows(Map<?, ?> rowMap, boolean isWriteKeys) {
		Assert.isFalse(this.isClosed, "ExcelWriter has been closed!");
		if (isWriteKeys) {
			writeHeadRow(aliasHeader(rowMap.keySet()));
		}
		writeRow(rowMap.values());
		return this;
	}

	/**
	 * 给指定单元格赋值，使用默认单元格样式
	 * 
	 * @param x X坐标，从0计数，既列号
	 * @param y Y坐标，从0计数，既行号
	 * @param value 值
	 * @return this
	 * @since 4.0.2
	 */
	public ExcelWriter writeCellValue(int x, int y, Object value) {
		final Cell cell = getOrCreateCell(x, y);
		CellUtil.setCellValue(cell, value, styleSet, false);
		return this;
	}

	/**
	 * 获取或者创建指定位置的单元格
	 * 
	 * @param x X坐标，从0计数，既列号
	 * @param y Y坐标，从0计数，既行号
	 * @return {@link Cell}
	 * @since 4.0.9
	 */
	public Cell getOrCreateCell(int x, int y) {
		final Row row = RowUtil.getOrCreateRow(this.sheet, y);
		return CellUtil.getOrCreateCell(row, x);
	}

	/**
	 * 为指定单元格创建样式
	 * 
	 * @param x X坐标，从0计数，既列号
	 * @param y Y坐标，从0计数，既行号
	 * @return {@link CellStyle}
	 * @since 4.0.9
	 */
	public CellStyle createStyleForCell(int x, int y) {
		final CellStyle cellStyle = this.workbook.createCellStyle();
		final Cell cell = getOrCreateCell(x, y);
		cell.setCellStyle(this.workbook.createCellStyle());
		return cellStyle;
	}

	/**
	 * 创建字体
	 * 
	 * @return 字体
	 * @since 4.1.0
	 */
	public Font createFont() {
		return getWorkbook().createFont();
	}

	/**
	 * 将Excel Workbook刷出到预定义的文件<br>
	 * 如果用户未自定义输出的文件，将抛出{@link NullPointerException}<br>
	 * 预定义文件可以通过{@link #setDestFile(File)} 方法预定义，或者通过构造定义
	 * 
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public ExcelWriter flush() throws IORuntimeException {
		return flush(this.destFile);
	}

	/**
	 * 将Excel Workbook刷出到文件<br>
	 * 如果用户未自定义输出的文件，将抛出{@link NullPointerException}
	 * 
	 * @param destFile 写出到的文件
	 * @return this
	 * @throws IORuntimeException IO异常
	 * @since 4.0.6
	 */
	public ExcelWriter flush(File destFile) throws IORuntimeException {
		Assert.notNull(destFile, "[destFile] is null, and you must call setDestFile(File) first or call flush(OutputStream).");
		OutputStream out = null;
		try {
			out = FileUtil.getOutputStream(destFile);
			flush(out);
		} finally {
			IoUtil.close(out);
		}
		return this;
	}

	/**
	 * 将Excel Workbook刷出到输出流
	 * 
	 * @param out 输出流
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public ExcelWriter flush(OutputStream out) throws IORuntimeException {
		Assert.isFalse(this.isClosed, "ExcelWriter has been closed!");
		try {
			this.workbook.write(out);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 关闭工作簿<br>
	 * 如果用户设定了目标文件，先写出目标文件后给关闭工作簿
	 */
	@Override
	public void close() {
		if (null != this.destFile) {
			flush();
		}
		IoUtil.close(this.workbook);

		// 清空对象
		this.currentRow = null;
		this.styleSet = null;
		this.sheet = null;
		this.workbook = null;
		isClosed = true;
	}

	// -------------------------------------------------------------------------- Private method start
	/**
	 * 为指定的key列表添加标题别名，如果没有定义key的别名，使用原key
	 * 
	 * @param keys 键列表
	 * @return 别名列表
	 */
	private Collection<?> aliasHeader(Collection<?> keys) {
		if (MapUtil.isEmpty(this.headerAlias)) {
			return keys;
		}
		final List<Object> alias = new ArrayList<>();
		String aliasName;
		for (Object key : keys) {
			aliasName = this.headerAlias.get(key);
			alias.add(null == aliasName ? key : aliasName);
		}
		return alias;
	}
	// -------------------------------------------------------------------------- Private method end
}
