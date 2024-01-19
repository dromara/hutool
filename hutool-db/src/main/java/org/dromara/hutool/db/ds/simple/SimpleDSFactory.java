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

package org.dromara.hutool.db.ds.simple;

import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.ds.DSFactory;

import javax.sql.DataSource;

/**
 * 简单数据源工厂类
 *
 * @author Looly
 *
 */
public class SimpleDSFactory implements DSFactory {
	private static final long serialVersionUID = 4738029988261034743L;

	@Override
	public String getDataSourceName() {
		return "Hutool-Simple-DataSource";
	}

	@Override
	public DataSource createDataSource(final ConnectionConfig<?> config) {
		return new SimpleDataSource(config);
	}
}
