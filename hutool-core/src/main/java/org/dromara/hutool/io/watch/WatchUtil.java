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

import org.dromara.hutool.io.IORuntimeException;
import org.dromara.hutool.net.url.URLUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;

/**
 * 监听工具类<br>
 * 主要负责文件监听器的快捷创建
 *
 * @author Looly
 * @since 3.1.0
 */
public class WatchUtil {
	/**
	 * 创建并初始化监听
	 *
	 * @param url URL
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor of(final URL url, final WatchEvent.Kind<?>... events) {
		return of(url, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param url URL
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor of(final URL url, final int maxDepth, final WatchEvent.Kind<?>... events) {
		return of(URLUtil.toURI(url), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param uri URI
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor of(final URI uri, final WatchEvent.Kind<?>... events) {
		return of(uri, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param uri URI
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor of(final URI uri, final int maxDepth, final WatchEvent.Kind<?>... events) {
		return of(Paths.get(uri), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param file 文件
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor of(final File file, final WatchEvent.Kind<?>... events) {
		return of(file, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param file 文件
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor of(final File file, final int maxDepth, final WatchEvent.Kind<?>... events) {
		return of(file.toPath(), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path 路径
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor of(final String path, final WatchEvent.Kind<?>... events) {
		return of(path, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path 路径
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor of(final String path, final int maxDepth, final WatchEvent.Kind<?>... events) {
		return of(Paths.get(path), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path 路径
	 * @param events 监听事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor of(final Path path, final WatchEvent.Kind<?>... events) {
		return of(path, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path 路径
	 * @param events 监听事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor of(final Path path, final int maxDepth, final WatchEvent.Kind<?>... events) {
		return new WatchMonitor(path, maxDepth, events);
	}

	// ---------------------------------------------------------------------------------------------------------- createAll
	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param url URL
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor ofAll(final URL url, final Watcher watcher) {
		return ofAll(url, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param url URL
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor ofAll(final URL url, final int maxDepth, final Watcher watcher) {
		return ofAll(URLUtil.toURI(url), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param uri URI
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor ofAll(final URI uri, final Watcher watcher) {
		return ofAll(uri, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param uri URI
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor ofAll(final URI uri, final int maxDepth, final Watcher watcher) {
		return ofAll(Paths.get(uri), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param file 被监听文件
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor ofAll(final File file, final Watcher watcher) {
		return ofAll(file, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param file 被监听文件
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor ofAll(final File file, final int maxDepth, final Watcher watcher) {
		return ofAll(file.toPath(), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param path 路径
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor ofAll(final String path, final Watcher watcher) {
		return ofAll(path, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param path 路径
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor ofAll(final String path, final int maxDepth, final Watcher watcher) {
		return ofAll(Paths.get(path), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param path 路径
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor ofAll(final Path path, final Watcher watcher) {
		return ofAll(path, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param path 路径
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor ofAll(final Path path, final int maxDepth, final Watcher watcher) {
		final WatchMonitor watchMonitor = of(path, maxDepth, WatchKind.ALL);
		watchMonitor.setWatcher(watcher);
		return watchMonitor;
	}

	// ---------------------------------------------------------------------------------------------------------- createModify
	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param url URL
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(final URL url, final Watcher watcher) {
		return createModify(url, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param url URL
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(final URL url, final int maxDepth, final Watcher watcher) {
		return createModify(URLUtil.toURI(url), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param uri URI
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(final URI uri, final Watcher watcher) {
		return createModify(uri, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param uri URI
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(final URI uri, final int maxDepth, final Watcher watcher) {
		return createModify(Paths.get(uri), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param file 被监听文件
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(final File file, final Watcher watcher) {
		return createModify(file, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param file 被监听文件
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(final File file, final int maxDepth, final Watcher watcher) {
		return createModify(file.toPath(), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param path 路径
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(final String path, final Watcher watcher) {
		return createModify(path, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param path 路径
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(final String path, final int maxDepth, final Watcher watcher) {
		return createModify(Paths.get(path), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param path 路径
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(final Path path, final Watcher watcher) {
		return createModify(path, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param path 路径
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(final Path path, final int maxDepth, final Watcher watcher) {
		final WatchMonitor watchMonitor = of(path, maxDepth, WatchKind.MODIFY.getValue());
		watchMonitor.setWatcher(watcher);
		return watchMonitor;
	}

	/**
	 * 注册Watchable对象到WatchService服务
	 *
	 * @param watchable 可注册对象
	 * @param watcher WatchService对象
	 * @param events 监听事件
	 * @return {@link WatchKey}
	 * @since 4.6.9
	 */
	public static WatchKey register(final Watchable watchable, final WatchService watcher, final WatchEvent.Kind<?>... events){
		try {
			return watchable.register(watcher, events);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
