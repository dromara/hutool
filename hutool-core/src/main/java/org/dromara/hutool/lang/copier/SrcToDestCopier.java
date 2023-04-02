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

package org.dromara.hutool.lang.copier;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * 复制器抽象类<br>
 * 抽象复制器抽象了一个对象复制到另一个对象，通过实现{@link #copy()}方法实现复制逻辑。<br>
 *
 * @author Looly
 *
 * @param <T> 拷贝的对象
 * @param <C> 本类的类型。用于set方法返回本对象，方便流式编程
 * @since 3.0.9
 */
public abstract class SrcToDestCopier<T, C extends SrcToDestCopier<T, C>> implements Copier<T>, Serializable{
	private static final long serialVersionUID = 1L;

	/** 源 */
	protected T src;
	/** 目标 */
	protected T target;
	/** 拷贝过滤器，可以过滤掉不需要拷贝的源 */
	protected Predicate<T> copyPredicate;

	//-------------------------------------------------------------------------------------------------------- Getters and Setters start
	/**
	 * 获取源
	 * @return 源
	 */
	public T getSrc() {
		return src;
	}
	/**
	 * 设置源
	 *
	 * @param src 源
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public C setSrc(final T src) {
		this.src = src;
		return (C)this;
	}

	/**
	 * 获得目标
	 *
	 * @return 目标
	 */
	public T getTarget() {
		return target;
	}
	/**
	 * 设置目标
	 *
	 * @param target 目标
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public C setTarget(final T target) {
		this.target = target;
		return (C)this;
	}

	/**
	 * 获得过滤器
	 * @return 过滤器
	 */
	public Predicate<T> getCopyPredicate() {
		return copyPredicate;
	}
	/**
	 * 设置过滤器
	 *
	 * @param copyPredicate 过滤器
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public C setCopyPredicate(final Predicate<T> copyPredicate) {
		this.copyPredicate = copyPredicate;
		return (C)this;
	}
	//-------------------------------------------------------------------------------------------------------- Getters and Setters end
}
