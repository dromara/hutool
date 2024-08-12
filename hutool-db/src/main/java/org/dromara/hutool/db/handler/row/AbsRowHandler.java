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

package org.dromara.hutool.db.handler.row;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 将{@link ResultSet}结果中的某行处理为List
 *
 * @param <R> 一行数据处理后的结果类型
 * @author looly
 */
public abstract class AbsRowHandler<R> implements RowHandler<R> {

	protected final ResultSetMetaData meta;
	protected final int columnCount;

	/**
	 * 构造
	 *
	 * @param meta {@link ResultSetMetaData}
	 * @throws SQLException SQL异常
	 */
	public AbsRowHandler(final ResultSetMetaData meta) throws SQLException {
		this.meta = meta;
		this.columnCount = meta.getColumnCount();
	}
}
