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

package org.dromara.hutool.db.meta;

/**
 * 元信息中表的类型
 *
 * @author Looly
 */
public enum TableType {
	/**
	 * 数据库表
	 */
	TABLE("TABLE"),
	/**
	 * 视图
	 */
	VIEW("VIEW"),
	/**
	 * 系统表
	 */
	SYSTEM_TABLE("SYSTEM TABLE"),
	/**
	 * 全局临时表
	 */
	GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),
	/**
	 * 本地临时表
	 */
	LOCAL_TEMPORARY("LOCAL TEMPORARY"),
	/**
	 * 别名
	 */
	ALIAS("ALIAS"),
	/**
	 * 快捷方式
	 */
	SYNONYM("SYNONYM");

	private final String value;

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	TableType(final String value) {
		this.value = value;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.getValue();
	}
}
