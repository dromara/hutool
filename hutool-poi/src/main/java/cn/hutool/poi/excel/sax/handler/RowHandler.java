package cn.hutool.poi.excel.sax.handler;

import java.util.List;

/**
 * Sax方式读取Excel行处理器
 * @author looly
 *
 */
@FunctionalInterface
public interface RowHandler {
	
	/**
	 * 处理一行数据
	 * @param sheetIndex 当前Sheet序号
	 * @param rowIndex 当前行号
	 * @param rowList 行数据列表
	 */
	void handle(int sheetIndex, int rowIndex, List<Object> rowList);
}
