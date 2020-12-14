package cn.hutool.core.comparator;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Bean属性排序器<br>
 * 支持读取Bean多层次下的属性
 * 
 * @author Looly
 *
 * @param <T> 被比较的Bean
 */
public class PropertyComparator<T> implements Comparator<T>, Serializable {
	private static final long serialVersionUID = 9157326766723846313L;
	
	private final String property;
	private final boolean isNullGreater;
	
	/**
	 * 构造
	 * 
	 * @param property 属性名
	 */
	public PropertyComparator(String property) {
		this(property, true);
	}

	/**
	 * 构造
	 * 
	 * @param property 属性名
	 * @param isNullGreater null值是否排在后（从小到大排序）
	 */
	public PropertyComparator(String property, boolean isNullGreater) {
		this.property = property;
		this.isNullGreater = isNullGreater;
	}

	@Override
	public int compare(T o1, T o2) {
		if (o1 == o2) {
			return 0;
		} else if (null == o1) {// null 排在后面
			return isNullGreater ? 1 : -1;
		} else if (null == o2) {
			return isNullGreater ? -1 : 1;
		}

		Comparable<?> v1;
		Comparable<?> v2;
		try {
			v1 = BeanUtil.getProperty(o1, property);
			v2 = BeanUtil.getProperty(o2, property);
		} catch (Exception e) {
			throw new ComparatorException(e);
		}

		return compare(o1, o2, v1, v2);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private int compare(T o1, T o2, Comparable fieldValue1, Comparable fieldValue2) {
		int result = ObjectUtil.compare(fieldValue1, fieldValue2, isNullGreater);
		if(0 == result) {
			//避免TreeSet / TreeMap 过滤掉排序字段相同但是对象不相同的情况
			result = CompareUtil.compare(o1, o2, this.isNullGreater);
		}
		return result;
	}
}
