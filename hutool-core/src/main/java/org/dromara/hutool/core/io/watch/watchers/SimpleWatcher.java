/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
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
