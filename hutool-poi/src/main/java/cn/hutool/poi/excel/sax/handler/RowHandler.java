package cn.hutool.poi.excel.sax.handler;

import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;

/**
 * Sax方式读取Excel行处理器
 *
 * @author looly
 */
@FunctionalInterface
public interface RowHandler {

	/**
	 * 处理一行数据
	 *
	 * @param sheetIndex 当前Sheet序号
	 * @param rowIndex   当前行号，从0开始计数
	 * @param rowList    行数据列表
	 */
	void handle(int sheetIndex, long rowIndex, List<Object> rowList);

	/**
	 * 处理一个单元格的数据
	 *
	 * @param sheetIndex    当前Sheet序号
	 * @param rowIndex      当前行号
	 * @param cellIndex     当前列号
	 * @param value         单元格的值
	 * @param xssfCellStyle 单元格样式
	 */
	default void handleCell(int sheetIndex, long rowIndex, int cellIndex, Object value, CellStyle xssfCellStyle) {
		//pass
	}

	/**
	 * 处理一个sheet页完成的操作
	 */
	default void doAfterAllAnalysed() {
		//pass
	}
}
