package com.xiaoleilu.hutool.poi.excel;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.xiaoleilu.hutool.bean.BeanUtil;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.map.MapUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Excel 写入器<br>
 * 此工具用于通过POI将数据写出到Excel
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
		this(ExcelUtil.createBook(isXlsx ? ".xlsx" : ".xls"), null);
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
		this(ExcelUtil.createBook(destFile), sheetName);
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
		this.workbook = workbook;
		this.sheet = workbook.createSheet(StrUtil.isBlank(sheetName) ? "sheet1" : sheetName);
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
	 * 设置某列为自动宽度
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
	 * 合并某行的单元格，并写入对象到单元格<br>
	 * 如果写到单元格中的内容非null，行号自动+1，否则当前行号不变<br>
	 * 样式为默认标题样式，可使用{@link #getHeadCellStyle()}方法调用后自定义默认样式
	 * 
	 * @param lastColumn 合并到的最后一个列号
	 * @param content 合并单元格后的内容
	 * @return this
	 */
	public ExcelWriter merge(int lastColumn, Object content) {
		Assert.isFalse(this.isClosed, "ExcelWriter has been closed!");

		final int rowIndex = this.currentRow.get();
		final Cell cell = this.sheet.createRow(rowIndex).createCell(0);

		// 设置合并后的单元格样式
		if (null != this.styleSet.headCellStyle) {
			cell.setCellStyle(this.styleSet.headCellStyle);
			InternalExcelUtil.mergingCells(this.sheet, rowIndex, rowIndex, 0, lastColumn, this.styleSet.headCellStyle);
		}

		// 设置内容
		if (null != content) {
			InternalExcelUtil.setCellValue(cell, content, this.styleSet);
			// 设置内容后跳到下一行
			this.currentRow.incrementAndGet();
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
				writeRows(BeanUtil.beanToMap(object), 0 == index);
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
		InternalExcelUtil.writeRow(this.sheet.createRow(this.currentRow.getAndIncrement()), rowData, this.styleSet);
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
		InternalExcelUtil.writeRow(this.sheet.createRow(this.currentRow.getAndIncrement()), rowData, this.styleSet);
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
	 * 将Excel Workbook刷出到预定义的文件<br>
	 * 如果用户未自定义输出的文件，将抛出{@link NullPointerException}
	 * 
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public ExcelWriter flush() throws IORuntimeException {
		OutputStream out = null;
		try {
			out = FileUtil.getOutputStream(this.destFile);
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
