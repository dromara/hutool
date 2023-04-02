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

package org.dromara.hutool.core.lang;

import java.io.Serializable;

/**
 * 枚举元素通用接口，在自定义枚举上实现此接口可以用于数据转换<br>
 * 数据库保存时建议保存 intVal()而非ordinal()防备需求变更<br>
 *
 * @param <E> Enum类型
 * @author nierjia
 * @since 5.4.2
 */
public interface EnumItem<E extends EnumItem<E>> extends Serializable {

	String name();

	/**
	 * 在中文语境下，多数时间枚举会配合一个中文说明
	 *
	 * @return enum名
	 */
	default String text() {
		return name();
	}

	int intVal();

	/**
	 * 获取所有枚举对象
	 *
	 * @return 枚举对象数组
	 */
	@SuppressWarnings("unchecked")
	default E[] items() {
		return (E[]) this.getClass().getEnumConstants();
	}

	/**
	 * 通过int类型值查找兄弟其他枚举
	 *
	 * @param intVal int值
	 * @return Enum
	 */
	default E fromInt(final Integer intVal) {
		if (intVal == null) {
			return null;
		}
		final E[] vs = items();
		for (final E enumItem : vs) {
			if (enumItem.intVal() == intVal) {
				return enumItem;
			}
		}
		return null;
	}

	/**
	 * 通过String类型的值转换，根据实现可以用name/text
	 *
	 * @param strVal String值
	 * @return Enum
	 */
	default E fromStr(final String strVal) {
		if (strVal == null) {
			return null;
		}
		final E[] vs = items();
		for (final E enumItem : vs) {
			if (strVal.equalsIgnoreCase(enumItem.name())) {
				return enumItem;
			}
		}
		return null;
	}


}

