package cn.hutool.extra.management;

import cn.hutool.core.util.SystemUtil;

import java.io.Serializable;

/**
 * 代表Java Virtual Machine Implementation的信息。
 */
public class JvmInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private final String JAVA_VM_NAME = SystemUtil.get("java.vm.name", false);
	private final String JAVA_VM_VERSION = SystemUtil.get("java.vm.version", false);
	private final String JAVA_VM_VENDOR = SystemUtil.get("java.vm.vendor", false);
	private final String JAVA_VM_INFO = SystemUtil.get("java.vm.info", false);

	/**
	 * 取得当前JVM impl.的名称（取自系统属性：{@code java.vm.name}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "Java HotSpot(TM) Client VM"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getName() {
		return JAVA_VM_NAME;
	}

	/**
	 * 取得当前JVM impl.的版本（取自系统属性：{@code java.vm.version}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "1.4.2-b28"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getVersion() {
		return JAVA_VM_VERSION;
	}

	/**
	 * 取得当前JVM impl.的厂商（取自系统属性：{@code java.vm.vendor}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "Sun Microsystems Inc."}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getVendor() {
		return JAVA_VM_VENDOR;
	}

	/**
	 * 取得当前JVM impl.的信息（取自系统属性：{@code java.vm.info}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "mixed mode"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getInfo() {
		return JAVA_VM_INFO;
	}

	/**
	 * 将Java Virutal Machine Implementation的信息转换成字符串。
	 *
	 * @return JVM impl.的字符串表示
	 */
	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		ManagementUtil.append(builder, "JavaVM Name:    ", getName());
		ManagementUtil.append(builder, "JavaVM Version: ", getVersion());
		ManagementUtil.append(builder, "JavaVM Vendor:  ", getVendor());
		ManagementUtil.append(builder, "JavaVM Info:    ", getInfo());

		return builder.toString();
	}

}
