package cn.hutool.system.oshi;

import oshi.hardware.CentralProcessor;

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

	/**
	 * CPU型号信息
	 */
	private CpuTicks ticks;

	/**
	 * 空构造
	 */
	public CpuInfo() {
	}

	/**
	 * 构造，等待时间为用于计算在一定时长内的CPU负载情况，如传入1000表示最近1秒的负载情况
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

	public CpuTicks getTicks() {
		return ticks;
	}

	public void setTicks(CpuTicks ticks) {
		this.ticks = ticks;
	}

	@Override
	public String toString() {
		return "CpuInfo{" +
				"cpu核心数=" + cpuNum +
				", CPU总的使用率=" + toTal +
				", CPU系统使用率=" + sys +
				", CPU用户使用率=" + used +
				", CPU当前等待率=" + wait +
				", CPU当前空闲率=" + free +
				", CPU利用率=" + LOAD_FORMAT.format(100 - free) +
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
		final CpuTicks ticks = new CpuTicks(processor, waitingTime);
		this.ticks = ticks;

		this.cpuNum = processor.getLogicalProcessorCount();
		this.cpuModel = processor.toString();

		final long totalCpu = ticks.totalCpu();
		this.toTal = totalCpu;
		this.sys = formatDouble(ticks.cSys, totalCpu);
		this.used = formatDouble(ticks.user, totalCpu);
		this.wait = formatDouble(ticks.ioWait, totalCpu);
		this.free = formatDouble(ticks.idle, totalCpu);
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
