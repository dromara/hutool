package com.xiaoleilu.hutool.demo;

import java.nio.file.WatchEvent;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.watch.WatchListener;
import com.xiaoleilu.hutool.watch.WatchMonitor;

/**
 * 监听Demo
 * @author Looly
 *
 */
public class WatchDemo {
	private static final Log log = LogFactory.get();
	
	public static void main(String[] args) {
		WatchMonitor monitor = WatchMonitor.create("e:/data");
		monitor.start(new WatchListener(){
			
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
