/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.db.meta;

import org.dromara.hutool.core.text.StrUtil;

/**
 * 数据库字段类型
 *
 * @author Looly
 * @since 6.0.0
 */
public class ColumnType {
	/**
	 * 类型，对应java.sql.Types中的类型
	 */
	private int type;
	/**
	 * 类型名称
	 */
	private String typeName;

	/**
	 * 构造
	 *
	 * @param type     类型，对应java.sql.Types中的类型
	 * @param typeName 类型名称
	 * @param size     大小或数据长度
	 */
	public ColumnType(final int type, final String typeName, final long size) {
		this.type = type;
		this.typeName = typeName;
		this.size = size;
	}

	/**
	 * 大小或数据长度
	 */
	private long size;


	/**
	 * 获取字段类型的枚举
	 *
	 * @return 阻断类型枚举
	 * @since 4.5.8
	 */
	public JdbcType getTypeEnum() {
		return JdbcType.valueOf(this.type);
	}

	/**
	 * 获取类型，对应{@link java.sql.Types}中的类型
	 *
	 * @return 类型
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置类型，对应java.sql.Types中的类型
	 *
	 * @param type 类型
	 * @return this
	 */
	public ColumnType setType(final int type) {
		this.type = type;
		return this;
	}

	/**
	 * 获取类型名称
	 *
	 * @return 类型名称
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * 设置类型名称
	 *
	 * @param typeName 类型名称
	 * @return this
	 */
	public ColumnType setTypeName(final String typeName) {
		this.typeName = typeName;
		return this;
	}

	/**
	 * 获取大小或数据长度
	 *
	 * @return 大小或数据长度
	 */
	public long getSize() {
		return size;
	}

	/**
	 * 设置大小或数据长度
	 *
	 * @param size 大小或数据长度
	 * @return this
	 */
	public ColumnType setSize(final long size) {
		this.size = size;
		return this;
	}

	@Override
	public String toString() {
		return StrUtil.format("{}-{}({})", this.type, this.typeName, this.size);
	}
}
