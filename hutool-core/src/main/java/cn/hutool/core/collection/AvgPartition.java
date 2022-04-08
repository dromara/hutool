package cn.hutool.core.collection;

import cn.hutool.core.lang.Assert;

import java.util.List;

/**
 * 列表分区或分段<br>
 * 通过传入分区个数，将指定列表分区为不同的块，每块区域的长度均匀分布（个数差不超过1）<br>
 * <pre>
 *     [1,2,3,4] -》 [1,2], [3, 4]
 *     [1,2,3,4] -》 [1,2], [3], [4]
 *     [1,2,3,4] -》 [1], [2], [3], [4]
 *     [1,2,3,4] -》 [1], [2], [3], [4], []
 * </pre>
 * 分区是在原List的基础上进行的，返回的分区是不可变的抽象列表，原列表元素变更，分区中元素也会变更。
 *
 * @param <T> 元素类型
 * @author looly
 * @since 5.7.10
 */
public class AvgPartition<T> extends Partition<T> {

	final int limit;
	// 平均分完后剩余的个数，平均放在前remainder个分区中
	final int remainder;

	/**
	 * 列表分区
	 *
	 * @param list  被分区的列表
	 * @param limit 分区个数
	 */
	public AvgPartition(List<T> list, int limit) {
		super(list, list.size() / (limit <= 0 ? 1 : limit));
		Assert.isTrue(limit > 0, "Partition limit must be > 0");
		this.limit = limit;
		this.remainder = list.size() % limit;
	}

	@Override
	public List<T> get(int index) {
		final int size = this.size;
		final int remainder = this.remainder;
		// 当limit个数超过list的size时，size为0，此时每个分区分1个元素，直到remainder个分配完，剩余分区为[]
		int start = index * size + Math.min(index, remainder);
		int end = start + size;
		if (index + 1 <= remainder) {
			// 将remainder个元素平均分布在前面，每个分区分1个
			end += 1;
		}
		return list.subList(start, end);
	}

	@Override
	public int size() {
		return limit;
	}
}
