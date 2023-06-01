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
