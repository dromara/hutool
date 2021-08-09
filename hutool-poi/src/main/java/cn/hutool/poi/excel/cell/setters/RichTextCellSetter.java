package cn.hutool.poi.excel.cell.setters;

import cn.hutool.poi.excel.cell.CellSetter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;

/**
 * {@link RichTextString} 值单元格设置器
 *
 * @author looly
 * @since 5.7.8
 */
public class RichTextCellSetter implements CellSetter {

	private final RichTextString value;

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	RichTextCellSetter(RichTextString value) {
		this.value = value;
	}

	@Override
	public void setValue(Cell cell) {
		cell.setCellValue(value);
	}
}
