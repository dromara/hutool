package cn.hutool.system.oshi;

import oshi.hardware.CentralProcessor;
import oshi.util.Util;

import java.text.DecimalFormat;

/**
 * <p>CPU相关信息</p>
 *
 * @author Dai Yuanchuan
 **/
public class CpuInfo {

	private static final DecimalFormat LOAD_FORMAT = new DecimalFormat("#.00");

	/**
	 * cpu核心数
	 */
	private Integer cpuNum;

	/**
	 * CPU总的使用率
	 */
	private double toTal;

	/**
	 * CPU系统使用率
	 */
	private double sys;

	/**
	 * CPU用户使用率
	 */
	private double used;

	/**
	 * CPU当前等待率
	 */
	private double wait;

	/**
	 * CPU当前空闲率
	 */
	private double free;

	/**
	 * CPU型号信息
	 */
	private String cpuModel;

	public CpuInfo() {
	}

	/**
	 * 构造
	 *
	 * @param processor   {@link CentralProcessor}
	 * @param waitingTime 设置等待时间，单位毫秒
	 */
	public CpuInfo(CentralProcessor processor, long waitingTime) {
		init(processor, waitingTime);
	}

	/**
	 * 构造
	 *
	 * @param cpuNum   CPU核心数
	 * @param toTal    CPU总的使用率
	 * @param sys      CPU系统使用率
	 * @param used     CPU用户使用率
	 * @param wait     CPU当前等待率
	 * @param free     CPU当前空闲率
	 * @param cpuModel CPU型号信息
	 */
	public CpuInfo(Integer cpuNum, double toTal, double sys, double used, double wait, double free, String cpuModel) {
		this.cpuNum = cpuNum;
		this.toTal = toTal;
		this.sys = sys;
		this.used = used;
		this.wait = wait;
		this.free = free;
		this.cpuModel = cpuModel;
	}

	public Integer getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(Integer cpuNum) {
		this.cpuNum = cpuNum;
	}

	public double getToTal() {
		return toTal;
	}

	public void setToTal(double toTal) {
		this.toTal = toTal;
	}

	public double getSys() {
		return sys;
	}

	public void setSys(double sys) {
		this.sys = sys;
	}

	public double getUsed() {
		return used;
	}

	public void setUsed(double used) {
		this.used = used;
	}

	public double getWait() {
		return wait;
	}

	public void setWait(double wait) {
		this.wait = wait;
	}

	public double getFree() {
		return free;
	}

	public void setFree(double free) {
		this.free = free;
	}

	public String getCpuModel() {
		return cpuModel;
	}

	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}

	@Override
	public String toString() {
		DecimalFormat format = new DecimalFormat("#.00");
		return "CpuInfo{" +
				"cpu核心数=" + cpuNum +
				", CPU总的使用率=" + toTal +
				", CPU系统使用率=" + sys +
				", CPU用户使用率=" + used +
				", CPU当前等待率=" + wait +
				", CPU当前空闲率=" + free +
				", CPU利用率=" + Double.parseDouble(format.format((100 - getFree()))) +
				", CPU型号信息='" + cpuModel + '\'' +
				'}';
	}

	/**
	 * 获取指定等待时间内系统CPU 系统使用率、用户使用率、利用率等等 相关信息
	 *
	 * @param processor   {@link CentralProcessor}
	 * @param waitingTime 设置等待时间，单位毫秒
	 * @since 5.7.12
	 */
	private void init(CentralProcessor processor, long waitingTime) {
		// CPU信息
		final long[] prevTicks = processor.getSystemCpuLoadTicks();
		// 这里必须要设置延迟
		Util.sleep(waitingTime);
		final long[] ticks = processor.getSystemCpuLoadTicks();
		final long nice = tick(prevTicks, ticks, CentralProcessor.TickType.NICE);
		final long irq = tick(prevTicks, ticks, CentralProcessor.TickType.IRQ);
		final long softIrq = tick(prevTicks, ticks, CentralProcessor.TickType.SOFTIRQ);
		final long steal = tick(prevTicks, ticks, CentralProcessor.TickType.STEAL);
		final long cSys = tick(prevTicks, ticks, CentralProcessor.TickType.SYSTEM);
		final long user = tick(prevTicks, ticks, CentralProcessor.TickType.USER);
		final long ioWait = tick(prevTicks, ticks, CentralProcessor.TickType.IOWAIT);
		// CPU闲置时间
		final long idle = tick(prevTicks, ticks, CentralProcessor.TickType.IDLE);

		this.cpuNum = processor.getLogicalProcessorCount();
		final long totalCpu = Math.max(user + nice + cSys + idle + ioWait + irq + softIrq + steal, 0);
		this.toTal = totalCpu;

		this.sys = formatDouble(cSys, totalCpu);
		this.used = formatDouble(user, totalCpu);
		this.wait = formatDouble(ioWait, totalCpu);
		this.free = formatDouble(idle, totalCpu);
		this.cpuModel = processor.toString();
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

	/**
	 * 获取每个CPU核心的tick，计算方式为 100 * tick / totalCpu
	 *
	 * @param tick     tick
	 * @param totalCpu CPU总数
	 * @return 平均每个CPU核心的tick
	 * @since 5.7.12
	 */
	private static double formatDouble(long tick, long totalCpu) {
		if (0 == totalCpu) {
			return 0D;
		}
		return Double.parseDouble(LOAD_FORMAT.format(tick <= 0 ? 0 : (100d * tick / totalCpu)));
	}
}
