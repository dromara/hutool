package cn.hutool.poi.excel.sax;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Sax方式读取Excel相关工具类
 * 
 * @author looly
 *
 */
public class ExcelSaxUtil {

	// 填充字符串
	public static final char CELL_FILL_CHAR = '@';
	// 列的最大位数
	public static final int MAX_CELL_BIT = 3;

	/**
	 * 根据数据类型获取数据
	 * 
	 * @param cellDataType 数据类型枚举
	 * @param value 数据值
	 * @param sharedStringsTable {@link SharedStringsTable}
	 * @param numFmtString 数字格式名
	 * @return 数据值
	 */
	public static Object getDataValue(CellDataType cellDataType, String value, SharedStringsTable sharedStringsTable, String numFmtString) {
		if (null == value) {
			return null;
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
			result = new XSSFRichTextString(value.toString()).toString();
			break;
		case SSTINDEX:
			try {
				final int index = Integer.parseInt(value);
				result = new XSSFRichTextString(sharedStringsTable.getEntryAt(index)).getString();
			} catch (NumberFormatException e) {
				result = value;
			}
			break;
		case NUMBER:
			result = getNumberValue(value, numFmtString);
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
	 * @param value 值
	 * @param numFmtIndex 数字格式索引
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
	 * @param ref 当前单元格位置，例如A8
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
	 * 获取日期
	 * 
	 * @param value 单元格值
	 * @return 日期
	 * @since 4.1.0
	 */
	private static DateTime getDateValue(String value) {
		return DateUtil.date(org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(value), false));
	}

	/**
	 * 获取数字类型值
	 * 
	 * @param value 值
	 * @param numFmtString 格式
	 * @return 数字，可以是Double、Long
	 * @since 4.1.0
	 */
	private static Number getNumberValue(String value, String numFmtString) {
		if(StrUtil.isBlank(value)) {
			return null;
		}
		double numValue = Double.parseDouble(value);
		// 普通数字
		if (null != numFmtString && numFmtString.indexOf(StrUtil.C_DOT) < 0) {
			final long longPart = (long) numValue;
			if (longPart == numValue) {
				// 对于无小数部分的数字类型，转为Long
				return longPart;
			}
		}
		return numValue;
	}
}
