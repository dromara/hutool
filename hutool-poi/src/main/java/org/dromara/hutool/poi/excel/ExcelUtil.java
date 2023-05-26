/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel;

import org.dromara.hutool.core.exception.DependencyException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.poi.PoiChecker;
import org.dromara.hutool.poi.excel.cell.CellLocation;
import org.dromara.hutool.poi.excel.cell.CellLocationUtil;
import org.dromara.hutool.poi.excel.sax.ExcelSaxReader;
import org.dromara.hutool.poi.excel.sax.ExcelSaxUtil;
import org.dromara.hutool.poi.excel.sax.handler.RowHandler;

import java.io.File;
import java.io.InputStream;

/**
 * Excel工具类,不建议直接使用index直接操作sheet，在wps/excel中sheet显示顺序与index无关，还有隐藏sheet
 *
 * @author Looly
 */
public class ExcelUtil {

	/**
	 * xls的ContentType
	 */
	public static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";

	/**
	 * xlsx的ContentType
	 */
	public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	// ------------------------------------------------------------------------------------ Read by Sax start

	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 *
	 * @param path       Excel文件路径
	 * @param rid        sheet rid，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @since 3.2.0
	 */
	public static void readBySax(final String path, final int rid, final RowHandler rowHandler) {
		readBySax(FileUtil.file(path), rid, rowHandler);
	}

	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 *
	 * @param path       Excel文件路径
	 * @param idOrRid    Excel中的sheet id或者rid编号，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @param rowHandler 行处理器
	 * @since 5.4.4
	 */
	public static void readBySax(final String path, final String idOrRid, final RowHandler rowHandler) {
		readBySax(FileUtil.file(path), idOrRid, rowHandler);
	}

	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 *
	 * @param file       Excel文件
	 * @param rid        sheet rid，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @since 3.2.0
	 */
	public static void readBySax(final File file, final int rid, final RowHandler rowHandler) {
		final ExcelSaxReader<?> reader = ExcelSaxUtil.createSaxReader(ExcelFileUtil.isXlsx(file), rowHandler);
		reader.read(file, rid);
	}

	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 *
	 * @param file               Excel文件
	 * @param idOrRidOrSheetName Excel中的sheet id或rid编号或sheet名称，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @param rowHandler         行处理器
	 * @since 5.4.4
	 */
	public static void readBySax(final File file, final String idOrRidOrSheetName, final RowHandler rowHandler) {
		final ExcelSaxReader<?> reader = ExcelSaxUtil.createSaxReader(ExcelFileUtil.isXlsx(file), rowHandler);
		reader.read(file, idOrRidOrSheetName);
	}

	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 *
	 * @param in         Excel流
	 * @param rid        sheet rid，-1表示全部Sheet, 0表示第一个Sheet
	 * @param rowHandler 行处理器
	 * @since 3.2.0
	 */
	public static void readBySax(InputStream in, final int rid, final RowHandler rowHandler) {
		in = IoUtil.toMarkSupport(in);
		final ExcelSaxReader<?> reader = ExcelSaxUtil.createSaxReader(ExcelFileUtil.isXlsx(in), rowHandler);
		reader.read(in, rid);
	}

