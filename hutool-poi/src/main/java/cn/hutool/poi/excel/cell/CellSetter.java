package cn.hutool.poi.excel.cell;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 单元格值自定义设置器，主要用于Excel数据导出，用户通过自定义此接口，实现可定制化的单元格值设定
 *
 * @author looly
 * @since 5.7.8
 */
@FunctionalInterface
public interface CellSetter {

	/**
	 * 自定义单元格值设置，同时可以设置单元格样式、格式等信息
	 * @param cell 单元格
	 */
	void setValue(Cell cell);
}
