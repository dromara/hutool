package cn.hutool.core.comparator;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 反转比较器
 * 
 * @author Looly
 *
 * @param <E> 被比较对象类型
 */
public class ReverseComparator<E> implements Comparator<E>, Serializable {
	private static final long serialVersionUID = 8083701245147495562L;

	/** 原始比较器 */
	private final Comparator<? super E> comparator;

	@SuppressWarnings("unchecked")
	public ReverseComparator(Comparator<? super E> comparator) {
		this.comparator = (null == comparator) ? ComparableComparator.INSTANCE : comparator;
	}

	//-----------------------------------------------------------------------------------------------------
	@Override
	public int compare(E o1, E o2) {
		return comparator.compare(o2, o1);
	}

	@Override
	public int hashCode() {
		return "ReverseComparator".hashCode() ^ comparator.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (null == object) {
			return false;
		}
		if (object.getClass().equals(this.getClass())) {
			final ReverseComparator<?> thatrc = (ReverseComparator<?>) object;
			return comparator.equals(thatrc.comparator);
		}
		return false;
	}
}
