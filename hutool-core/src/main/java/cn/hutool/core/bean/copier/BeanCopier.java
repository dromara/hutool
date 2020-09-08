package cn.hutool.core.bean.copier;

import cn.hutool.core.bean.BeanDesc.PropDesc;
import cn.hutool.core.bean.BeanException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.provider.BeanValueProvider;
import cn.hutool.core.bean.copier.provider.MapValueProvider;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.copier.Copier;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * Bean拷贝，提供：
 *
 * <pre>
 *     1. Bean 转 Bean
 *     2. Bean 转 Map
 *     3. Map  转 Bean
 *     4. Map  转 Map
 * </pre>
 * 
 * @author looly
 *
 * @param <T> 目标对象类型
 * @since 3.2.3
 */
public class BeanCopier<T> implements Copier<T>, Serializable {
	private static final long serialVersionUID = 1L;
	
	/** 源对象 */
	private final Object source;
	/** 目标对象 */
	private final T dest;
	/** 目标的类型（用于泛型类注入） */
	private final Type destType;
	/** 拷贝选项 */
	private final CopyOptions copyOptions;

	/**
	 * 创建BeanCopier
	 * 
	 * @param <T> 目标Bean类型
	 * @param source 来源对象，可以是Bean或者Map
	 * @param dest 目标Bean对象
	 * @param copyOptions 拷贝属性选项
	 * @return BeanCopier
	 */
	public static <T> BeanCopier<T> create(Object source, T dest, CopyOptions copyOptions) {
		return create(source, dest, dest.getClass(), copyOptions);
	}

	/**
	 * 创建BeanCopier
	 * 
	 * @param <T> 目标Bean类型
	 * @param source 来源对象，可以是Bean或者Map
	 * @param dest 目标Bean对象
	 * @param destType 目标的泛型类型，用于标注有泛型参数的Bean对象
	 * @param copyOptions 拷贝属性选项
	 * @return BeanCopier
	 */
	public static <T> BeanCopier<T> create(Object source, T dest, Type destType, CopyOptions copyOptions) {
		return new BeanCopier<>(source, dest, destType, copyOptions);
	}

