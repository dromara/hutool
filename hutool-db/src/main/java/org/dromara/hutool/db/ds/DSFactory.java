/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
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
