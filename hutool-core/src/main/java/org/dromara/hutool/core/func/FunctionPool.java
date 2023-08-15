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

package org.dromara.hutool.core.func;

import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.util.JdkUtil;

import java.lang.reflect.Constructor;
import java.util.function.BiFunction;

/**
 * 常用Lambda函数封装<br>
 * 提供常用对象方法的Lambda包装，减少Lambda初始化时间。
 *
 * @author looly
 */
@SuppressWarnings("unchecked")
public class FunctionPool {

	/**
	 * 通过{@code String(char[] value, boolean share)}这个内部构造生成一个Lambda函数<br>
	 * 此函数通过传入char[]，实现zero-copy的String创建，效率很高。但是要求传入的char[]不可以在其他地方修改。<br>
	 * 此函数只支持JKDK8
	 */
	public static final BiFunction<char[], Boolean, String> STRING_CREATOR_JDK8;

	static {
		final Constructor<String> constructor = ConstructorUtil.getConstructor(String.class, char[].class, boolean.class);
		STRING_CREATOR_JDK8 = JdkUtil.IS_JDK8 ? LambdaFactory.build(BiFunction.class, constructor) : null;
	}

	/**
	 * 通过{@code String(char[] value, boolean share)}这个内部构造创建String对象。<br>
	 * 此函数通过传入char[]，实现zero-copy的String创建，效率很高。但是要求传入的char[]不可以在其他地方修改。
	 *
	 * @param value char[]值，注意这个数组不可修改！！
	 * @return String
	 */
	public static String createString(final char[] value) {
		if(JdkUtil.IS_JDK8){
			return STRING_CREATOR_JDK8.apply(value, true);
		} else {
			// TODO JDK9+优化
			return new String(value);
		}
	}
}
