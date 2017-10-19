package com.xiaoleilu.hutool.comparator;

import java.io.Serializable;
import java.util.Comparator;

import com.xiaoleilu.hutool.bean.BeanUtil;
import com.xiaoleilu.hutool.util.ObjectUtil;

/**
 * Bean属性排序器<br>
 * 参阅feilong-core中的PropertyComparator
 * 
 * @author Looly
 *
 * @param <T> 被比较的Bean
 */
public class PropertyComparator<T> implements Comparator<T>, Serializable {
	private static final long serialVersionUID = 9157326766723846313L;
	
	private final String property;

	public PropertyComparator(String property) {
		this.property = property;
	}

	@Override
	public int compare(T o1, T o2) {
		if (o1 == o2) {
			return 0;
		} else if (null == o1) {// null 排在后面
			return 1;
		} else if (null == o2) {
			return -1;
		}

		Comparable<?> v1;
		Comparable<?> v2;
		try {
			v1 = (Comparable<?>) BeanUtil.getProperty(o1, property);
			v2 = (Comparable<?>) BeanUtil.getProperty(o1, property);
		} catch (Exception e) {
			throw new ComparatorException(e);
		}

		return compare(o1, o2, v1, v2);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int compare(T o1, T o2, Comparable fieldValue1, Comparable fieldValue2) {
		int result = ObjectUtil.compare(fieldValue1, fieldValue2);
		if(0 == result && ObjectUtil.notEqual(o1, o2)){
			//避免TreeSet / TreeMap 过滤掉排序字段相同但是对象不相同的情况
			return 1;
		}
		return result;
	}
}
