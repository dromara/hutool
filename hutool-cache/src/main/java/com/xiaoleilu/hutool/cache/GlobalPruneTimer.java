package com.xiaoleilu.hutool.cache;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.xiaoleilu.hutool.thread.ThreadUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 全局缓存清理定时器池，用于在需要过期支持的缓存对象中超时任务池
 * 
 * @author looly
 *
 */
public enum GlobalPruneTimer {
	/** 单例对象 */
	INSTANCE;

	/** 缓存任务计数 */
	private AtomicInteger cacheTaskNumber = new AtomicInteger(1);

	/** 定时器 */
	private ScheduledExecutorService pruneTimer;

	/**
	 * 构造
	 */
	private GlobalPruneTimer() {
		create();
	}

	/**
	 * 启动定时任务
	 * 
	 * @param task 任务
	 * @param delay 周期
	 * @return {@link ScheduledFuture}对象，可手动取消此任务
	 */
	public ScheduledFuture<?> schedule(Runnable task, long delay) {
		return this.pruneTimer.scheduleAtFixedRate(task, delay, delay, TimeUnit.MILLISECONDS);
	}

	/**
	 * 创建定时器
	 */
	public void create() {
		if (null != pruneTimer) {
			shutdownNow();
		}
		this.pruneTimer = new ScheduledThreadPoolExecutor(16, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return ThreadUtil.newThread(r, StrUtil.format("Pure-Timer-{}", cacheTaskNumber.getAndIncrement()));
			}
		});
	}

	/**
	 * 销毁全局定时器
	 */
	public void shutdown() {
		if (null != pruneTimer) {
			pruneTimer.shutdown();
		}
	}

	/**
	 * 销毁全局定时器
	 * 
	 * @return 销毁时未被执行的任务列表
	 */
	public List<Runnable> shutdownNow() {
		if (null != pruneTimer) {
			return pruneTimer.shutdownNow();
		}
		return null;
	}
}
