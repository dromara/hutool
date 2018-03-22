package com.xiaoleilu.hutool.io.watch.watchers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.util.Set;

import com.xiaoleilu.hutool.collection.ConcurrentHashSet;
import com.xiaoleilu.hutool.io.watch.Watcher;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.thread.ThreadUtil;

/**
 * 延迟观察者<br>
 * 使用此观察者通过定义一定的延迟时间，解决{@link WatchService}多个modify的问题<br>
 * 在监听目录或文件时，如果这个文件有修改操作，会多次触发modify方法。<br>
 * 此类通过维护一个Set将短时间内相同文件多次modify的事件合并处理触发，从而避免以上问题。
 * 
 * @author Looly
 * @since 3.1.0
 */
public class DelayWatcher implements Watcher {
	
	/** Path集合。此集合用于去重在指定delay内多次触发的文件Path */
	private final Set<Path> eventSet = new ConcurrentHashSet<>();
	/** 实际处理 */
	private final Watcher watcher;
	/** 延迟，单位毫秒 */
	private final long delay;
	
	//---------------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param watcher 实际处理触发事件的监视器{@link Watcher}，不可以是{@link DelayWatcher}
	 * @param delay 延迟时间，单位毫秒
	 */
	public DelayWatcher(Watcher watcher, long delay) {
		Assert.notNull(watcher);
		if(watcher instanceof DelayWatcher) {
			throw new IllegalArgumentException("Watcher must not be a DelayWatcher");
		}
		this.watcher = watcher;
		this.delay = delay;
	}
	//---------------------------------------------------------------------------------------------------------- Constructor end
	
	@Override
	public void onModify(WatchEvent<?> event, Path currentPath) {
		if(this.delay < 1) {
			this.watcher.onModify(event, currentPath);
		}else {
			onDelayModify(event, currentPath);
		}
	}
	
	@Override
	public void onCreate(WatchEvent<?> event, Path currentPath) {
		watcher.onCreate(event, currentPath);
	}
	
	@Override
	public void onDelete(WatchEvent<?> event, Path currentPath) {
		watcher.onDelete(event, currentPath);
	}
	
	@Override
	public void onOverflow(WatchEvent<?> event, Path currentPath) {
		watcher.onOverflow(event, currentPath);
	}
	
	//---------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 触发延迟修改
	 * @param event 事件
	 * @param currentPath 事件发生的当前Path路径
	 */
	private void onDelayModify(WatchEvent<?> event, Path currentPath) {
		Path eventPath = Paths.get(currentPath.toString(), event.context().toString());
		if(eventSet.contains(eventPath)) {
			//此事件已经被触发过，后续事件忽略，等待统一处理。
			return;
		}
		
		//事件第一次触发，此时标记事件，并启动处理线程延迟处理，处理结束后会删除标记
		eventSet.add(eventPath);
		startHandleModifyThread(event, currentPath);
	}
	
	/**
	 * 开启处理线程
	 * 
	 * @param event 事件
	 * @param currentPath 事件发生的当前Path路径
	 */
	private void startHandleModifyThread(final WatchEvent<?> event, final Path currentPath) {
		ThreadUtil.execute(new Runnable(){
			@Override
			public void run() {
				ThreadUtil.sleep(delay);
				eventSet.remove(Paths.get(currentPath.toString(), event.context().toString()));
				watcher.onModify(event, currentPath);
			}
		});
	}
	//---------------------------------------------------------------------------------------------------------- Private method end
}
