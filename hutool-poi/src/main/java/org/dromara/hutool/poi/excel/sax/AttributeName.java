/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.poi.excel.sax;

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
