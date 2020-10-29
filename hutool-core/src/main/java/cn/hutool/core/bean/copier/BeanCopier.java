package cn.hutool.core.bean.copier;

import cn.hutool.core.bean.BeanException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.bean.copier.provider.BeanValueProvider;
import cn.hutool.core.bean.copier.provider.DynaBeanValueProvider;
import cn.hutool.core.bean.copier.provider.MapValueProvider;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.copier.Copier;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
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
			} else if (this.source instanceof DynaBean) {
				// 目标只支持Bean
				valueProviderToBean(new DynaBeanValueProvider((DynaBean) this.source, copyOptions.ignoreError), this.dest);
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
		final HashSet<String> ignoreSet = (null != copyOptions.ignoreProperties) ? CollUtil.newHashSet(copyOptions.ignoreProperties) : null;
		final CopyOptions copyOptions = this.copyOptions;

		BeanUtil.descForEach(bean.getClass(), (prop)->{
			if(false == prop.isReadable(copyOptions.isTransientSupport())){
				// 忽略的属性跳过之
				return;
			}
			String key = prop.getFieldName();
			if (CollUtil.contains(ignoreSet, key)) {
				// 目标属性值被忽略或值提供者无此key时跳过
				return;
			}

			// 对key做映射，映射后为null的忽略之
			key = copyOptions.editFieldName(copyOptions.getMappedFieldName(key, false));
			if(null == key){
				return;
			}

			Object value;
			try {
				value = prop.getValue(bean);
			} catch (Exception e) {
				if (copyOptions.ignoreError) {
					return;// 忽略反射失败
				} else {
					throw new BeanException(e, "Get value of [{}] error!", prop.getFieldName());
				}
			}
			if ((null == value && copyOptions.ignoreNullValue) || bean == value) {
				// 当允许跳过空时，跳过
				//值不能为bean本身，防止循环引用，此类也跳过
				return;
			}

			targetMap.put(key, value);
		});
	}

	/**
	 * 值提供器转Bean<br>
	 * 此方法通过遍历目标Bean的字段，从ValueProvider查找对应值
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

		// 遍历目标bean的所有属性
		BeanUtil.descForEach(actualEditable, (prop)->{
			if(false == prop.isWritable(this.copyOptions.isTransientSupport())){
				// 字段不可写，跳过之
				return;
			}
			// 检查属性名
			String fieldName = prop.getFieldName();
			if (CollUtil.contains(ignoreSet, fieldName)) {
				// 目标属性值被忽略或值提供者无此key时跳过
				return;
			}

			final String providerKey = copyOptions.getMappedFieldName(fieldName, true);
			if (false == valueProvider.containsKey(providerKey)) {
				// 无对应值可提供
				return;
			}

			// 获取目标字段真实类型
			final Type fieldType = TypeUtil.getActualType(this.destType ,prop.getFieldType());

			// 获取属性值
			Object value = valueProvider.value(providerKey, fieldType);
			if ((null == value && copyOptions.ignoreNullValue) || bean == value) {
				// 当允许跳过空时，跳过
				// 值不能为bean本身，防止循环引用
				return;
			}

			prop.setValue(bean, value, copyOptions.ignoreNullValue, copyOptions.ignoreError);
		});
	}
}
