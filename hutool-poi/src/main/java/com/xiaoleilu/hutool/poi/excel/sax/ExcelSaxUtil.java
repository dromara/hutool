package com.xiaoleilu.hutool.poi.excel.sax;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Sax方式读取Excel相关工具类
 * 
 * @author looly
 *
 */
public class ExcelSaxUtil {

	/**
	 * 根据数据类型获取数据
	 * 
	 * @param cellDataType 数据类型枚举
	 * @param value 数据值
	 * @param style 单元格样式
	 * @return
	 */
	public static String getDataValue(CellDataType cellDataType, String value, XSSFCellStyle style) {
		String thisStr;

		switch (cellDataType) {
		case BOOL:
			char first = value.charAt(0);
			thisStr = first == '0' ? "FALSE" : "TRUE";
			break;
		case ERROR:
			thisStr = "\"ERROR:" + value.toString() + '"';
			break;
		case FORMULA:
			thisStr = '"' + value.toString() + '"';
			break;
		case INLINESTR:
			XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
			thisStr = rtsi.toString();
			rtsi = null;
			break;
		case SSTINDEX:
			// String sstIndex = value.toString();
			thisStr = value.toString();
			break;
		case NUMBER:
			final String formatStr = style.getDataFormatString();
			if (formatStr != null) {
				thisStr = formatCellContent(value, style);
			} else {
				thisStr = value;
			}
			thisStr = thisStr.replace("_", "").trim();
			break;
		case DATE:
			thisStr = StrUtil.cleanBlank(formatCellContent(value, style));
			break;
		default:
			thisStr = "";
			break;
		}
		return thisStr;
	}

	/**
	 * 格式化数字或日期值
	 * 
	 * @param value 值
	 * @param style XSSFCellStyle风格对象
	 * @return 格式化后的值
	 */
	public static String formatCellContent(String value, XSSFCellStyle style) {
		try {
			return new DataFormatter().formatRawCellContents(Double.parseDouble(value), style.getDataFormat(), style.getDataFormatString());
		} catch (NumberFormatException e) {
			return value;
		}
	}

	/**
	 * 计算两个单元格之间的单元格数目(同一行)
	 * @param preRef 前一个单元格位置，例如A1
	 * @param ref 当前单元格位置，例如A8
	 * @return 同一行中两个单元格之间的空单元格数
	 */
	public static int countNullCell(String preRef, String ref) {
		// excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
		//数字代表列，去掉列信息
		String xfd = ref.replaceAll("\\d+", "");
		String preXfd = preRef.replaceAll("\\d+", "");

		//A表示65，@表示64，如果A算作1，那@代表0
		xfd = StrUtil.fillBefore(xfd, '@', 3);
		preXfd = StrUtil.fillBefore(preXfd, '@', 3);

		char[] letter = xfd.toCharArray();
		char[] preLetter = preXfd.toCharArray();
		//用字母表示则最多三位，每26个字母进一位
		int res = (letter[0] - preLetter[0]) * 26 * 26 + (letter[1] - preLetter[1]) * 26 + (letter[2] - preLetter[2]);
		return Math.abs(res) - 1;
	}
}
