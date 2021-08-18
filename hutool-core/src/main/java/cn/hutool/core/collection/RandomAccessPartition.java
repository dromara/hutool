package cn.hutool.core.collection;

import java.util.List;
import java.util.RandomAccess;

/**
 * 列表分区或分段（可随机访问列表）<br>
 * 通过传入分区长度，将指定列表分区为不同的块，每块区域的长度相同（最后一块可能小于长度）<br>
 * 分区是在原List的基础上进行的，返回的分区是不可变的抽象列表，原列表元素变更，分区中元素也会变更。
 * 参考：Guava的Lists#RandomAccessPartition
 *
 * @param <T> 元素类型
 * @author looly, guava
 * @since 5.7.10
 */
public class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {

	/**
	 * 构造
	 *
	 * @param list 被分区的列表，必须实现{@link RandomAccess}
	 * @param size 每个分区的长度
	 */
	public RandomAccessPartition(List<T> list, int size) {
		super(list, size);
	}
}
