package cn.hutool.system;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.StrUtil;

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

/**
 * Java的System类封装工具类。<br>
 * 参考：http://blog.csdn.net/zhongweijian/article/details/7619383
 *
 * @author Looly
 */
public class SystemUtil {

	// ----- Java运行时环境信息 -----/
	/**
	 * Java 运行时环境规范名称的KEY
	 */
	public final static String SPECIFICATION_NAME = SystemPropsKeys.SPECIFICATION_NAME;
	/**
	 * Java 运行时环境版本的KEY
	 */
	public final static String VERSION = SystemPropsKeys.VERSION;
	/**
	 * Java 运行时环境规范版本的KEY
	 */
	public final static String SPECIFICATION_VERSION = SystemPropsKeys.SPECIFICATION_VERSION;
	/**
	 * Java 运行时环境供应商的KEY
	 */
	public final static String VENDOR = SystemPropsKeys.VENDOR;
	/**
	 * Java 运行时环境规范供应商的KEY
	 */
	public final static String SPECIFICATION_VENDOR = SystemPropsKeys.SPECIFICATION_VENDOR;
	/**
	 * Java 供应商的 URL的KEY
	 */
	public final static String VENDOR_URL = SystemPropsKeys.VENDOR_URL;
	/**
	 * Java 安装目录的KEY
	 */
	public final static String HOME = SystemPropsKeys.HOME;
	/**
	 * 加载库时搜索的路径列表的KEY
	 */
	public final static String LIBRARY_PATH = SystemPropsKeys.LIBRARY_PATH;
	/**
	 * 默认的临时文件路径的KEY
	 */
	public final static String TMPDIR = SystemPropsKeys.TMPDIR;
	/**
	 * 要使用的 JIT 编译器的名称的KEY
	 */
	public final static String COMPILER = SystemPropsKeys.COMPILER;
	/**
	 * 一个或多个扩展目录的路径的KEY
	 */
	public final static String EXT_DIRS = SystemPropsKeys.EXT_DIRS;

	// ----- Java虚拟机信息 -----/
	/**
	 * Java 虚拟机实现名称的KEY
	 */
	public final static String VM_NAME = SystemPropsKeys.VM_NAME;
	/**
	 * Java 虚拟机规范名称的KEY
	 */
	public final static String VM_SPECIFICATION_NAME = SystemPropsKeys.VM_SPECIFICATION_NAME;
	/**
	 * Java 虚拟机实现版本的KEY
	 */
	public final static String VM_VERSION = SystemPropsKeys.VM_VERSION;
	/**
	 * Java 虚拟机规范版本的KEY
	 */
	public final static String VM_SPECIFICATION_VERSION = SystemPropsKeys.VM_SPECIFICATION_VERSION;
	/**
	 * Java 虚拟机实现供应商的KEY
	 */
	public final static String VM_VENDOR = SystemPropsKeys.VM_VENDOR;
	/**
	 * Java 虚拟机规范供应商的KEY
	 */
	public final static String VM_SPECIFICATION_VENDOR = SystemPropsKeys.VM_SPECIFICATION_VENDOR;

	// ----- Java类信息 -----/
	/**
	 * Java 类格式版本号的KEY
	 */
	public final static String CLASS_VERSION = SystemPropsKeys.CLASS_VERSION;
	/**
	 * Java 类路径的KEY
	 */
	public final static String CLASS_PATH = SystemPropsKeys.CLASS_PATH;

