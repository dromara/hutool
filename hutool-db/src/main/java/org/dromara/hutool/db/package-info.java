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
