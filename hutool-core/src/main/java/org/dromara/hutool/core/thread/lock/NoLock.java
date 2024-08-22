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

	/**
	 * 单例
	 */
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
