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
import org.dromara.hutool.db.DbRuntimeException;
import org.dromara.hutool.db.ds.AbstractDSFactory;
import org.dromara.hutool.db.ds.DSUtil;
import org.dromara.hutool.setting.Setting;

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
public class JndiDSFactory extends AbstractDSFactory {
	private static final long serialVersionUID = 1573625812927370432L;

	/**
	 * 数据源名称：JNDI DataSource
	 */
	public static final String DS_NAME = "JNDI DataSource";

	/**
	 * 构造，使用默认配置文件
	 */
	public JndiDSFactory() {
		this(null);
	}

	/**
	 * 构造，使用自定义配置文件
	 *
	 * @param setting 配置
	 */
	public JndiDSFactory(final Setting setting) {
		super(DS_NAME, null, setting);
	}

	@Override
	protected DataSource createDataSource(final String jdbcUrl, final String driver, final String user, final String pass, final Setting poolSetting) {
		final String jndiName = poolSetting.getStr("jndi");
		if (StrUtil.isEmpty(jndiName)) {
			throw new DbRuntimeException("No setting name [jndi] for this group.");
		}
		return DSUtil.getJndiDS(jndiName);
	}
}
