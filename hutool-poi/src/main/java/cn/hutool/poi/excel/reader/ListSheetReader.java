package cn.hutool.poi.excel.reader;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取{@link Sheet}为List列表形式
 *
 * @author looly
 * @since 5.4.4
 */
public class ListSheetReader extends AbstractSheetReader<List<List<Object>>> {

	/** 是否首行作为标题行转换别名 */
	private final boolean aliasFirstLine;

	/**
	 * 构造
	 *
	 * @param startRowIndex  起始行（包含，从0开始计数）
	 * @param endRowIndex    结束行（包含，从0开始计数）
	 * @param aliasFirstLine 是否首行作为标题行转换别名
	 */
	public ListSheetReader(int startRowIndex, int endRowIndex, boolean aliasFirstLine) {
		super(startRowIndex, endRowIndex);
		this.aliasFirstLine = aliasFirstLine;
	}

	@Override
	public List<List<Object>> read(Sheet sheet) {
		final List<List<Object>> resultList = new ArrayList<>();

		int startRowIndex = Math.max(this.startRowIndex, sheet.getFirstRowNum());// 读取起始行（包含）
		int endRowIndex = Math.min(this.endRowIndex, sheet.getLastRowNum());// 读取结束行（包含）
		List<Object> rowList;
		for (int i = startRowIndex; i <= endRowIndex; i++) {
			rowList = readRow(sheet, i);
			if (CollUtil.isNotEmpty(rowList) || false == ignoreEmptyRow) {
				if (aliasFirstLine && i == startRowIndex) {
					// 第一行作为标题行，替换别名
					rowList = Convert.toList(Object.class, aliasHeader(rowList));
				}
				resultList.add(rowList);
			}
		}
		return resultList;
	}
}
