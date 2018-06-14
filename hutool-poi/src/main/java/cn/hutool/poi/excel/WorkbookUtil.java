
package cn.hutool.poi.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.exceptions.POIException;

/**
 * Excel工作簿{@link Workbook}相关工具类
 * 
 * @author looly
 * @since 4.0.7
 *
 */
public class WorkbookUtil {
	
	/**
	 * 创建或加载工作簿
	 * 
	 * @param excelFilePath Excel文件路径，绝对路径或相对于ClassPath路径
	 * @return {@link Workbook}
	 * @since 3.1.1
	 */
	public static Workbook createBook(String excelFilePath) {
		return createBook(FileUtil.file(excelFilePath), null);
	}

	/**
	 * 创建或加载工作簿
	 * 
	 * @param excelFile Excel文件
	 * @return {@link Workbook}
	 */
	public static Workbook createBook(File excelFile) {
		return createBook(excelFile, null);
	}

	/**
	 * 创建或加载工作簿，只读模式
	 * 
	 * @param excelFile Excel文件
	 * @param password Excel工作簿密码，如果无密码传{@code null}
	 * @return {@link Workbook}
	 */
	public static Workbook createBook(File excelFile, String password) {
		try {
			return WorkbookFactory.create(excelFile, password);
		} catch (Exception e) {
			throw new POIException(e);
		}
	}

	/**
	 * 创建或加载工作簿
	 * 
	 * @param in Excel输入流
	 * @param closeAfterRead 读取结束是否关闭流
	 * @return {@link Workbook}
	 */
	public static Workbook createBook(InputStream in, boolean closeAfterRead) {
		return createBook(in, null, closeAfterRead);
	}

	/**
	 * 创建或加载工作簿
	 * 
	 * @param in Excel输入流
	 * @param password 密码
	 * @param closeAfterRead 读取结束是否关闭流
	 * @return {@link Workbook}
	 * @since 4.0.3
	 */
	public static Workbook createBook(InputStream in, String password, boolean closeAfterRead) {
		try {
			return WorkbookFactory.create(IoUtil.toMarkSupportStream(in), password);
		} catch (Exception e) {
			throw new POIException(e);
		} finally {
			if (closeAfterRead) {
				IoUtil.close(in);
			}
		}
	}
	
	/**
	 * 根据文件路径创建新的工作簿，文件路径
	 * 
	 * @param isXlsx 是否为xlsx格式的Excel
	 * @return {@link Workbook}
	 * @since 4.1.0
	 */
	public static Workbook createBook(boolean isXlsx) {
		Workbook workbook;
		if (isXlsx) {
			workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
		} else {
			workbook = new org.apache.poi.hssf.usermodel.HSSFWorkbook();
		}
		return workbook;
	}

	/**
	 * 将Excel Workbook刷出到输出流，不关闭流
	 * 
	 * @param book {@link Workbook}
	 * @param out 输出流
	 * @throws IORuntimeException IO异常
	 * @since 3.2.0
	 */
	public static void writeBook(Workbook book, OutputStream out) throws IORuntimeException {
		try {
			book.write(out);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
