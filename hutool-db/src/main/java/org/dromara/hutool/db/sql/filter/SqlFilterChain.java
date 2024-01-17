/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.sql.filter;

import org.dromara.hutool.core.lang.Chain;
import org.dromara.hutool.db.sql.BoundSql;
import org.jetbrains.annotations.NotNull;

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

	@NotNull
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
