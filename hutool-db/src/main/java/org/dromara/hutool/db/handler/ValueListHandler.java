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
