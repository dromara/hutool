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

package org.dromara.hutool.db.dialect;

import org.dromara.hutool.core.text.StrUtil;

/**
 * 方言名<br>
 * 方言枚举列出了Hutool支持的所有数据库方言
 *
 * @author Looly
 */
public enum DialectName {
	ANSI, MYSQL, ORACLE, POSTGRESQL, SQLITE3, H2, SQLSERVER, SQLSERVER2012, PHOENIX;

	/**
	 * 是否为指定数据库方言，检查时不分区大小写
	 *
	 * @param dialectName     方言名
	 * @return 是否时Oracle数据库
	 * @since 5.7.2
	 */
	public boolean match(final String dialectName) {
		return StrUtil.equalsIgnoreCase(dialectName, name());
	}
}
