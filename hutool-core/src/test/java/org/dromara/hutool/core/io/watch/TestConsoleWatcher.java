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
