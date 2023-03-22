package cn.hutool.poi.excel.reader;

import cn.hutool.poi.excel.cell.CellUtil;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取单独一列
 *
 * @author looly
 * @since 5.7.17
 */
public class ColumnSheetReader extends AbstractSheetReader<List<Object>> {

	private final int columnIndex;

	/**
	 * 构造
	 *
	 * @param columnIndex   列号，从0开始计数
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex   结束行（包含，从0开始计数）
	 */
	public ColumnSheetReader(int columnIndex, int startRowIndex, int endRowIndex) {
		super(startRowIndex, endRowIndex);
		this.columnIndex = columnIndex;
	}

	@Override
	public List<Object> read(Sheet sheet) {
		final List<Object> resultList = new ArrayList<>();

		int startRowIndex = Math.max(this.startRowIndex, sheet.getFirstRowNum());// 读取起始行（包含）
		int endRowIndex = Math.min(this.endRowIndex, sheet.getLastRowNum());// 读取结束行（包含）

		Object value;
		for (int i = startRowIndex; i <= endRowIndex; i++) {
			value = CellUtil.getCellValue(CellUtil.getCell(sheet.getRow(i), columnIndex), cellEditor);
			if(null != value || false == ignoreEmptyRow){
				resultList.add(value);
			}
		}

		return resultList;
	}
}
