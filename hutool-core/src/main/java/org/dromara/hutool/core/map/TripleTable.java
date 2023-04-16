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

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.tuple.Triple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 三值表结构，可重复<br>
 * 用于提供三种值相互查找操作<br>
 * 查找方式为indexOf方式遍历查找，数据越多越慢。
 *
 * @param <L> 左值类型
 * @param <M> 中值类型
 * @param <R> 右值类型
 * @author looly
 * @since 6.0.0
 */
public class TripleTable<L, M, R> implements Serializable {
	private static final long serialVersionUID = 1L;

	private final List<L> lList;
	private final List<M> mList;
	private final List<R> rList;

	/**
	 * 构造
	 *
	 * @param triples 三元组列表
	 */
	public TripleTable(final List<Triple<L, M, R>> triples) {
		this(Assert.notNull(triples).size());
		for (final Triple<L, M, R> triple : triples) {
			put(triple.getLeft(), triple.getMiddle(), triple.getRight());
		}
	}

	/**
	 * 构造
	 *
	 * @param size 初始容量
	 */
	public TripleTable(final int size) {
		this(new ArrayList<>(size), new ArrayList<>(size), new ArrayList<>(size));
	}

	/**
	 * @param lList 左列表
	 * @param mList 中列表
	 * @param rList 右列表
	 */
	public TripleTable(final List<L> lList, final List<M> mList, final List<R> rList) {
		Assert.notNull(lList);
		Assert.notNull(mList);
		Assert.notNull(rList);
		final int size = lList.size();
		if (size != mList.size() || size != rList.size()) {
			throw new IllegalArgumentException("List size must be equals!");
		}

		this.lList = lList;
		this.mList = mList;
		this.rList = rList;
	}

	// region ----- getLeft

	/**
	 * 通过中间值，查找左边值<br>
	 * 如果有多个重复值，只返回找到的第一个值
	 *
	 * @param mValue 中间值
	 * @return 左边值，未找到返回{@code null}
	 */
	public L getLeftByMiddle(final M mValue) {
		final int index = this.mList.indexOf(mValue);
		if (index > -1) {
			return this.lList.get(index);
		}
		return null;
	}

	/**
	 * 通过右值，查找左边值<br>
	 * 如果有多个重复值，只返回找到的第一个值
	 *
	 * @param rValue 右值
	 * @return 左边值，未找到返回{@code null}
	 */
	public L getLeftByRight(final R rValue) {
		final int index = this.rList.indexOf(rValue);
		if (index > -1) {
			return this.lList.get(index);
		}
		return null;
	}
	// endregion

	// region ----- getMiddle

	/**
	 * 通过左值，查找中值<br>
	 * 如果有多个重复值，只返回找到的第一个值
	 *
	 * @param lValue 左值
	 * @return 中值，未找到返回{@code null}
	 */
	public M getMiddleByLeft(final L lValue) {
		final int index = this.lList.indexOf(lValue);
		if (index > -1) {
			return this.mList.get(index);
		}
		return null;
	}

	/**
	 * 通过右值，查找中值<br>
	 * 如果有多个重复值，只返回找到的第一个值
	 *
	 * @param rValue 右值
	 * @return 中值，未找到返回{@code null}
	 */
	public M getMiddleByRight(final R rValue) {
		final int index = this.rList.indexOf(rValue);
		if (index > -1) {
			return this.mList.get(index);
		}
		return null;
	}
	// endregion

	// region ----- getRight

	/**
	 * 通过左值，查找右值<br>
	 * 如果有多个重复值，只返回找到的第一个值
	 *
	 * @param lValue 左值
	 * @return 右值，未找到返回{@code null}
	 */
	public R getRightByLeft(final L lValue) {
		final int index = this.lList.indexOf(lValue);
		if (index > -1) {
			return this.rList.get(index);
		}
		return null;
	}

	/**
	 * 通过中间值，查找右值<br>
	 * 如果有多个重复值，只返回找到的第一个值
	 *
	 * @param mValue 中间值
	 * @return 右值，未找到返回{@code null}
	 */
	public R getRightByMiddle(final M mValue) {
		final int index = this.mList.indexOf(mValue);
		if (index > -1) {
			return this.rList.get(index);
		}
		return null;
	}
	// endregion

	// region ----- getBy

	/**
	 * 通过左值查找三元组（所有值）
	 *
	 * @param lValue 左值
	 * @return 三元组（所有值）
	 */
	public Triple<L, M, R> getByLeft(final L lValue) {
		final int index = this.lList.indexOf(lValue);
		if (index > -1) {
			return new Triple<>(
				lList.get(index),
				mList.get(index),
				rList.get(index)
			);
		}
		return null;
	}

	/**
	 * 通过中值查找三元组（所有值）
	 *
	 * @param mValue 中值
	 * @return 三元组（所有值）
	 */
	public Triple<L, M, R> getByMiddle(final M mValue) {
		final int index = this.mList.indexOf(mValue);
		if (index > -1) {
			return new Triple<>(
				lList.get(index),
				mList.get(index),
				rList.get(index)
			);
		}
		return null;
	}

	/**
	 * 通过右值查找三元组（所有值）
	 *
	 * @param rValue 右值
	 * @return 三元组（所有值）
	 */
	public Triple<L, M, R> getByRight(final R rValue) {
		final int index = this.rList.indexOf(rValue);
		if (index > -1) {
			return new Triple<>(
				lList.get(index),
				mList.get(index),
				rList.get(index)
			);
		}
		return null;
	}
	// endregion

	/**
	 * 长度
	 *
	 * @return this
	 */
	public int size() {
		return this.lList.size();
	}

	/**
	 * 加入值
	 *
	 * @param lValue 左值
	 * @param mValue 中值
	 * @param rValue 右值
	 * @return this
	 */
	public TripleTable<L, M, R> put(final L lValue, final M mValue, final R rValue) {
		this.lList.add(lValue);
		this.mList.add(mValue);
		this.rList.add(rValue);
		return this;
	}

	/**
	 * 清空
	 *
	 * @return this
	 */
	public TripleTable<L, M, R> clear() {
		this.lList.clear();
		this.mList.clear();
		this.rList.clear();
		return this;
	}

	/**
	 * 移除值
	 *
	 * @param index 序号
	 * @return this
	 */
	public TripleTable<L, M, R> remove(final int index) {
		this.lList.remove(index);
		this.mList.remove(index);
		this.rList.remove(index);
		return this;
	}
}
