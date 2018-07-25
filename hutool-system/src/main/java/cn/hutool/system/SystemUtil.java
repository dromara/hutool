package cn.hutool.system;

import java.io.PrintWriter;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.Properties;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.StrUtil;

/**
 * Java的System类封装工具类。<br>
 * http://blog.csdn.net/zhongweijian/article/details/7619383
 * 
 * @author Looly
 *
 */
public class SystemUtil {

	/***** Java运行时环境信息 *****/
	// Java 运行时环境规范名称
	public final static String SPECIFICATION_NAME = "java.specification.name";
	// Java 运行时环境版本
	public final static String VERSION = "java.version";
	// Java 运行时环境规范版本
	public final static String SPECIFICATION_VERSION = "java.specification.version";
	// Java 运行时环境供应商
	public final static String VENDOR = "java.vendor";
	// Java 运行时环境规范供应商
	public final static String SPECIFICATION_VENDOR = "java.specification.vendor";
	// Java 供应商的 URL
	public final static String VENDOR_URL = "java.vendor.url";
	// Java 安装目录
	public final static String HOME = "java.home";
	// 加载库时搜索的路径列表
	public final static String LIBRARY_PATH = "java.library.path";
	// 默认的临时文件路径
	public final static String TMPDIR = "java.io.tmpdir";
	// 要使用的 JIT 编译器的名称
	public final static String COMPILER = "java.compiler";
	// 一个或多个扩展目录的路径
	public final static String EXT_DIRS = "java.ext.dirs";

	/***** Java虚拟机信息 *****/
	// Java 虚拟机实现名称
	public final static String VM_NAME = "java.vm.name";
	// Java 虚拟机规范名称
	public final static String VM_SPECIFICATION_NAME = "java.vm.specification.name";
	// Java 虚拟机实现版本
	public final static String VM_VERSION = "java.vm.version";
	// Java 虚拟机规范版本
	public final static String VM_SPECIFICATION_VERSION = "java.vm.specification.version";
	// Java 虚拟机实现供应商
	public final static String VM_VENDEOR = "java.vm.vendor";
	// Java 虚拟机规范供应商
	public final static String VM_SPECIFICATION_VENDOR = "java.vm.specification.vendor";

	/***** Java类信息 *****/
	// Java 类格式版本号
	public final static String CLASS_VERSION = "java.class.version";
	// Java 类路径
	public final static String CLASS_PATH = "java.class.path";

	/***** OS信息 *****/
	// 操作系统的名称
	public final static String OS_NAME = "os.name";
	// 操作系统的架构
	public final static String OS_ARCH = "os.arch";
	// 操作系统的版本
	public final static String OS_VERSION = "os.version";
	// 文件分隔符（在 UNIX 系统中是“/”）
	public final static String FILE_SEPRATOR = "file.separator";
	// 路径分隔符（在 UNIX 系统中是“:”）
	public final static String PATH_SEPRATOR = "path.separator";
	// 行分隔符（在 UNIX 系统中是“\n”）
	public final static String LINE_SEPRATOR = "line.separator";

	/***** 用户信息 *****/
	// 用户的账户名称
	public final static String USER_NAME = "user.name";
	// 用户的主目录
	public final static String USER_HOME = "user.home";
	// 用户的当前工作目录
	public final static String USER_DIR = "user.dir";

	// ----------------------------------------------------------------------- Basic start

	/**
	 * 取得系统属性，如果因为Java安全的限制而失败，则将错误打在Log中，然后返回 <code>null</code>。
	 * 
	 * @param name 属性名
	 * @param defaultValue 默认值
	 * @return 属性值或<code>null</code>
	 */
	public static String get(String name, String defaultValue) {
		return StrUtil.nullToDefault(get(name, false), defaultValue);
	}

	/**
	 * 取得系统属性，如果因为Java安全的限制而失败，则将错误打在Log中，然后返回 <code>null</code>。
	 * 
	 * @param name 属性名
	 * @param quiet 安静模式，不将出错信息打在<code>System.err</code>中
	 * 
	 * @return 属性值或<code>null</code>
	 */
	public static String get(String name, boolean quiet) {
		try {
			return System.getProperty(name);
		} catch (SecurityException e) {
			if (!quiet) {
				Console.error("Caught a SecurityException reading the system property '{}'; the SystemUtil property value will default to null.", name);
			}
			return null;
		}
	}

