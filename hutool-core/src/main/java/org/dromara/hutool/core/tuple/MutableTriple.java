package org.dromara.hutool.core.tuple;

import java.io.Serializable;
import java.util.Objects;

/**
 * 可变三元组对象
 *
 * @param <L> 左值类型
 * @param <M> 中值类型
 * @param <R> 右值类型
 * @author kirno7
 * @since 6.0.0
 */
public class MutableTriple<L, M, R> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected L left;
	protected M middle;
	protected R right;

	/**
	 * 构建MutableTriple对象
	 *
	 * @param <L>    左值类型
	 * @param <M>    中值类型
	 * @param <R>    右值类型
	 * @param left   左值
	 * @param middle 中值
	 * @param right  右值
	 * @return MutableTriple
	 * @since 6.0.3
	 */
	public static <L, M, R> MutableTriple<L, M, R> of(L left, M middle, R right) {
		return new MutableTriple<>(left, middle, right);
	}

	/**
	 * 构造
	 *
	 * @param left   左值
	 * @param middle 中值
	 * @param right  右值
	 */
	private MutableTriple(L left, M middle, R right) {
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

	/**
	 * 设置左值
	 *
	 * @param left 左值
	 */
	public void setLeft(L left) {
		this.left = left;
	}

	/**
	 * 设置中值
	 *
	 * @param middle 中值
	 */
	public void setMiddle(M middle) {
		this.middle = middle;
	}

	/**
	 * 设置右值
	 *
	 * @param right 右值
	 */
	public void setRight(R right) {
		this.right = right;
	}

	@Override
	public String toString() {
		return "MutableTriple {" + "left=" + left + ", middle=" + middle + ", right=" + right + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof MutableTriple) {
			MutableTriple<?, ?, ?> triple = (MutableTriple<?, ?, ?>) o;
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
