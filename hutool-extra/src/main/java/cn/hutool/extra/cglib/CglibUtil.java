package cn.hutool.extra.cglib;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.core.Converter;

/**
 * Cglib工具类
 *
 * @author looly
 * @since 5.4.1
 */
public class CglibUtil {

	/**
	 * 拷贝Bean对象属性到目标类型<br>
	 * 此方法通过指定目标类型自动创建之，然后拷贝属性
	 *
	 * @param <T>         目标对象类型
	 * @param source      源bean对象
	 * @param targetClass 目标bean类，自动实例化此对象
	 */
	public static <T> T copy(Object source, Class<T> targetClass) {
		return copy(source, targetClass, null);
	}

	/**
	 * 拷贝Bean对象属性<br>
	 * 此方法通过指定目标类型自动创建之，然后拷贝属性
	 *
	 * @param <T>         目标对象类型
	 * @param source      源bean对象
	 * @param targetClass 目标bean类，自动实例化此对象
	 * @param converter   转换器，无需可传{@code null}
	 */
	public static <T> T copy(Object source, Class<T> targetClass, Converter converter) {
		final T target = ReflectUtil.newInstanceIfPossible(targetClass);
		copy(source, target);
		return target;
	}

	/**
	 * 拷贝Bean对象属性
	 *
	 * @param source 源bean对象
	 * @param target 目标bean对象
	 */
	public static void copy(Object source, Object target) {
		copy(source, target, null);
	}

	/**
	 * 拷贝Bean对象属性
	 *
	 * @param source    源bean对象
	 * @param target    目标bean对象
	 * @param converter 转换器，无需可传{@code null}
	 */
	public static void copy(Object source, Object target, Converter converter) {
		Assert.notNull(source, "Source bean must be not null.");
		Assert.notNull(target, "Target bean must be not null.");

		final Class<?> sourceClass = source.getClass();
		final Class<?> targetClass = target.getClass();
		final BeanCopier beanCopier = BeanCopierCache.INSTANCE.get(
				sourceClass, targetClass,
				() -> BeanCopier.create(sourceClass, targetClass, null != converter));

		beanCopier.copy(source, target, converter);
	}

	/**
	 * 将Bean转换为Map
	 *
	 * @param bean Bean对象
	 * @return {@link BeanMap}
	 */
	public static BeanMap toMap(Object bean){
		return BeanMap.create(bean);
	}
}
