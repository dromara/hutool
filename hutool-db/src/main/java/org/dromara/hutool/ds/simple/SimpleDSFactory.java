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

package org.dromara.hutool.ds.simple;

import org.dromara.hutool.ds.AbstractDSFactory;
import org.dromara.hutool.Setting;

import javax.sql.DataSource;

/**
 * 简单数据源工厂类
 *
 * @author Looly
 *
 */
public class SimpleDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 4738029988261034743L;

	/**
	 * 数据源名称：Hutool-Simple-DataSource
	 */
	public static final String DS_NAME = "Hutool-Simple-DataSource";

	/**
	 * 构造，使用默认配置文件
	 */
	public SimpleDSFactory() {
		this(null);
	}

	/**
	 * 构造，使用自定义配置文件
	 *
	 * @param setting 配置
	 */
	public SimpleDSFactory(final Setting setting) {
		super(DS_NAME, SimpleDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {
		final SimpleDataSource ds = new SimpleDataSource(//
				jdbcUrl, //
				user, //
				pass, //
				driver//
		);
		ds.setConnProps(poolSetting.getProps(Setting.DEFAULT_GROUP));
		return ds;
	}
}
