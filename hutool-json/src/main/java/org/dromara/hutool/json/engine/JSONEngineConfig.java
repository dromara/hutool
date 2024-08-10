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

package org.dromara.hutool.json.engine;

import java.io.Serializable;

/**
 * JSON引擎配置
 *
 * @author Looly
 * @since 6.0.0
 */
public class JSONEngineConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 是否格式化输出
	 */
	private boolean prettyPrint;

	/**
	 * 获取是否启用格式化输出
	 *
	 * @return true如果配置了格式化输出，否则返回false
	 */
	public boolean isPrettyPrint() {
		return prettyPrint;
	}

	/**
	 * 设置是否启用格式化输出
	 *
	 * @param prettyPrint 布尔值，指示是否启用格式化输出
	 * @return this
	 */
	public JSONEngineConfig setPrettyPrint(final boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
		return this;
	}
}
