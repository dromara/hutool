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

package org.dromara.hutool.core.io.watch;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.file.PathUtil;
import org.dromara.hutool.core.io.watch.watchers.WatcherChain;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.Closeable;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 路径监听器
 *
 * <p>
 * 监听器可监听目录或文件<br>
 * 如果监听的Path不存在，则递归创建空目录然后监听此空目录<br>
 * 递归监听目录时，并不会监听新创建的目录
 *
 * @author Looly
 */
public class WatchMonitor extends Thread implements Closeable, Serializable {
	private static final long serialVersionUID = 1L;

	private final WatchServiceWrapper watchService;

	/**
	 * 监听路径，必须为目录
	 */
	private Path dir;
	/**
	 * 监听的文件，对于单文件监听不为空
	 */
	private Path file;

	/**
	 * 递归目录的最大深度，当小于1时不递归下层目录
	 */
	private int maxDepth;
	/**
	 * 监听器
	 */
	private Watcher watcher;

	/**
	 * 构造
	 *
	 * @param dir    字符串路径
	 * @param events 监听事件列表，如创建、修改和删除等
	 */
	public WatchMonitor(final Path dir, final WatchEvent.Kind<?>... events) {
		this(dir, 0, events);
	}

	/**
	 * 构造<br>
	 * 例如设置：
	 * <pre>
	 * maxDepth &lt;= 1 表示只监听当前目录
	 * maxDepth = 2 表示监听当前目录以及下层目录
	 * maxDepth = 3 表示监听当前目录以及下两层
	 * </pre>
	 *
	 * @param dir      路径
	 * @param maxDepth 递归目录的最大深度，当小于2时不递归下层目录
	 * @param events   监听事件列表，如创建、修改和删除等
	 */
	public WatchMonitor(final Path dir, final int maxDepth, final WatchEvent.Kind<?>... events) {
		this.watchService = WatchServiceWrapper.of(events);
		this.dir = dir;
		this.maxDepth = maxDepth;
		this.init();
	}

	/**
	 * 设置监听<br>
	 * 多个监听请使用{@link WatcherChain}
	 *
	 * @param watcher 监听
	 * @return WatchMonitor
	 */
	public WatchMonitor setWatcher(final Watcher watcher) {
		this.watcher = watcher;
		return this;
	}

	@Override
	public void run() {
		watch();
	}

	/**
	 * 开始监听事件，阻塞当前进程
	 */
	public void watch() {
		watch(this.watcher);
	}

	/**
	 * 开始监听事件，阻塞当前进程
	 *
	 * @param watcher 监听
	 * @throws WatchException 监听异常，如果监听关闭抛出此异常
	 */
	public void watch(final Watcher watcher) throws WatchException {
		if (this.watchService.isClosed()) {
			throw new WatchException("Watch Monitor is closed !");
		}

		// 按照层级注册路径及其子路径
		registerPath();
//		log.debug("Start watching path: [{}]", this.path);

		while (!this.watchService.isClosed()) {
			doTakeAndWatch(watcher);
		}
	}

	/**
	 * 当监听目录时，监听目录的最大深度<br>
	 * 当设置值为1（或小于1）时，表示不递归监听子目录<br>
	 * 例如设置：
	 * <pre>
	 * maxDepth &lt;= 1 表示只监听当前目录
	 * maxDepth = 2 表示监听当前目录以及下层目录
	 * maxDepth = 3 表示监听当前目录以及下层
	 * </pre>
	 *
	 * @param maxDepth 最大深度，当设置值为1（或小于1）时，表示不递归监听子目录，监听所有子目录请传{@link Integer#MAX_VALUE}
	 * @return this
	 */
	public WatchMonitor setMaxDepth(final int maxDepth) {
		this.maxDepth = maxDepth;
		return this;
	}

	@Override
	public void close() {
		this.watchService.close();
	}

	//------------------------------------------------------ private method start

	/**
	 * 初始化<br>
	 * 初始化包括：
	 * <pre>
	 * 1、解析传入的路径，判断其为目录还是文件
	 * </pre>
	 *
	 * @throws WatchException 监听异常，IO异常时抛出此异常
	 */
	private void init() throws WatchException {
		//获取目录或文件路径
		if (!PathUtil.exists(this.dir, false)) {
			// 不存在的路径
			final Path lastPathEle = FileUtil.getLastPathEle(this.dir);
			if (null != lastPathEle) {
				final String lastPathEleStr = lastPathEle.toString();
				//带有点表示有扩展名，按照未创建的文件对待。Linux下.d的为目录，排除之
				if (StrUtil.contains(lastPathEleStr, CharUtil.DOT) && !StrUtil.endWithIgnoreCase(lastPathEleStr, ".d")) {
					this.file = this.dir;
					this.dir = this.file.getParent();
				}
			}

			//创建不存在的目录或父目录
			PathUtil.mkdir(this.dir);
		} else if (PathUtil.isFile(this.dir, false)) {
			// 文件路径
			this.file = this.dir;
			this.dir = this.file.getParent();
		}
	}

	/**
	 * 执行事件获取并处理
	 *
	 * @param watcher {@link Watcher}
	 */
	private void doTakeAndWatch(final Watcher watcher) {
		this.watchService.watch(watcher,
			// 对于文件监听，忽略目录下其他文件和目录的事件
			watchEvent -> null == file || file.endsWith(watchEvent.context().toString()));
	}

	/**
	 * 注册监听路径
	 */
	private void registerPath() {
		this.watchService.registerPath(this.dir, (null != this.file) ? 0 : this.maxDepth);
	}
	//------------------------------------------------------ private method end
}