	/**
	 * 通过Sax方式读取Excel，同时支持03和07格式
	 *
	 * @param in                 Excel流
	 * @param idOrRidOrSheetName Excel中的sheet id或rid编号或sheet名称，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @param rowHandler         行处理器
	 * @since 5.4.4
	 */
	public static void readBySax(InputStream in, final String idOrRidOrSheetName, final RowHandler rowHandler) {
		in = IoUtil.toMarkSupport(in);
		final ExcelSaxReader<?> reader = ExcelSaxUtil.createSaxReader(ExcelFileUtil.isXlsx(in), rowHandler);
		reader.read(in, idOrRidOrSheetName);
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
	public static ExcelReader getReader(final String bookFilePath) {
		return getReader(bookFilePath, 0);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 默认调用第一个sheet
	 *
	 * @param bookFile Excel文件
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(final File bookFile) {
		return getReader(bookFile, 0);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 *
	 * @param bookFilePath Excel文件路径，绝对路径或相对于ClassPath路径
	 * @param sheetIndex   sheet序号，0表示第一个sheet
	 * @return {@link ExcelReader}
	 * @since 3.1.1
	 */
	public static ExcelReader getReader(final String bookFilePath, final int sheetIndex) {
		try {
			return new ExcelReader(bookFilePath, sheetIndex);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 *
	 * @param bookFilePath Excel文件路径，绝对路径或相对于ClassPath路径
	 * @param sheetName    sheet名，第一个默认是sheet1
	 * @return {@link ExcelReader}
	 * @since 5.8.0
	 */
	public static ExcelReader getReader(final String bookFilePath, final String sheetName) {
		try {
			return new ExcelReader(bookFilePath, sheetName);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 *
	 * @param bookFile   Excel文件
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(final File bookFile, final int sheetIndex) {
		try {
			return new ExcelReader(bookFile, sheetIndex);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容
	 *
	 * @param bookFile  Excel文件
	 * @param sheetName sheet名，第一个默认是sheet1
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(final File bookFile, final String sheetName) {
		try {
			return new ExcelReader(bookFile, sheetName);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 默认调用第一个sheet，读取结束自动关闭流
	 *
	 * @param bookStream Excel文件的流
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(final InputStream bookStream) {
		return getReader(bookStream, 0);
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 读取结束自动关闭流
	 *
	 * @param bookStream Excel文件的流
	 * @param sheetIndex sheet序号，0表示第一个sheet
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(final InputStream bookStream, final int sheetIndex) {
		try {
			return new ExcelReader(bookStream, sheetIndex);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获取Excel读取器，通过调用{@link ExcelReader}的read或readXXX方法读取Excel内容<br>
	 * 读取结束自动关闭流
	 *
	 * @param bookStream Excel文件的流
	 * @param sheetName  sheet名，第一个默认是sheet1
	 * @return {@link ExcelReader}
	 */
	public static ExcelReader getReader(final InputStream bookStream, final String sheetName) {
		try {
			return new ExcelReader(bookStream, sheetName);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	// ------------------------------------------------------------------------------------------------ getWriter

	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet<br>
	 * 不传入写出的Excel文件路径，只能调用ExcelWriter#flush(OutputStream)方法写出到流<br>
	 * 若写出到文件，还需调用{@link ExcelWriter#setDestFile(File)}方法自定义写出的文件，然后调用{@link ExcelWriter#flush()}方法写出到文件
	 *
	 * @return {@link ExcelWriter}
	 * @since 3.2.1
	 */
	public static ExcelWriter getWriter() {
		try {
			return new ExcelWriter();
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet<br>
	 * 不传入写出的Excel文件路径，只能调用ExcelWriter#flush(OutputStream)方法写出到流<br>
	 * 若写出到文件，还需调用{@link ExcelWriter#setDestFile(File)}方法自定义写出的文件，然后调用{@link ExcelWriter#flush()}方法写出到文件
	 *
	 * @param isXlsx 是否为xlsx格式
	 * @return {@link ExcelWriter}
	 * @since 3.2.1
	 */
	public static ExcelWriter getWriter(final boolean isXlsx) {
		try {
			return new ExcelWriter(isXlsx);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet
	 *
	 * @param destFilePath 目标文件路径
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(final String destFilePath) {
		try {
			return new ExcelWriter(destFilePath);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet
	 *
	 * @param sheetName Sheet名
	 * @return {@link ExcelWriter}
	 * @since 4.5.18
	 */
	public static ExcelWriter getWriterWithSheet(final String sheetName) {
		try {
			return new ExcelWriter((File) null, sheetName);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link ExcelWriter}，默认写出到第一个sheet，名字为sheet1
	 *
	 * @param destFile 目标文件
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(final File destFile) {
		try {
			return new ExcelWriter(destFile);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link ExcelWriter}
	 *
	 * @param destFilePath 目标文件路径
	 * @param sheetName    sheet表名
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(final String destFilePath, final String sheetName) {
		try {
			return new ExcelWriter(destFilePath, sheetName);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link ExcelWriter}
	 *
	 * @param destFile  目标文件
	 * @param sheetName sheet表名
	 * @return {@link ExcelWriter}
	 */
	public static ExcelWriter getWriter(final File destFile, final String sheetName) {
		try {
			return new ExcelWriter(destFile, sheetName);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	// ------------------------------------------------------------------------------------------------ getBigWriter

	/**
	 * 获得{@link BigExcelWriter}，默认写出到第一个sheet<br>
	 * 不传入写出的Excel文件路径，只能调用ExcelWriter#flush(OutputStream)方法写出到流<br>
	 * 若写出到文件，还需调用{@link BigExcelWriter#setDestFile(File)}方法自定义写出的文件，然后调用{@link BigExcelWriter#flush()}方法写出到文件
	 *
	 * @return {@link BigExcelWriter}
	 * @since 4.1.13
	 */
	public static BigExcelWriter getBigWriter() {
		try {
			return new BigExcelWriter();
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link BigExcelWriter}，默认写出到第一个sheet<br>
	 * 不传入写出的Excel文件路径，只能调用ExcelWriter#flush(OutputStream)方法写出到流<br>
	 * 若写出到文件，还需调用{@link BigExcelWriter#setDestFile(File)}方法自定义写出的文件，然后调用{@link BigExcelWriter#flush()}方法写出到文件
	 *
	 * @param rowAccessWindowSize 在内存中的行数
	 * @return {@link BigExcelWriter}
	 * @since 4.1.13
	 */
	public static BigExcelWriter getBigWriter(final int rowAccessWindowSize) {
		try {
			return new BigExcelWriter(rowAccessWindowSize);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link BigExcelWriter}，默认写出到第一个sheet
	 *
	 * @param destFilePath 目标文件路径
	 * @return {@link BigExcelWriter}
	 */
	public static BigExcelWriter getBigWriter(final String destFilePath) {
		try {
			return new BigExcelWriter(destFilePath);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link BigExcelWriter}，默认写出到第一个sheet，名字为sheet1
	 *
	 * @param destFile 目标文件
	 * @return {@link BigExcelWriter}
	 */
	public static BigExcelWriter getBigWriter(final File destFile) {
		try {
			return new BigExcelWriter(destFile);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link BigExcelWriter}
	 *
	 * @param destFilePath 目标文件路径
	 * @param sheetName    sheet表名
	 * @return {@link BigExcelWriter}
	 */
	public static BigExcelWriter getBigWriter(final String destFilePath, final String sheetName) {
		try {
			return new BigExcelWriter(destFilePath, sheetName);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 获得{@link BigExcelWriter}
	 *
	 * @param destFile  目标文件
	 * @param sheetName sheet表名
	 * @return {@link BigExcelWriter}
	 */
	public static BigExcelWriter getBigWriter(final File destFile, final String sheetName) {
		try {
			return new BigExcelWriter(destFile, sheetName);
		} catch (final NoClassDefFoundError e) {
			throw new DependencyException(ObjUtil.defaultIfNull(e.getCause(), e), PoiChecker.NO_POI_ERROR_MSG);
		}
	}

	/**
	 * 将Sheet列号变为列名
	 *
	 * @param index 列号, 从0开始
	 * @return 0-》A; 1-》B...26-》AA
	 * @since 4.1.20
	 */
	public static String indexToColName(int index) {
		return CellLocationUtil.indexToColName(index);
	}

	/**
	 * 根据表元的列名转换为列号
	 *
	 * @param colName 列名, 从A开始
	 * @return A1-》0; B1-》1...AA1-》26
	 * @since 4.1.20
	 */
	public static int colNameToIndex(final String colName) {
		return CellLocationUtil.colNameToIndex(colName);
	}

	/**
	 * 将Excel中地址标识符（例如A11，B5）等转换为行列表示<br>
	 * 例如：A11 -》 x:0,y:10，B5-》x:1,y:4
	 *
	 * @param locationRef 单元格地址标识符，例如A11，B5
	 * @return 坐标点，x表示行，从0开始，y表示列，从0开始
	 * @since 5.1.4
	 */
	public static CellLocation toLocation(final String locationRef) {
		return CellLocationUtil.toLocation(locationRef);
	}
}
