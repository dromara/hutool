package com.xiaoleilu.hutool.poi.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DocumentFactoryHelper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.poi.excel.editors.TrimEditor;
import com.xiaoleilu.hutool.poi.excel.sax.Excel03SaxReader;
import com.xiaoleilu.hutool.poi.excel.sax.Excel07SaxReader;
import com.xiaoleilu.hutool.poi.excel.sax.handler.RowHandler;
import com.xiaoleilu.hutool.poi.exceptions.POIException;
import com.xiaoleilu.hutool.util.CollectionUtil;
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

	/**
	 * 获取单元格值
	 * 
	 * @param cell {@link Cell}单元格
	 * @param isTrimCellValue 如果单元格类型为字符串，是否去掉两边空白符
	 * @return 值，类型可能为：Date、Double、Boolean、String
	 */
	public static Object getCellValue(Cell cell, boolean isTrimCellValue) {
		return getCellValue(cell, cell.getCellTypeEnum(), isTrimCellValue);
	}

	/**
	 * 获取单元格值
	 * 
	 * @param cell {@link Cell}单元格
	 * @param cellEditor 单元格值编辑器。可以通过此编辑器对单元格值做自定义操作
	 * @return 值，类型可能为：Date、Double、Boolean、String
	 */
	public static Object getCellValue(Cell cell, CellEditor cellEditor) {
		if (null == cell) {
			return null;
		}
		return getCellValue(cell, cell.getCellTypeEnum(), cellEditor);
	}

	/**
	 * 获取单元格值
	 * 
	 * @param cell {@link Cell}单元格
	 * @param cellType 单元格值类型{@link CellType}枚举
	 * @param isTrimCellValue 如果单元格类型为字符串，是否去掉两边空白符
	 * @return 值，类型可能为：Date、Double、Boolean、String
	 */
	public static Object getCellValue(Cell cell, CellType cellType, final boolean isTrimCellValue) {
		return getCellValue(cell, cellType, isTrimCellValue ? new TrimEditor() : null);
	}

	/**
	 * 获取单元格值<br>
	 * 如果单元格值为数字格式，则判断其格式中是否有小数部分，无则返回Long类型，否则返回Double类型
	 * 
	 * @param cell {@link Cell}单元格
	 * @param cellType 单元格值类型{@link CellType}枚举，如果为{@code null}默认使用cell的类型
	 * @param cellEditor 单元格值编辑器。可以通过此编辑器对单元格值做自定义操作
	 * @return 值，类型可能为：Date、Double、Boolean、String
	 */
	public static Object getCellValue(Cell cell, CellType cellType, CellEditor cellEditor) {
		if (null == cell) {
			return null;
		}
		if (null == cellType) {
			cellType = cell.getCellTypeEnum();
		}

		Object value;
		switch (cellType) {
		case NUMERIC:
			value = getNumericValue(cell);
			break;
		case BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case FORMULA:
			// 遇到公式时查找公式结果类型
			value = getCellValue(cell, cell.getCachedFormulaResultTypeEnum(), cellEditor);
			break;
		case BLANK:
			value = StrUtil.EMPTY;
			break;
		default:
			value = cell.getStringCellValue();
		}

		return null == cellEditor ? value : cellEditor.edit(cell, value);
	}

	/**
	 * 获取工作簿指定sheet中图片列表
	 * 
	 * @param workbook 工作簿{@link Workbook}
	 * @param sheetIndex sheet的索引
	 * @return 图片映射，键格式：行_列，值：{@link PictureData}
	 */
	public static Map<String, PictureData> getPicMap(Workbook workbook, int sheetIndex) {
		Assert.notNull(workbook, "Workbook must be not null !");
		if (sheetIndex < 0) {
			sheetIndex = 0;
		}

		if (workbook instanceof HSSFWorkbook) {
			return getPicMapXls((HSSFWorkbook) workbook, sheetIndex);
		} else if (workbook instanceof XSSFWorkbook) {
			return getPicMapXlsx((XSSFWorkbook) workbook, sheetIndex);
		} else {
			throw new IllegalArgumentException(StrUtil.format("Workbook type [{}] is not supported!", workbook.getClass()));
		}
	}

	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet {@link Sheet}
	 * @param row 行号
	 * @param column 列号
	 * @return 是否是合并单元格
	 * @since 3.1.1
	 */
	public static boolean isMergedRegion(Sheet sheet, int row, int column) {
		final int sheetMergeCount = sheet.getNumMergedRegions();
		CellRangeAddress ca;
		for (int i = 0; i < sheetMergeCount; i++) {
			ca = sheet.getMergedRegion(i);
			if (row >= ca.getFirstRow() && row <= ca.getLastRow() && column >= ca.getFirstColumn() && column <= ca.getLastColumn()) {
				return true;
			}
		}
		return false;
	}

	// -------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 获取XLS工作簿指定sheet中图片列表
	 * 
	 * @param workbook 工作簿{@link Workbook}
	 * @param sheetIndex sheet的索引
	 * @return 图片映射，键格式：行_列，值：{@link PictureData}
	 */
	private static Map<String, PictureData> getPicMapXls(HSSFWorkbook workbook, int sheetIndex) {
		final Map<String, PictureData> picMap = new HashMap<>();
		final List<HSSFPictureData> pictures = workbook.getAllPictures();
		if (CollectionUtil.isNotEmpty(pictures)) {
			final HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			HSSFClientAnchor anchor;
			int pictureIndex;
			for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
				if (shape instanceof HSSFPicture) {
					pictureIndex = ((HSSFPicture) shape).getPictureIndex() - 1;
					anchor = (HSSFClientAnchor) shape.getAnchor();
					picMap.put(StrUtil.format("{}_{}", anchor.getRow1(), anchor.getCol1()), pictures.get(pictureIndex));
				}
			}
		}
		return picMap;
	}

	/**
	 * 获取XLSX工作簿指定sheet中图片列表
	 * 
	 * @param workbook 工作簿{@link Workbook}
	 * @param sheetIndex sheet的索引
	 * @return 图片映射，键格式：行_列，值：{@link PictureData}
	 */
	private static Map<String, PictureData> getPicMapXlsx(XSSFWorkbook workbook, int sheetIndex) {
		final Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
		final XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		XSSFDrawing drawing;
		for (POIXMLDocumentPart dr : sheet.getRelations()) {
			if (dr instanceof XSSFDrawing) {
				drawing = (XSSFDrawing) dr;
				final List<XSSFShape> shapes = drawing.getShapes();
				XSSFPicture pic;
				CTMarker ctMarker;
				for (XSSFShape shape : shapes) {
					pic = (XSSFPicture) shape;
					ctMarker = pic.getPreferredSize().getFrom();
					sheetIndexPicMap.put(StrUtil.format("{}_{}", ctMarker.getRow(), ctMarker.getCol()), pic.getPictureData());
				}
			}
		}
		return sheetIndexPicMap;
	}

	/**
	 * 获取数字类型的单元格值
	 * 
	 * @param cell 单元格
	 * @return 单元格值，可能为Long、Double、Date
	 */
	private static Object getNumericValue(Cell cell) {
		final double value = cell.getNumericCellValue();

		final CellStyle style = cell.getCellStyle();
		if (null == style) {
			return value;
		}

		final short formatIndex = style.getDataFormat();
		final String format = style.getDataFormatString();

		// 判断是否为日期
		if (isDateType(formatIndex, format)) {
			return DateUtil.date(cell.getDateCellValue());// 使用Hutool的DateTime包装
		}

		// 普通数字
		if (null != format && format.indexOf('.') < 0) {
			// 对于无小数部分的数字类型，转为Long
			return (long) value;
		} else {
			return value;
		}
	}

	/**
	 * 是否为日期格式<br>
	 * 判断方式：
	 * 
	 * <pre>
	 * 1、指定序号
	 * 2、org.apache.poi.ss.usermodel.DateUtil.isADateFormat方法判定
	 * </pre>
	 * 
	 * @param formatIndex 格式序号
	 * @param format 格式字符串
	 * @return 是否为日期格式
	 */
	private static boolean isDateType(int formatIndex, String format) {
		// yyyy-MM-dd----- 14
		// yyyy年m月d日---- 31
		// yyyy年m月------- 57
		// m月d日 ---------- 58
		// HH:mm----------- 20
		// h时mm分 -------- 32
		if (formatIndex == 14 || formatIndex == 31 || formatIndex == 57 || formatIndex == 58 || formatIndex == 20 || formatIndex == 32) {
			return true;
		}

		if (org.apache.poi.ss.usermodel.DateUtil.isADateFormat(formatIndex, format)) {
			return true;
		}

		return false;
	}
	// -------------------------------------------------------------------------------------------------------------- Private method end
}
