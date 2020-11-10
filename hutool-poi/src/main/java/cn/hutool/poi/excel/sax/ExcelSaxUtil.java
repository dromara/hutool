package cn.hutool.poi.excel.sax;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.DependencyException;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import cn.hutool.poi.exceptions.POIException;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Sax方式读取Excel相关工具类
 *
 * @author looly
 */
public class ExcelSaxUtil {

	// 填充字符串
	public static final char CELL_FILL_CHAR = '@';
	// 列的最大位数
	public static final int MAX_CELL_BIT = 3;

	/**
	 * 根据数据类型获取数据
	 *
	 * @param cellDataType       数据类型枚举
	 * @param value              数据值
	 * @param sharedStringsTable {@link SharedStringsTable}
	 * @param numFmtString       数字格式名
	 * @return 数据值
	 */
	public static Object getDataValue(CellDataType cellDataType, String value, SharedStringsTable sharedStringsTable, String numFmtString) {
		if (null == value) {
			return null;
		}

		if (null == cellDataType) {
			cellDataType = CellDataType.NULL;
		}

		Object result;
		switch (cellDataType) {
			case BOOL:
				result = (value.charAt(0) != '0');
				break;
			case ERROR:
				result = StrUtil.format("\\\"ERROR: {} ", value);
				break;
			case FORMULA:
				result = StrUtil.format("\"{}\"", value);
				break;
			case INLINESTR:
				result = new XSSFRichTextString(value).toString();
				break;
			case SSTINDEX:
				try {
					final int index = Integer.parseInt(value);
					//noinspection deprecation
					result = new XSSFRichTextString(sharedStringsTable.getEntryAt(index)).getString();
				} catch (NumberFormatException e) {
					result = value;
				}
				break;
			case NUMBER:
				try {
					result = getNumberValue(value, numFmtString);
				} catch (NumberFormatException e) {
					result = value;
				}
				break;
			case DATE:
				try {
					result = getDateValue(value);
				} catch (Exception e) {
					result = value;
				}
				break;
			default:
				result = value;
				break;
		}
		return result;
	}

