package com.xiaoleilu.hutool.core.io;

import java.nio.file.WatchEvent;

import com.xiaoleilu.hutool.io.watch.SimpleWatcher;
import com.xiaoleilu.hutool.io.watch.WatchMonitor;

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
			public void onCreate(WatchEvent<?> event) {
				Object obj = event.context();
				System.out.println("创建：" + obj);

			}

			@Override
			public void onModify(WatchEvent<?> event) {
				Object obj = event.context();
				System.out.println("修改：" + obj);
			}

			@Override
			public void onDelete(WatchEvent<?> event) {
				Object obj = event.context();
				System.out.println("删除：" + obj);
			}

			@Override
			public void onOverflow(WatchEvent<?> event) {
				Object obj = event.context();
				System.out.println("Overflow：" + obj);
			}
		});
		
		monitor.setMaxDepth(3);
		monitor.start();
	}
}
