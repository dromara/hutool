package cn.hutool.poi.excel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.apache.poi.poifs.filesystem.FileMagic;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

/**
 * Excel文件工具类
 * 
 * @author looly
 * @since 4.2.1
 */
public class ExcelFileUtil {
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
		}
	}
}
