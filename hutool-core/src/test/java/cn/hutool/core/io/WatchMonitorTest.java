package cn.hutool.core.io;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.Watchable;

import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.io.watch.watchers.DelayWatcher;
import cn.hutool.core.lang.Console;
import org.junit.Test;

/**
 * 文件监听单元测试
 *
 * @author Looly
 */
public class WatchMonitorTest {
	WatchMonitor monitor;
	Watcher watcher = new SimpleWatcher() {
		@Override
		public void onCreate(WatchEvent<?> event, Path currentPath) {
			Object obj = event.context();
			Console.log("创建：{}-> {}", currentPath, obj);
			WatchKey watchKey = monitor.getWatchKey(currentPath);
			Path watchable = (Path) watchKey.watchable();
			Path fullPath = watchable.resolve((Path) event.context());
			Console.log("Path 完整对象：{}", fullPath);
		}

		@Override
		public void onModify(WatchEvent<?> event, Path currentPath) {
			Object obj = event.context();
			Console.log("修改：{}-> {}", currentPath, obj);
		}

		@Override
		public void onDelete(WatchEvent<?> event, Path currentPath) {
			Object obj = event.context();
			Console.log("删除：{}-> {}", currentPath, obj);
		}

		@Override
		public void onOverflow(WatchEvent<?> event, Path currentPath) {
			Object obj = event.context();
			Console.log("Overflow：{}-> {}", currentPath, obj);
		}
	};


	@Test
	public void testFile() {

		monitor = WatchMonitor.createAll("d:/test/aaa.txt", new DelayWatcher(watcher, 500));

		monitor.setMaxDepth(0);
		monitor.start();
	}

	@Test
	public void testDir() {
		monitor = WatchMonitor.createAll("d:/", new DelayWatcher(watcher, 500));

		monitor.run();
	}

}
