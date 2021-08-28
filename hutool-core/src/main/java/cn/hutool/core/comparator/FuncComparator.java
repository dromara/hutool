package cn.hutool.core.comparator;

import cn.hutool.core.util.ObjectUtil;

import java.util.function.Function;

/**
 * 指定函数排序器
 *
 * @param <T> 被比较的对象
 * @author looly
 */
public class FuncComparator<T> extends NullComparator<T> {
	private static final long serialVersionUID = 1L;

	private final Function<T, Comparable<?>> func;

	/**
	 * 构造
	 *
	 * @param nullGreater 是否{@code null}在后
	 * @param func        比较项获取函数
	 */
	public FuncComparator(boolean nullGreater, Function<T, Comparable<?>> func) {
		super(nullGreater, null);
		this.func = func;
	}

	@Override
	protected int doCompare(T a, T b) {
		Comparable<?> v1;
		Comparable<?> v2;
		try {
			v1 = func.apply(a);
			v2 = func.apply(b);
		} catch (Exception e) {
			throw new ComparatorException(e);
		}

		return compare(a, b, v1, v2);
	}

	/**
	 * 对象及对应比较的值的综合比较<br>
	 * 考虑到如果对象对应的比较值相同，如对象的字段值相同，则返回相同结果，此时在TreeMap等容器比较去重时会去重。<br>
	 * 因此需要比较下对象本身以避免去重
	 *
	 * @param o1 对象1
	 * @param o2 对象2
	 * @param v1 被比较的值1
	 * @param v2 被比较的值2
	 * @return 比较结果
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private int compare(T o1, T o2, Comparable v1, Comparable v2) {
		int result = ObjectUtil.compare(v1, v2);
		if (0 == result) {
			//避免TreeSet / TreeMap 过滤掉排序字段相同但是对象不相同的情况
			result = CompareUtil.compare(o1, o2, this.nullGreater);
		}
		return result;
	}
}