	/**
	 * 获得System属性（调用System.getProperty）
	 * 
	 * @param key 键
	 * @return 属性值
	 */
	public static String get(String key) {
		return get(key, null);
	}

	/**
	 * 获得boolean类型值
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}

		value = value.trim().toLowerCase();
		if (value.isEmpty()) {
			return true;
		}

		if ("true".equals(value) || "yes".equals(value) || "1".equals(value)) {
			return true;
		}

		if ("false".equals(value) || "no".equals(value) || "0".equals(value)) {
			return false;
		}

		return defaultValue;
	}

	/**
	 * 获得int类型值
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static long getInt(String key, int defaultValue) {
		return Convert.toInt(get(key), defaultValue);
	}

	/**
	 * 获得long类型值
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static long getLong(String key, long defaultValue) {
		return Convert.toLong(get(key), defaultValue);
	}

	/**
	 * @return 属性列表
	 */
	public static Properties props() {
		return System.getProperties();
	}

	/**
	 * 获取当前进程 PID
	 * 
	 * @return 当前进程 ID
	 */
	public static long getCurrentPID() {
		return Long.parseLong(getRuntimeMXBean().getName().split("@")[0]);
	}
	// ----------------------------------------------------------------------- Basic end

	/**
	 * 返回Java虚拟机类加载系统相关属性
	 *
	 * @return {@link ClassLoadingMXBean}
	 * @since 4.1.4
	 */
	public static ClassLoadingMXBean getClassLoadingMXBean() {
		return ManagementFactory.getClassLoadingMXBean();
	}

	/**
	 * 返回Java虚拟机内存系统相关属性
	 *
	 * @return {@link MemoryMXBean}
	 * @since 4.1.4
	 */
	public static MemoryMXBean getMemoryMXBean() {
		return ManagementFactory.getMemoryMXBean();
	}

	/**
	 * 返回Java虚拟机线程系统相关属性
	 *
	 * @return {@link ThreadMXBean}
	 * @since 4.1.4
	 */
	public static ThreadMXBean getThreadMXBean() {
		return ManagementFactory.getThreadMXBean();
	}

	/**
	 * 返回Java虚拟机运行时系统相关属性
	 *
	 * @return {@link RuntimeMXBean}
	 * @since 4.1.4
	 */
	public static RuntimeMXBean getRuntimeMXBean() {
		return ManagementFactory.getRuntimeMXBean();
	}

	/**
	 * 返回Java虚拟机编译系统相关属性<br>
	 * 如果没有编译系统，则返回<code>null</code>
	 *
	 * @return a {@link CompilationMXBean} ，如果没有编译系统，则返回<code>null</code>
	 * @since 4.1.4
	 */
	public static CompilationMXBean getCompilationMXBean() {
		return ManagementFactory.getCompilationMXBean();
	}

	/**
	 * 返回Java虚拟机运行下的操作系统相关信息属性
	 *
	 * @return {@link OperatingSystemMXBean}
	 * @since 4.1.4
	 */
	public static OperatingSystemMXBean getOperatingSystemMXBean() {
		return ManagementFactory.getOperatingSystemMXBean();
	}

