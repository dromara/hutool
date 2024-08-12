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

	/**
	 * 枚举名称
	 *
	 * @return 名称
	 */
	String name();

	/**
	 * 在中文语境下，多数时间枚举会配合一个中文说明
	 *
	 * @return enum名
	 */
	default String text() {
		return name();
	}

	/**
	 * int值
	 *
	 * @return int值
	 */
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

