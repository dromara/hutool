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

/**
 * Hutool-db是一个在JDBC基础上封装的数据库操作工具类，通过包装，使用ActiveRecord思想操作数据库。<br>
 * 在Hutool-db中，使用Entity（本质上是个Map）代替Bean来使数据库操作更加灵活，同时提供Bean和Entity的转换提供传统ORM的兼容支持。
 * <pre>{@code
 *     数据库配置文件(db.setting)
 *           | <ConfigParser> 可选配置来源
 *        DbConfig
 *           | <DSFactory>    可选连接池
 *       DataSource
 *           |
 *       Connection
 *           | <DialectRunner> 可选数据库方言
 *          Db
 *          | <Entity>
 *       RsHandler
 * }</pre>
 *
 * @author looly
 *
 */
package org.dromara.hutool.db;
