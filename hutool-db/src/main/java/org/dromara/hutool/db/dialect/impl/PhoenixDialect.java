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

package org.dromara.hutool.db.dialect.impl;

import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.dialect.DialectName;
import org.dromara.hutool.db.sql.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Phoenix数据库方言
 *
 * @author loolly
 * @since 5.7.2
 */
public class PhoenixDialect extends AnsiSqlDialect {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 */
	public PhoenixDialect() {
//		wrapper = new Wrapper('"');
	}

	@Override
	public PreparedStatement psForUpdate(final Connection conn, final Entity entity, final Query query) throws SQLException {
		// Phoenix的插入、更新语句是统一的，统一使用upsert into关键字
		// Phoenix只支持通过主键更新操作，因此query无效，自动根据entity中的主键更新
		return super.psForInsert(conn, entity);
	}

	@Override
	public String dialectName() {
		return DialectName.PHOENIX.name();
	}

	@Override
	public PreparedStatement psForUpsert(final Connection conn, final Entity entity, final String... keys) throws SQLException {
		// Phoenix只支持通过主键更新操作，因此query无效，自动根据entity中的主键更新
		return psForInsert(conn, entity);
	}
}
