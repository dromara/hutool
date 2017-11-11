package com.xiaoleilu.hutool.poi.excel;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Excel 写入器<br>
 * 此工具用于通过POI将数据写出到Excel
 * 
 * @author Looly
 * @since 3.2.0
 */
public class ExcelWriter implements Closeable {

	private Workbook workbook;
	/** Excel中对应的Sheet */
	private Sheet sheet;
	/** 目标文件 */
	private File destFile;
	/** 标题样式 */
	private CellStyle headCellStyle;
	/** 默认样式 */
	private CellStyle cellStyle;
	
	/** 当前行 */
	private AtomicInteger currentRow = new AtomicInteger(0);

	// -------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，默认写出到第一个sheet
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
	 * @param sheetName sheet名
	 */
	public ExcelWriter(String destFilePath, String sheetName) {
		this(FileUtil.file(destFilePath), sheetName);
	}

	/**
	 * 构造，默认写出到第一个sheet
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
	 * @param sheetName sheet名
	 */
	public ExcelWriter(File destFile, String sheetName) {
		this(ExcelUtil.createBook(destFile), sheetName);
		this.destFile = destFile;
	}

	/**
	 * 构造
	 * 
	 * @param workbook {@link Workbook}
	 * @param sheetName sheet名，例如sheet1
	 */
	public ExcelWriter(Workbook workbook, String sheetName) {
		this.workbook = workbook;
		this.sheet = workbook.createSheet(StrUtil.isBlank(sheetName) ? "sheet1" : sheetName);
		this.headCellStyle = InternalExcelUtil.createHeadCellStyle(workbook);
		this.cellStyle = InternalExcelUtil.createDefaultCellStyle(workbook);
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
	 * 获取头部样式，获取样式后可自定义样式
	 * 
	 * @return 头部样式
	 */
	public CellStyle getHeadCellStyle() {
		return this.headCellStyle;
	}

	/**
	 * 获取单元格样式，获取样式后可自定义样式
	 * 
	 * @return 单元格样式
	 */
	public CellStyle getCellStyle() {
		return this.cellStyle;
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
	 * 将数据写出到Excel，然后写出到流，此方法会关闭文件流和工作簿<br>
	 * 从第0行开始写出
	 * 
	 * @param data 数据
	 * @throws IORuntimeException
	 * @return this
	 */
	public ExcelWriter write(Collection<? extends Collection<?>> data) throws IORuntimeException {
		return write(data, this.cellStyle);
	}

	/**
	 * 将数据写出到Excel，然后写出到流，此方法会关闭文件流和工作簿<br>
	 * 从第0行开始写出
	 * 
	 * @param data 数据
	 * @param cellStyle 单元格样式，null表示无样式
	 * @throws IORuntimeException
	 * @return this
	 */
	public ExcelWriter write(Collection<? extends Collection<?>> data, CellStyle cellStyle) throws IORuntimeException {
		return write(data, getCurrentRow(), cellStyle);
	}

	/**
	 * 将数据写出到Excel，然后写出到流，此方法会关闭文件流和工作簿<br>
	 * 单元格采用默认样式
	 * 
	 * @param data 数据
	 * @param startRow 起始行，0表示第一行
	 * @throws IORuntimeException
	 * @return this
	 */
	public ExcelWriter write(Collection<? extends Collection<?>> data, int startRow) throws IORuntimeException {
		return write(data, startRow, this.cellStyle);
	}

	/**
	 * 将数据写出到Excel，然后写出到流，此方法会关闭文件流和工作簿
	 * 
	 * @param data 数据
	 * @param startRow 起始行，0表示第一行
	 * @param cellStyle 单元格样式，null表示无样式
	 * @throws IORuntimeException
	 * @return this
	 */
	public ExcelWriter write(Collection<? extends Collection<?>> data, int startRow, CellStyle cellStyle) throws IORuntimeException {
		final OutputStream out = FileUtil.getOutputStream(this.destFile);
		try {
			return write(data, out, startRow, cellStyle);
		} finally {
			IoUtil.close(out);
			close();
		}
	}

	/**
	 * 将数据写出到Excel，然后写出到流，此方法并不会关闭流和工作簿<br>
	 * 从第0行开始写出，使用默认样式
	 * 
	 * @param data 数据
	 * @param out 输出流
	 * @throws IORuntimeException
	 * @return this
	 */
	public ExcelWriter write(Collection<? extends Collection<?>> data, OutputStream out) throws IORuntimeException {
		return write(data, out, this.cellStyle);
	}

	/**
	 * 将数据写出到Excel，然后写出到流，此方法并不会关闭流和工作簿<br>
	 * 从第0行开始写出
	 * 
	 * @param data 数据
	 * @param out 输出流
	 * @param cellStyle 单元格样式，null表示无样式
	 * @throws IORuntimeException
	 * @return this
	 */
	public ExcelWriter write(Collection<? extends Collection<?>> data, OutputStream out, CellStyle cellStyle) throws IORuntimeException {
		return write(data, out, getCurrentRow(), cellStyle);
	}

	/**
	 * 将数据写出到Excel，然后写出到流，此方法并不会关闭流和工作簿
	 * 
	 * @param data 数据
	 * @param out 输出流
	 * @param startRow 起始行，0表示第一行
	 * @throws IORuntimeException
	 * @return this
	 */
	public ExcelWriter write(Collection<? extends Collection<?>> data, OutputStream out, int startRow) throws IORuntimeException {
		return write(data, out, startRow, this.cellStyle);
	}

	/**
	 * 将数据写出到Excel，然后写出到流，此方法并不会关闭流和工作簿
	 * 
	 * @param data 数据
	 * @param out 输出流
	 * @param startRow 起始行，0表示第一行
	 * @param cellStyle 单元格样式，null表示无样式
	 * @throws IORuntimeException
	 * @return this
	 */
	public ExcelWriter write(Collection<? extends Collection<?>> data, OutputStream out, int startRow, CellStyle cellStyle) throws IORuntimeException {
		writeToSheet(data, startRow, cellStyle);
		flush(out);
		return this;
	}
	
	/**
	 * 合并当前行的单元格<br>
	 * 使用默认的标题样式
	 * 
	 * @param lastColumn 合并到的最后一个列号
	 * @return this
	 */
	public ExcelWriter merge(int lastColumn) {
		return merge(getCurrentRow(), lastColumn);
	}

	/**
	 * 合并某行的单元格<br>
	 * 使用默认的标题样式
	 * 
	 * @param rowIndex 行号
	 * @param lastColumn 合并到的最后一个列号
	 * @return this
	 */
	public ExcelWriter merge(int rowIndex, int lastColumn) {
		return merge(rowIndex, lastColumn, this.headCellStyle);
	}

	/**
	 * 合并某行的单元格
	 * 
	 * @param rowIndex 行号
	 * @param lastColumn 合并到的最后一个列号
	 * @param cellStyle 单元格样式
	 * @return this
	 */
	public ExcelWriter merge(int lastColumn, CellStyle cellStyle) {
		return merge(getCurrentRow(), lastColumn, null, cellStyle);
	}
	
	/**
	 * 合并某行的单元格
	 * 
	 * @param rowIndex 行号
	 * @param lastColumn 合并到的最后一个列号
	 * @param cellStyle 单元格样式
	 * @return this
	 */
	public ExcelWriter merge(int rowIndex, int lastColumn, CellStyle cellStyle) {
		return merge(rowIndex, lastColumn, null, cellStyle);
	}
	
	/**
	 * 合并某行的单元格，并写入对象到单元格<br>
	 * 使用默认的标题样式
	 * 
	 * @param lastColumn 合并到的最后一个列号
	 * @param content 合并单元格后的内容
	 * @return this
	 */
	public ExcelWriter merge(int lastColumn, Object content) {
		return merge(getCurrentRow(), lastColumn, content);
	}

	/**
	 * 合并某行的单元格，并写入对象到单元格<br>
	 * 使用默认的标题样式
	 * 
	 * @param rowIndex 行号
	 * @param lastColumn 合并到的最后一个列号
	 * @param content 合并单元格后的内容
	 * @return this
	 */
	public ExcelWriter merge(int rowIndex, int lastColumn, Object content) {
		return merge(rowIndex, lastColumn, content, this.headCellStyle);
	}
	
	/**
	 * 合并某行的单元格，并写入对象到单元格
	 * 
	 * @param lastColumn 合并到的最后一个列号
	 * @param content 合并单元格后的内容
	 * @param cellStyle 单元格样式
	 * @return this
	 */
	public ExcelWriter merge(int lastColumn, Object content, CellStyle cellStyle) {
		return merge(getCurrentRow(), lastColumn, content, cellStyle);
	}

	/**
	 * 合并某行的单元格，并写入对象到单元格
	 * 
	 * @param rowIndex 行号
	 * @param lastColumn 合并到的最后一个列号
	 * @param content 合并单元格后的内容
	 * @param cellStyle 单元格样式
	 * @return this
	 */
	public ExcelWriter merge(int rowIndex, int lastColumn, Object content, CellStyle cellStyle) {
		this.currentRow.set(rowIndex);
		final Cell cell = this.sheet.createRow(rowIndex).createCell(0);
		
		//设置合并后的单元格样式
		if (null != cellStyle) {
			cell.setCellStyle(cellStyle);
			InternalExcelUtil.mergingCells(this.sheet, rowIndex, rowIndex, 0, lastColumn, cellStyle);
		}
		
		//设置内容
		if (null != content) {
			InternalExcelUtil.setCellValue(cell, content);
			//设置内容后跳到下一行
			this.currentRow.incrementAndGet();
		}
		return this;
	}
	
	/**
	 * 写出数据，本方法只是将数据写入Workbook中的Sheet，并不写出到文件<br>
	 * 使用默认样式
	 * 
	 * @param data 数据
	 * @param startRow 起始行，0表示第一行
	 * @return this
	 */
	public ExcelWriter writeToSheet(Collection<? extends Collection<?>> data, int startRow) {
		return writeToSheet(data, startRow, this.cellStyle);
	}

	/**
	 * 写出数据，本方法只是将数据写入Workbook中的Sheet，并不写出到文件
	 * 
	 * @param data 数据
	 * @param startRow 起始行，0表示第一行
	 * @param cellStyle 单元格样式，null表示无样式
	 * @return this
	 */
	public ExcelWriter writeToSheet(Collection<? extends Collection<?>> data, int startRow, CellStyle cellStyle) {
		this.currentRow.set(startRow);
		for (Collection<?> rowData : data) {
			writeRow(this.sheet.createRow(this.currentRow.getAndIncrement()), rowData, cellStyle);
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
		try {
			this.workbook.write(out);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 关闭工作簿
	 * 
	 * @return this
	 */
	@Override
	public void close() {
		IoUtil.close(this.workbook);
	}

	// -------------------------------------------------------------------------- Private method start
	/**
	 * 写一行数据
	 * 
	 * @param row 行
	 * @param rowData 一行的数据
	 * @param cellStyle 单元格样式
	 */
	private void writeRow(Row row, Collection<?> rowData, CellStyle cellStyle) {
		int i = 0;
		Cell cell;
		for (Object value : rowData) {
			cell = row.createCell(i);
			if (null != cellStyle) {
				cell.setCellStyle(cellStyle);
			}
			InternalExcelUtil.setCellValue(cell, value);
			i++;
		}
	}
	// -------------------------------------------------------------------------- Private method end
}
