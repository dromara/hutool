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

package org.dromara.hutool.core.io.watch.watchers;

import org.dromara.hutool.core.io.watch.Watcher;

import java.io.Serializable;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

/**
 * 空白WatchListener<br>
 * 用户继承此类后实现需要监听的方法
 *
 * @author Looly
 */
public class SimpleWatcher implements Watcher, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void onCreate(final WatchEvent<?> event, final WatchKey key) {
	}

	@Override
	public void onModify(final WatchEvent<?> event, final WatchKey key) {
	}

	@Override
	public void onDelete(final WatchEvent<?> event, final WatchKey key) {
	}

	@Override
	public void onOverflow(final WatchEvent<?> event, final WatchKey key) {
	}
}
