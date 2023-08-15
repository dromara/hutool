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

package org.dromara.hutool.db.ds.c3p0;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbRuntimeException;
import org.dromara.hutool.db.ds.AbstractDSFactory;
import org.dromara.hutool.db.ds.DSKeys;
import org.dromara.hutool.setting.Setting;
import org.dromara.hutool.setting.props.Props;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * C3P0数据源工厂类
 *
 * @author Looly
 *
 */
public class C3p0DSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = -6090788225842047281L;

	/**
	 * 数据源名称：C3P0
	 */
	public static final String DS_NAME = "C3P0";

	/**
	 * 构造，使用默认配置
	 */
	public C3p0DSFactory() {
		this(null);
	}

	/**
	 * 构造，使用自定义配置文件
	 *
	 * @param setting 配置
	 */
	public C3p0DSFactory(final Setting setting) {
		super(DS_NAME, ComboPooledDataSource.class, setting);
	}

	@Override
	protected DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {
		final ComboPooledDataSource ds = new ComboPooledDataSource();

		// remarks等特殊配置，since 5.3.8
		final Props connProps = new Props();
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = poolSetting.getAndRemove(key);
			if(StrUtil.isNotBlank(connValue)){
				connProps.setProperty(key, connValue);
			}
		}
		if(MapUtil.isNotEmpty(connProps)){
			ds.setProperties(connProps);
		}

		ds.setJdbcUrl(jdbcUrl);
		try {
			ds.setDriverClass(driver);
		} catch (final PropertyVetoException e) {
			throw new DbRuntimeException(e);
		}
		ds.setUser(user);
		ds.setPassword(pass);

		// 注入属性
		poolSetting.toBean(ds);

		return ds;
	}
}
