package cn.hutool.system.oshi;

import java.text.DecimalFormat;

/**
 * <p>CPU相关信息</p>
 *
 * @author Dai Yuanchuan
 **/
public class CpuInfo {

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
}