package org.dromara.hutool.core.tuple;

import java.io.Serializable;
import java.util.Objects;

/**
 * 不可变三元组对象
 *
 * @param <L> 左值类型
 * @param <M> 中值类型
 * @param <R> 右值类型
 * @author kirno7
 * @since 6.0.0
 */
public class ImmutableTriple<L, M, R> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected final L left;
	protected final M middle;
	protected final R right;

	/**
	 * 构建ImmutableTriple对象
	 *
	 * @param <L>    左值类型
	 * @param <M>    中值类型
	 * @param <R>    右值类型
	 * @param left   左值
	 * @param middle 中值
	 * @param right  右值
	 * @return ImmutableTriple
	 * @since 6.0.3
	 */
	public static <L, M, R> ImmutableTriple<L, M, R> of(L left, M middle, R right) {
		return new ImmutableTriple<>(left, middle, right);
	}

	/**
	 * 构造
	 *
	 * @param left   左值
	 * @param middle 中值
	 * @param right  右值
	 */
	private ImmutableTriple(L left, M middle, R right) {
		this.left = left;
		this.middle = middle;
		this.right = right;
	}

	/**
	 * 获取左值
	 *
	 * @return 左值
	 */
	public L getLeft() {
		return this.left;
	}

	/**
	 * 获取中值
	 *
	 * @return 中值
	 */
	public M getMiddle() {
		return this.middle;
	}

	/**
	 * 获取右值
	 *
	 * @return 右值
	 */
	public R getRight() {
		return this.right;
	}

	@Override
	public String toString() {
		return "ImmutableTriple {" + "left=" + left + ", middle=" + middle + ", right=" + right + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof ImmutableTriple) {
			ImmutableTriple<?, ?, ?> triple = (ImmutableTriple<?, ?, ?>) o;
			return Objects.equals(left, triple.getLeft()) &&
					Objects.equals(middle, triple.getMiddle()) &&
					Objects.equals(right, triple.getRight());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(left) ^ Objects.hashCode(middle) ^ Objects.hashCode(right);
	}
}
