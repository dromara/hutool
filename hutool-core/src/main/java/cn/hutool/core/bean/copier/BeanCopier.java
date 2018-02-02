package cn.hutool.core.bean.copier;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import cn.hutool.core.bean.BeanDesc.PropDesc;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.provider.BeanValueProvider;
import cn.hutool.core.bean.copier.provider.MapValueProvider;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.copier.Copier;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;

/**
 * Bean拷贝
 * @author looly
 *
 * @param <T> 目标对象类型
 * @since 3.2.3
 */
public class BeanCopier<T> implements Copier<T>{
	
	private Object source;
	private T dest;
	private CopyOptions copyOptions;
	
	/**
	 * 创建BeanCopier
	 * 
	 * @param <T> 目标Bean类型
	 * @param source 来源对象，可以是Bean或者Map
	 * @param dest 目标Bean对象
	 * @param copyOptions 拷贝属性选项
	 * @return BeanCopier
	 */
	public static <T> BeanCopier<T> create(Object source, T dest, CopyOptions copyOptions){
		return new BeanCopier<>(source, dest, copyOptions);
	}
	
	/**
	 * 构造
	 * 
	 * @param source 来源对象，可以是Bean或者Map
	 * @param dest 目标Bean对象
	 * @param copyOptions 拷贝属性选项
	 */
	public BeanCopier(Object source, T dest, CopyOptions copyOptions) {
		this.source = source;
		this.dest = dest;
		this.copyOptions = copyOptions;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T copy() {
		if(null != this.source) {
			if(this.source instanceof ValueProvider) {
				valueProviderToBean((ValueProvider<String>)this.source, this.dest);
			}else if(this.source instanceof Map) {
				mapToBean((Map<?, ?>)this.source, this.dest);
			}else {
				beanToBean(this.source, this.dest);
			}
		}
		return this.dest;
	}
	
	/**
	 * Bean和Bean之间属性拷贝
	 * @param providerBean 来源Bean
	 * @param destBean 目标Bean
	 */
	private void beanToBean(Object providerBean, Object destBean) {
		valueProviderToBean(new BeanValueProvider(providerBean, this.copyOptions.ignoreCase, this.copyOptions.ignoreError), destBean);
	}
	
	/**
	 * Map转Bean属性拷贝
	 * @param map Map
	 * @param bean Bean
	 */
	private void mapToBean(Map<?, ?> map, Object bean) {
		valueProviderToBean(new MapValueProvider(map, this.copyOptions.ignoreCase), bean);
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

		Class<?> actualEditable = bean.getClass();
		if (copyOptions.editable != null) {
			// 检查限制类是否为target的父类或接口
			if (false == copyOptions.editable.isInstance(bean)) {
				throw new IllegalArgumentException(StrUtil.format("Target class [{}] not assignable to Editable class [{}]", bean.getClass().getName(), copyOptions.editable.getName()));
			}
			actualEditable = copyOptions.editable;
		}
		final HashSet<String> ignoreSet = null != (copyOptions.ignoreProperties) ? CollectionUtil.newHashSet(copyOptions.ignoreProperties) : null;
		final Map<String, String> fieldReverseMapping = (null != copyOptions.fieldMapping) ? MapUtil.reverse(copyOptions.fieldMapping) : null;

		final Collection<PropDesc> props = BeanUtil.getBeanDesc(actualEditable).getProps();
		String fieldName;
		Object value;
		Method setterMethod;
		Class<?> propClass;
		for (PropDesc prop : props) {
			// 获取值
			fieldName = prop.getFieldName();
			if ((null != ignoreSet && ignoreSet.contains(fieldName)) || false == valueProvider.containsKey(fieldName)) {
				// 属性值被忽略或值提供者无此key时跳过
				continue;
			}
			setterMethod = prop.getSetter();
			if(null == setterMethod) {
				//Setter方法不存在跳过
				continue;
			}
			
			// 此处对valueProvider传递的为Type对象，而非Class，因为Type中包含泛型类型信息
			String providerKey = null;
			if(null != fieldReverseMapping) {
				providerKey = fieldReverseMapping.get(fieldName);
			}
			if(null == providerKey) {
				providerKey = fieldName;
			}
			value = valueProvider.value(providerKey, TypeUtil.getFirstParamType(setterMethod));
			if (null == value && copyOptions.ignoreNullValue) {
				continue;// 当允许跳过空时，跳过
			}

			try {
				// valueProvider在没有对值做转换且当类型不匹配的时候，执行默认转换
				propClass = prop.getFieldClass();
				if (false == propClass.isInstance(value)) {
					value = Convert.convert(propClass, value);
					if (null == value && copyOptions.ignoreNullValue) {
						continue;// 当允许跳过空时，跳过
					}
				}

				// 执行set方法注入值
				setterMethod.invoke(bean, value);
			} catch (Exception e) {
				if (copyOptions.ignoreError) {
					continue;// 忽略注入失败
				} else {
					throw new UtilException(e, "Inject [{}] error!", prop.getFieldName());
				}
			}
		}
	}
	
}
