package cn.hutool.core.comparator;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Bean字段排序器<br>
 * 参阅feilong-core中的PropertyComparator
 *
 * @param <T> 被比较的Bean
 * @author Looly
 */
public class FieldsComparator<T> extends BaseFieldComparator<T> {
	private static final long serialVersionUID = 8649196282886500803L;

	private final Field[] fields;

	/**
	 * 构造
	 *
	 * @param beanClass  Bean类
	 * @param fieldNames 多个字段名
	 */
	public FieldsComparator(Class<T> beanClass, String... fieldNames) {
		this.fields = new Field[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			this.fields[i] = ClassUtil.getDeclaredField(beanClass, fieldNames[i]);
			if (this.fields[i] == null) {
				throw new IllegalArgumentException(StrUtil.format("Field [{}] not found in Class [{}]", fieldNames[i], beanClass.getName()));
			}
		}
	}

	@Override
	public int compare(T o1, T o2) {
		for (Field field : fields) {
			int compare = this.compareItem(o1, o2, field);
			if (compare != 0) {
				return compare;
			}
		}
		return 0;
	}
}
