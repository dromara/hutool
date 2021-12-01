package cn.hutool.poi.excel.cell.values;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelDateUtil;
import cn.hutool.poi.excel.cell.CellValue;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.NumberToTextConverter;

/**
 * 数字类型单元格值<br>
 * 单元格值可能为Long、Double、Date
 *
 * @author looly
 * @since 5.7.8
 */
public class NumericCellValue implements CellValue<Object> {

	private final Cell cell;

	/**
	 * 构造
	 *
	 * @param cell {@link Cell}
	 */
	public NumericCellValue(Cell cell) {
		this.cell = cell;
	}

	@Override
	public Object getValue() {
		final double value = cell.getNumericCellValue();

		final CellStyle style = cell.getCellStyle();
		if (null != style) {
			// 判断是否为日期
			if (ExcelDateUtil.isDateFormat(cell)) {
				// 1899年写入会导致数据错乱，读取到1899年证明这个单元格的信息不关注年月日
				Date dateCellValue = cell.getDateCellValue();
				if ("1899".equals(DateUtil.format(dateCellValue, "yyyy"))) {
					return DateUtil.format(dateCellValue, style.getDataFormatString());
				}
				// 使用Hutool的DateTime包装
				return DateUtil.date(dateCellValue);
			}

			final String format = style.getDataFormatString();
			// 普通数字
			if (null != format && format.indexOf(StrUtil.C_DOT) < 0) {
				final long longPart = (long) value;
				if (((double) longPart) == value) {
					// 对于无小数部分的数字类型，转为Long
					return longPart;
				}
			}
		}

		// 某些Excel单元格值为double计算结果，可能导致精度问题，通过转换解决精度问题。
		return Double.parseDouble(NumberToTextConverter.toText(value));
	}
}
