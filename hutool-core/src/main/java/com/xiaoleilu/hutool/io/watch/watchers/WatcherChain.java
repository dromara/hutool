package com.xiaoleilu.hutool.io.watch.watchers;

import java.nio.file.WatchEvent;
import java.util.List;

import com.xiaoleilu.hutool.io.watch.Watcher;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * 观察者链<br>
 * 用于加入多个观察者
 * 
 * @author Looly
 * @since 3.1.0
 */
public class WatcherChain implements Watcher {

	/** 观察者列表 */
	final private List<Watcher> chain;
	
	/**
	 * 创建观察者链{@link WatcherChain}
	 * @param watchers  观察者列表
	 * @return {@link WatcherChain}
	 */
	public static WatcherChain create(Watcher... watchers) {
		return new WatcherChain(watchers);
	}
	
	/**
	 * 构造
	 * @param watchers 观察者列表
	 */
	public WatcherChain(Watcher... watchers) {
		chain = CollectionUtil.newArrayList(watchers);
	}
	
	/**
	 * 增加观察者
	 * @param watcher 观察者
	 * @return this
	 */
	public WatcherChain add(Watcher watcher) {
		this.chain.add(watcher);
		return this;
	}

	@Override
	public void onCreate(WatchEvent<?> event) {
		for (Watcher watcher : chain) {
			watcher.onCreate(event);
		}
	}

	@Override
	public void onModify(WatchEvent<?> event) {
		for (Watcher watcher : chain) {
			watcher.onModify(event);
		}
	}

	@Override
	public void onDelete(WatchEvent<?> event) {
		for (Watcher watcher : chain) {
			watcher.onDelete(event);
		}
	}

	@Override
	public void onOverflow(WatchEvent<?> event) {
		for (Watcher watcher : chain) {
			watcher.onOverflow(event);
		}
	}

}
