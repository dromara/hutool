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

package org.dromara.hutool.management;

import org.dromara.hutool.util.SystemUtil;

import java.io.Serializable;

/**
 * 代表Java Virutal Machine Specification的信息。
 */
public class JvmSpecInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private final String JAVA_VM_SPECIFICATION_NAME = SystemUtil.get("java.vm.specification.name", false);
	private final String JAVA_VM_SPECIFICATION_VERSION = SystemUtil.get("java.vm.specification.version", false);
	private final String JAVA_VM_SPECIFICATION_VENDOR = SystemUtil.get("java.vm.specification.vendor", false);

	/**
	 * 取得当前JVM spec.的名称（取自系统属性：{@code java.vm.specification.name}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "Java Virtual Machine Specification"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getName() {
		return JAVA_VM_SPECIFICATION_NAME;
	}

	/**
	 * 取得当前JVM spec.的版本（取自系统属性：{@code java.vm.specification.version}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "1.0"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getVersion() {
		return JAVA_VM_SPECIFICATION_VERSION;
	}

	/**
	 * 取得当前JVM spec.的厂商（取自系统属性：{@code java.vm.specification.vendor}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "Sun Microsystems Inc."}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getVendor() {
		return JAVA_VM_SPECIFICATION_VENDOR;
	}

	/**
	 * 将Java Virutal Machine Specification的信息转换成字符串。
	 *
	 * @return JVM spec.的字符串表示
	 */
	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		ManagementUtil.append(builder, "JavaVM Spec. Name:    ", getName());
		ManagementUtil.append(builder, "JavaVM Spec. Version: ", getVersion());
		ManagementUtil.append(builder, "JavaVM Spec. Vendor:  ", getVendor());

		return builder.toString();
	}

}
