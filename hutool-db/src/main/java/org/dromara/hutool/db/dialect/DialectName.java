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

package org.dromara.hutool.db.dialect;

import org.dromara.hutool.core.text.StrUtil;

/**
 * 方言名<br>
 * 方言枚举列出了Hutool支持的所有数据库方言
 *
 * @author Looly
 */
public enum DialectName {
	ANSI, MYSQL, ORACLE, POSTGRESQL, SQLITE3, H2, SQLSERVER, SQLSERVER2012, PHOENIX, DM;

	/**
	 * 是否为指定数据库方言，检查时不分区大小写
	 *
	 * @param dialectName     方言名
	 * @return 是否时Oracle数据库
	 * @since 5.7.2
	 */
	public boolean match(final String dialectName) {
		return StrUtil.equalsIgnoreCase(dialectName, name());
	}
}
