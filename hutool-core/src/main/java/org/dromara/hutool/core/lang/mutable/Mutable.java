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

package org.dromara.hutool.core.lang.mutable;

import org.dromara.hutool.core.lang.Opt;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * <p>提供可变值类型接口 <br />
 *
 * 相较于{@link Opt}或{@link java.util.Optional}，该所有实现类中的方法都<b>不区分值是否为{@code null}</b>，
 * 因此在使用前需要自行判断值是否为{@code null}，
 * 确保不会因为{@code null}值而抛出{@link NullPointerException}的情况。
 *
 * @param <T> 值得类型
 * @since 3.0.1
 */
public interface Mutable<T> {

	// ==================== factory methods ====================

	/**
	 * 创建一个{@link MutableBool}对象
	 *
	 * @param value 值
	 * @return {@link MutableBool}
	 */
	static MutableBool of(final boolean value) {
		return new MutableBool(value);
	}

	/**
	 * 创建一个{@link MutableByte}对象
	 *
	 * @param value 值
	 * @return {@link MutableByte}
	 */
	static MutableByte of(final byte value) {
		return new MutableByte(value);
	}

	/**
	 * 创建一个{@link MutableFloat}对象
	 *
	 * @param value 值
	 * @return {@link MutableFloat}
	 */
	static MutableFloat of(final float value) {
		return new MutableFloat(value);
	}

	/**
	 * 创建一个{@link MutableInt}对象
	 *
	 * @param value 值
	 * @return {@link MutableInt}
	 */
	static MutableInt of(final int value) {
		return new MutableInt(value);
	}

	/**
	 * 创建一个{@link MutableLong}对象
	 *
	 * @param value 值
	 * @return {@link MutableLong}
	 */
	static MutableLong of(final long value) {
		return new MutableLong(value);
	}

	/**
	 * 创建一个{@link MutableDouble}对象
	 *
	 * @param value 值
	 * @return {@link MutableDouble}
	 */
	static MutableDouble of(final double value) {
		return new MutableDouble(value);
	}

	/**
	 * 创建一个{@link MutableShort}对象
	 *
	 * @param value 值
	 * @return {@link MutableShort}
	 */
	static MutableShort of(final short value) {
		return new MutableShort(value);
	}

	/**
	 * 创建一个{@link MutableObj}对象
	 *
	 * @param value 值
	 * @return {@link MutableObj}
	 */
	static <T> MutableObj<T> of(final T value) {
		return new MutableObj<>(value);
	}

	// ==================== base methods ====================

	/**
	 * 获得原始值
	 * @return 原始值
	 */
	T get();

	/**
	 * 设置值
	 * @param value 值
	 */
	void set(T value);

	/**
	 * 根据操作修改值
	 *
	 * @param operator 操作
	 * @return 值
	 */
	default Mutable<T> map(final UnaryOperator<T> operator) {
		set(operator.apply(get()));
		return this;
	}

	/**
	 * 检查并操作值
	 *
	 * @param consumer 操作
	 * @return 当前对象
	 */
	default Mutable<T> peek(final Consumer<T> consumer) {
		consumer.accept(get());
		return this;
	}

	/**
	 * 检查值是否满足条件
	 *
	 * @param predicate 条件
	 * @return 是否满足条件
	 */
	default boolean test(final Predicate<T> predicate) {
		return predicate.test(get());
	}

	/**
	 * 获取值，并将值转换为{@link Opt}
	 *
	 * @return {@link Opt}
	 */
	default Opt<T> toOpt() {
		return to(Opt::ofNullable);
	}

	/**
	 * 获取值，并将值转换为指定类型。<br>
	 * 注意，值为null时，转换函数依然会被调用。
	 *
	 * @param function 转换函数
	 * @param <R> 转换后的类型
	 * @return 转换后的值
	 */
	default <R> R to(final Function<T, R> function) {
		Objects.requireNonNull(function);
		return function.apply(get());
	}
}
