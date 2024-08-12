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

package org.dromara.hutool.core.io.watch;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

/**
 * 观察者（监视器）
 *
 * @author Looly
 */
public interface Watcher {
	/**
	 * 文件创建时执行的方法
	 *
	 * @param event 事件，可通过{@link WatchEvent#context()}获取创建的文件或目录名称
	 * @param key   事件发生的{@link WatchKey}，可以通过{@link WatchKey#watchable()}获取监听的Path路径
	 */
	void onCreate(WatchEvent<?> event, WatchKey key);

	/**
	 * 文件修改时执行的方法<br>
	 * 文件修改可能触发多次
	 *
	 * @param event 事件，可通过{@link WatchEvent#context()}获取创建的文件或目录名称
	 * @param key   事件发生的{@link WatchKey}，可以通过{@link WatchKey#watchable()}获取监听的Path路径
	 */
	void onModify(WatchEvent<?> event, WatchKey key);

	/**
	 * 文件删除时执行的方法
	 *
	 * @param event 事件，可通过{@link WatchEvent#context()}获取创建的文件或目录名称
	 * @param key   事件发生的{@link WatchKey}，可以通过{@link WatchKey#watchable()}获取监听的Path路径
	 */
	void onDelete(WatchEvent<?> event, WatchKey key);

	/**
	 * 事件丢失或出错时执行的方法
	 *
	 * @param event 事件，可通过{@link WatchEvent#context()}获取创建的文件或目录名称
	 * @param key   事件发生的{@link WatchKey}，可以通过{@link WatchKey#watchable()}获取监听的Path路径
	 */
	void onOverflow(WatchEvent<?> event, WatchKey key);
}
