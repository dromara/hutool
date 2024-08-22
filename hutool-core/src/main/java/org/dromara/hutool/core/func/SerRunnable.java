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

package org.dromara.hutool.core.func;


import org.dromara.hutool.core.exception.ExceptionUtil;

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
	void running() throws Throwable;

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
		} catch (final Throwable e) {
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
