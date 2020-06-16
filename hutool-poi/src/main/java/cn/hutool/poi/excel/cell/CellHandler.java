package cn.hutool.poi.excel.cell;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 单元格处理器接口
 *
 * @author Looly
 */
@FunctionalInterface
public interface CellHandler {

	/**
	 * 处理
	 *
	 * @param cell  单元格对象，可以获取单元格行、列样式等信息
	 * @param value 单元格值
	 */
	void handle(Cell cell, Object value);
}
