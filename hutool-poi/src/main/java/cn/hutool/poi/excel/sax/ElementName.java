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
	 * Cell单元格标签名，表示一个单元格
	 */
	c,
	/**
	 * Value单元格值的标签，表示单元格内的值
	 */
	v,
	/**
	 * Formula公式，表示一个存放公式的单元格
	 */
	f;

	/**
	 * 给定标签名是否匹配当前标签
	 *
	 * @param elementName 标签名
	 * @return 是否匹配
	 */
	public boolean match(String elementName){
		return this.name().equals(elementName);
	}

	/**
	 * 解析支持的节点名枚举
	 * @param elementName 节点名
	 * @return 节点名枚举
	 */
	public static ElementName of(String elementName){
		try {
			return valueOf(elementName);
		} catch (Exception ignore){
		}
		return null;
	}
}
