package com.xiaoleilu.hutool.poi.excel;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 单元格编辑器接口
 * @author Looly
 *
 */
public interface CellEditor {
	/**
	 * 编辑
	 * @param cell 单元格对象，可以获取单元格行、列样式等信息
	 * @param value 单元格值
	 */
	public Object edit(Cell cell, Object value);
}
