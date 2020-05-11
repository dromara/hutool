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
	 */
	public static HWDiskStore[] getDiskStores() {
		return hardware.getDiskStores();
	}

	/**
	 * 获取网络相关信息，可能多块网卡
	 * @return 网络相关信息
	 * @since 5.3.5
	 */
	public static NetworkIF[] getNetworkIFs(){
		return hardware.getNetworkIFs();
	}


}
