/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.reflect.method;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;

import java.beans.Introspector;

/**
 * 方法名相关工具，如生成Getter和Setter方法，获取其字段名等。
 *
 * @author Looly
 * @since 6.0.0
 */
public class MethodNameUtil {

	/**
	 * getter方法前缀
	 */
	public static final String GET_PREFIX = "get";
	/**
	 * setter方法前缀
	 */
	public static final String SET_PREFIX = "set";
	/**
	 * is方法前缀
	 */
	public static final String IS_PREFIX = "is";

	/**
	 * 转换名称为标准的字段名称，规则为：
	 * <ul>
	 *     <li>首字母小写</li>
	 *     <li>转悠名字保持大写（第一个和第二个字母均为大写）</li>
	 * </ul>
	 *
	 * <pre>
	 * Name =》name
	 * name =》name
	 * CPU  =》CPU
	 * </pre>
	 *
	 * @param name 字段名称
	 * @return 转换后的名称
	 * @see java.beans.Introspector#decapitalize(String)
	 */
	public static String decapitalize(final CharSequence name) {
		return Introspector.decapitalize(StrUtil.toStringOrNull(name));
	}

	/**
	 * 获得set或get或is方法对应的标准属性名<br>
	 * 例如：setName 返回 name
	 *
	 * <pre>
	 * getName =》name
	 * setName =》name
	 * isName  =》name
	 * </pre>
	 * <p>
	 * 需要注意的是，相比{@link java.beans.Introspector#decapitalize(String)}，此方法始终小写第一个字符。
	 *
	 * @param getOrSetMethodName Get或Set方法名
	 * @return 如果是set或get方法名，返回field， 否则null
	 * @see java.beans.Introspector#decapitalize(String)
	 */
	public static String getGeneralField(final CharSequence getOrSetMethodName) {
		Assert.notBlank(getOrSetMethodName);
		final String getOrSetMethodNameStr = getOrSetMethodName.toString();
		if (getOrSetMethodNameStr.startsWith(GET_PREFIX) || getOrSetMethodNameStr.startsWith(SET_PREFIX)) {
			return StrUtil.removePreAndLowerFirst(getOrSetMethodName, 3);
		} else if (getOrSetMethodNameStr.startsWith(IS_PREFIX)) {
			return StrUtil.removePreAndLowerFirst(getOrSetMethodName, 2);
		}
		return null;
	}

	/**
	 * 生成set方法名<br>
	 * 例如：name 返回 setName
	 *
	 * @param fieldName 属性名
	 * @return setXxx
	 */
	public static String genSetter(final CharSequence fieldName) {
		return StrUtil.upperFirstAndAddPre(fieldName, SET_PREFIX);
	}

	/**
	 * 生成get方法名
	 *
	 * @param fieldName 属性名
	 * @return getXxx
	 */
	public static String genGetter(final CharSequence fieldName) {
		return StrUtil.upperFirstAndAddPre(fieldName, GET_PREFIX);
	}
}
