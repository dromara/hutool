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

import org.dromara.hutool.math.NumberUtil;
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
	 * CPU核心数
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
	private double user;

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
	public CpuInfo(final CentralProcessor processor, final long waitingTime) {
		init(processor, waitingTime);
	}

	/**
	 * 构造
	 *
	 * @param cpuNum   CPU核心数
	 * @param toTal    CPU总的使用率
	 * @param sys      CPU系统使用率
	 * @param user     CPU用户使用率
	 * @param wait     CPU当前等待率
	 * @param free     CPU当前空闲率
	 * @param cpuModel CPU型号信息
	 */
	public CpuInfo(final Integer cpuNum, final double toTal, final double sys, final double user, final double wait, final double free, final String cpuModel) {
		this.cpuNum = cpuNum;
		this.toTal = toTal;
		this.sys = sys;
		this.user = user;
		this.wait = wait;
		this.free = free;
		this.cpuModel = cpuModel;
	}

	public Integer getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(final Integer cpuNum) {
		this.cpuNum = cpuNum;
	}

	public double getToTal() {
		return toTal;
	}

	public void setToTal(final double toTal) {
		this.toTal = toTal;
	}

	public double getSys() {
		return sys;
	}

	public void setSys(final double sys) {
		this.sys = sys;
	}

	public double getUser() {
		return user;
	}

	public void setUser(final double user) {
		this.user = user;
	}

	public double getWait() {
		return wait;
	}

	public void setWait(final double wait) {
		this.wait = wait;
	}

	public double getFree() {
		return free;
	}

	public void setFree(final double free) {
		this.free = free;
	}

	public String getCpuModel() {
		return cpuModel;
	}

	public void setCpuModel(final String cpuModel) {
		this.cpuModel = cpuModel;
	}

	public CpuTicks getTicks() {
		return ticks;
	}

	public void setTicks(final CpuTicks ticks) {
		this.ticks = ticks;
	}

	/**
	 * 获取用户+系统的总的CPU使用率
	 *
	 * @return 总CPU使用率
	 */
	public double getUsed() {
		return NumberUtil.sub(100, this.free).doubleValue();
	}

	@Override
	public String toString() {
		return "CpuInfo{" +
				"CPU核心数=" + cpuNum +
				", CPU总的使用率=" + toTal +
				", CPU系统使用率=" + sys +
				", CPU用户使用率=" + user +
				", CPU当前等待率=" + wait +
				", CPU当前空闲率=" + free +
				", CPU利用率=" + getUsed() +
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
	private void init(final CentralProcessor processor, final long waitingTime) {
		final CpuTicks ticks = new CpuTicks(processor, waitingTime);
		this.ticks = ticks;

		this.cpuNum = processor.getLogicalProcessorCount();
		this.cpuModel = processor.toString();

		final long totalCpu = ticks.totalCpu();
		this.toTal = totalCpu;
		this.sys = formatDouble(ticks.cSys, totalCpu);
		this.user = formatDouble(ticks.user, totalCpu);
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
	private static double formatDouble(final long tick, final long totalCpu) {
		if (0 == totalCpu) {
			return 0D;
		}
		return Double.parseDouble(LOAD_FORMAT.format(tick <= 0 ? 0 : (100d * tick / totalCpu)));
	}
}
