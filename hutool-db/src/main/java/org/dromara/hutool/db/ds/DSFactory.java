/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.db.ds;

import org.dromara.hutool.db.config.ConnectionConfig;

import javax.sql.DataSource;
import java.io.Serializable;

/**
 * 多数据源{@link DataSource}工厂方法接口，借助不同配置，同一个工厂可以连接多个相同或不同的数据库，但是连接池只能使用一种。<br>
 * 通过实现{@link #createDataSource(ConnectionConfig)} 方法完成数据源的创建。关系如下：<br>
 * <pre>
 *                            DSFactory
 *            _____________________|____________________
 *            |              |             |           |
 *     HikariDSFactory DruidDSFactory XXXDSFactory    ...
 *       _____|____          |        _____|____
 *       |        |          |        |        |
 *     MySQL    SQLite    SQLServer  XXXDB   XXXDB2
 * </pre>
 *
 * @author Looly
 */
public interface DSFactory extends Serializable {

	/**
	 * 获取自定义的数据源名称，用于识别连接池
	 *
	 * @return 自定义的数据源名称
	 */
	String getDataSourceName();

	/**
	 * 创建数据源
	 *
	 * @param config 数据库配置
	 * @return {@link DataSource}
	 */
	DataSource createDataSource(ConnectionConfig<?> config);
}
