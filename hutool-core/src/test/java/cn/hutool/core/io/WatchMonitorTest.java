package cn.hutool.core.io;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.io.watch.watchers.DelayWatcher;
import cn.hutool.core.lang.Console;

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
			public void onCreate(final WatchEvent<?> event, final Path currentPath) {
				final Object obj = event.context();
				Console.log("创建：{}-> {}", currentPath, obj);
			}

			@Override
			public void onModify(final WatchEvent<?> event, final Path currentPath) {
				final Object obj = event.context();
				Console.log("修改：{}-> {}", currentPath, obj);
			}

			@Override
			public void onDelete(final WatchEvent<?> event, final Path currentPath) {
				final Object obj = event.context();
				Console.log("删除：{}-> {}", currentPath, obj);
			}

			@Override
			public void onOverflow(final WatchEvent<?> event, final Path currentPath) {
				final Object obj = event.context();
				Console.log("Overflow：{}-> {}", currentPath, obj);
			}
		};

		final WatchMonitor monitor = WatchMonitor.createAll("d:/test/aaa.txt", new DelayWatcher(watcher, 500));

		monitor.setMaxDepth(0);
		monitor.start();
	}


}
