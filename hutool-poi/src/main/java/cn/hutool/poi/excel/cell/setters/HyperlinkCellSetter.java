package cn.hutool.poi.excel.cell.setters;

import cn.hutool.core.lang.Console;
import cn.hutool.poi.excel.cell.CellSetter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;

/**
 * {@link Hyperlink} 值单元格设置器
 *
 * @author looly
 * @since 5.7.13
 */
public class HyperlinkCellSetter implements CellSetter {

	private final Hyperlink value;

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	HyperlinkCellSetter(Hyperlink value) {
		this.value = value;
	}

	@Override
	public void setValue(Cell cell) {
		cell.setHyperlink(this.value);
		cell.setCellValue(this.value.getLabel());
	}
}
