package cn.hutool.core.util;

import cn.hutool.core.text.StrUtil;

/**
 * JDK相关工具类，包括判断JDK版本等<br>
 * 工具部分方法来自fastjson2的JDKUtils
 *
 * @author fastjson, looly
 */
public class JdkUtil {
	/**
	 * JDK版本
	 */
	public static final int JVM_VERSION;

	static {
		JVM_VERSION = _getJvmVersion();
	}

	/**
	 * 是否JDK8
	 *
	 * @return 是否JDK8
	 */
	public static boolean isJdk8() {
		return 8 == JVM_VERSION;
	}

	/**
	 * 根据{@code java.specification.version}属性值，获取版本号
	 *
	 * @return 版本号
	 */
	private static int _getJvmVersion() {
		int jvmVersion = -1;

		String javaSpecVer = System.getProperty("java.specification.version");
		if (StrUtil.isNotBlank(javaSpecVer)) {
			if (javaSpecVer.startsWith("1.")) {
				javaSpecVer = javaSpecVer.substring(2);
			}
			if (javaSpecVer.indexOf('.') == -1) {
				jvmVersion = Integer.parseInt(javaSpecVer);
			}
		}

		return jvmVersion;
	}
}
