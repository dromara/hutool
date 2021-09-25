package cn.hutool.core.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 带有计算属性的遍历器<br>
 * 通过继承此抽象遍历器，实现{@link #computeNext()}计算下一个节点，即可完成节点遍历<br>
 * 当调用{@link #hasNext()}时将此方法产生的节点缓存，直到调用{@link #next()}取出<br>
 * 当无下一个节点时，须返回{@code null}表示遍历结束
 *
 * @param <T> 节点类型
 * @author looly
 * @since 5.7.14
 */
public abstract class ComputeIter<T> implements Iterator<T> {

	private T next;
	/**
	 * A flag indicating if the iterator has been fully read.
	 */
	private boolean finished;

	/**
	 * 计算新的节点，通过实现此方法，当调用{@link #hasNext()}时将此方法产生的节点缓存，直到调用{@link #next()}取出<br>
	 * 当无下一个节点时，须返回{@code null}表示遍历结束
	 *
	 * @return 节点值
	 */
	protected abstract T computeNext();

	@Override
	public boolean hasNext() {
		if (null != next) {
			// 用户读取了节点，但是没有使用
			return true;
		} else if (finished) {
			// 读取结束
			return false;
		}

		T result = computeNext();
		if (null == result) {
			// 不再有新的节点，结束
			this.finished = true;
			return false;
		} else {
			this.next = result;
			return true;
		}

	}

	@Override
	public T next() {
		if (false == hasNext()) {
			throw new NoSuchElementException("No more lines");
		}

		T result = this.next;
		// 清空cache，表示此节点读取完毕，下次计算新节点
		this.next = null;
		return result;
	}

	/**
	 * 手动结束遍历器，用于关闭操作等
	 */
	public void finish(){
		this.finished = true;
		this.next = null;
	}
}
