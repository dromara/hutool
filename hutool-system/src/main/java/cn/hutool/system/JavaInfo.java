package cn.hutool.system;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;

import java.io.Serializable;

/**
 * 代表Java Implementation的信息。
 */
public class JavaInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String JAVA_VERSION = SystemUtil.get("java.version", false);
	private final float JAVA_VERSION_FLOAT = getJavaVersionAsFloat();
	private final int JAVA_VERSION_INT = getJavaVersionAsInt();
	private final String JAVA_VENDOR = SystemUtil.get("java.vendor", false);
	private final String JAVA_VENDOR_URL = SystemUtil.get("java.vendor.url", false);

	private final boolean IS_JAVA_1_8 = getJavaVersionMatches("1.8");
	private final boolean IS_JAVA_9 = getJavaVersionMatches("9");
	private final boolean IS_JAVA_10 = getJavaVersionMatches("10");
	private final boolean IS_JAVA_11 = getJavaVersionMatches("11");
	private final boolean IS_JAVA_12 = getJavaVersionMatches("12");
	private final boolean IS_JAVA_13 = getJavaVersionMatches("13");
	private final boolean IS_JAVA_14 = getJavaVersionMatches("14");
	private final boolean IS_JAVA_15 = getJavaVersionMatches("15");
	private final boolean IS_JAVA_16 = getJavaVersionMatches("16");
	private final boolean IS_JAVA_17 = getJavaVersionMatches("17");
	private final boolean IS_JAVA_18 = getJavaVersionMatches("18");


	/**
	 * 取得当前Java impl.的版本（取自系统属性：{@code java.version}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "1.4.2"}
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 * @since Java 1.1
	 */
	public final String getVersion() {
		return JAVA_VERSION;
	}

	/**
	 * 取得当前Java impl.的版本（取自系统属性：{@code java.version}）。
	 *
	 * <p>
	 * 例如：
	 *
	 * <ul>
	 * <li>JDK 1.2：{@code 1.2f}。</li>
	 * <li>JDK 1.3.1：{@code 1.31f}</li>
	 * </ul>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code 0}。
	 */
	public final float getVersionFloat() {
		return JAVA_VERSION_FLOAT;
	}

	/**
	 * 取得当前Java impl.的版本（取自系统属性：{@code java.version}），java10及其之后的版本返回值为4位。
	 *
	 * <p>
	 * 例如：
	 *
	 * <ul>
	 * <li>JDK 1.2：{@code 120}。</li>
	 * <li>JDK 1.3.1：{@code 131}</li>
	 * <li>JDK 11.0.2：{@code 1102}</li>
	 * </ul>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code 0}。
	 * @since Java 1.1
	 */
	public final int getVersionInt() {
		return JAVA_VERSION_INT;
	}

	/**
	 * 取得当前Java impl.的版本的{@code float}值。
	 *
	 * @return Java版本的<code>float</code>值或{@code 0}
	 */
	private float getJavaVersionAsFloat() {
		if (JAVA_VERSION == null) {
			return 0f;
		}

		String str = JAVA_VERSION;

		str = ReUtil.get("^[0-9]{1,2}(\\.[0-9]{1,2})?", str, 0);

		return Float.parseFloat(str);
	}

	/**
	 * 取得当前Java impl.的版本的{@code int}值。
	 *
	 * @return Java版本的<code>int</code>值或{@code 0}
	 */
	private int getJavaVersionAsInt() {
		if (JAVA_VERSION == null) {
			return 0;
		}

		final String javaVersion = ReUtil.get("^[0-9]{1,2}(\\.[0-9]{1,2}){0,2}", JAVA_VERSION, 0);

		final String[] split = javaVersion.split("\\.");
		String result = ArrayUtil.join(split, "");

		//保证java10及其之后的版本返回的值为4位
		if (split[0].length() > 1) {
			result = (result + "0000").substring(0, 4);
		}

		return Integer.parseInt(result);
	}

	/**
	 * 取得当前Java impl.的厂商（取自系统属性：{@code java.vendor}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "Sun Microsystems Inc."}
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 * @since Java 1.1
	 */
	public final String getVendor() {
		return JAVA_VENDOR;
	}

	/**
	 * 取得当前Java impl.的厂商网站的URL（取自系统属性：{@code java.vendor.url}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "<a href="http://java.sun.com/">http://java.sun.com/</a>"}
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 * @since Java 1.1
	 */
	public final String getVendorURL() {
		return JAVA_VENDOR_URL;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为1.1，则返回{@code true}
	 */
	@Deprecated
	public final boolean isJava1_1() {
		return false;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为1.2，则返回{@code true}
	 */
	@Deprecated
	public final boolean isJava1_2() {
		return false;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为1.3，则返回{@code true}
	 */
	@Deprecated
	public final boolean isJava1_3() {
		return false;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为1.4，则返回{@code true}
	 */
	@Deprecated
	public final boolean isJava1_4() {
		return false;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为1.5，则返回{@code true}
	 */
	@Deprecated
	public final boolean isJava1_5() {
		return false;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为1.6，则返回{@code true}
	 */
	@Deprecated
	public final boolean isJava1_6() {
		return false;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为1.7，则返回{@code true}
	 */
	@Deprecated
	public final boolean isJava1_7() {
		return false;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为1.8，则返回{@code true}
	 */
	public final boolean isJava1_8() {
		return IS_JAVA_1_8;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为9，则返回{@code true}
	 */
	public final boolean isJava9() {
		return IS_JAVA_9;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为10，则返回{@code true}
	 */
	public final boolean isJava10() {
		return IS_JAVA_10;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为11，则返回{@code true}
	 */
	public final boolean isJava11() {
		return IS_JAVA_11;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为12，则返回{@code true}
	 */
	public final boolean isJava12() {
		return IS_JAVA_12;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为13，则返回{@code true}
	 */
	public final boolean isJava13() {
		return IS_JAVA_13;
	}


	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为14，则返回{@code true}
	 */
	public final boolean isJava14() {
		return IS_JAVA_14;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为15，则返回{@code true}
	 */
	public final boolean isJava15() {
		return IS_JAVA_15;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为16，则返回{@code true}
	 */
	public final boolean isJava16() {
		return IS_JAVA_16;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为17，则返回{@code true}
	 */
	public final boolean isJava17() {
		return IS_JAVA_17;
	}

	/**
	 * 判断当前Java的版本。
	 *
	 * <p>
	 * 如果不能取得系统属性{@code java.version}（因为Java安全限制），则总是返回 {@code false}
	 *
	 * @return 如果当前Java版本为18，则返回{@code true}
	 */
	public final boolean isJava18() {
		return IS_JAVA_18;
	}

	/**
	 * 匹配当前Java的版本。
	 *
	 * @param versionPrefix Java版本前缀
	 * @return 如果版本匹配，则返回{@code true}
	 */
	private boolean getJavaVersionMatches(final String versionPrefix) {
		if (JAVA_VERSION == null) {
			return false;
		}

		return JAVA_VERSION.startsWith(versionPrefix);
	}

	/**
	 * 判定当前Java的版本是否大于等于指定的版本号。
	 *
	 * <p>
	 * 例如：
	 *
	 *
	 * <ul>
	 * <li>测试JDK 1.2：{@code isJavaVersionAtLeast(1.2f)}</li>
	 * <li>测试JDK 1.2.1：{@code isJavaVersionAtLeast(1.31f)}</li>
	 * </ul>
	 *
	 * @param requiredVersion 需要的版本
	 * @return 如果当前Java版本大于或等于指定的版本，则返回{@code true}
	 */
	public final boolean isJavaVersionAtLeast(final float requiredVersion) {
		return getVersionFloat() >= requiredVersion;
	}

	/**
	 * 判定当前Java的版本是否大于等于指定的版本号。
	 *
	 * <p>
	 * 例如：
	 *
	 *
	 * <ul>
	 * <li>测试JDK 1.2：{@code isJavaVersionAtLeast(120)}</li>
	 * <li>测试JDK 1.2.1：{@code isJavaVersionAtLeast(131)}</li>
	 * </ul>
	 *
	 * @param requiredVersion 需要的版本
	 * @return 如果当前Java版本大于或等于指定的版本，则返回{@code true}
	 */
	public final boolean isJavaVersionAtLeast(final int requiredVersion) {
		return getVersionInt() >= requiredVersion;
	}

	/**
	 * 将Java Implementation的信息转换成字符串。
	 *
	 * @return JVM impl.的字符串表示
	 */
	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		SystemUtil.append(builder, "Java Version:    ", getVersion());
		SystemUtil.append(builder, "Java Vendor:     ", getVendor());
		SystemUtil.append(builder, "Java Vendor URL: ", getVendorURL());

		return builder.toString();
	}

}
