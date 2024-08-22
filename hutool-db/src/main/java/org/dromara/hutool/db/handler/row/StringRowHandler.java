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
