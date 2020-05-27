package cn.hutool.poi.excel.sax;

import org.xml.sax.Attributes;

/**
 * Excel的XML中属性名枚举
 *
 * @author looly
 * @since 5.3.6
 */
public enum AttributeName {

	/**
	 * 行列号属性，行标签下此为行号属性名，cell标签下下为列号属性名
	 */
	r,
	/**
	 * ST（StylesTable） 的索引，样式index，用于获取行或单元格样式
	 */
	s,
	/**
	 * Type类型，单元格类型属性，见{@link CellDataType}
	 */
	t;

	/**
	 * 是否匹配给定属性
	 *
	 * @param attributeName 属性
	 * @return 是否匹配
	 */
	public boolean match(String attributeName) {
		return this.name().equals(attributeName);
	}

	/**
	 * 从属性里列表中获取对应属性值
	 *
	 * @param attributes 属性列表
	 * @return 属性值
	 */
	public String getValue(Attributes attributes){
		return attributes.getValue(name());
	}
}
