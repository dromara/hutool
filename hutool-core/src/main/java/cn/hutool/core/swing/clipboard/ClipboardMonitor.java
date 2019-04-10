package cn.hutool.core.swing.clipboard;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import cn.hutool.core.swing.ClipboardUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * 剪贴板监听
 * 
 * @author looly
 * @since 4.5.6
 */
public class ClipboardMonitor implements ClipboardOwner, Runnable {

	/**
	 * 新建剪贴板监听，尝试获取剪贴板内容的次数为10，第二次之后延迟100毫秒
	 * 
	 * @param listener 剪贴板事件监听
	 * @return {@link ClipboardMonitor}
	 */
	public static ClipboardMonitor create(ClipboardListener listener) {
		return new ClipboardMonitor(listener);
	}

	/**
	 * 新建剪贴板监听
	 * 
	 * @param tryCount 尝试获取剪贴板内容的次数
	 * @param delay 响应延迟，当从第二次开始，延迟一定毫秒数等待剪贴板可以获取
	 * @param listener 剪贴板事件监听
	 * @return {@link ClipboardMonitor}
	 */
	public static ClipboardMonitor create(int tryCount, long delay, ClipboardListener listener) {
		return new ClipboardMonitor(tryCount, delay, listener);
	}

	/** 重试次数 */
	private int tryCount;
	/** 重试等待 */
	private long delay;
	/** 监听事件处理 */
	private ClipboardListener listener;

	/**
	 * 构造，尝试获取剪贴板内容的次数为10，第二次之后延迟100毫秒
	 * 
	 * @param tryCount 尝试获取剪贴板内容的次数
	 * @param delay 响应延迟，当从第二次开始，延迟一定毫秒数等待剪贴板可以获取
	 * @param listener 剪贴板事件监听
	 */
	public ClipboardMonitor(ClipboardListener listener) {
		this(10, 100, listener);
	}

	/**
	 * 构造
	 * 
	 * @param tryCount 尝试获取剪贴板内容的次数
	 * @param delay 响应延迟，当从第二次开始，延迟一定毫秒数等待剪贴板可以获取，当tryCount小于2时无效
	 * @param listener 剪贴板事件监听
	 */
	public ClipboardMonitor(int tryCount, long delay, ClipboardListener listener) {
		this.tryCount = tryCount;
		this.delay = delay;
		this.listener = listener;
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		Transferable newContents;
		try {
			newContents = tryGetContent(clipboard);
		} catch (InterruptedException e) {
			// 中断后结束简体
			return;
		}

		final Transferable transferable = listener.onChange(clipboard, newContents);
		clipboard.setContents(ObjectUtil.defaultIfNull(transferable, ObjectUtil.defaultIfNull(newContents, contents)), this);
	}

	@Override
	public void run() {
		final Clipboard clipboard = ClipboardUtil.getClipboard();
		clipboard.setContents(clipboard.getContents(null), this);
	}

	/**
	 * 开始监听
	 * 
	 * @param sync 是否阻塞
	 */
	public void listen(boolean sync) {
		run();

		if (sync) {
			ThreadUtil.sync(this);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 尝试获取剪贴板内容
	 * 
	 * @param clipboard 剪贴板
	 * @return 剪贴板内容，{@code null} 表示未获取到
	 * @throws InterruptedException 线程中断
	 */
	private Transferable tryGetContent(Clipboard clipboard) throws InterruptedException {
		Transferable newContents = null;
		for (int i = 0; i < this.tryCount; i++) {
			if (this.delay > 0 && i > 0) {
				// 第一次获取不等待，只有从第二次获取时才开始等待
				Thread.sleep(this.delay);
			}

			try {
				newContents = clipboard.getContents(null);
			} catch (IllegalStateException e) {
				// ignore
			}
			if (null != newContents) {
				return newContents;
			}
		}
		return newContents;
	}
	// ------------------------------------------------------------------------------------------------------------------------- Private method end
}
