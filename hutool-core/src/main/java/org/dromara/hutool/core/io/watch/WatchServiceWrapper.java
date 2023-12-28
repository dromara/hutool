/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.PathUtil;
import org.dromara.hutool.core.lang.wrapper.Wrapper;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * {@link WatchEvent} 包装类，提供可选的监听事件和监听选项<br>
 * 通过多次调用{@link #registerPath(Path, int)} 可以递归注册多个路径
 *
 * @author looly
 * @since 6.0.0
 */
public class WatchServiceWrapper implements WatchService, Wrapper<WatchService>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 监听服务
	 */
	private final WatchService watchService;
	/**
	 * 监听事件列表
	 */
	private WatchEvent.Kind<?>[] events;
	/**
	 * 监听选项，例如监听频率等
	 */
	private WatchEvent.Modifier[] modifiers;
	/**
	 * 监听是否已经关闭
	 */
	private boolean isClosed;

	/**
	 * 构造
	 */
	public WatchServiceWrapper() {
		//初始化监听
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (final IOException e) {
			throw new WatchException(e);
		}

		isClosed = false;
	}

	@Override
	public WatchService getRaw() {
		return this.watchService;
	}

	/**
	 * 是否已关闭
	 *
	 * @return 是否已关闭
	 */
	public boolean isClosed() {
		return this.isClosed;
	}

	@Override
	public void close() {
		if (!this.isClosed) {
			this.isClosed = true;
			IoUtil.closeQuietly(this.watchService);
		}
	}

	@Override
	public WatchKey poll() {
		return this.watchService.poll();
	}

	@Override
	public WatchKey poll(final long timeout, final TimeUnit unit) throws InterruptedException {
		return this.watchService.poll(timeout, unit);
	}

	@Override
	public WatchKey take() throws InterruptedException {
		return this.watchService.take();
	}

	/**
	 * 监听事件列表，见：StandardWatchEventKinds
	 *
	 * @param kinds 事件列表
	 * @return this
	 */
	@SuppressWarnings("resource")
	public WatchServiceWrapper setEvents(final WatchKind... kinds) {
		if (ArrayUtil.isNotEmpty(kinds)) {
			setEvents(ArrayUtil.mapToArray(kinds, WatchKind::getValue, WatchEvent.Kind<?>[]::new));
		}
		return this;
	}

	/**
	 * 监听事件列表，见：StandardWatchEventKinds
	 *
	 * @param events 事件列表
	 * @return this
	 */
	public WatchServiceWrapper setEvents(final WatchEvent.Kind<?>... events) {
		this.events = events;
		return this;
	}

	/**
	 * 设置监听选项，例如监听频率等，可设置项包括：
	 *
	 * <pre>
	 * 1、com.sun.nio.file.StandardWatchEventKinds
	 * 2、com.sun.nio.file.SensitivityWatchEventModifier
	 * </pre>
	 *
	 * @param modifiers 监听选项，例如监听频率等
	 * @return this
	 */
	public WatchServiceWrapper setModifiers(final WatchEvent.Modifier... modifiers) {
		this.modifiers = modifiers;
		return this;
	}

	/**
	 * 注册单一的监听
	 *
	 * @param watchable 可注册对象，如Path
	 * @return {@link WatchKey}，如果为{@code null}，表示注册失败
	 */
	public WatchKey register(final Watchable watchable) {
		final WatchEvent.Kind<?>[] kinds = ArrayUtil.defaultIfEmpty(this.events, WatchKind.ALL);

		WatchKey watchKey = null;
		try {
			if (ArrayUtil.isEmpty(this.modifiers)) {
				watchKey = watchable.register(this.watchService, kinds);
			} else {
				watchKey = watchable.register(this.watchService, kinds, this.modifiers);
			}
		} catch (final IOException e) {
			if (!(e instanceof AccessDeniedException)) {
				throw new WatchException(e);
			}
		}

		return watchKey;
	}

	/**
	 * 递归将指定路径加入到监听中<br>
	 * 如果提供的是目录，则监听目录本身和目录下的目录和文件，深度取决于maxDepth
	 *
	 * @param path     路径
	 * @param maxDepth 递归下层目录的最大深度
	 */
	public void registerPath(final Path path, final int maxDepth) {
		// 注册当前目录或文件
		final WatchKey watchKey = register(path);
		if (null == watchKey) {
			// 注册失败，跳过（可能目录或文件无权限）
			return;
		}

		// 递归注册下一层层级的目录和文件
		PathUtil.walkFiles(path, maxDepth, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
				//继续添加目录
				registerPath(dir, 0);
				return super.postVisitDirectory(dir, exc);
			}
		});
	}

	/**
	 * 执行事件获取并处理
	 *
	 * @param watcher     {@link Watcher}
	 * @param watchFilter 监听过滤接口，通过实现此接口过滤掉不需要监听的情况，{@link Predicate#test(Object)}为{@code true}保留，null表示不过滤
	 */
	public void watch(final Watcher watcher, final Predicate<WatchEvent<?>> watchFilter) {
		watch((event, watchKey) -> {
			final WatchEvent.Kind<?> kind = event.kind();

			if (kind == WatchKind.CREATE.getValue()) {
				watcher.onCreate(event, watchKey);
			} else if (kind == WatchKind.MODIFY.getValue()) {
				watcher.onModify(event, watchKey);
			} else if (kind == WatchKind.DELETE.getValue()) {
				watcher.onDelete(event, watchKey);
			} else if (kind == WatchKind.OVERFLOW.getValue()) {
				watcher.onOverflow(event, watchKey);
			}
		}, watchFilter);
	}

	/**
	 * 执行事件获取并处理
	 *
	 * @param action      监听回调函数，实现此函数接口用于处理WatchEvent事件
	 * @param watchFilter 监听过滤接口，通过实现此接口过滤掉不需要监听的情况，{@link Predicate#test(Object)}为{@code true}保留，null表示不过滤
	 */
	public void watch(final BiConsumer<WatchEvent<?>, WatchKey> action, final Predicate<WatchEvent<?>> watchFilter) {
		final WatchKey wk;
		try {
			wk = watchService.take();
		} catch (final InterruptedException | ClosedWatchServiceException e) {
			// 用户中断
			close();
			return;
		}

		for (final WatchEvent<?> event : wk.pollEvents()) {
			// 如果监听文件，检查当前事件是否与所监听文件关联
			if (null != watchFilter && !watchFilter.test(event)) {
				continue;
			}
			action.accept(event, wk);
		}

		wk.reset();
	}
}
