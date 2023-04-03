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

package org.dromara.hutool.db.ds;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.setting.Setting;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.Serializable;

/**
 * 多数据源工厂方法接口，借助不同配置，同一个工厂可以连接多个相同或不同的数据库，但是连接池只能使用一种。<br>
 * 通过实现{@link #getDataSource(String)} 方法完成数据源的获取。<br>
 * 如果{@link DataSource} 的实现是数据库连接池库，应该在getDataSource调用时创建数据源并缓存，关系如下：
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
 * <p>
 *     工厂创建请使用{@link DSUtil#createFactory(Setting)}
 * </p>
 *
 * @author Looly
 */
public interface DSFactory extends Closeable, Serializable {

	/**
	 * 获取自定义的数据源名称，用于识别连接池
	 *
	 * @return 自定义的数据源名称
	 */
	String getDataSourceName();

	/**
	 * 获得默认数据源，即""分组的数据源
	 *
	 * @return 数据源
	 */
	default DataSource getDataSource() {
		return getDataSource(StrUtil.EMPTY);
	}

	/**
	 * 获得分组对应数据源
	 *
	 * @param group 分组名
	 * @return 数据源
	 */
	DataSource getDataSource(String group);

	/**
	 * 关闭默认数据源（空组）
	 */
	default void closeDataSource() {
		closeDataSource(StrUtil.EMPTY);
	}

	/**
	 * 关闭(归还)对应数据源
	 *
	 * @param group 分组
	 */
	void closeDataSource(String group);
}
