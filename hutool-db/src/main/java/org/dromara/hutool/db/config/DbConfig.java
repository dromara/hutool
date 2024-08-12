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

package org.dromara.hutool.db.config;

import org.dromara.hutool.db.dialect.Dialect;
import org.dromara.hutool.db.driver.DriverUtil;
import org.dromara.hutool.db.ds.DSFactory;
import org.dromara.hutool.db.sql.filter.SqlFilter;
import org.dromara.hutool.db.sql.filter.SqlFilterChain;

/**
 * 数据库配置，包括：
 * <ul>
 *     <li>基本配置项，如driver、url、user、password等</li>
 *     <li>连接配置，如remarks、useInformationSchema等</li>
 *     <li>连接池配置，如初始容量、最大容量等，取决于连接池库具体要求</li>
 *     <li>其它配置，如是否大小写敏感、SQL过滤器等</li>
 * </ul>
 *
 * @author Looly
 */
public class DbConfig extends ConnectionConfig<DbConfig> {

	// region ----- of

	/**
	 * 创建DsConfig
	 *
	 * @param url  jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 * @return DsConfig
	 */
	public static DbConfig of(final String url, final String user, final String pass) {
		return of().setUrl(url).setUser(user).setPass(pass).setDriver(DriverUtil.identifyDriver(url));
	}

	/**
	 * 创建DsConfig
	 *
	 * @return DsConfig
	 */
	public static DbConfig of() {
		return new DbConfig();
	}
	// endregion

	// 其它配置
	/**
	 * 是否大小写不敏感（默认大小写不敏感）
	 */
	private boolean caseInsensitive = true;

	/**
	 * SQL过滤器，用于在生成SQL前对SQL做操作，如记录日志等
	 */
	private SqlFilterChain sqlFilters;

	/**
	 * 自定义{@link DSFactory}，用于自定义连接池
	 */
	private DSFactory dsFactory;

	/**
	 * 自定义数据库方言
	 */
	private Dialect dialect;

	/**
	 * 构造
	 */
	public DbConfig() {
	}

	/**
	 * 获取是否在结果中忽略大小写
	 *
	 * @return 是否在结果中忽略大小写
	 */
	public boolean isCaseInsensitive() {
		return this.caseInsensitive;
	}

	/**
	 * 设置是否在结果中忽略大小写<br>
	 * 如果忽略，则在Entity中调用getXXX时，字段值忽略大小写，默认忽略
	 *
	 * @param isCaseInsensitive 是否在结果中忽略大小写
	 * @return this
	 */
	public DbConfig setCaseInsensitive(final boolean isCaseInsensitive) {
		this.caseInsensitive = isCaseInsensitive;
		return this;
	}

	/**
	 * 获取SQL过滤器
	 *
	 * @return SQL过滤器
	 */
	public SqlFilterChain getSqlFilters() {
		return this.sqlFilters;
	}

	/**
	 * 增加SQL过滤器
	 *
	 * @param filter SQL过滤器
	 * @return this
	 */
	public DbConfig addSqlFilter(final SqlFilter filter) {
		if (null == this.sqlFilters) {
			this.sqlFilters = new SqlFilterChain();
		}
		this.sqlFilters.addChain(filter);
		return this;
	}

	/**
	 * 获取自定义数据源工厂
	 *
	 * @return 数据源工厂
	 */
	public DSFactory getDsFactory() {
		return dsFactory;
	}

	/**
	 * 设置数据源工厂
	 *
	 * @param dsFactory 数据源工厂
	 * @return this
	 */
	public DbConfig setDsFactory(final DSFactory dsFactory) {
		this.dsFactory = dsFactory;
		return this;
	}

	/**
	 * 获取自定义方言
	 *
	 * @return 自定义方言
	 */
	public Dialect getDialect() {
		return dialect;
	}

	/**
	 * 设置自定义方言
	 *
	 * @param dialect 自定义方言
	 * @return this
	 */
	public DbConfig setDialect(final Dialect dialect) {
		this.dialect = dialect;
		return this;
	}
}
