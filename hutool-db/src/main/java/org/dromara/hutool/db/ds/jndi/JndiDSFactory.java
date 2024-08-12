/*
 * Copyright (c) 2013-2024 Hutool Team.
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
