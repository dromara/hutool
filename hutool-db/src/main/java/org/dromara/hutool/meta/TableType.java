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

package org.dromara.hutool.meta;

/**
 * 元信息中表的类型
 *
 * @author Looly
 */
public enum TableType {
	TABLE("TABLE"),
	VIEW("VIEW"),
	SYSTEM_TABLE("SYSTEM TABLE"),
	GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),
	LOCAL_TEMPORARY("LOCAL TEMPORARY"),
	ALIAS("ALIAS"),
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
	public String value() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.value();
	}
}
