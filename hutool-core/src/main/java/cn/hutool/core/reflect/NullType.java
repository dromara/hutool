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

package cn.hutool.core.reflect;

import java.lang.reflect.Type;

/**
 * 空类型表示
 *
 * @author looly
 * @since 6.0.0
 */
public class NullType implements Type {
	/**
	 * 单例对象
	 */
	public static NullType INSTANCE = new NullType();

	private NullType(){}

	@Override
	public String toString() {
		return "Type of null";
	}
}
