package org.dromara.hutool.core.tuple;

import java.io.Serializable;
import java.util.Objects;

/**
 * 三元组抽象类
 *
 * @param <L> 左值类型
 * @param <M> 中值类型
 * @param <R> 右值类型
 * @author kirno7
 * @since 6.0.0
 */
public abstract class Triple<L, M, R> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 获取左值
	 *
	 * @return 左值
	 */
	public abstract L getLeft();

	/**
	 * 获取中值
	 *
	 * @return 中值
	 */
	public abstract M getMiddle();

	/**
	 * 获取右值
	 *
	 * @return 右值
	 */
	public abstract R getRight();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof ImmutableTriple) {
			ImmutableTriple<?, ?, ?> triple = (ImmutableTriple<?, ?, ?>) o;
			return Objects.equals(getLeft(), triple.getLeft()) &&
					Objects.equals(getMiddle(), triple.getMiddle()) &&
					Objects.equals(getRight(), triple.getRight());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getLeft()) ^ Objects.hashCode(getMiddle()) ^ Objects.hashCode(getRight());
	}

	@Override
	public String toString() {
		return "Triple {" + "left=" + getLeft() + ", middle=" + getMiddle() + ", right=" + getRight() + '}';
	}
}

