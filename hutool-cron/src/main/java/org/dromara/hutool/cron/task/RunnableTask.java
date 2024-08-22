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

package org.dromara.hutool.cron.task;

/**
 * {@link Runnable} 的 {@link Task}包装
 *
 * @author Looly
 */
public class RunnableTask implements Task {
	private final Runnable runnable;

	/**
	 * 构造
	 *
	 * @param runnable {@link Runnable}
	 */
	public RunnableTask(final Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void execute() {
		runnable.run();
	}
}
