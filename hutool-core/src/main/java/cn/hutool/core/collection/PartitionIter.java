package cn.hutool.core.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 分批迭代工具，可以分批处理数据
 * <ol>
 *     <li>比如调用其他客户的接口，传入的入参有限，需要分批</li>
 *     <li>比如mysql/oracle用in语句查询，超过1000可以分批</li>
 *     <li>比如数据库取出游标，可以把游标里的数据一批一批处理</li>
 * </ol>
 *
 * @param <T> 字段类型
 * @author qiqi.chen
 * @since 5.7.10
 */
public class PartitionIter<T> implements Iterator<List<T>>, Iterable<List<T>>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 被分批的迭代器
	 */
	protected final Iterator<T> iterator;
	/**
	 * 实际每批大小
	 */
	protected final int partitionSize;

	/**
	 * 创建分组对象
	 *
	 * @param iterator      迭代器
	 * @param partitionSize 每批大小，最后一批不满一批算一批
	 */
	public PartitionIter(Iterator<T> iterator, int partitionSize) {
		this.iterator = iterator;
		this.partitionSize = partitionSize;
	}

	@Override
	public Iterator<List<T>> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	@Override
	public List<T> next() {
		final List<T> list = new ArrayList<>(this.partitionSize);
		for (int i = 0; i < this.partitionSize; i++) {
			if (false == iterator.hasNext()) {
				break;
			}
			list.add(iterator.next());
		}
		return list;
	}
}
