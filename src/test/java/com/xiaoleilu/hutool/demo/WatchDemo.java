package com.xiaoleilu.hutool.demo;

import java.nio.file.WatchEvent;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.watch.SimpleWatcher;
import com.xiaoleilu.hutool.watch.WatchMonitor;
import com.xiaoleilu.hutool.watch.Watcher;

/**
 * 监听Demo
 * @author Looly
 *
 */
public class WatchDemo {
	private static final Log log = LogFactory.get();
	
	public static void main(String[] args) {
		WatchMonitor monitor = WatchMonitor.create("e:/data");
		
		
		
		//方法1，阻塞当前线程
		monitor.setWatcher(new SimpleWatcher()).watch();
		//方法2，非阻塞当前线程
		monitor.setWatcher(new SimpleWatcher()).start();
		
		//方法3，阻塞当前线程
		monitor.watch(new Watcher(){
			
			@Override
			public void onOverflow(WatchEvent<?> event) {
				log.debug("[{}] {}", event.kind(), event.context());
			}
			
			@Override
			public void onModify(WatchEvent<?> event) {
				log.debug("[{}] {}", event.kind(), event.context());
				
			}
			
			@Override
			public void onDelete(WatchEvent<?> event) {
				log.debug("[{}] {}", event.kind(), event.context());
				
			}
			
			@Override
			public void onCreate(WatchEvent<?> event) {
				log.debug("[{}] {}", event.kind(), event.context());
			}
		});
	}
}
