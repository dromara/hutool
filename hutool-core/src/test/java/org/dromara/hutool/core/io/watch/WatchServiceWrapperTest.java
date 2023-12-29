package org.dromara.hutool.core.io.watch;

import org.dromara.hutool.core.io.file.PathUtil;
import org.dromara.hutool.core.io.watch.watchers.SimpleWatcher;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

public class WatchServiceWrapperTest {

	@SuppressWarnings("resource")
	@Test
	@Disabled
	void watchTest() {
		WatchServiceWrapper.of(WatchKind.ALL)
			.registerPath(PathUtil.of("d:/test"), 0)
			.watch(new TestConsoleWatcher(), null);
	}
}
