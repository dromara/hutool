package com.xiaoleilu.hutool.poi.excel.sax;

import java.util.List;

/**
 * Sax方式读取Excel行处理器
 * @author looly
 *
 */
public interface RowHandler {
	
	/**
	 * 处理一行数据
	 * @param sheetIndex 当前Sheet序号
	 * @param rowIndex 当前行号
	 * @param rowlist 行数据列表
	 */
	void handle(int sheetIndex, int rowIndex, List<String> rowlist);
}
