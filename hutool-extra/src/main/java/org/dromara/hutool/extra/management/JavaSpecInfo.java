/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.extra.management;

import org.dromara.hutool.core.util.SystemUtil;

import java.io.Serializable;

/**
 * 代表Java Specification的信息。
 */
public class JavaSpecInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private final String JAVA_SPECIFICATION_NAME = SystemUtil.get("java.specification.name", false);
	private final String JAVA_SPECIFICATION_VERSION = SystemUtil.get("java.specification.version", false);
	private final String JAVA_SPECIFICATION_VENDOR = SystemUtil.get("java.specification.vendor", false);

	/**
	 * 取得当前Java Spec.的名称（取自系统属性：{@code java.specification.name}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "Java Platform API Specification"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getName() {
		return JAVA_SPECIFICATION_NAME;
	}

	/**
	 * 取得当前Java Spec.的版本（取自系统属性：{@code java.specification.version}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "1.4"}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 * @since Java 1.3
	 */
	public final String getVersion() {
		return JAVA_SPECIFICATION_VERSION;
	}

	/**
	 * 取得当前Java Spec.的厂商（取自系统属性：{@code java.specification.vendor}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "Sun Microsystems Inc."}
	 * </p>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 *
	 */
	public final String getVendor() {
		return JAVA_SPECIFICATION_VENDOR;
	}

	/**
	 * 将Java Specification的信息转换成字符串。
	 *
	 * @return JVM spec.的字符串表示
	 */
	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		ManagementUtil.append(builder, "Java Spec. Name:    ", getName());
		ManagementUtil.append(builder, "Java Spec. Version: ", getVersion());
		ManagementUtil.append(builder, "Java Spec. Vendor:  ", getVendor());

		return builder.toString();
	}

}
