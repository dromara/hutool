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

package org.dromara.hutool.management.oshi;

import oshi.hardware.CentralProcessor;
import oshi.util.Util;

/**
 * CPU负载时间信息
 *
 * @author looly
 * @since 5.7.12
 */
public class CpuTicks {

	long idle;
	long nice;
	long irq;
	long softIrq;
	long steal;
	long cSys;
	long user;
	long ioWait;

	/**
	 * 构造，等待时间为用于计算在一定时长内的CPU负载情况，如传入1000表示最近1秒的负载情况
	 *
	 * @param processor   {@link CentralProcessor}
	 * @param waitingTime 设置等待时间，单位毫秒
	 */
	public CpuTicks(final CentralProcessor processor, final long waitingTime) {
		// CPU信息
		final long[] prevTicks = processor.getSystemCpuLoadTicks();
		// 这里必须设置延迟
		Util.sleep(waitingTime);
		final long[] ticks = processor.getSystemCpuLoadTicks();

		this.idle = tick(prevTicks, ticks, CentralProcessor.TickType.IDLE);
		this.nice = tick(prevTicks, ticks, CentralProcessor.TickType.NICE);
		this.irq = tick(prevTicks, ticks, CentralProcessor.TickType.IRQ);
		this.softIrq = tick(prevTicks, ticks, CentralProcessor.TickType.SOFTIRQ);
		this.steal = tick(prevTicks, ticks, CentralProcessor.TickType.STEAL);
		this.cSys = tick(prevTicks, ticks, CentralProcessor.TickType.SYSTEM);
		this.user = tick(prevTicks, ticks, CentralProcessor.TickType.USER);
		this.ioWait = tick(prevTicks, ticks, CentralProcessor.TickType.IOWAIT);
	}

	public long getIdle() {
		return idle;
	}

	public void setIdle(final long idle) {
		this.idle = idle;
	}

	public long getNice() {
		return nice;
	}

	public void setNice(final long nice) {
		this.nice = nice;
	}

	public long getIrq() {
		return irq;
	}

	public void setIrq(final long irq) {
		this.irq = irq;
	}

	public long getSoftIrq() {
		return softIrq;
	}

	public void setSoftIrq(final long softIrq) {
		this.softIrq = softIrq;
	}

	public long getSteal() {
		return steal;
	}

	public void setSteal(final long steal) {
		this.steal = steal;
	}

	public long getcSys() {
		return cSys;
	}

	public void setcSys(final long cSys) {
		this.cSys = cSys;
	}

	public long getUser() {
		return user;
	}

	public void setUser(final long user) {
		this.user = user;
	}

	public long getIoWait() {
		return ioWait;
	}

	public void setIoWait(final long ioWait) {
		this.ioWait = ioWait;
	}

	/**
	 * 获取CPU总的使用率
	 *
	 * @return CPU总使用率
	 */
	public long totalCpu() {
		return Math.max(user + nice + cSys + idle + ioWait + irq + softIrq + steal, 0);
	}

	@Override
	public String toString() {
		return "CpuTicks{" +
				"idle=" + idle +
				", nice=" + nice +
				", irq=" + irq +
				", softIrq=" + softIrq +
				", steal=" + steal +
				", cSys=" + cSys +
				", user=" + user +
				", ioWait=" + ioWait +
				'}';
	}

	/**
	 * 获取一段时间内的CPU负载标记差
	 *
	 * @param prevTicks 开始的ticks
	 * @param ticks     结束的ticks
	 * @param tickType  tick类型
	 * @return 标记差
	 * @since 5.7.12
	 */
	private static long tick(final long[] prevTicks, final long[] ticks, final CentralProcessor.TickType tickType) {
		return ticks[tickType.getIndex()] - prevTicks[tickType.getIndex()];
	}
}
