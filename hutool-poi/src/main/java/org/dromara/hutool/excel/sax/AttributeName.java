/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.excel.sax;

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
	public boolean match(final String attributeName) {
		return this.name().equals(attributeName);
	}

	/**
	 * 从属性里列表中获取对应属性值
	 *
	 * @param attributes 属性列表
	 * @return 属性值
	 */
	public String getValue(final Attributes attributes){
		return attributes.getValue(name());
	}
}
