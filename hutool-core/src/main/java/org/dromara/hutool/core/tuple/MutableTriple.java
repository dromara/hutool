package org.dromara.hutool.core.tuple;

/**
 * 可变三元组对象
 *
 * @param <L> 左值类型
 * @param <M> 中值类型
 * @param <R> 右值类型
 * @author kirno7
 * @since 6.0.0
 */
public class MutableTriple<L, M, R> extends Triple<L, M, R> {
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
	 * @since 6.0.0
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
	@Override
	public L getLeft() {
		return this.left;
	}

	/**
	 * 获取中值
	 *
	 * @return 中值
	 */
	@Override
	public M getMiddle() {
		return this.middle;
	}

	/**
	 * 获取右值
	 *
	 * @return 右值
	 */
	@Override
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
}
