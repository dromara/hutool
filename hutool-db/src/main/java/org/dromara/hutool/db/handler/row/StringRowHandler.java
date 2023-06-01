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

import org.dromara.hutool.core.text.StrUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 将{@link ResultSet}结果中的某行处理为数组
 *
 * @author looly
 */
public class StringRowHandler implements RowHandler<String> {

	private final ArrayRowHandler<Object> arrayRowHandler;
	private final String delimiter;

	/**
	 * 构造
	 *
	 * @param meta      {@link ResultSetMetaData}
	 * @param delimiter 分隔符
	 * @throws SQLException SQL异常
	 */
	public StringRowHandler(final ResultSetMetaData meta, final String delimiter) throws SQLException {
		this.arrayRowHandler = new ArrayRowHandler<>(meta, Object.class);
		this.delimiter = delimiter;
	}

	@Override
	public String handle(final ResultSet rs) throws SQLException {
		return StrUtil.join(delimiter, arrayRowHandler.handle(rs));
	}
}
