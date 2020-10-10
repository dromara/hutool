package cn.hutool.poi.excel.reader;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.poi.excel.cell.CellEditor;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 读取{@link Sheet}为bean的List列表形式
 *
 * @author looly
 * @since 5.4.4
 */
public class BeanSheetReader<T> implements SheetReader<List<T>> {

	private final Class<T> beanClass;
	private final MapSheetReader mapSheetReader;

	/**
	 * 构造
	 *
	 * @param headerRowIndex 标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
	 * @param startRowIndex  起始行（包含，从0开始计数）
	 * @param endRowIndex    结束行（包含，从0开始计数）
	 * @param beanClass      每行对应Bean的类型
	 */
	public BeanSheetReader(int headerRowIndex, int startRowIndex, int endRowIndex, Class<T> beanClass) {
		mapSheetReader = new MapSheetReader(headerRowIndex, startRowIndex, endRowIndex);
		this.beanClass = beanClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> read(Sheet sheet) {
		final List<Map<String, Object>> mapList = mapSheetReader.read(sheet);
		if (Map.class.isAssignableFrom(this.beanClass)) {
			return (List<T>) mapList;
		}

		final List<T> beanList = new ArrayList<>(mapList.size());
		for (Map<String, Object> map : mapList) {
			beanList.add(BeanUtil.toBean(map, this.beanClass));
		}
		return beanList;
	}

	/**
	 * 设置单元格值处理逻辑<br>
	 * 当Excel中的值并不能满足我们的读取要求时，通过传入一个编辑接口，可以对单元格值自定义，例如对数字和日期类型值转换为字符串等
	 *
	 * @param cellEditor 单元格值处理接口
	 */
	public void setCellEditor(CellEditor cellEditor) {
		this.mapSheetReader.setCellEditor(cellEditor);
	}

	/**
	 * 设置是否忽略空行
	 *
	 * @param ignoreEmptyRow 是否忽略空行
	 */
	public void setIgnoreEmptyRow(boolean ignoreEmptyRow) {
		this.mapSheetReader.setIgnoreEmptyRow(ignoreEmptyRow);
	}

	/**
	 * 设置标题行的别名Map
	 *
	 * @param headerAlias 别名Map
	 */
	public void setHeaderAlias(Map<String, String> headerAlias) {
		this.mapSheetReader.setHeaderAlias(headerAlias);
	}

	/**
	 * 增加标题别名
	 *
	 * @param header 标题
	 * @param alias  别名
	 */
	public void addHeaderAlias(String header, String alias) {
		this.mapSheetReader.addHeaderAlias(header, alias);
	}
}