	/**
	 * 构造
	 * 
	 * @param source 来源对象，可以是Bean或者Map
	 * @param dest 目标Bean对象
	 * @param destType 目标的泛型类型，用于标注有泛型参数的Bean对象
	 * @param copyOptions 拷贝属性选项
	 */
	public BeanCopier(Object source, T dest, Type destType, CopyOptions copyOptions) {
		this.source = source;
		this.dest = dest;
		this.destType = destType;
		this.copyOptions = copyOptions;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T copy() {
		if (null != this.source) {
			if (this.source instanceof ValueProvider) {
				// 目标只支持Bean
				valueProviderToBean((ValueProvider<String>) this.source, this.dest);
			} else if (this.source instanceof Map) {
				if (this.dest instanceof Map) {
					mapToMap((Map<?, ?>) this.source, (Map<?, ?>) this.dest);
				} else {
					mapToBean((Map<?, ?>) this.source, this.dest);
				}
			} else {
				if (this.dest instanceof Map) {
					beanToMap(this.source, (Map<?, ?>) this.dest);
				} else {
					beanToBean(this.source, this.dest);
				}
			}
		}
		return this.dest;
	}

	/**
	 * Bean和Bean之间属性拷贝
	 * 
	 * @param providerBean 来源Bean
	 * @param destBean 目标Bean
	 */
	private void beanToBean(Object providerBean, Object destBean) {
		valueProviderToBean(new BeanValueProvider(providerBean, this.copyOptions.ignoreCase, this.copyOptions.ignoreError), destBean);
	}

	/**
	 * Map转Bean属性拷贝
	 * 
	 * @param map Map
	 * @param bean Bean
	 */
	private void mapToBean(Map<?, ?> map, Object bean) {
		valueProviderToBean(
				new MapValueProvider(map, this.copyOptions.ignoreCase, this.copyOptions.ignoreError),
				bean
		);
	}

	/**
	 * Map转Map
	 * 
	 * @param source 源Map
	 * @param dest 目标Map
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void mapToMap(Map source, Map dest) {
		if (null != dest && null != source) {
			dest.putAll(source);
		}
	}

	/**
	 * 对象转Map
	 * 
	 * @param bean bean对象
	 * @param targetMap 目标的Map
	 * @since 4.1.22
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void beanToMap(Object bean, Map targetMap) {
		final Collection<PropDesc> props = BeanUtil.getBeanDesc(bean.getClass()).getProps();
		final HashSet<String> ignoreSet = (null != copyOptions.ignoreProperties) ? CollUtil.newHashSet(copyOptions.ignoreProperties) : null;
		final CopyOptions copyOptions = this.copyOptions;

		String key;
		Object value;
		for (PropDesc prop : props) {
			// 忽略注解的字段
			if(prop.isIgnoreGet()){
				continue;
			}
			key = prop.getFieldName();
			if (CollUtil.contains(ignoreSet, key)) {
				// 目标属性值被忽略或值提供者无此key时跳过
				continue;
			}
			try {
				value = prop.getValue(bean);
			} catch (Exception e) {
				if (copyOptions.ignoreError) {
					continue;// 忽略反射失败
				} else {
					throw new BeanException(e, "Get value of [{}] error!", prop.getFieldName());
				}
			}
			if (null == value && copyOptions.ignoreNullValue) {
				continue;// 当允许跳过空时，跳过
			}
			if (bean.equals(value)) {
				continue;// 值不能为bean本身，防止循环引用
			}
			targetMap.put(mappingKey(copyOptions.fieldMapping, key), value);
		}
	}

	/**
	 * 值提供器转Bean
	 * 
	 * @param valueProvider 值提供器
	 * @param bean Bean
	 */
	private void valueProviderToBean(ValueProvider<String> valueProvider, Object bean) {
		if (null == valueProvider) {
			return;
		}

		final CopyOptions copyOptions = this.copyOptions;
		Class<?> actualEditable = bean.getClass();
		if (null != copyOptions.editable) {
			// 检查限制类是否为target的父类或接口
			if (false == copyOptions.editable.isInstance(bean)) {
				throw new IllegalArgumentException(StrUtil.format("Target class [{}] not assignable to Editable class [{}]", bean.getClass().getName(), copyOptions.editable.getName()));
			}
			actualEditable = copyOptions.editable;
		}
		final HashSet<String> ignoreSet = (null != copyOptions.ignoreProperties) ? CollUtil.newHashSet(copyOptions.ignoreProperties) : null;
		final Map<String, String> fieldReverseMapping = copyOptions.getReversedMapping();

		final Collection<PropDesc> props = BeanUtil.getBeanDesc(actualEditable).getProps();
		Field field;
		String fieldName;
		Object value;
		Method setterMethod;
		Class<?> propClass;
		for (PropDesc prop : props) {
			// 获取值
			field = prop.getField();
			fieldName = prop.getFieldName();
			if (CollUtil.contains(ignoreSet, fieldName)) {
				// 目标属性值被忽略或值提供者无此key时跳过
				continue;
			}

			// 在支持情况下，忽略transient修饰（包括修饰和注解）
			if(copyOptions.isTransientSupport() && prop.isTransient()){
				continue;
			}

			// @Ignore修饰的字段和setXXX方法忽略之
			if(prop.isIgnoreSet()){
				continue;
			}

			final String providerKey = mappingKey(fieldReverseMapping, fieldName);
			if (false == valueProvider.containsKey(providerKey)) {
				// 无对应值可提供
				continue;
			}
			setterMethod = prop.getSetter();
			if (null == setterMethod && false == ModifierUtil.isPublic(field)) {
				// Setter方法不存在或者字段为非public跳过
				//5.1.0新增支持public字段注入支持
				continue;
			}

			// 获取目标字段真实类型
			final Type fieldType = TypeUtil.getActualType(this.destType ,prop.getFieldType());

			value = valueProvider.value(providerKey, fieldType);
			if (null == value && copyOptions.ignoreNullValue) {
				continue;// 当允许跳过空时，跳过
			}
			if (bean == value) {
				// 值不能为bean本身，防止循环引用
				continue;
			}

			prop.setValueWithConvert(bean, value, copyOptions.ignoreNullValue, copyOptions.ignoreError);
		}
	}

	/**
	 * 获取指定字段名对应的映射值
	 * 
	 * @param mapping 反向映射Map
	 * @param fieldName 字段名
	 * @return 映射值，无对应值返回字段名
	 * @since 4.1.10
	 */
	private static String mappingKey(Map<String, String> mapping, String fieldName) {
		if (MapUtil.isEmpty(mapping)) {
			return fieldName;
		}
		return ObjectUtil.defaultIfNull(mapping.get(fieldName), fieldName);
	}
}
