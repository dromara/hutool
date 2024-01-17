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

package org.dromara.hutool.db.config;

/**
 * 数据库配置解析接口，通过实现此接口，可完成不同类型的数据源解析为数据库配置
 *
 * @author Looly
 */
public interface ConfigParser {
	/**
	 * 解析，包括数据库基本连接信息、连接属性、连接池参数和其他配置项等
	 *
	 * @param group 分组，当配置文件中有多个数据源时，通过分组区分
	 * @return {@link DbConfig}
	 */
	DbConfig parse(String group);
}
