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

package org.dromara.hutool.swing.clipboard;

import org.dromara.hutool.core.thread.ThreadUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.io.Closeable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 剪贴板监听
 *
 * @author looly
 * @since 4.5.6
 */
public class ClipboardMonitor implements ClipboardOwner, Runnable, Closeable {
	/** 默认重试此时：10 */
	public static final int DEFAULT_TRY_COUNT = 10;
	/** 默认重试等待：100 */
	public static final long DEFAULT_DELAY = 100;

	/** 重试次数 */
	private final int tryCount;
	/** 重试等待 */
	private final long delay;
	/** 系统剪贴板对象 */
	private final Clipboard clipboard;
	/** 监听事件处理 */
	private final Set<ClipboardListener> listenerSet = new LinkedHashSet<>();
	/** 是否正在监听 */
	private boolean isRunning;

	// ---------------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，尝试获取剪贴板内容的次数为10，第二次之后延迟100毫秒
	 */
	public ClipboardMonitor() {
		this(DEFAULT_TRY_COUNT, DEFAULT_DELAY);
	}

	/**
	 * 构造
	 *
	 * @param tryCount 尝试获取剪贴板内容的次数
	 * @param delay 响应延迟，当从第二次开始，延迟一定毫秒数等待剪贴板可以获取，当tryCount小于2时无效
	 */
	public ClipboardMonitor(final int tryCount, final long delay) {
		this(tryCount, delay, ClipboardUtil.getClipboard());
	}

	/**
	 * 构造
	 *
	 * @param tryCount 尝试获取剪贴板内容的次数
	 * @param delay 响应延迟，当从第二次开始，延迟一定毫秒数等待剪贴板可以获取，当tryCount小于2时无效
	 * @param clipboard 剪贴板对象
	 */
	ClipboardMonitor(final int tryCount, final long delay, final Clipboard clipboard) {
		this.tryCount = tryCount;
		this.delay = delay;
		this.clipboard = clipboard;
	}
	// ---------------------------------------------------------------------------------------------------------- Constructor end
	/**
	 * 设置 监听事件处理
	 *
	 * @param listener 监听事件处理
	 * @return this
	 */
	public ClipboardMonitor addListener(final ClipboardListener listener) {
		this.listenerSet.add(listener);
		return this;
	}

	/**
	 * 去除指定监听
	 *
	 * @param listener 监听
	 * @return this
	 */
	public ClipboardMonitor removeListener(final ClipboardListener listener) {
		this.listenerSet.remove(listener);
		return this;
	}

	/**
	 * 清空监听
	 *
	 * @return this
	 */
	public ClipboardMonitor clearListener() {
		this.listenerSet.clear();
		return this;
	}

	@Override
	public void lostOwnership(final Clipboard clipboard, final Transferable contents) {
		final Transferable newContents;
		try {
			newContents = tryGetContent(clipboard);
		} catch (final InterruptedException e) {
			// 中断后结束简体
			return;
		}

		Transferable transferable = null;
		for (final ClipboardListener listener : listenerSet) {
			try {
				transferable = listener.onChange(clipboard, ObjUtil.defaultIfNull(transferable, newContents));
			} catch (final Throwable e) {
				// 忽略事件处理异常，保证所有监听正常执行
			}
		}

		if (isRunning) {
			// 继续监听
			clipboard.setContents(ObjUtil.defaultIfNull(transferable, ObjUtil.defaultIfNull(newContents, contents)), this);
		}
	}

	@Override
	public synchronized void run() {
		if(!isRunning) {
			final Clipboard clipboard = this.clipboard;
			clipboard.setContents(clipboard.getContents(null), this);
			isRunning = true;
		}
	}

	/**
	 * 开始监听
	 *
	 * @param sync 是否阻塞
	 */
	public void listen(final boolean sync) {
		run();

		if (sync) {
			ThreadUtil.sync(this);
		}
	}

	/**
	 * 关闭（停止）监听
	 */
	@Override
	public void close() {
		this.isRunning = false;
	}

	// ------------------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 尝试获取剪贴板内容
	 *
	 * @param clipboard 剪贴板
	 * @return 剪贴板内容，{@code null} 表示未获取到
	 * @throws InterruptedException 线程中断
	 */
	private Transferable tryGetContent(final Clipboard clipboard) throws InterruptedException {
		Transferable newContents = null;
		for (int i = 0; i < this.tryCount; i++) {
			if (this.delay > 0 && i > 0) {
				// 第一次获取不等待，只有从第二次获取时才开始等待
				Thread.sleep(this.delay);
			}

			try {
				newContents = clipboard.getContents(null);
			} catch (final IllegalStateException e) {
				// ignore
			}
			if (null != newContents) {
				return newContents;
			}
		}
		return null;
	}
	// ------------------------------------------------------------------------------------------------------------------------- Private method end
}
