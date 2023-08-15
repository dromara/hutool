/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.handler.row;

import org.dromara.hutool.db.handler.ResultSetUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将{@link ResultSet}结果中的某行处理为List
 *
 * @param <E> 元素类型
 * @author looly
 */
public class ListRowHandler<E> extends AbsRowHandler<List<E>> {

	private final Class<E> elementType;

	/**
	 * 构造
	 *
	 * @param meta        {@link ResultSetMetaData}
	 * @param elementType 列表元素类型
	 * @throws SQLException SQL异常
	 */
	public ListRowHandler(final ResultSetMetaData meta, final Class<E> elementType) throws SQLException {
		super(meta);
		this.elementType = elementType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> handle(final ResultSet rs) throws SQLException {
		final List<E> row = new ArrayList<>(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			row.add((E) ResultSetUtil.getColumnValue(rs, i, meta.getColumnType(i), elementType));
		}

		return row;
	}
}
