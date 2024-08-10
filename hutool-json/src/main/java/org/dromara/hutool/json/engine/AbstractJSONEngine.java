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

/**
 * JSONEngine抽象实现，
 */
public abstract class AbstractJSONEngine implements JSONEngine {

	/**
	 * JSON引擎配置，{@code null}表示默认配置
	 */
	protected JSONEngineConfig config;

	@Override
	public JSONEngine init(final JSONEngineConfig config) {
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
