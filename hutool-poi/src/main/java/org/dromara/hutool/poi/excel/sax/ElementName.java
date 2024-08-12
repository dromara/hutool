/*
 * Copyright (c) 2013-2024 Hutool Team.
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
	public boolean match(final String elementName){
		return this.name().equals(elementName);
	}

	/**
	 * 解析支持的节点名枚举
	 * @param elementName 节点名
	 * @return 节点名枚举
	 */
	public static ElementName of(final String elementName){
		try {
			return valueOf(elementName);
		} catch (final Exception ignore){
		}
		return null;
	}
}
