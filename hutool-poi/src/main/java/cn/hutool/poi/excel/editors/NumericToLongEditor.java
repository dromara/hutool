package cn.hutool.poi.excel.editors;

import org.apache.poi.ss.usermodel.Cell;

import cn.hutool.poi.excel.CellEditor;

/**
 * POI中NUMRIC类型的值默认返回的是Double类型，此编辑器用于转换其为Long型
 * @author Looly
 *@deprecated 自3.1.1开始，Hutool会自动判断单元格值为Double还是Long类型
 */
@Deprecated
public class NumericToLongEditor implements CellEditor{

	@Override
	public Object edit(Cell cell, Object value) {
		if(value instanceof Number) {
			return ((Number)value).longValue();
		}
		return value;
	}

}
