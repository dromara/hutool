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

package org.dromara.hutool.core.func;


import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.exception.HutoolException;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * 可序列化的Runnable
 *
 * @author VampireAchao
 * @see Runnable
 */
@FunctionalInterface
public interface SerRunnable extends Runnable, Serializable {

	/**
	 * When an object implementing interface {@code Runnable} is used
	 * to create a thread, starting the thread causes the object's
	 * {@code run} method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method {@code run} is that it may
	 * take any action whatsoever.
	 *
	 * @throws Exception wrapped checked exception
	 * @see Thread#run()
	 */
	void running() throws Exception;

	/**
	 * When an object implementing interface {@code Runnable} is used
	 * to create a thread, starting the thread causes the object's
	 * {@code run} method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method {@code run} is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	default void run() {
		try {
			running();
		} catch (final Exception e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	/**
	 * multi
	 *
	 * @param serRunnableArray lambda
	 * @return lambda
	 */
	static SerRunnable multi(final SerRunnable... serRunnableArray) {
		return () -> Stream.of(serRunnableArray).forEach(SerRunnable::run);
	}

}
