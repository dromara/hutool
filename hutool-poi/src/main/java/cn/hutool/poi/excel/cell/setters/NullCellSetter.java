package cn.hutool.poi.excel.cell.setters;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.cell.CellSetter;
import org.apache.poi.ss.usermodel.Cell;

/**
 * {@link Number} 值单元格设置器
 *
 * @author looly
 * @since 5.7.8
 */
public class NullCellSetter implements CellSetter {

	public static final NullCellSetter INSTANCE = new NullCellSetter();

	@Override
	public void setValue(Cell cell) {
		cell.setCellValue(StrUtil.EMPTY);
	}
}
