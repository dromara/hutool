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

import org.dromara.hutool.core.io.file.PathUtil;
import org.dromara.hutool.core.io.watch.watchers.DelayWatcher;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

/**
 * 文件监听单元测试
 *
 * @author Looly
 */
public class WatchMonitorTest {

	@Test
	@Disabled
	void watchTest() {
		final Path path = PathUtil.of("d:/test/");
		Console.log("监听：{}", path);

		//noinspection resource
		final WatchMonitor monitor = WatchUtil.ofAll(path, new DelayWatcher(new TestConsoleWatcher(), 500));
		monitor.setMaxDepth(0);
		monitor.start();
	}
}
