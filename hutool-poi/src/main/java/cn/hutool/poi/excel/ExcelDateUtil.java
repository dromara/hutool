package cn.hutool.poi.excel;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.formula.ConditionalFormattingEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ExcelNumberFormat;

/**
 * Excel中日期判断、读取、处理等补充工具类
 *
 * @author looly
 * @since 5.5.5
 */
public class ExcelDateUtil {

	/**
	 * 某些特殊的自定义日期格式
	 */
	private static final int[] customFormats = new int[]{28, 30, 31, 32, 33, 55, 56, 57, 58};

	public static boolean isDateFormat(Cell cell){
		return isDateFormat(cell, null);
	}

	/**
	 * 判断是否日期格式
	 * @param cell 单元格
	 * @param cfEvaluator {@link ConditionalFormattingEvaluator}
	 * @return 是否日期格式
	 */
	public static boolean isDateFormat(Cell cell, ConditionalFormattingEvaluator cfEvaluator){
		final ExcelNumberFormat nf = ExcelNumberFormat.from(cell, cfEvaluator);
		return isDateFormat(nf);
	}

	/**
	 * 判断是否日期格式
	 * @param numFmt {@link ExcelNumberFormat}
	 * @return 是否日期格式
	 */
	public static boolean isDateFormat(ExcelNumberFormat numFmt) {
		return isDateFormat(numFmt.getIdx(), numFmt.getFormat());
	}

	/**
	 * 判断日期格式
	 *
	 * @param formatIndex  格式索引，一般用于内建格式
	 * @param formatString 格式字符串
	 * @return 是否为日期格式
	 * @since 5.5.3
	 */
	public static boolean isDateFormat(int formatIndex, String formatString) {
		// issue#1283@Github
		if (ArrayUtil.contains(customFormats, formatIndex)) {
			return true;
		}

		// 自定义格式判断
		if (StrUtil.isNotEmpty(formatString) &&
				StrUtil.containsAny(formatString, "周", "星期", "aa")) {
			// aa  -> 周一
			// aaa -> 星期一
			return true;
		}

		return org.apache.poi.ss.usermodel.DateUtil.isADateFormat(formatIndex, formatString);
	}
}
