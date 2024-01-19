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

package org.dromara.hutool.db.ds.jndi;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.ds.DSFactory;
import org.dromara.hutool.db.ds.DSUtil;

import javax.sql.DataSource;

/**
 * JNDI数据源工厂类<br>
 * Setting配置样例：
 * <pre>
 *     [group]
 *     jndi = jdbc/TestDB
 * </pre>
 *
 * @author Looly
 *
 */
public class JndiDSFactory implements DSFactory {
	private static final long serialVersionUID = 1573625812927370432L;

	@Override
	public String getDataSourceName() {
		return "JNDI DataSource";
	}

	@Override
	public DataSource createDataSource(final ConnectionConfig<?> config) {
		final String jndiName = config.getPoolProps().getProperty("jndi");
		if (StrUtil.isEmpty(jndiName)) {
			throw new DbException("No setting name [jndi] for this group.");
		}
		return DSUtil.getJndiDS(jndiName);
	}
}
