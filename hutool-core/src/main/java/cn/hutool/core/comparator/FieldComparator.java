package cn.hutool.core.comparator;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;

/**
 * Bean字段排序器<br>
 * 参阅feilong-core中的PropertyComparator
 *
 * @param <T> 被比较的Bean
 * @author Looly
 */
public class FieldComparator<T> extends BaseFieldComparator<T> {
	private static final long serialVersionUID = 9157326766723846313L;

	private final Field field;

	/**
	 * 构造
	 *
	 * @param beanClass Bean类
	 * @param fieldName 字段名
	 */
	public FieldComparator(Class<T> beanClass, String fieldName) {
		this.field = ClassUtil.getDeclaredField(beanClass, fieldName);
		if (this.field == null) {
			throw new IllegalArgumentException(StrUtil.format("Field [{}] not found in Class [{}]", fieldName, beanClass.getName()));
		}
	}

	/**
	 * 构造
	 *
	 * @param field 字段
	 */
	public FieldComparator(Field field) {
		this.field = field;
	}

	@Override
	public int compare(T o1, T o2) {
		return compareItem(o1, o2, this.field);
	}
}
