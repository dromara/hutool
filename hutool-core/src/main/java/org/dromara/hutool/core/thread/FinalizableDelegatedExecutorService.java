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

package org.dromara.hutool.core.thread;

import java.util.concurrent.ExecutorService;

/**
 * 保证ExecutorService在对象回收时正常结束
 *
 * @author loolly
 */
public class FinalizableDelegatedExecutorService extends DelegatedExecutorService {

	/**
	 * 构造
	 *
	 * @param executor {@link ExecutorService}
	 */
	FinalizableDelegatedExecutorService(final ExecutorService executor) {
		super(executor);
	}

	@Override
	protected void finalize() {
		super.shutdown();
	}
}
