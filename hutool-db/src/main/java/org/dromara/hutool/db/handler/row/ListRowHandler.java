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
