package com.xiaoleilu.hutool.poi.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.poi.excel.sax.Excel03SaxReader;
import com.xiaoleilu.hutool.poi.excel.sax.Excel07SaxReader;
import com.xiaoleilu.hutool.poi.excel.sax.handler.RowHandler;
import com.xiaoleilu.hutool.poi.exceptions.POIException;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Excel工具类
 * 
 * @author Looly
 *
 */
public class ExcelUtil {

	// ------------------------------------------------------------------------------------ Read by Sax start
	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 * 
	 * @param path Excel文件路径
	 * @param sheetIndex sheet序号
	 * @param rowHandler 行处理器
	 * @since 3.2.0
	 */
	public static void readBySax(String path, int sheetIndex, RowHandler rowHandler) {
		readBySax(FileUtil.getInputStream(path), sheetIndex, rowHandler);
	}

	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 * 
	 * @param file Excel文件
	 * @param sheetIndex sheet序号
	 * @param rowHandler 行处理器
	 * @since 3.2.0
	 */
	public static void readBySax(File file, int sheetIndex, RowHandler rowHandler) {
		readBySax(FileUtil.getInputStream(file), sheetIndex, rowHandler);
	}

	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 * 
	 * @param in Excel流
	 * @param sheetIndex sheet序号
	 * @param rowHandler 行处理器
	 * @since 3.2.0
	 */
	public static void readBySax(InputStream in, int sheetIndex, RowHandler rowHandler) {
		if (isXlsx(in)) {
			read07BySax(in, sheetIndex, rowHandler);
		} else {
			read03BySax(in, sheetIndex, rowHandler);
		}
	}

	/**
	 * Sax方式读取Excel07
	 * 
	 * @param in 输入流
	 * @param sheetIndex Sheet索引，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @return {@link Excel07SaxReader}
	 * @since 3.2.0
	 */
	public static Excel07SaxReader read07BySax(InputStream in, int sheetIndex, RowHandler rowHandler) {
		return new Excel07SaxReader(rowHandler).read(in, sheetIndex);
	}

	/**
	 * Sax方式读取Excel07
	 * 
	 * @param file 文件
	 * @param sheetIndex Sheet索引，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @return {@link Excel07SaxReader}
	 * @since 3.2.0
	 */
	public static Excel07SaxReader read07BySax(File file, int sheetIndex, RowHandler rowHandler) {
		return new Excel07SaxReader(rowHandler).read(file, sheetIndex);
	}

	/**
	 * Sax方式读取Excel07
	 * 
	 * @param path 路径
	 * @param sheetIndex Sheet索引，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @return {@link Excel07SaxReader}
	 * @since 3.2.0
	 */
	public static Excel07SaxReader read07BySax(String path, int sheetIndex, RowHandler rowHandler) {
		return new Excel07SaxReader(rowHandler).read(path, sheetIndex);
	}

	/**
	 * Sax方式读取Excel03
	 * 
	 * @param in 输入流
	 * @param sheetIndex Sheet索引，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @return {@link Excel07SaxReader}
	 * @since 3.2.0
	 */
	public static Excel03SaxReader read03BySax(InputStream in, int sheetIndex, RowHandler rowHandler) {
		return new Excel03SaxReader(rowHandler).read(in, sheetIndex);
	}

	/**
	 * Sax方式读取Excel03
	 * 
	 * @param file 文件
	 * @param sheetIndex Sheet索引，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @return {@link Excel03SaxReader}
	 * @since 3.2.0
	 */
	public static Excel03SaxReader read03BySax(File file, int sheetIndex, RowHandler rowHandler) {
		return new Excel03SaxReader(rowHandler).read(file, sheetIndex);
	}

