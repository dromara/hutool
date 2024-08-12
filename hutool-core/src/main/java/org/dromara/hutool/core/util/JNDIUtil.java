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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.map.MapUtil;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Map;

/**
 * JNDI工具类<br>
 * JNDI是Java Naming and Directory Interface（JAVA命名和目录接口）的英文简写，<br>
 * 它是为JAVA应用程序提供命名和目录访问服务的API（Application Programing Interface，应用程序编程接口）。
 *
 * <p>
 * 见：<a href="https://blog.csdn.net/u010430304/article/details/54601302">https://blog.csdn.net/u010430304/article/details/54601302</a>
 * </p>
 *
 * @author loolY
 * @since 5.7.7
 */
public class JNDIUtil {

	/**
	 * 创建{@link InitialDirContext}
	 *
	 * @param environment 环境参数，{@code null}表示无参数
	 * @return {@link InitialDirContext}
	 */
	public static InitialDirContext createInitialDirContext(final Map<String, String> environment) {
		try {
			if (MapUtil.isEmpty(environment)) {
				return new InitialDirContext();
			}
			return new InitialDirContext(Convert.convert(Hashtable.class, environment));
		} catch (final NamingException e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 创建{@link InitialContext}
	 *
	 * @param environment 环境参数，{@code null}表示无参数
	 * @return {@link InitialContext}
	 */
	public static InitialContext createInitialContext(final Map<String, String> environment) {
		try {
			if (MapUtil.isEmpty(environment)) {
				return new InitialContext();
			}
			return new InitialContext(Convert.convert(Hashtable.class, environment));
		} catch (final NamingException e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 获取指定容器环境的对象的属性<br>
	 * 如获取DNS属性，则URI为类似：dns:hutool.cn
	 *
	 * @param uri     URI字符串，格式为[scheme:][name]/[domain]
	 * @param attrIds 需要获取的属性ID名称
	 * @return {@link Attributes}
	 */
	public static Attributes getAttributes(final String uri, final String... attrIds) {
		try {
			return createInitialDirContext(null).getAttributes(uri, attrIds);
		} catch (final NamingException e) {
			throw new HutoolException(e);
		}
	}
}
