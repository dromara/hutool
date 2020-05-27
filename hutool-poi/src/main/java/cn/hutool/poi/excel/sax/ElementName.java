package cn.hutool.poi.excel.sax;

/**
 * 标签名枚举
 *
 * @author looly
 * @since 5.3.6
 */
public enum ElementName {
	/**
	 * 行标签名，表示一行
	 */
	row,
	/**
	 * 单元格标签名，表示一个单元格
	 */
	c;

	/**
	 * 给定标签名是否匹配当前标签
	 *
	 * @param elementName 标签名
	 * @return 是否匹配
	 */
	public boolean match(String elementName){
		return this.name().equals(elementName);
	}
}
