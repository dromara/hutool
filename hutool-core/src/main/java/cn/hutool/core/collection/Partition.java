package cn.hutool.core.collection;

import java.util.AbstractList;
import java.util.List;

/**
 * 列表分区或分段<br>
 * 通过传入分区长度，将指定列表分区为不同的块，每块区域的长度相同（最后一块可能小于长度）<br>
 * 分区是在原List的基础上进行的，返回的分区是不可变的抽象列表，原列表元素变更，分区中元素也会变更。
 * 参考：Guava的Lists#Partition
 *
 * @param <T> 元素类型
 * @author looly, guava
 * @since 5.7.10
 */
public class Partition<T> extends AbstractList<List<T>> {

	protected final List<T> list;
	protected final int size;

	/**
	 * 列表分区
	 *
	 * @param list 被分区的列表
	 * @param size 每个分区的长度
	 */
	public Partition(List<T> list, int size) {
		this.list = list;
		this.size = Math.min(size, list.size());
	}

	@Override
	public List<T> get(int index) {
		int start = index * size;
		int end = Math.min(start + size, list.size());
		return list.subList(start, end);
	}

	@Override
	public int size() {
		// 此处采用动态计算，以应对list变
		final int size = this.size;
		final int total = list.size();
		int length = total / size;
		if(total % size > 0){
			length += 1;
		}
		return length;
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
}
