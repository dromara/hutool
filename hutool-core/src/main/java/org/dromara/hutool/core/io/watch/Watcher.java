/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
