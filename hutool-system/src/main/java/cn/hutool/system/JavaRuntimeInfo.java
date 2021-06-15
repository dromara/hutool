package cn.hutool.system;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * 代表当前运行的JRE的信息。
 */
public class JavaRuntimeInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private final String JAVA_RUNTIME_NAME = SystemUtil.get("java.runtime.name", false);
	private final String JAVA_RUNTIME_VERSION = SystemUtil.get("java.runtime.version", false);
	private final String JAVA_HOME = SystemUtil.get("java.home", false);
	private final String JAVA_EXT_DIRS = SystemUtil.get("java.ext.dirs", false);
	private final String JAVA_ENDORSED_DIRS = SystemUtil.get("java.endorsed.dirs", false);
	private final String JAVA_CLASS_PATH = SystemUtil.get("java.class.path", false);
	private final String JAVA_CLASS_VERSION = SystemUtil.get("java.class.version", false);
	private final String JAVA_LIBRARY_PATH = SystemUtil.get("java.library.path", false);

	private final String SUN_BOOT_CLASS_PATH = SystemUtil.get("sun.boot.class.path", false);

	private final String SUN_ARCH_DATA_MODEL = SystemUtil.get("sun.arch.data.model", false);

	public final String getSunBoothClassPath() {
		return SUN_BOOT_CLASS_PATH;
	}

	/**
	 * JVM is 32M <code>or</code> 64M
	 *
	 * @return 32 <code>or</code> 64
	 */
	public final String getSunArchDataModel() {
		return SUN_ARCH_DATA_MODEL;
	}

	/**
	 * 取得当前JRE的名称（取自系统属性：<code>java.runtime.name</code>）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2： <code>"Java(TM) 2 Runtime Environment, Standard Edition"</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 * @since Java 1.3
	 */
	public final String getName() {
		return JAVA_RUNTIME_NAME;
	}

	/**
	 * 取得当前JRE的版本（取自系统属性：<code>java.runtime.version</code>）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"1.4.2-b28"</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 * @since Java 1.3
	 */
	public final String getVersion() {
		return JAVA_RUNTIME_VERSION;
	}

	/**
	 * 取得当前JRE的安装目录（取自系统属性：<code>java.home</code>）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"/opt/jdk1.4.2/jre"</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 * @since Java 1.1
	 */
	public final String getHomeDir() {
		return JAVA_HOME;
	}

	/**
	 * 取得当前JRE的扩展目录列表（取自系统属性：<code>java.ext.dirs</code>）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"/opt/jdk1.4.2/jre/lib/ext:..."</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 * @since Java 1.3
	 */
	public final String getExtDirs() {
		return JAVA_EXT_DIRS;
	}

	/**
	 * 取得当前JRE的endorsed目录列表（取自系统属性：<code>java.endorsed.dirs</code>）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"/opt/jdk1.4.2/jre/lib/endorsed:..."</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 * @since Java 1.4
	 */
	public final String getEndorsedDirs() {
		return JAVA_ENDORSED_DIRS;
	}

	/**
	 * 取得当前JRE的系统classpath（取自系统属性：<code>java.class.path</code>）。
	 *
	 * <p>
	 * 例如：<code>"/home/admin/myclasses:/home/admin/..."</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 * @since Java 1.1
	 */
	public final String getClassPath() {
		return JAVA_CLASS_PATH;
	}

	/**
	 * 取得当前JRE的系统classpath（取自系统属性：<code>java.class.path</code>）。
	 *
	 * <p>
	 * 例如：<code>"/home/admin/myclasses:/home/admin/..."</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 * @since Java 1.1
	 */
	public final String[] getClassPathArray() {
		return StrUtil.splitToArray(getClassPath(), SystemUtil.get("path.separator", false));
	}

	/**
	 * 取得当前JRE的class文件格式的版本（取自系统属性：<code>java.class.version</code>）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"48.0"</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 * @since Java 1.1
	 */
	public final String getClassVersion() {
		return JAVA_CLASS_VERSION;
	}

	/**
	 * 取得当前JRE的library搜索路径（取自系统属性：<code>java.library.path</code>）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"/opt/jdk1.4.2/bin:..."</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 */
	public final String getLibraryPath() {
		return JAVA_LIBRARY_PATH;
	}

	/**
	 * 取得当前JRE的library搜索路径（取自系统属性：<code>java.library.path</code>）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"/opt/jdk1.4.2/bin:..."</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 *
	 */
	public final String[] getLibraryPathArray() {
		return StrUtil.splitToArray(getLibraryPath(), SystemUtil.get("path.separator", false));
	}

	/**
	 * 取得当前JRE的URL协议packages列表（取自系统属性：<code>java.library.path</code>）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：<code>"sun.net.www.protocol|..."</code>
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
	 *
	 *
	 */
	public final String getProtocolPackages() {
		return SystemUtil.get("java.protocol.handler.pkgs", true);
	}

	/**
	 * 将当前运行的JRE信息转换成字符串。
	 *
	 * @return JRE信息的字符串表示
	 */
	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();

		SystemUtil.append(builder, "Java Runtime Name:      ", getName());
		SystemUtil.append(builder, "Java Runtime Version:   ", getVersion());
		SystemUtil.append(builder, "Java Home Dir:          ", getHomeDir());
		SystemUtil.append(builder, "Java Extension Dirs:    ", getExtDirs());
		SystemUtil.append(builder, "Java Endorsed Dirs:     ", getEndorsedDirs());
		SystemUtil.append(builder, "Java Class Path:        ", getClassPath());
		SystemUtil.append(builder, "Java Class Version:     ", getClassVersion());
		SystemUtil.append(builder, "Java Library Path:      ", getLibraryPath());
		SystemUtil.append(builder, "Java Protocol Packages: ", getProtocolPackages());

		return builder.toString();
	}

}
