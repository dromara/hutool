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
import java.sql.SQLException;

/**
 * 行处理器，用于处理一行数据
 *
 * @param <R> 一行数据处理后的结果类型
 * @author looly
 * @since 6.0.0
 */
@FunctionalInterface
public interface RowHandler<R> {

	/**
	 * 处理一行数据
	 *
	 * @param rs {@link ResultSet}，传入前，须调用{@link ResultSet#next()}定位到行。
	 * @return 处理行结果对象
	 * @throws SQLException SQL异常
	 */
	R handle(ResultSet rs) throws SQLException;
}
