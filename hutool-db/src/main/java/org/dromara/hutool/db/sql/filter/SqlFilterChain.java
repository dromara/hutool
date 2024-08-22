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

package org.dromara.hutool.db.sql.filter;

import org.dromara.hutool.core.lang.Chain;
import org.dromara.hutool.db.sql.BoundSql;

import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 多个{@link SqlFilter}
 *
 * @author Looly
 */
public class SqlFilterChain implements SqlFilter, Chain<SqlFilter, SqlFilterChain> {

	private final List<SqlFilter> filters = new LinkedList<>();

	@Override
	public SqlFilterChain addChain(final SqlFilter element) {
		filters.add(element);
		return this;
	}

	@Override
	public Iterator<SqlFilter> iterator() {
		return filters.iterator();
	}

	@Override
	public void filter(final Connection conn, final BoundSql boundSql, final boolean returnGeneratedKey) {
		for (final SqlFilter filter : filters) {
			filter.filter(conn, boundSql, returnGeneratedKey);
		}
	}
}
