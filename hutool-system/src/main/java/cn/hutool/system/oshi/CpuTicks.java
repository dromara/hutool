package cn.hutool.system.oshi;

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
	public CpuTicks(CentralProcessor processor, long waitingTime) {
		// CPU信息
		final long[] prevTicks = processor.getSystemCpuLoadTicks();
		// 这里必须要设置延迟
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

	public void setIdle(long idle) {
		this.idle = idle;
	}

	public long getNice() {
		return nice;
	}

	public void setNice(long nice) {
		this.nice = nice;
	}

	public long getIrq() {
		return irq;
	}

	public void setIrq(long irq) {
		this.irq = irq;
	}

	public long getSoftIrq() {
		return softIrq;
	}

	public void setSoftIrq(long softIrq) {
		this.softIrq = softIrq;
	}

	public long getSteal() {
		return steal;
	}

	public void setSteal(long steal) {
		this.steal = steal;
	}

	public long getcSys() {
		return cSys;
	}

	public void setcSys(long cSys) {
		this.cSys = cSys;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public long getIoWait() {
		return ioWait;
	}

	public void setIoWait(long ioWait) {
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
	private static long tick(long[] prevTicks, long[] ticks, CentralProcessor.TickType tickType) {
		return ticks[tickType.getIndex()] - prevTicks[tickType.getIndex()];
	}
}
