/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.text.StrUtil;

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
	/**
	 * 是否JDK8<br>
	 * 由于Hutool基于JDK8编译，当使用JDK版本低于8时，不支持。
	 */
	public static final boolean IS_JDK8;
	/**
	 * 是否大于JDK8<br>
	 * 由于大于jdk8的部分功能，jdk有变化，需要做版本判断
	 */
	public static final boolean IS_GT_JDK8;
	/**
	 * 是否大于等于JDK17
	 */
	public static final boolean IS_AT_LEAST_JDK17;

	/**
	 * 是否Android环境
	 */
	public static final boolean IS_ANDROID;

	static {
		// JVM版本
		JVM_VERSION = _getJvmVersion();
		IS_JDK8 = 8 == JVM_VERSION;
		IS_GT_JDK8 = JVM_VERSION > 8;
		IS_AT_LEAST_JDK17 = JVM_VERSION >= 17;

		// JVM名称
		final String jvmName = _getJvmName();
		IS_ANDROID = jvmName.equals("Dalvik");
	}

	/**
	 * 获取JVM名称
	 *
	 * @return JVM名称
	 */
	private static String _getJvmName() {
		return SystemUtil.getQuietly("java.vm.name");
	}

	/**
	 * 根据{@code java.specification.version}属性值，获取版本号<br>
	 * 默认8
	 *
	 * @return 版本号
	 */
	private static int _getJvmVersion() {
		int jvmVersion = 8;

		String javaSpecVer = SystemUtil.getQuietly("java.specification.version");
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
