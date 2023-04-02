/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.io.watch;

import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.io.file.PathUtil;
import org.dromara.hutool.io.watch.watchers.WatcherChain;
import org.dromara.hutool.net.url.URLUtil;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.util.CharUtil;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;

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
public class WatchMonitor extends WatchServer {
	private static final long serialVersionUID = 1L;

	/**
	 * 监听路径，必须为目录
	 */
	private Path path;
	/**
	 * 递归目录的最大深度，当小于1时不递归下层目录
	 */
	private int maxDepth;
	/**
	 * 监听的文件，对于单文件监听不为空
	 */
	private Path filePath;

	/**
	 * 监听器
	 */
	private Watcher watcher;
	//------------------------------------------------------ Static method start

	/**
	 * 创建并初始化监听
	 *
	 * @param url    URL
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor of(final URL url, final WatchEvent.Kind<?>... events) {
		return of(url, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param url      URL
	 * @param events   监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor of(final URL url, final int maxDepth, final WatchEvent.Kind<?>... events) {
		return of(URLUtil.toURI(url), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param uri    URI
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor of(final URI uri, final WatchEvent.Kind<?>... events) {
		return of(uri, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param uri      URI
	 * @param events   监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor of(final URI uri, final int maxDepth, final WatchEvent.Kind<?>... events) {
		return of(Paths.get(uri), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param file   文件
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor of(final File file, final WatchEvent.Kind<?>... events) {
		return of(file, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param file     文件
	 * @param events   监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor of(final File file, final int maxDepth, final WatchEvent.Kind<?>... events) {
		return of(file.toPath(), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path   路径
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor of(final String path, final WatchEvent.Kind<?>... events) {
		return of(path, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path     路径
	 * @param events   监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor of(final String path, final int maxDepth, final WatchEvent.Kind<?>... events) {
		return of(Paths.get(path), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path   路径
	 * @param events 监听事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor of(final Path path, final WatchEvent.Kind<?>... events) {
		return of(path, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path     路径
	 * @param events   监听事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor of(final Path path, final int maxDepth, final WatchEvent.Kind<?>... events) {
		return new WatchMonitor(path, maxDepth, events);
	}

	//--------- createAll

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param uri     URI
	 * @param watcher {@link Watcher}
	 * @return WatchMonitor
	 */
	public static WatchMonitor ofAll(final URI uri, final Watcher watcher) {
		return ofAll(Paths.get(uri), watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param url     URL
	 * @param watcher {@link Watcher}
	 * @return WatchMonitor
	 */
	public static WatchMonitor ofAll(final URL url, final Watcher watcher) {
		try {
			return ofAll(Paths.get(url.toURI()), watcher);
		} catch (final URISyntaxException e) {
			throw new WatchException(e);
		}
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param file    被监听文件
	 * @param watcher {@link Watcher}
	 * @return WatchMonitor
	 */
	public static WatchMonitor ofAll(final File file, final Watcher watcher) {
		return ofAll(file.toPath(), watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param path    路径
	 * @param watcher {@link Watcher}
	 * @return WatchMonitor
	 */
	public static WatchMonitor ofAll(final String path, final Watcher watcher) {
		return ofAll(Paths.get(path), watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param path    路径
	 * @param watcher {@link Watcher}
	 * @return WatchMonitor
	 */
	public static WatchMonitor ofAll(final Path path, final Watcher watcher) {
		final WatchMonitor watchMonitor = of(path, WatchKind.ALL);
		watchMonitor.setWatcher(watcher);
		return watchMonitor;
	}
	//------------------------------------------------------ Static method end

	//------------------------------------------------------ Constructor method start

	/**
	 * 构造
	 *
	 * @param file   文件
	 * @param events 监听的事件列表
	 */
	public WatchMonitor(final File file, final WatchEvent.Kind<?>... events) {
		this(file.toPath(), events);
	}

	/**
	 * 构造
	 *
	 * @param path   字符串路径
	 * @param events 监听的事件列表
	 */
	public WatchMonitor(final String path, final WatchEvent.Kind<?>... events) {
		this(Paths.get(path), events);
	}

	/**
	 * 构造
	 *
	 * @param path   字符串路径
	 * @param events 监听事件列表
	 */
	public WatchMonitor(final Path path, final WatchEvent.Kind<?>... events) {
		this(path, 0, events);
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
	 * @param path     字符串路径
	 * @param maxDepth 递归目录的最大深度，当小于2时不递归下层目录
	 * @param events   监听事件列表
	 */
	public WatchMonitor(final Path path, final int maxDepth, final WatchEvent.Kind<?>... events) {
		this.path = path;
		this.maxDepth = maxDepth;
		this.events = events;
		this.init();
	}
	//------------------------------------------------------ Constructor method end

	/**
	 * 初始化<br>
	 * 初始化包括：
	 * <pre>
	 * 1、解析传入的路径，判断其为目录还是文件
	 * 2、创建{@link WatchService} 对象
	 * </pre>
	 *
	 * @throws WatchException 监听异常，IO异常时抛出此异常
	 */
	@Override
	public void init() throws WatchException {
		//获取目录或文件路径
		if (false == PathUtil.exists(this.path, false)) {
			// 不存在的路径
			final Path lastPathEle = FileUtil.getLastPathEle(this.path);
			if (null != lastPathEle) {
				final String lastPathEleStr = lastPathEle.toString();
				//带有点表示有扩展名，按照未创建的文件对待。Linux下.d的为目录，排除之
				if (StrUtil.contains(lastPathEleStr, CharUtil.DOT) && false == StrUtil.endWithIgnoreCase(lastPathEleStr, ".d")) {
					this.filePath = this.path;
					this.path = this.filePath.getParent();
				}
			}

			//创建不存在的目录或父目录
			PathUtil.mkdir(this.path);
		} else if (PathUtil.isFile(this.path, false)) {
			// 文件路径
			this.filePath = this.path;
			this.path = this.filePath.getParent();
		}

		super.init();
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
		if (isClosed) {
			throw new WatchException("Watch Monitor is closed !");
		}

		// 按照层级注册路径及其子路径
		registerPath();
//		log.debug("Start watching path: [{}]", this.path);

		while (false == isClosed) {
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

	//------------------------------------------------------ private method start

	/**
	 * 执行事件获取并处理
	 *
	 * @param watcher {@link Watcher}
	 */
	private void doTakeAndWatch(final Watcher watcher) {
		super.watch(watcher, watchEvent -> null == filePath || filePath.endsWith(watchEvent.context().toString()));
	}

	/**
	 * 注册监听路径
	 */
	private void registerPath() {
		registerPath(this.path, (null != this.filePath) ? 0 : this.maxDepth);
	}
	//------------------------------------------------------ private method end
}