	// ----- OS信息 -----/
	/**
	 * 操作系统的名称的KEY
	 */
	public final static String OS_NAME = SystemPropsKeys.OS_NAME;
	/**
	 * 操作系统的架构的KEY
	 */
	public final static String OS_ARCH = SystemPropsKeys.OS_ARCH;
	/**
	 * 操作系统的版本的KEY
	 */
	public final static String OS_VERSION = SystemPropsKeys.OS_VERSION;
	/**
	 * 文件分隔符（在 UNIX 系统中是“/”）的KEY
	 */
	public final static String FILE_SEPARATOR = SystemPropsKeys.FILE_SEPARATOR;
	/**
	 * 路径分隔符（在 UNIX 系统中是“:”）的KEY
	 */
	public final static String PATH_SEPARATOR = SystemPropsKeys.PATH_SEPARATOR;
	/**
	 * 行分隔符（在 UNIX 系统中是“\n”）的KEY
	 */
	public final static String LINE_SEPARATOR = SystemPropsKeys.LINE_SEPARATOR;

	// ----- 用户信息 -----/
	/**
	 * 用户的账户名称的KEY
	 */
	public final static String USER_NAME = SystemPropsKeys.USER_NAME;
	/**
	 * 用户的主目录的KEY
	 */
	public final static String USER_HOME = SystemPropsKeys.USER_HOME;
	/**
	 * 用户的当前工作目录的KEY
	 */
	public final static String USER_DIR = SystemPropsKeys.USER_DIR;

	// ----------------------------------------------------------------------- Basic start

	/**
	 * 取得系统属性，如果因为Java安全的限制而失败，则将错误打在Log中，然后返回 defaultValue
	 *
	 * @param name         属性名
	 * @param defaultValue 默认值
	 * @return 属性值或defaultValue
	 * @see System#getProperty(String)
	 * @see System#getenv(String)
	 */
	public static String get(String name, String defaultValue) {
		return StrUtil.nullToDefault(get(name, false), defaultValue);
	}

	/**
	 * 取得系统属性，如果因为Java安全的限制而失败，则将错误打在Log中，然后返回 {@code null}
	 *
	 * @param name  属性名
	 * @param quiet 安静模式，不将出错信息打在{@code System.err}中
	 * @return 属性值或{@code null}
	 * @see System#getProperty(String)
	 * @see System#getenv(String)
	 */
	public static String get(String name, boolean quiet) {
		String value = null;
		try {
			value = System.getProperty(name);
		} catch (SecurityException e) {
			if (false == quiet) {
				Console.error("Caught a SecurityException reading the system property '{}'; " +
						"the SystemUtil property value will default to null.", name);
			}
		}

		if (null == value) {
			try {
				value = System.getenv(name);
			} catch (SecurityException e) {
				if (false == quiet) {
					Console.error("Caught a SecurityException reading the system env '{}'; " +
							"the SystemUtil env value will default to null.", name);
				}
			}
		}

		return value;
	}

	/**
	 * 获得System属性
	 *
	 * @param key 键
	 * @return 属性值
	 * @see System#getProperty(String)
	 * @see System#getenv(String)
	 */
	public static String get(String key) {
		return get(key, null);
	}

