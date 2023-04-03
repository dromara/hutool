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

package org.dromara.hutool.core.date;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 系统时钟<br>
 * 高并发场景下System.currentTimeMillis()的性能问题的优化
 * System.currentTimeMillis()的调用比new一个普通对象要耗时的多（具体耗时高出多少我还没测试过，有人说是100倍左右）
 * System.currentTimeMillis()之所以慢是因为去跟系统打了一次交道
 * 后台定时更新时钟，JVM退出时，线程自动回收
 * <p>
 * see： <a href="http://git.oschina.net/yu120/sequence">http://git.oschina.net/yu120/sequence</a>
 * @author lry, looly
 */
public class SystemClock {

	/** 时钟更新间隔，单位毫秒 */
	private final long period;
	/** 现在时刻的毫秒数 */
	private volatile long now;

	/**
	 * 构造
	 * @param period 时钟更新间隔，单位毫秒
	 */
	public SystemClock(final long period) {
		this.period = period;
		this.now = System.currentTimeMillis();
		scheduleClockUpdating();
	}

	/**
	 * 开启计时器线程
	 */
	private void scheduleClockUpdating() {
		final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
			final Thread thread = new Thread(runnable, "System Clock");
			thread.setDaemon(true);
			return thread;
		});
		scheduler.scheduleAtFixedRate(() -> now = System.currentTimeMillis(), period, period, TimeUnit.MILLISECONDS);
	}

	/**
	 * @return 当前时间毫秒数
	 */
	private long currentTimeMillis() {
		return now;
	}

	//------------------------------------------------------------------------ static
	/**
	 * 单例
	 * @author Looly
	 *
	 */
	private static class InstanceHolder {
		public static final SystemClock INSTANCE = new SystemClock(1);
	}

	/**
	 * @return 当前时间
	 */
	public static long now() {
		return InstanceHolder.INSTANCE.currentTimeMillis();
	}

	/**
	 * @return 当前时间字符串表现形式
	 */
	public static String nowDate() {
		return new Timestamp(InstanceHolder.INSTANCE.currentTimeMillis()).toString();
	}
}
