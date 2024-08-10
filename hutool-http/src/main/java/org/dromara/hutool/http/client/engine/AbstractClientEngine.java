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

package org.dromara.hutool.http.client.engine;

import org.dromara.hutool.http.client.ClientConfig;

/**
 * 客户端引擎抽象类，用于保存配置和定义初始化，并提供：
 * <ul>
 *     <li>{@link #reset()}用于重置客户端</li>
 *     <li>{@link #initEngine()}初始化客户端</li>
 * </ul>
 *
 * @author Looly
 * @since 6.0.0
 */
public abstract class AbstractClientEngine implements ClientEngine{

	protected ClientConfig config;

	@Override
	public ClientEngine init(final ClientConfig config) {
		this.config = config;
		reset();
		return this;
	}

	/**
	 * 重置引擎
	 */
	protected abstract void reset();

	/**
	 * 初始化引擎，实现逻辑中如果初始化完成，不再重新初始化
	 */
	protected abstract void initEngine();
}
