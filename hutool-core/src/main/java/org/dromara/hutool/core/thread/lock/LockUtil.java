/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * 锁相关工具
 *
 * @author looly
 * @since 5.2.5
 */
public class LockUtil {

	private static final NoLock NO_LOCK = new NoLock();

	/**
	 * 创建{@link StampedLock}锁
	 *
	 * @return {@link StampedLock}锁
	 */
	public static StampedLock createStampLock() {
		return new StampedLock();
	}

	/**
	 * 创建{@link ReentrantReadWriteLock}锁
	 *
	 * @param fair 是否公平锁
	 * @return {@link ReentrantReadWriteLock}锁
	 */
	public static ReentrantReadWriteLock createReadWriteLock(final boolean fair) {
		return new ReentrantReadWriteLock(fair);
	}

	/**
	 * 获取单例的无锁对象
	 *
	 * @return {@link NoLock}
	 */
	public static NoLock getNoLock(){
		return NO_LOCK;
	}
}
