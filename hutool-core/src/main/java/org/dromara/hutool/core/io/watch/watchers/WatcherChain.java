/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io.watch.watchers;

import org.dromara.hutool.core.io.watch.Watcher;
import org.dromara.hutool.core.lang.Chain;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 观察者链<br>
 * 用于加入多个观察者
 *
 * @author Looly
 * @since 3.1.0
 */
public class WatcherChain implements Watcher, Chain<Watcher, WatcherChain> {

	/** 观察者列表 */
	final private List<Watcher> chain;

	/**
	 * 创建观察者链{@code WatcherChain}
	 * @param watchers  观察者列表
	 * @return {@code WatcherChain}
	 */
	public static WatcherChain of(final Watcher... watchers) {
		return new WatcherChain(watchers);
	}

	/**
	 * 构造
	 * @param watchers 观察者列表
	 */
	public WatcherChain(final Watcher... watchers) {
		chain = Arrays.asList(watchers);
	}

	@Override
	public void onCreate(final WatchEvent<?> event, final WatchKey key) {
		for (final Watcher watcher : chain) {
			watcher.onCreate(event, key);
		}
	}

	@Override
	public void onModify(final WatchEvent<?> event, final WatchKey key) {
		for (final Watcher watcher : chain) {
			watcher.onModify(event, key);
		}
	}

	@Override
	public void onDelete(final WatchEvent<?> event, final WatchKey key) {
		for (final Watcher watcher : chain) {
			watcher.onDelete(event, key);
		}
	}

	@Override
	public void onOverflow(final WatchEvent<?> event, final WatchKey key) {
		for (final Watcher watcher : chain) {
			watcher.onOverflow(event, key);
		}
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public Iterator<Watcher> iterator() {
		return this.chain.iterator();
	}

	@Override
	public WatcherChain addChain(final Watcher element) {
		this.chain.add(element);
		return this;
	}

}