	/**
	 * 获得boolean类型值
	 *
	 * @param key          键
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

		return Convert.toBool(value, defaultValue);
	}

	/**
	 * 获得int类型值
	 *
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static long getInt(String key, int defaultValue) {
		return Convert.toInt(get(key), defaultValue);
	}

	/**
	 * 获得long类型值
	 *
	 * @param key          键
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
	 * 如果没有编译系统，则返回{@code null}
	 *
	 * @return a {@link CompilationMXBean} ，如果没有编译系统，则返回{@code null}
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
	 * 获取Java虚拟机中的{@link MemoryPoolMXBean}列表<br>
	 * The Java virtual machine can have one or more memory pools. It may add or remove memory pools during execution.
	 *
	 * @return a list of <tt>MemoryPoolMXBean</tt> objects.
	 */
	public static List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
		return ManagementFactory.getMemoryPoolMXBeans();
	}

	/**
	 * 获取Java虚拟机中的{@link MemoryManagerMXBean}列表<br>
	 * The Java virtual machine can have one or more memory managers. It may add or remove memory managers during execution.
	 *
	 * @return a list of <tt>MemoryManagerMXBean</tt> objects.
	 */
	public static List<MemoryManagerMXBean> getMemoryManagerMXBeans() {
		return ManagementFactory.getMemoryManagerMXBeans();
	}

	/**
	 * 获取Java虚拟机中的{@link GarbageCollectorMXBean}列表
	 *
	 * @return {@link GarbageCollectorMXBean}列表
	 */
	public static List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
		return ManagementFactory.getGarbageCollectorMXBeans();
	}

	/**
	 * 取得Java Virtual Machine Specification的信息。
	 *
	 * @return {@link JvmSpecInfo}对象
	 */
	public static JvmSpecInfo getJvmSpecInfo() {
		return Singleton.get(JvmSpecInfo.class);
	}

	/**
	 * 取得Java Virtual Machine Implementation的信息。
	 *
	 * @return {@link JvmInfo}对象
	 */
	public static JvmInfo getJvmInfo() {
		return Singleton.get(JvmInfo.class);
	}

	/**
	 * 取得Java Specification的信息。
	 *
	 * @return {@link JavaSpecInfo}对象
	 */
	public static JavaSpecInfo getJavaSpecInfo() {
		return Singleton.get(JavaSpecInfo.class);
	}

	/**
	 * 取得Java Implementation的信息。
	 *
	 * @return {@link JavaInfo}对象
	 */
	public static JavaInfo getJavaInfo() {
		return Singleton.get(JavaInfo.class);
	}

	/**
	 * 取得当前运行的JRE的信息。
	 *
	 * @return {@link JavaRuntimeInfo}对象
	 */
	public static JavaRuntimeInfo getJavaRuntimeInfo() {
		return Singleton.get(JavaRuntimeInfo.class);
	}

	/**
	 * 取得OS的信息。
	 *
	 * @return {@code OsInfo}对象
	 */
	public static OsInfo getOsInfo() {
		return Singleton.get(OsInfo.class);
	}

	/**
	 * 取得User的信息。
	 *
	 * @return {@code UserInfo}对象
	 */
	public static UserInfo getUserInfo() {
		return Singleton.get(UserInfo.class);
	}

	/**
	 * 取得Host的信息。
	 *
	 * @return {@link HostInfo}对象
	 */
	public static HostInfo getHostInfo() {
		return Singleton.get(HostInfo.class);
	}

	/**
	 * 取得Runtime的信息。
	 *
	 * @return {@link RuntimeInfo}对象
	 */
	public static RuntimeInfo getRuntimeInfo() {
		return Singleton.get(RuntimeInfo.class);
	}

	/**
	 * 获取JVM中内存总大小
	 *
	 * @return 内存总大小
	 * @since 4.5.4
	 */
	public static long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	/**
	 * 获取JVM中内存剩余大小
	 *
	 * @return 内存剩余大小
	 * @since 4.5.4
	 */
	public static long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	/**
	 * 获取JVM可用的内存总大小
	 *
	 * @return JVM可用的内存总大小
	 * @since 4.5.4
	 */
	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	/**
	 * 获取总线程数
	 *
	 * @return 总线程数
	 */
	public static int getTotalThreadCount() {
		ThreadGroup parentThread = Thread.currentThread().getThreadGroup();
		while (null != parentThread.getParent()) {
			parentThread = parentThread.getParent();
		}
		return parentThread.activeCount();
	}

	// ------------------------------------------------------------------ Dump

	/**
	 * 将系统信息输出到{@link System#out}中。
	 */
	public static void dumpSystemInfo() {
		dumpSystemInfo(new PrintWriter(System.out));
	}

	/**
	 * 将系统信息输出到指定{@link PrintWriter}中。
	 *
	 * @param out {@link PrintWriter}输出流
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
	 * 输出到{@link StringBuilder}。
	 *
	 * @param builder {@link StringBuilder}对象
	 * @param caption 标题
	 * @param value   值
	 */
	protected static void append(StringBuilder builder, String caption, Object value) {
		builder.append(caption).append(StrUtil.nullToDefault(Convert.toStr(value), "[n/a]")).append("\n");
	}
}