	/**
	 * Returns a list of {@link MemoryPoolMXBean} objects in the Java virtual machine. The Java virtual machine can have one or more memory pools. It may add or remove memory pools during execution.
	 *
	 * @return a list of <tt>MemoryPoolMXBean</tt> objects.
	 *
	 */
	public static List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
		return ManagementFactory.getMemoryPoolMXBeans();
	}

	/**
	 * Returns a list of {@link MemoryManagerMXBean} objects in the Java virtual machine. The Java virtual machine can have one or more memory managers. It may add or remove memory managers during
	 * execution.
	 *
	 * @return a list of <tt>MemoryManagerMXBean</tt> objects.
	 *
	 */
	public static List<MemoryManagerMXBean> getMemoryManagerMXBeans() {
		return ManagementFactory.getMemoryManagerMXBeans();
	}

	/**
	 * Returns a list of {@link GarbageCollectorMXBean} objects in the Java virtual machine. The Java virtual machine may have one or more <tt>GarbageCollectorMXBean</tt> objects. It may add or remove
	 * <tt>GarbageCollectorMXBean</tt> during execution.
	 *
	 * @return a list of <tt>GarbageCollectorMXBean</tt> objects.
	 *
	 */
	public static List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
		return ManagementFactory.getGarbageCollectorMXBeans();
	}

	/**
	 * 取得Java Virtual Machine Specification的信息。
	 * 
	 * @return <code>JvmSpecInfo</code>对象
	 */
	public static JvmSpecInfo getJvmSpecInfo() {
		return Singleton.get(JvmSpecInfo.class);
	}

	/**
	 * 取得Java Virtual Machine Implementation的信息。
	 * 
	 * @return <code>JvmInfo</code>对象
	 */
	public static JvmInfo getJvmInfo() {
		return Singleton.get(JvmInfo.class);
	}

	/**
	 * 取得Java Specification的信息。
	 * 
	 * @return <code>JavaSpecInfo</code>对象
	 */
	public static JavaSpecInfo getJavaSpecInfo() {
		return Singleton.get(JavaSpecInfo.class);
	}

	/**
	 * 取得Java Implementation的信息。
	 * 
	 * @return <code>JavaInfo</code>对象
	 */
	public static JavaInfo getJavaInfo() {
		return Singleton.get(JavaInfo.class);
	}

	/**
	 * 取得当前运行的JRE的信息。
	 * 
	 * @return <code>JreInfo</code>对象
	 */
	public static JavaRuntimeInfo getJavaRuntimeInfo() {
		return Singleton.get(JavaRuntimeInfo.class);
	}

	/**
	 * 取得OS的信息。
	 * 
	 * @return <code>OsInfo</code>对象
	 */
	public static OsInfo getOsInfo() {
		return Singleton.get(OsInfo.class);
	}

	/**
	 * 取得User的信息。
	 * 
	 * @return <code>UserInfo</code>对象
	 */
	public static UserInfo getUserInfo() {
		return Singleton.get(UserInfo.class);
	}

	/**
	 * 取得Host的信息。
	 * 
	 * @return <code>HostInfo</code>对象
	 */
	public static HostInfo getHostInfo() {
		return Singleton.get(HostInfo.class);
	}

	/**
	 * 取得Runtime的信息。
	 * 
	 * @return <code>RuntimeInfo</code>对象
	 */
	public static RuntimeInfo getRuntimeInfo() {
		return Singleton.get(RuntimeInfo.class);
	}

	// ------------------------------------------------------------------ Dump
	/**
	 * 将系统信息输出到<code>System.out</code>中。
	 */
	public static void dumpSystemInfo() {
		dumpSystemInfo(new PrintWriter(System.out));
	}

	/**
	 * 将系统信息输出到指定<code>PrintWriter</code>中。
	 * 
	 * @param out <code>PrintWriter</code>输出流
	 */
	public static void dumpSystemInfo(PrintWriter out) {
		out.println("--------------");
		out.println(getJvmSpecInfo());
		out.println("--------------");
		out.println(getJvmInfo());
		out.println("--------------");
		out.println(getJavaSpecInfo());
		out.println("--------------");
		out.println(getJavaInfo());
		out.println("--------------");
		out.println(getJavaRuntimeInfo());
		out.println("--------------");
		out.println(getOsInfo());
		out.println("--------------");
		out.println(getUserInfo());
		out.println("--------------");
		out.println(getHostInfo());
		out.println("--------------");
		out.println(getRuntimeInfo());
		out.println("--------------");
		out.flush();
	}

	/**
	 * 输出到<code>StringBuilder</code>。
	 * 
	 * @param builder <code>StringBuilder</code>对象
	 * @param caption 标题
	 * @param value 值
	 */
	protected static void append(StringBuilder builder, String caption, Object value) {
		builder.append(caption).append(StrUtil.nullToDefault(Convert.toStr(value), "[n/a]")).append("\n");
	}
}
