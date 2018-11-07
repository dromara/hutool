package cn.hutool.poi.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.PoiChecker;
import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;

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
		in = IoUtil.toMarkSupportStream(in);
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
		try {
			return new Excel07SaxReader(rowHandler).read(in, sheetIndex);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
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
		try {
			return new Excel07SaxReader(rowHandler).read(file, sheetIndex);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
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
		try {
			return new Excel07SaxReader(rowHandler).read(path, sheetIndex);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
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
		try {
			return new Excel03SaxReader(rowHandler).read(in, sheetIndex);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
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
		try {
			return new Excel03SaxReader(rowHandler).read(file, sheetIndex);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
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
		try {
			return new Excel03SaxReader(rowHandler).read(path, sheetIndex);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}
	// ------------------------------------------------------------------------------------ Read by Sax end

	// ------------------------------------------------------------------------------------------------ getReader
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
		try {
			return new ExcelReader(bookFilePath, sheetIndex);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 * 
	 * @param bookFile Excel文件
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(File bookFile, int sheetIndex) {
		try {
			return new ExcelReader(bookFile, sheetIndex);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 * 
	 * @param bookFile Excel文件
	 * @param sheetName sheet名，第一个默认是sheet1
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(File bookFile, String sheetName) {
		try {
			return new ExcelReader(bookFile, sheetName);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 默认调用第一个sheet，读取结束自动关闭流
	 * 
	 * @param bookStream Excel文件的流
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(InputStream bookStream) {
		return getReader(bookStream, 0, true);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 默认调用第一个sheet
	 * 
	 * @param bookStream Excel文件的流
	 * @param closeAfterRead 读取结束是否关闭流
	 * @return {@link ExcelReader}
	 * @since 4.0.3
	 */
	public static ExcelReader getReader(InputStream bookStream, boolean closeAfterRead) {
		try {
			return getReader(bookStream, 0, closeAfterRead);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 读取结束自动关闭流
	 * 
	 * @param bookStream Excel文件的流
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(InputStream bookStream, int sheetIndex) {
		try {
			return new ExcelReader(bookStream, sheetIndex, true);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 * 
	 * @param bookStream Excel文件的流
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 * @param closeAfterRead 读取结束是否关闭流
	 * @return {@link ExcelReader}
	 * @since 4.0.3
	 */
	public static ExcelReader getReader(InputStream bookStream, int sheetIndex, boolean closeAfterRead) {
		try {
			return new ExcelReader(bookStream, sheetIndex, closeAfterRead);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 读取结束自动关闭流
	 * 
	 * @param bookStream Excel文件的流
	 * @param sheetName sheet名，第一个默认是sheet1
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(InputStream bookStream, String sheetName) {
		try {
			return new ExcelReader(bookStream, sheetName, true);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 * 
	 * @param bookStream Excel文件的流
	 * @param sheetName sheet名，第一个默认是sheet1
	 * @param closeAfterRead 读取结束是否关闭流
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(InputStream bookStream, String sheetName, boolean closeAfterRead) {
		try {
			return new ExcelReader(bookStream, sheetName, closeAfterRead);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	// ------------------------------------------------------------------------------------------------ getWriter
	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet<br>
	 * 不传入写出的Excel文件路径，只能调用{@link ExcelWriter#flush(OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link ExcelWriter#setDestFile(File)}方法自定义写出的文件，然后调用{@link ExcelWriter#flush()}方法写出到文件
	 * 
	 * @return {@link ExcelWriter}
	 * @since 3.2.1
	 */
	public static ExcelWriter getWriter() {
		try {
			return new ExcelWriter();
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet<br>
	 * 不传入写出的Excel文件路径，只能调用{@link ExcelWriter#flush(OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link ExcelWriter#setDestFile(File)}方法自定义写出的文件，然后调用{@link ExcelWriter#flush()}方法写出到文件
	 * 
	 * @param isXlsx 是否为xlsx格式
	 * @return {@link ExcelWriter}
	 * @since 3.2.1
	 */
	public static ExcelWriter getWriter(boolean isXlsx) {
		try {
			return new ExcelWriter(isXlsx);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet
	 * 
	 * @param destFilePath 目标文件路径
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(String destFilePath) {
		try {
			return new ExcelWriter(destFilePath);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet，名字为sheet1
	 * 
	 * @param destFile 目标文件
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(File destFile) {
		try {
			return new ExcelWriter(destFile);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获得{@link ExcelWriter}
	 * 
	 * @param destFilePath 目标文件路径
	 * @param sheetName sheet表名
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(String destFilePath, String sheetName) {
		try {
			return new ExcelWriter(destFilePath, sheetName);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获得{@link ExcelWriter}
	 * 
	 * @param destFile 目标文件
	 * @param sheetName sheet表名
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(File destFile, String sheetName) {
		try {
			return new ExcelWriter(destFile, sheetName);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	// ------------------------------------------------------------------------------------------------ getBigWriter
	/**
	 * 获得{@link BigExcelWriter}，默认写出到第一个sheet<br>
	 * 不传入写出的Excel文件路径，只能调用{@link BigExcelWriter#flush(OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link BigExcelWriter#setDestFile(File)}方法自定义写出的文件，然后调用{@link BigExcelWriter#flush()}方法写出到文件
	 * 
	 * @return {@link BigExcelWriter}
	 * @since 4.1.13
	 */
	public static ExcelWriter getBigWriter() {
		try {
			return new BigExcelWriter();
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获得{@link BigExcelWriter}，默认写出到第一个sheet<br>
	 * 不传入写出的Excel文件路径，只能调用{@link BigExcelWriter#flush(OutputStream)}方法写出到流<br>
	 * 若写出到文件，还需调用{@link BigExcelWriter#setDestFile(File)}方法自定义写出的文件，然后调用{@link BigExcelWriter#flush()}方法写出到文件
	 * 
	 * @param rowAccessWindowSize 在内存中的行数
	 * @return {@link BigExcelWriter}
	 * @since 4.1.13
	 */
	public static ExcelWriter getBigWriter(int rowAccessWindowSize) {
		try {
			return new BigExcelWriter(rowAccessWindowSize);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获得{@link BigExcelWriter}，默认写出到第一个sheet
	 * 
	 * @param destFilePath 目标文件路径
	 * @return {@link BigExcelWriter}
	 */
	public static BigExcelWriter getBigWriter(String destFilePath) {
		try {
			return new BigExcelWriter(destFilePath);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获得{@link BigExcelWriter}，默认写出到第一个sheet，名字为sheet1
	 * 
	 * @param destFile 目标文件
	 * @return {@link BigExcelWriter}
	 */
	public static BigExcelWriter getBigWriter(File destFile) {
		try {
			return new BigExcelWriter(destFile);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获得{@link BigExcelWriter}
	 * 
	 * @param destFilePath 目标文件路径
	 * @param sheetName sheet表名
	 * @return {@link BigExcelWriter}
	 */
	public static BigExcelWriter getBigWriter(String destFilePath, String sheetName) {
		try {
			return new BigExcelWriter(destFilePath, sheetName);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获得{@link BigExcelWriter}
	 * 
	 * @param destFile 目标文件
	 * @param sheetName sheet表名
	 * @return {@link BigExcelWriter}
	 */
	public static BigExcelWriter getBigWriter(File destFile, String sheetName) {
		try {
			return new BigExcelWriter(destFile, sheetName);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	// ------------------------------------------------------------------------------------------------ isXls
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
			return FileMagic.valueOf(pin) == FileMagic.OLE2;
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
		if (false == in.markSupported()) {
			in = new BufferedInputStream(in);
		}
		try {
			return FileMagic.valueOf(in) == FileMagic.OOXML;
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (NoClassDefFoundError e) {
			throw PoiChecker.transError(e);
		}
	}

	/**
	 * 获取或者创建sheet表<br>
	 * 如果sheet表在Workbook中已经存在，则获取之，否则创建之
	 * 
	 * @param book 工作簿{@link Workbook}
	 * @param sheetName 工作表名
	 * @return 工作表{@link Sheet}
	 * @since 4.0.2
	 */
	public static Sheet getOrCreateSheet(Workbook book, String sheetName) {
		if (null == book) {
			return null;
		}
		sheetName = StrUtil.isBlank(sheetName) ? "sheet1" : sheetName;
		Sheet sheet = book.getSheet(sheetName);
		if (null == sheet) {
			sheet = book.createSheet(sheetName);
		}
		return sheet;
	}

	/**
	 * 
	 * sheet是否为空
	 * 
	 * @param sheet {@link Sheet}
	 * @return sheet是否为空
	 * @since 4.0.1
	 */
	public static boolean isEmpty(Sheet sheet) {
		return null == sheet || (sheet.getLastRowNum() == 0 && sheet.getPhysicalNumberOfRows() == 0);
	}

	/**
	 * 将Sheet列号变为列名
	 * 
	 * @param index 列号, 从0开始
	 * @return 0->A; 1->B...26->AA
	 * @since 4.1.20
	 */
	public static String indexToColName(int index) {
		if (index < 0) {
			return null;
		}
		final StringBuilder colName = StrUtil.builder();
		do {
			if (colName.length() > 0) {
				index--;
			}
			int remainder = index % 26;
			colName.append((char) (remainder + 'A'));
			index = (int) ((index - remainder) / 26);
		} while (index > 0);
		return colName.reverse().toString();
	}

	/**
	 * 根据表元的列名转换为列号
	 * 
	 * @param colName 列名, 从A开始
	 * @return A1->0; B1->1...AA1->26
	 * @since 4.1.20
	 */
	public static int colNameToIndex(String colName) {
		int length = colName.length();
		char c;
		int index = -1;
		for (int i = 0; i < length; i++) {
			c = Character.toUpperCase(colName.charAt(i));
			if (Character.isDigit(c)) {
				break;// 确定指定的char值是否为数字
			}
			index = (index + 1) * 26 + (int) c - 'A';
		}
		return index;
	}
}
