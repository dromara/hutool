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

package org.dromara.hutool.db.ds.pooled;

import org.dromara.hutool.db.config.ConnectionConfig;
import org.dromara.hutool.db.ds.DSFactory;

import javax.sql.DataSource;

/**
 * Hutool自身实现的池化数据源工厂类
 *
 * @author Looly
 *
 */
public class PooledDSFactory implements DSFactory {
	private static final long serialVersionUID = 8093886210895248277L;

	@Override
	public String getDataSourceName() {
		return "Hutool-Pooled-DataSource";
	}

	@Override
	public DataSource createDataSource(final ConnectionConfig<?> config) {
		return new PooledDataSource(config);
	}
}