	/**
	 * Sax方式读取Excel03
	 * 
	 * @param path 路径
	 * @param sheetIndex Sheet索引，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @return {@link Excel03SaxReader}
	 * @since 3.2.0
	 */
	public static Excel03SaxReader read03BySax(String path, int sheetIndex, RowHandler rowHandler) {
		return new Excel03SaxReader(rowHandler).read(path, sheetIndex);
	}
	// ------------------------------------------------------------------------------------ Read by Sax end

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 默认调用第一个sheet
	 * 
	 * @param bookFilePath Excel文件路径，绝对路径或相对于ClassPath路径
	 * @return {@link ExcelReader}
	 * @since 3.1.1
	 */
	public static ExcelReader getReader(String bookFilePath) {
		return getReader(bookFilePath, 0);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 默认调用第一个sheet
	 * 
	 * @param bookFile Excel文件
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(File bookFile) {
		return getReader(bookFile, 0);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 * 
	 * @param bookFilePath Excel文件路径，绝对路径或相对于ClassPath路径
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 * @return {@link ExcelReader}
	 * @since 3.1.1
	 */
	public static ExcelReader getReader(String bookFilePath, int sheetIndex) {
		return new ExcelReader(bookFilePath, sheetIndex);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 * 
	 * @param bookFile Excel文件
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(File bookFile, int sheetIndex) {
		return new ExcelReader(bookFile, sheetIndex);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 * 
	 * @param bookFile Excel文件
	 * @param sheetName sheet名，第一个默认是sheet1
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(File bookFile, String sheetName) {
		return new ExcelReader(bookFile, sheetName);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 默认调用第一个sheet
	 * 
	 * @param bookStream Excel文件的流
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(InputStream bookStream) {
		return getReader(bookStream, 0);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 * 
	 * @param bookStream Excel文件的流
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(InputStream bookStream, int sheetIndex) {
		return new ExcelReader(bookStream, sheetIndex);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 * 
	 * @param bookStream Excel文件的流
	 * @param sheetName sheet名，第一个默认是sheet1
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(InputStream bookStream, String sheetName) {
		return new ExcelReader(bookStream, sheetName);
	}

	/**
	 * 加载工作簿
	 * 
	 * @param excelFilePath Excel文件路径，绝对路径或相对于ClassPath路径
	 * @return {@link Workbook}
	 * @since 3.1.1
	 */
	public static Workbook loadBook(String excelFilePath) {
		return loadBook(FileUtil.file(excelFilePath), null);
	}

	/**
	 * 加载工作簿
	 * 
	 * @param excelFile Excel文件
	 * @return {@link Workbook}
	 */
	public static Workbook loadBook(File excelFile) {
		return loadBook(excelFile, null);
	}

	/**
	 * 加载工作簿
	 * 
	 * @param excelFile Excel文件
	 * @param password Excel工作簿密码，如果无密码传{@code null}
	 * @return {@link Workbook}
	 */
	public static Workbook loadBook(File excelFile, String password) {
		try {
			return WorkbookFactory.create(excelFile, password);
		} catch (Exception e) {
			throw new POIException(e);
		}
	}

	/**
	 * 加载工作簿
	 * 
	 * @param in Excel输入流
	 * @return {@link Workbook}
	 */
	public static Workbook loadBook(InputStream in) {
		try {
			return WorkbookFactory.create(in);
		} catch (Exception e) {
			throw new POIException(e);
		}
	}
	
	/**
	 * 根据文件路径创建新的工作簿，文件路径
	 * 
	 * @param destFilePath 目标文件路径，文件可以不存在，通过扩展名判断Workbook类型
	 * @return {@link Workbook}
	 * @since 3.2.0
	 */
	public static Workbook createBook(String destFilePath) {
		Workbook workbook;
		if(StrUtil.endWithIgnoreCase(destFilePath, "xlsx")) {
			workbook = new XSSFWorkbook();
		}else {
			workbook = new HSSFWorkbook();
		}
		return workbook;
	}
	
	/**
	 * 根据文件路径创建新的工作簿
	 * 
	 * @param destFile 目标文件，文件可以不存在
	 * @return {@link Workbook}
	 * @since 3.2.0
	 */
	public static Workbook createBook(File destFile) {
		return createBook(destFile.getName());
	}
	
	/**
	 * 将Excel Workbook刷出到输出流
	 * 
	 * @param book {@link Workbook}
	 * @param out 输出流
	 * @throws IORuntimeException IO异常
	 * @since 3.2.0
	 */
	public static void writeBook(Workbook book, OutputStream out) throws IORuntimeException{
		try {
			book.write(out);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
	
	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet
	 * 
	 * @param destFilePath 目标文件路径
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(String destFilePath) {
		return new ExcelWriter(destFilePath);
	}
	
	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet，名字为sheet1
	 * 
	 * @param destFile 目标文件
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(File destFile) {
		return new ExcelWriter(destFile);
	}
	
	/**
	 * 获得{@link ExcelWriter}
	 * 
	 * @param destFilePath 目标文件路径
	 * @param sheetName sheet表名
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(String destFilePath, String sheetName) {
		return new ExcelWriter(destFilePath, sheetName);
	}
	
	/**
	 * 获得{@link ExcelWriter}
	 * 
	 * @param destFile 目标文件
	 * @param sheetName sheet表名
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(File destFile, String sheetName) {
		return new ExcelWriter(destFile, sheetName);
	}

	/**
	 * 是否为XLS格式的Excel文件（HSSF）<br>
	 * XLS文件主要用于Excel 97~2003创建
	 * 
	 * @param in excel输入流
	 * @return 是否为XLS格式的Excel文件（HSSF）
	 */
	public static boolean isXls(InputStream in) {
		final PushbackInputStream pin = IoUtil.toPushbackStream(in, 8);
		try {
			return POIFSFileSystem.hasPOIFSHeader(pin);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 是否为XLSX格式的Excel文件（XSSF）<br>
	 * XLSX文件主要用于Excel 2007+创建
	 * 
	 * @param in excel输入流
	 * @return 是否为XLSX格式的Excel文件（XSSF）
	 */
	public static boolean isXlsx(InputStream in) {
		final PushbackInputStream pin = IoUtil.toPushbackStream(in, 8);
		try {
			return DocumentFactoryHelper.hasOOXMLHeader(pin);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
