package cn.hutool.system.oshi;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Oshi库封装的工具类，通过此工具类，可获取系统、硬件相关信息
 * 
 * <pre>
 * 1、系统信息
 * 2、硬件信息
 * </pre>
 *
 * 相关内容见：https://github.com/oshi/oshi
 * 
 * @author Looly
 * @since 4.6.4
 */
public class OshiUtil {

	private static final SystemInfo systemInfo;
	/** 硬件信息 */
	private static final HardwareAbstractionLayer hardware;
	/** 系统信息 */
	private static final OperatingSystem os;

	static {
		systemInfo = new SystemInfo();
		hardware = systemInfo.getHardware();
		os = systemInfo.getOperatingSystem();
	}

	/**
	 * 获取操作系统相关信息，包括系统版本、文件系统、进程等
	 * 
	 * @return 操作系统相关信息
	 */
	public static OperatingSystem getOs() {
		return os;
	}

	/**
	 * 获取硬件相关信息，包括内存、硬盘、网络设备、显示器、USB、声卡等
	 * 
	 * @return 硬件相关信息
	 */
	public static HardwareAbstractionLayer getHardware() {
		return hardware;
	}

	/**
	 * 获取BIOS中计算机相关信息，比如序列号、固件版本等
	 * 
	 * @return 获取BIOS中计算机相关信息
	 */
	public static ComputerSystem getSystem() {
		return hardware.getComputerSystem();
	}

	/**
	 * 获取内存相关信息，比如总内存、可用内存等
	 * 
	 * @return 内存相关信息
	 */
	public static GlobalMemory getMemory() {
		return hardware.getMemory();
	}

	/**
	 * 获取CPU（处理器）相关信息，比如CPU负载等
	 * 
	 * @return CPU（处理器）相关信息
	 */
	public static CentralProcessor getProcessor() {
		return hardware.getProcessor();
	}

	/**
	 * 获取传感器相关信息，例如CPU温度、风扇转速等，传感器可能有多个
	 * 
	 * @return 传感器相关信息
	 */
	public static Sensors getSensors() {
		return hardware.getSensors();
	}

	/**
	 * 获取磁盘相关信息，可能有多个磁盘（包括可移动磁盘等）
	 * 
	 * @return 磁盘相关信息
	 * @since 5.3.6
	 */
	public static List<HWDiskStore> getDiskStores() {
		return hardware.getDiskStores();
	}

	/**
	 * 获取网络相关信息，可能多块网卡
	 * @return 网络相关信息
	 * @since 5.3.6
	 */
	public static List<NetworkIF> getNetworkIFs(){
		return hardware.getNetworkIFs();
	}

	// ------------------------------------------------------------------ cpu

	/**
	 * 获取系统CPU 系统使用率、用户使用率、利用率等等 相关信息
	 *
	 * @return 系统 CPU 使用率 等信息
	 */
	public static CpuInfo getCpuInfo() {
		return getCpuInfo(1000);
	}

	/**
	 * 获取系统CPU 系统使用率、用户使用率、利用率等等 相关信息
	 *
	 * @param waitingTime 设置等待时间
	 * @return 系统 CPU 使用率 等信息
	 */
	public static CpuInfo getCpuInfo(long waitingTime) {
		return getCpuInfo(OshiUtil.getProcessor(), waitingTime);
	}

	/**
	 * 获取系统CPU 系统使用率、用户使用率、利用率等等 相关信息
	 *
	 * @param processor {@link CentralProcessor}
	 * @param waitingTime 设置等待时间
	 * @return 系统 CPU 使用率 等信息
	 */
	private static CpuInfo getCpuInfo(CentralProcessor processor, long waitingTime) {
		CpuInfo cpuInfo = new CpuInfo();
		// CPU信息
		long[] prevTicks = processor.getSystemCpuLoadTicks();
		// 这里必须要设置延迟
		Util.sleep(waitingTime);
		long[] ticks = processor.getSystemCpuLoadTicks();
		long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
		long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
		long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
		long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
		long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
		long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
		long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
		long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
		long totalCpu = Math.max(user + nice + cSys + idle + ioWait + irq + softIrq + steal, 0);
		final DecimalFormat format = new DecimalFormat("#.00");
		cpuInfo.setCpuNum(processor.getLogicalProcessorCount());
		cpuInfo.setToTal(totalCpu);
		cpuInfo.setSys(Double.parseDouble(format.format(cSys <= 0 ? 0 : (100d * cSys / totalCpu))));
		cpuInfo.setUsed(Double.parseDouble(format.format(user <= 0 ? 0 : (100d * user / totalCpu))));
		if (totalCpu == 0) {
			cpuInfo.setWait(0);
		} else {
			cpuInfo.setWait(Double.parseDouble(format.format(100d * ioWait / totalCpu)));
		}
		cpuInfo.setFree(Double.parseDouble(format.format(idle <= 0 ? 0 : (100d * idle / totalCpu))));
		cpuInfo.setCpuModel(processor.toString());
		return cpuInfo;
	}
}
