package cn.hutool.poi.excel.sax.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;

import java.util.List;

/**
 * Bean形式的行处理器<br>
 * 将一行数据转换为Map，key为指定行，value为当前行对应位置的值
 *
 * @author looly
 * @since 5.4.4
 */
public abstract class BeanRowHandler<T> extends AbstractRowHandler<T> {

	/**
	 * 标题所在行（从0开始计数）
	 */
	private final int headerRowIndex;
	/**
	 * 标题行
	 */
	List<String> headerList;

	/**
	 * 构造
	 *
	 * @param headerRowIndex 标题所在行（从0开始计数）
	 * @param startRowIndex  读取起始行（包含，从0开始计数）
	 * @param endRowIndex    读取结束行（包含，从0开始计数）
	 * @param clazz          Bean类型
	 */
	public BeanRowHandler(int headerRowIndex, int startRowIndex, int endRowIndex, Class<T> clazz) {
		super(startRowIndex, endRowIndex);
		Assert.isTrue(headerRowIndex <= startRowIndex, "Header row must before the start row!");
		this.headerRowIndex = headerRowIndex;
		this.convertFunc = (rowList) -> BeanUtil.toBean(IterUtil.toMap(headerList, rowList), clazz);
	}

	@Override
	public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
		if (rowIndex == this.headerRowIndex) {
			this.headerList = ListUtil.unmodifiable(Convert.toList(String.class, rowList));
			return;
		}
		super.handle(sheetIndex, rowIndex, rowList);
	}
}
