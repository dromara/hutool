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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理为数字结果，当查询结果为单个数字时使用此处理器（例如select count(1)）
 * @author loolly
 *
 */
public class NumberHandler implements RsHandler<Number>{
	private static final long serialVersionUID = 4081498054379705596L;

	/**
	 * 单例
	 */
	public static final NumberHandler INSTANCE = new NumberHandler();

	@Override
	public Number handle(final ResultSet rs) throws SQLException {
		return (null != rs && rs.next()) ? rs.getBigDecimal(1) : null;
	}
}
