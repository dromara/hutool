package cn.hutool.poi.excel.reader;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 读取{@link Sheet}为Map的List列表形式
 *
 * @author looly
 * @since 5.4.4
 */
public class MapSheetReader extends AbstractSheetReader<List<Map<String, Object>>> {

	private final int headerRowIndex;

	/**
	 * 构造
	 *
	 * @param headerRowIndex 标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex   结束行（包含，从0开始计数）
	 */
	public MapSheetReader(int headerRowIndex, int startRowIndex, int endRowIndex) {
		super(startRowIndex, endRowIndex);
		this.headerRowIndex = headerRowIndex;
	}

	@Override
	public List<Map<String, Object>> read(Sheet sheet) {
		// 边界判断
		final int firstRowNum = sheet.getFirstRowNum();
		final int lastRowNum = sheet.getLastRowNum();
		if (headerRowIndex < firstRowNum) {
			throw new IndexOutOfBoundsException(StrUtil.format("Header row index {} is lower than first row index {}.", headerRowIndex, firstRowNum));
		} else if (headerRowIndex > lastRowNum) {
			throw new IndexOutOfBoundsException(StrUtil.format("Header row index {} is greater than last row index {}.", headerRowIndex, firstRowNum));
		}
		final int startRowIndex = Math.max(this.startRowIndex, firstRowNum);// 读取起始行（包含）
		final int endRowIndex = Math.min(this.endRowIndex, lastRowNum);// 读取结束行（包含）

		// 读取header
		List<String> headerList = aliasHeader(readRow(sheet, headerRowIndex));

		final List<Map<String, Object>> result = new ArrayList<>(endRowIndex - startRowIndex + 1);
		List<Object> rowList;
		for (int i = startRowIndex; i <= endRowIndex; i++) {
			// 跳过标题行
			if (i != headerRowIndex) {
				rowList = readRow(sheet, i);
				if (CollUtil.isNotEmpty(rowList) || false == ignoreEmptyRow) {
					result.add(IterUtil.toMap(headerList, rowList, true));
				}
			}
		}
		return result;
	}
}
