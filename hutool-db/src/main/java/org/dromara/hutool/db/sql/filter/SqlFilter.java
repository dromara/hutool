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

import org.dromara.hutool.db.sql.BoundSql;

import java.sql.Connection;
import java.util.List;

/**
 * SQL拦截器
 */
public interface SqlFilter {

	/**
	 * 过滤
	 *
	 * @param conn               {@link Connection}
	 * @param boundSql           {@link BoundSql}，包含SQL语句和参数，
	 *                           可通过{@link BoundSql#setSql(String)}和{@link BoundSql#setParams(List)} 自定义SQL和参数
	 * @param returnGeneratedKey 是否自动生成主键
	 */
	void filter(Connection conn, BoundSql boundSql, boolean returnGeneratedKey);
}
