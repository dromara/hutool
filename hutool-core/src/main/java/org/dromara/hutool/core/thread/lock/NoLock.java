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

package org.dromara.hutool.core.thread.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 无锁实现
 *
 * @author looly
 *@since 4.3.1
 */
public class NoLock implements Lock{

	public static NoLock INSTANCE = new NoLock();

	@Override
	public void lock() {
	}

	@Override
	public void lockInterruptibly() {
	}

	@Override
	public boolean tryLock() {
		return true;
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public boolean tryLock(final long time, final TimeUnit unit) {
		return true;
	}

	@Override
	public void unlock() {
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public Condition newCondition() {
		throw new UnsupportedOperationException("NoLock`s newCondition method is unsupported");
	}

}
