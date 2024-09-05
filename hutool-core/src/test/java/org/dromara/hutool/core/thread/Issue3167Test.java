/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.thread;

import org.dromara.hutool.core.lang.Console;

import java.util.concurrent.ExecutorService;

public class Issue3167Test {
	public static void main(final String[] args) {
		final ExecutorService executorService = ThreadUtil.newExecutor(2);

		for (int i = 0; i < 1035; i++) {
			final int finalI = i;
			executorService.submit(() -> {
				Console.log(Thread.currentThread().getName(), finalI);
				ThreadUtil.sleep(5000);
			});
		}
	}
}
