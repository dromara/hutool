package com.xiaoleilu.hutool.core.io;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

import com.xiaoleilu.hutool.io.watch.SimpleWatcher;
import com.xiaoleilu.hutool.io.watch.WatchMonitor;
import com.xiaoleilu.hutool.lang.Console;

/**
 * 文件监听单元测试
 * 
 * @author Looly
 *
 */
public class WatchMonitorTest {

	public static void main(String[] args) {
		WatchMonitor monitor = WatchMonitor.createAll("d:/", new SimpleWatcher(){
			@Override
			public void onCreate(WatchEvent<?> event, Path currentPath) {
				Object obj = event.context();
				Console.log("创建：{}-> {}", currentPath, obj);
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
		});
		
		monitor.setMaxDepth(3);
		monitor.start();
	}
}
