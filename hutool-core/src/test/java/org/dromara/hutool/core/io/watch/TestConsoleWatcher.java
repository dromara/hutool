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

package org.dromara.hutool.core.io.watch;

import org.dromara.hutool.core.io.watch.watchers.SimpleWatcher;
import org.dromara.hutool.core.lang.Console;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

public class TestConsoleWatcher extends SimpleWatcher {
	private static final long serialVersionUID = 1L;

	@Override
	public void onCreate(final WatchEvent<?> event, final WatchKey key) {
		Console.log("创建：{}-> {}", key.watchable(), event.context());
		Console.log("Resolved Path：{}", WatchUtil.resolvePath(event, key));
	}

	@Override
	public void onModify(final WatchEvent<?> event, final WatchKey key) {
		Console.log("修改：{}-> {}", key.watchable(), event.context());
		Console.log("Resolved Path：{}", WatchUtil.resolvePath(event, key));
	}

	@Override
	public void onDelete(final WatchEvent<?> event, final WatchKey key) {
		Console.log("删除：{}-> {}", key.watchable(), event.context());
		Console.log("Resolved Path：{}", WatchUtil.resolvePath(event, key));
	}

	@Override
	public void onOverflow(final WatchEvent<?> event, final WatchKey key) {
		Console.log("Overflow：{}-> {}", key.watchable(), event.context());
		Console.log("Resolved Path：{}", WatchUtil.resolvePath(event, key));
	}
}
