package com.xiaoleilu.hutool.poi.excel;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.io.IoUtil;

/**
 * Excel 写入器<br>
 * 此工具用于通过POI将数据写出到Excel
 * 
 * @author Looly
 *@since 3.2.0
 */
public class ExcelWriter implements Closeable{
	
	private Workbook workbook;
	/** Excel中对应的Sheet */
	private Sheet sheet;
	/** 目标文件 */
	private File destFile;
	
	//-------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，默认写出到第一个sheet
	 * 
	 * @param destFilePath 目标文件路径，可以不存在
	 */
	public ExcelWriter(String destFilePath) {
		this(destFilePath, 0);
	}
	
	/**
	 * 构造
	 * 
	 * @param destFilePath 目标文件路径，可以不存在
	 * @param sheetIndex sheet序号
	 */
	public ExcelWriter(String destFilePath, int sheetIndex) {
		this(ExcelUtil.createBook(destFilePath), sheetIndex);
	}
	
	/**
	 * 构造
	 * 
	 * @param destFilePath 目标文件路径，可以不存在
	 * @param sheetName sheet名
	 */
	public ExcelWriter(String destFilePath, String sheetName) {
		this(ExcelUtil.createBook(destFilePath), sheetName);
	}
	
	/**
	 * 构造，默认写出到第一个sheet
	 * 
	 * @param destFile 目标文件，可以不存在
	 */
	public ExcelWriter(File destFile) {
		this(destFile, 0);
	}
	
	/**
	 * 构造
	 * 
	 * @param destFile 目标文件，可以不存在
	 * @param sheetIndex sheet序号
	 */
	public ExcelWriter(File destFile, int sheetIndex) {
		this(ExcelUtil.createBook(destFile), sheetIndex);
		this.destFile = destFile;
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
		this.sheet = workbook.getSheet(sheetName);
	}
	
	/**
	 * 构造
	 * 
	 * @param workbook {@link Workbook}
	 * @param sheetIndex sheet序号，从0开始
	 */
	public ExcelWriter(Workbook workbook, int sheetIndex) {
		this.workbook = workbook;
		this.sheet = workbook.getSheetAt(sheetIndex);
	}
	
	//-------------------------------------------------------------------------- Constructor end
	/**
	 * 将数据写出到Excel，然后写出到流，此方法会关闭文件流和工作簿
	 * 
	 * @param data 数据
	 * @throws IORuntimeException
	 * @return this
	 */
	public ExcelWriter write(Collection<Collection<Object>> data) throws IORuntimeException{
		final OutputStream out = FileUtil.getOutputStream(this.destFile);
		try {
			return write(data, out);
		} finally {
			IoUtil.close(out);
			close();
		}
	}
	
	/**
	 * 将数据写出到Excel，然后写出到流，此方法并不会关闭流和工作簿
	 * 
	 * @param data 数据
	 * @param out 输出流
	 * @throws IORuntimeException
	 * @return this
	 */
	public ExcelWriter write(Collection<Collection<Object>> data, OutputStream out) throws IORuntimeException{
		append(data, 0);
		flush(out);
		return this;
	}
	
	/**
	 * 写出数据
	 * @param data 数据
	 * @param startRow 起始行，0表示第一行
	 * @return this
	 */
	public ExcelWriter append(Collection<Collection<Object>> data, int startRow) {
		int i = startRow;
		for (Collection<Object> rowData : data) {
			writeRow(this.sheet.createRow(i), rowData);
			i++;
		}
		return this;
	}
	
	/**
	 * 将Excel Workbook刷出到输出流
	 * @param out 输出流
	 * @return this
	 * @throws IORuntimeException IO异常
	 */
	public ExcelWriter flush(OutputStream out) throws IORuntimeException{
		try {
			this.workbook.write(out);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}
	
	/**
	 * 关闭工作簿
	 * @return this
	 */
	@Override
	public void close() {
		IoUtil.close(this.workbook);
	}
	
	//-------------------------------------------------------------------------- Private method start
	/**
	 * 写一行数据
	 * @param row 行
	 * @param rowData 一行的数据
	 */
	private void writeRow(Row row, Collection<Object> rowData) {
		int i = 0;
		for (Object value : rowData) {
			InternalExcelUtil.setCellValue(row.createCell(i), value);
			i++;
		}
	}
	//-------------------------------------------------------------------------- Private method end
}
