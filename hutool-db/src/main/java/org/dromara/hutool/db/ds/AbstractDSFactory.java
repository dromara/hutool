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

package org.dromara.hutool.db.ds;

import org.dromara.hutool.core.lang.Assert;

import javax.sql.DataSource;

/**
 * 抽象数据源工厂
 *
 * @author looly
 */
public abstract class AbstractDSFactory implements DSFactory {
	private static final long serialVersionUID = 1L;

	private final String dataSourceName;

	/**
	 * 构造
	 *
	 * @param dataSourceClass 数据库连接池实现类，用于检测所提供的DataSource类是否存在，当传入的DataSource类不存在时抛出ClassNotFoundException<br>
	 *                        此参数的作用是在detectDSFactory方法自动检测所用连接池时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种连接池的检测。
	 * @param dataSourceName  数据源名称
	 */
	public AbstractDSFactory(final Class<? extends DataSource> dataSourceClass, final String dataSourceName) {
		//此参数的作用是在detectDSFactory方法自动检测所用连接池时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种连接池的检测。
		Assert.notNull(dataSourceClass);
		this.dataSourceName = dataSourceName;
	}

	@Override
	public String getDataSourceName() {
		return this.dataSourceName;
	}
}
