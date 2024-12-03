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

package org.dromara.hutool.core.lang.range;

import org.dromara.hutool.core.lang.Assert;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 范围生成器。根据给定的初始值、结束值和步进生成一个步进列表生成器<br>
 * 由于用户自行实现{@link Stepper}来定义步进，因此Range本身无法判定边界（是否达到end），需在step实现边界判定逻辑。
 *
 * <p>
 * 此类使用{@link ReentrantReadWriteLock}保证线程安全
 * </p>
 *
 * @param <T> 生成范围对象的类型
 * @author Looly
 */
public class Range<T> implements Iterable<T>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 起始对象
	 */
	private final T start;
	/**
	 * 结束对象
	 */
	private final T end;

	/**
	 * 步进
	 */
	private final Stepper<T> stepper;
	/**
	 * 是否包含第一个元素
	 */
	private final boolean includeStart;
	/**
	 * 是否包含最后一个元素
	 */
	private final boolean includeEnd;

	/**
	 * 构造
	 *
	 * @param start   起始对象（包括）
	 * @param stepper 步进
	 */
	public Range(final T start, final Stepper<T> stepper) {
		this(start, null, stepper);
	}

	/**
	 * 构造
	 *
	 * @param start   起始对象（包含）
	 * @param end     结束对象（包含）
	 * @param stepper 步进
	 */
	public Range(final T start, final T end, final Stepper<T> stepper) {
		this(start, end, stepper, true, true);
	}

	/**
	 * 构造
	 *
	 * @param start          起始对象
	 * @param end            结束对象
	 * @param stepper        步进
	 * @param isIncludeStart 是否包含第一个元素
	 * @param isIncludeEnd   是否包含最后一个元素
	 */
	public Range(final T start, final T end, final Stepper<T> stepper, final boolean isIncludeStart, final boolean isIncludeEnd) {
		Assert.notNull(start, "First element must be not null!");
		this.start = start;
		this.end = end;
		this.stepper = stepper;
		this.includeStart = isIncludeStart;
		this.includeEnd = isIncludeEnd;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>(){
			/**
			 * 下一个对象
			 */
			private T next = safeStep(start);
			/**
			 * 索引
			 */
			private int index = 0;

			@Override
			public boolean hasNext() {
				if (0 == this.index && includeStart) {
					return true;
				}
				if (null == this.next) {
					return false;
				} else {
					return includeEnd || !this.next.equals(end);
				}
			}

			@Override
			public T next() {
				if (!this.hasNext()) {
					throw new NoSuchElementException("Has no next range!");
				}
				return nextUncheck();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Can not remove ranged element!");
			}

			/**
			 * 获取下一个元素，并将下下个元素准备好
			 */
			private T nextUncheck() {
				final T current;
				if(0 == this.index){
					current = start;
					if(!includeStart){
						// 获取下一组元素
						index ++;
						return nextUncheck();
					}
				} else {
					current = next;
					this.next = safeStep(this.next);
				}

				index++;
				return current;
			}

			/**
			 * 不抛异常的获取下一步进的元素，如果获取失败返回{@code null}
			 *
			 * @param base  上一个元素
			 * @return 下一步进
			 */
			private T safeStep(final T base) {
				final int index = this.index;
				T next = null;
				try {
					next = stepper.step(base, end, index);
				} catch (final Exception e) {
					// ignore
				}

				return next;
			}
		};
	}

	/**
	 * 步进接口，此接口用于实现如何对一个对象按照指定步进增加步进<br>
	 * 步进接口可以定义以下逻辑：
	 *
	 * <pre>
	 * 1、步进规则，即对象如何做步进
	 * 2、步进大小，通过实现此接口，在实现类中定义一个对象属性，可灵活定义步进大小
	 * 3、限制range个数，通过实现此接口，在实现类中定义一个对象属性，可灵活定义limit，限制range个数
	 * </pre>
	 *
	 * @param <T> 需要增加步进的对象
	 * @author Looly
	 */
	@FunctionalInterface
	public interface Stepper<T> {
		/**
		 * 增加步进<br>
		 * 增加步进后的返回值如果为{@code null}则表示步进结束<br>
		 * 用户需根据end参数自行定义边界，当达到边界时返回null表示结束，否则Range中边界对象无效，会导致无限循环
		 *
		 * @param current 上一次增加步进后的基础对象
		 * @param end     结束对象
		 * @param index   当前索引（步进到第几个元素），从0开始计数
		 * @return 增加步进后的对象
		 */
		T step(T current, T end, int index);
	}

	@Override
	public String toString() {
		return "Range [start=" + start + ", end=" + end + "]";
	}
}
