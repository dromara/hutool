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

package org.dromara.hutool.db.handler;

import org.dromara.hutool.db.handler.row.ListRowHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 结果集处理类 ，处理出的结果为List列表
 * @author loolly
 *
 */
public class ValueListHandler implements RsHandler<List<List<Object>>>{
	private static final long serialVersionUID = 1L;

	/**
	 * 创建一个 EntityListHandler对象
	 * @return EntityListHandler对象
	 */
	public static ValueListHandler of() {
		return new ValueListHandler();
	}

	@Override
	public List<List<Object>> handle(final ResultSet rs) throws SQLException {
		final ListRowHandler<Object> listRowHandler = new ListRowHandler<>(rs.getMetaData(), Object.class);
		final ArrayList<List<Object>> result = new ArrayList<>();
		while (rs.next()) {
			result.add(listRowHandler.handle(rs));
		}
		return result;
	}
}
