/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
