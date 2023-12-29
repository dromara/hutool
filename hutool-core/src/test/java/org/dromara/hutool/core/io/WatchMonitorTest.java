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

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.io.watch.WatchMonitor;
import org.dromara.hutool.core.io.watch.WatchUtil;
import org.dromara.hutool.core.io.watch.Watcher;
import org.dromara.hutool.core.io.watch.watchers.DelayWatcher;
import org.dromara.hutool.core.io.watch.watchers.SimpleWatcher;
import org.dromara.hutool.core.lang.Console;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

/**
 * 文件监听单元测试
 *
 * @author Looly
 *
 */
public class WatchMonitorTest {

	public static void main(final String[] args) {
		final Watcher watcher = new SimpleWatcher(){
			@Override
			public void onCreate(final WatchEvent<?> event, final WatchKey key) {
				final Object obj = event.context();
				Console.log(((Path)obj).toAbsolutePath());
				Console.log("创建：{}-> {}", key.watchable(), obj);
			}

			@Override
			public void onModify(final WatchEvent<?> event, final WatchKey key) {
				final Object obj = event.context();
				Console.log("修改：{}-> {}", key.watchable(), obj);
			}

			@Override
			public void onDelete(final WatchEvent<?> event, final WatchKey key) {
				final Object obj = event.context();
				Console.log("删除：{}-> {}", key.watchable(), obj);
			}

			@Override
			public void onOverflow(final WatchEvent<?> event, final WatchKey key) {
				final Object obj = event.context();
				Console.log("Overflow：{}-> {}", key.watchable(), obj);
			}
		};

		//noinspection resource
		final WatchMonitor monitor = WatchUtil.ofAll("d:/test/aaa.txt", new DelayWatcher(watcher, 500));

		monitor.setMaxDepth(0);
		monitor.start();
	}


}
