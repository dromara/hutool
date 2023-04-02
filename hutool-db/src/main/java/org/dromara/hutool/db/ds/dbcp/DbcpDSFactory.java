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

package org.dromara.hutool.db.ds.dbcp;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.ds.AbstractDSFactory;
import org.dromara.hutool.db.ds.DSKeys;
import org.dromara.hutool.setting.Setting;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

/**
 * DBCP2数据源工厂类
 *
 * @author Looly
 *
 */
public class DbcpDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = -9133501414334104548L;

	/**
	 * 数据源名称：commons-dbcp2
	 */
	public static final String DS_NAME = "commons-dbcp2";

	/**
	 * 构造，使用默认配置文件
	 */
	public DbcpDSFactory() {
		this(null);
	}

	/**
	 * 构造，使用自定义配置文件
	 *
	 * @param setting 配置
	 */
	public DbcpDSFactory(final Setting setting) {
		super(DS_NAME, BasicDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {
		final BasicDataSource ds = new BasicDataSource();

		ds.setUrl(jdbcUrl);
		ds.setDriverClassName(driver);
		ds.setUsername(user);
		ds.setPassword(pass);

		// remarks等特殊配置，since 5.3.8
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemove(key);
			if(StrUtil.isNotBlank(connValue)){
				ds.addConnectionProperty(key, connValue);
			}
		}

		// 注入属性
		poolSetting.toBean(ds);

		return ds;
	}
}
