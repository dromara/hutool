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

package org.dromara.hutool.db.handler.row;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.db.handler.ResultSetUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 将{@link ResultSet}结果中的某行处理为数组
 *
 * @param <E> 数组元素类型
 * @author looly
 */
public class ArrayRowHandler<E> extends AbsRowHandler<E[]> {

	private final Class<E> componentType;

	/**
	 * 构造
	 *
	 * @param meta          {@link ResultSetMetaData}
	 * @param componentType 数组元素类型
	 * @throws SQLException SQL异常
	 */
	public ArrayRowHandler(final ResultSetMetaData meta, final Class<E> componentType) throws SQLException {
		super(meta);
		this.componentType = componentType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E[] handle(final ResultSet rs) throws SQLException {
		final E[] result = ArrayUtil.newArray(componentType, columnCount);
		for (int i = 0, j = 1; i < columnCount; i++, j++) {
			result[i] = (E) ResultSetUtil.getColumnValue(rs, j, meta.getColumnType(j), componentType);
		}
		return result;
	}
}