	/**
	 * 格式化数字或日期值
	 *
	 * @param value        值
	 * @param numFmtIndex  数字格式索引
	 * @param numFmtString 数字格式名
	 * @return 格式化后的值
	 */
	public static String formatCellContent(String value, int numFmtIndex, String numFmtString) {
		if (null != numFmtString) {
			try {
				value = new DataFormatter().formatRawCellContents(Double.parseDouble(value), numFmtIndex, numFmtString);
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		return value;
	}

	/**
	 * 计算两个单元格之间的单元格数目(同一行)
	 *
	 * @param preRef 前一个单元格位置，例如A1
	 * @param ref    当前单元格位置，例如A8
	 * @return 同一行中两个单元格之间的空单元格数
	 */
	public static int countNullCell(String preRef, String ref) {
		// excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
		// 数字代表列，去掉列信息
		String preXfd = StrUtil.nullToDefault(preRef, "@").replaceAll("\\d+", "");
		String xfd = StrUtil.nullToDefault(ref, "@").replaceAll("\\d+", "");

		// A表示65，@表示64，如果A算作1，那@代表0
		// 填充最大位数3
		preXfd = StrUtil.fillBefore(preXfd, CELL_FILL_CHAR, MAX_CELL_BIT);
		xfd = StrUtil.fillBefore(xfd, CELL_FILL_CHAR, MAX_CELL_BIT);

		char[] preLetter = preXfd.toCharArray();
		char[] letter = xfd.toCharArray();
		// 用字母表示则最多三位，每26个字母进一位
		int res = (letter[0] - preLetter[0]) * 26 * 26 + (letter[1] - preLetter[1]) * 26 + (letter[2] - preLetter[2]);
		return res - 1;
	}

	/**
	 * 从Excel的XML文档中读取内容，并使用{@link ContentHandler}处理
	 *
	 * @param xmlDocStream Excel的XML文档流
	 * @param handler      文档内容处理接口，实现此接口用于回调处理数据
	 * @throws DependencyException 依赖异常
	 * @throws POIException        POI异常，包装了SAXException
	 * @throws IORuntimeException  IO异常，如流关闭或异常等
	 * @since 5.1.4
	 */
	public static void readFrom(InputStream xmlDocStream, ContentHandler handler) throws DependencyException, POIException, IORuntimeException {
		XMLReader xmlReader;
		try {
//			xmlReader = XMLReaderFactory.createXMLReader();
			//noinspection deprecation
			xmlReader = SAXHelper.newXMLReader();
		} catch (SAXException | ParserConfigurationException e) {
			if (e.getMessage().contains("org.apache.xerces.parsers.SAXParser")) {
				throw new DependencyException(e, "You need to add 'xerces:xercesImpl' to your project and version >= 2.11.0");
			} else {
				throw new POIException(e);
			}
		}
		xmlReader.setContentHandler(handler);
		try {
			xmlReader.parse(new InputSource(xmlDocStream));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (SAXException e) {
			throw new POIException(e);
		}
	}

	/**
	 * 判断数字Record中是否为日期格式
	 * @param cell 单元格记录
	 * @param formatListener {@link FormatTrackingHSSFListener}
	 * @return 是否为日期格式
	 * @since 5.4.8
	 */
	public static boolean isDateFormat(CellValueRecordInterface cell, FormatTrackingHSSFListener formatListener){
		final int formatIndex = formatListener.getFormatIndex(cell);
		final String formatString = formatListener.getFormatString(cell);
		return org.apache.poi.ss.usermodel.DateUtil.isADateFormat(formatIndex, formatString);
	}

	/**
	 * 获取日期
	 *
	 * @param value 单元格值
	 * @return 日期
	 * @since 5.3.6
	 */
	public static DateTime getDateValue(String value) {
		return getDateValue(Double.parseDouble(value));
	}

	/**
	 * 获取日期
	 *
	 * @param value 单元格值
	 * @return 日期
	 * @since 4.1.0
	 */
	public static DateTime getDateValue(double value) {
		return DateUtil.date(org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value, false));
	}

	/**
	 * 创建 {@link ExcelSaxReader}
	 *
	 * @param isXlsx     是否为xlsx格式（07格式）
	 * @param rowHandler 行处理器
	 * @return {@link ExcelSaxReader}
	 * @since 5.4.4
	 */
	public static ExcelSaxReader<?> createSaxReader(boolean isXlsx, RowHandler rowHandler) {
		return isXlsx
				? new Excel07SaxReader(rowHandler)
				: new Excel03SaxReader(rowHandler);
	}

	/**
	 * 在Excel03 sax读取中获取日期或数字类型的结果值
	 * @param cell 记录单元格
	 * @param value 值
	 * @param formatListener {@link FormatTrackingHSSFListener}
	 * @return 值，可能为Date或Double或Long
	 * @since 5.5.0
	 */
	public static Object getNumberOrDateValue(CellValueRecordInterface cell, double value, FormatTrackingHSSFListener formatListener){
		Object result;
		if(ExcelSaxUtil.isDateFormat(cell, formatListener)){
			// 可能为日期格式
			result = ExcelSaxUtil.getDateValue(value);
		} else {
			final long longPart = (long) value;
			// 对于无小数部分的数字类型，转为Long，否则保留原数字
			if (((double) longPart) == value) {
				result = longPart;
			} else {
				result = value;
			}
		}
		return result;
	}

	/**
	 * 获取数字类型值
	 *
	 * @param value        值
	 * @param numFmtString 格式
	 * @return 数字，可以是Double、Long
	 * @since 4.1.0
	 */
	private static Number getNumberValue(String value, String numFmtString) {
		if (StrUtil.isBlank(value)) {
			return null;
		}
		double numValue = Double.parseDouble(value);
		// 普通数字
		if (null != numFmtString && numFmtString.indexOf(StrUtil.C_DOT) < 0) {
			final long longPart = (long) numValue;
			//noinspection RedundantIfStatement
			if (longPart == numValue) {
				// 对于无小数部分的数字类型，转为Long
				return longPart;
			}
		}
		return numValue;
	}
}
