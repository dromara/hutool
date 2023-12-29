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

package org.dromara.hutool.core.io.watch.watchers;

import org.dromara.hutool.core.io.watch.Watcher;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

/**
 * 跳过所有事件处理Watcher<br>
 * 用户继承此类后实现需要监听的方法
 *
 * @author Looly
 * @since 3.1.0
 */
public class IgnoreWatcher implements Watcher {

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
