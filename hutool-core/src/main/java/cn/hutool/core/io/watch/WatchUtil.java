package cn.hutool.core.io.watch;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.URLUtil;

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
	public static WatchMonitor create(URL url, WatchEvent.Kind<?>... events) {
		return create(url, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param url URL
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor create(URL url, int maxDepth, WatchEvent.Kind<?>... events) {
		return create(URLUtil.toURI(url), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param uri URI
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(URI uri, WatchEvent.Kind<?>... events) {
		return create(uri, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param uri URI
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor create(URI uri, int maxDepth, WatchEvent.Kind<?>... events) {
		return create(Paths.get(uri), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param file 文件
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(File file, WatchEvent.Kind<?>... events) {
		return create(file, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param file 文件
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor create(File file, int maxDepth, WatchEvent.Kind<?>... events) {
		return create(file.toPath(), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path 路径
	 * @param events 监听的事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(String path, WatchEvent.Kind<?>... events) {
		return create(path, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path 路径
	 * @param events 监听的事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor create(String path, int maxDepth, WatchEvent.Kind<?>... events) {
		return create(Paths.get(path), maxDepth, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path 路径
	 * @param events 监听事件列表
	 * @return 监听对象
	 */
	public static WatchMonitor create(Path path, WatchEvent.Kind<?>... events) {
		return create(path, 0, events);
	}

	/**
	 * 创建并初始化监听
	 *
	 * @param path 路径
	 * @param events 监听事件列表
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @return 监听对象
	 */
	public static WatchMonitor create(Path path, int maxDepth, WatchEvent.Kind<?>... events) {
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
	public static WatchMonitor createAll(URL url, Watcher watcher) {
		return createAll(url, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param url URL
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(URL url, int maxDepth, Watcher watcher) {
		return createAll(URLUtil.toURI(url), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param uri URI
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(URI uri, Watcher watcher) {
		return createAll(uri, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param uri URI
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(URI uri, int maxDepth, Watcher watcher) {
		return createAll(Paths.get(uri), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param file 被监听文件
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(File file, Watcher watcher) {
		return createAll(file, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param file 被监听文件
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(File file, int maxDepth, Watcher watcher) {
		return createAll(file.toPath(), 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param path 路径
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(String path, Watcher watcher) {
		return createAll(path, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param path 路径
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(String path, int maxDepth, Watcher watcher) {
		return createAll(Paths.get(path), maxDepth, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param path 路径
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(Path path, Watcher watcher) {
		return createAll(path, 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听所有事件
	 *
	 * @param path 路径
	 * @param maxDepth 当监听目录时，监听目录的最大深度，当设置值为1（或小于1）时，表示不递归监听子目录
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 */
	public static WatchMonitor createAll(Path path, int maxDepth, Watcher watcher) {
		final WatchMonitor watchMonitor = create(path, maxDepth, WatchMonitor.EVENTS_ALL);
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
	public static WatchMonitor createModify(URL url, Watcher watcher) {
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
	public static WatchMonitor createModify(URL url, int maxDepth, Watcher watcher) {
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
	public static WatchMonitor createModify(URI uri, Watcher watcher) {
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
	public static WatchMonitor createModify(URI uri, int maxDepth, Watcher watcher) {
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
	public static WatchMonitor createModify(File file, Watcher watcher) {
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
	public static WatchMonitor createModify(File file, int maxDepth, Watcher watcher) {
		return createModify(file.toPath(), 0, watcher);
	}

	/**
	 * 创建并初始化监听，监听修改事件
	 *
	 * @param path 路径
	 * @param watcher {@link Watcher}
	 * @return {@link WatchMonitor}
	 * @since 4.5.2
	 */
	public static WatchMonitor createModify(String path, Watcher watcher) {
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
	public static WatchMonitor createModify(String path, int maxDepth, Watcher watcher) {
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
	public static WatchMonitor createModify(Path path, Watcher watcher) {
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
	public static WatchMonitor createModify(Path path, int maxDepth, Watcher watcher) {
		final WatchMonitor watchMonitor = create(path, maxDepth, WatchMonitor.ENTRY_MODIFY);
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
	public static WatchKey register(Watchable watchable, WatchService watcher, WatchEvent.Kind<?>... events){
		try {
			return watchable.register(watcher, events);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
