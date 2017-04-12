package com.xiaoleilu.hutool.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.collection.CaseInsensitiveMap;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * Bean工具类
 * 
 * <p>把一个拥有对属性进行set和get方法的类，我们就可以称之为JavaBean。</p>
 * 
 * @author Looly
 *
 */
public final class BeanUtil {
	
	private BeanUtil() {}
	
	/**
	 * 判断是否为Bean对象<br>
	 * 判定方法是是否存在只有一个参数的setXXX方法
	 * @param clazz 待测试类
	 * @return 是否为Bean对象
	 */
	public static boolean isBean(Class<?> clazz){
		if(ClassUtil.isNormalClass(clazz)){
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if(method.getParameterTypes().length == 1 && method.getName().startsWith("set")){
					//检测包含标准的setXXX方法即视为标准的JavaBean
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 查找类型转换器 {@link PropertyEditor}
	 * @param type 需要转换的目标类型
	 * @return {@link PropertyEditor}
	 */
	public static PropertyEditor findEditor(Class<?> type){
		return PropertyEditorManager.findEditor(type);
	}

	/**
	 * 获得Bean字段描述数组
	 * 
	 * @param clazz Bean类
	 * @return 字段描述数组
	 * @throws IntrospectionException
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws IntrospectionException {
		return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
	}
	
	/**
	 * 获得字段名和字段描述Map
	 * @param clazz Bean类
	 * @return 字段名和字段描述Map
	 * @throws IntrospectionException
	 */
	public static Map<String, PropertyDescriptor> getFieldNamePropertyDescriptorMap(Class<?> clazz) throws IntrospectionException{
		final PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);
		Map<String, PropertyDescriptor> map = new HashMap<>();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			map.put(propertyDescriptor.getName(), propertyDescriptor);
		}
		return map;
	}

	/**
	 * 获得Bean类属性描述
	 * 
	 * @param clazz Bean类
	 * @param fieldName 字段名
	 * @return PropertyDescriptor
	 * @throws IntrospectionException
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, final String fieldName) throws IntrospectionException {
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (ObjectUtil.equal(fieldName, propertyDescriptor.getName())) {
				return propertyDescriptor;
			}
		}
		return null;
	}
	
	/**
	 * Map转换为Bean对象
	 * 
	 * @param map {@link Map}
	 * @param beanClass Bean Class
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError) {
		return fillBeanWithMap(map, ClassUtil.newInstance(beanClass), isIgnoreError);
	}

	/**
	 * Map转换为Bean对象<br>
	 * 忽略大小写
	 * 
	 * @param map Map
	 * @param beanClass Bean Class
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T mapToBeanIgnoreCase(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError) {
		return fillBeanWithMapIgnoreCase(map, ClassUtil.newInstance(beanClass), isIgnoreError);
	}

	/**
	 * 使用Map填充Bean对象
	 * 
	 * @param map Map
	 * @param bean Bean
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBeanWithMap(final Map<?, ?> map, T bean, final boolean isIgnoreError) {
		return fillBean(bean, new ValueProvider<String>(){
			@Override
			public Object value(String key, Class<?> valueType) {
				return map.get(key);
			}

			@Override
			public boolean containsKey(String key) {
				return map.containsKey(key);
			}
		}, CopyOptions.create().setIgnoreError(isIgnoreError));
	}
	
	/**
	 * 使用Map填充Bean对象，可配置将下划线转换为驼峰
	 * 
	 * @param map Map
	 * @param bean Bean
	 * @param isToCamelCase 是否将下划线模式转换为驼峰模式
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, boolean isToCamelCase, boolean isIgnoreError) {
		if(isToCamelCase){
			final Map<Object, Object> map2 = new HashMap<Object, Object>();
			for (Entry<?, ?> entry : map.entrySet()) {
				final Object key = entry.getKey();
				if (null != key && key instanceof String) {
					final String keyStr = (String) key;
					map2.put(StrUtil.toCamelCase(keyStr), entry.getValue());
				} else {
					map2.put(key, entry.getValue());
				}
			}
			return fillBeanWithMap(map2, bean, isIgnoreError);
		}
		
		return fillBeanWithMap(map, bean, isIgnoreError);
	}

	/**
	 * 使用Map填充Bean对象，忽略大小写
	 * 
	 * @param map Map
	 * @param bean Bean
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBeanWithMapIgnoreCase(Map<?, ?> map, T bean, final boolean isIgnoreError) {
		final CaseInsensitiveMap<Object,Object> caseInsensitiveMap = new CaseInsensitiveMap<>(map);

		return fillBean(bean, new ValueProvider<String>(){
			@Override
			public Object value(String key, Class<?> valueType) {
				return caseInsensitiveMap.get(key);
			}

			@Override
			public boolean containsKey(String key) {
				return caseInsensitiveMap.containsKey(key);
			}
		}, CopyOptions.create().setIgnoreError(isIgnoreError));
	}

	/**
	 * ServletRequest 参数转Bean
	 * 
	 * @param request ServletRequest
	 * @param beanClass Bean Class
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T requestParamToBean(javax.servlet.ServletRequest request, Class<T> beanClass, boolean isIgnoreError) {
		return fillBeanWithRequestParam(request, ClassUtil.newInstance(beanClass), isIgnoreError);
	}

	/**
	 * ServletRequest 参数转Bean
	 * 
	 * @param request ServletRequest
	 * @param bean Bean
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBeanWithRequestParam(final javax.servlet.ServletRequest request, T bean, final boolean isIgnoreError) {
		final String beanName = StrUtil.lowerFirst(bean.getClass().getSimpleName());
		return fillBean(bean, new ValueProvider<String>(){
			@Override
			public Object value(String key, Class<?> valueType) {
				String value = request.getParameter(key);
				if (StrUtil.isEmpty(value)) {
					// 使用类名前缀尝试查找值
					value = request.getParameter(beanName + StrUtil.DOT + key);
					if (StrUtil.isEmpty(value)) {
						// 此处取得的值为空时跳过，包括null和""
						value = null;
					}
				}
				return value;
			}

			@Override
			public boolean containsKey(String key) {
				//对于Servlet来说，返回值null意味着无此参数
				return null != request.getParameter(key);
			}
		}, CopyOptions.create().setIgnoreError(isIgnoreError));
	}

	/**
	 * ServletRequest 参数转Bean
	 * 
	 * @param <T>
	 * @param beanClass Bean Class
	 * @param valueProvider 值提供者
	 * @param copyOptions 拷贝选项，见 {@link CopyOptions}
	 * @return Bean
	 */
	public static <T> T toBean(Class<T> beanClass, ValueProvider<String> valueProvider, CopyOptions copyOptions) {
		return fillBean(ClassUtil.newInstance(beanClass), valueProvider, copyOptions);
	}

	/**
	 * 填充Bean
	 * 
	 * @param <T>
	 * @param bean Bean
	 * @param valueProvider 值提供者
	 * @param copyOptions 拷贝选项，见 {@link CopyOptions}
	 * @return Bean
	 */
	public static <T> T fillBean(T bean, ValueProvider<String> valueProvider, CopyOptions copyOptions) {
		if (null == valueProvider) {
			return bean;
		}

		Class<?> actualEditable = bean.getClass();
		if (copyOptions.editable != null) {
			//检查限制类是否为target的父类或接口
			if (!copyOptions.editable.isInstance(bean)) {
				throw new IllegalArgumentException(StrUtil.format("Target class [{}] not assignable to Editable class [{}]", bean.getClass().getName(), copyOptions.editable.getName()));
			}
			actualEditable = copyOptions.editable;
		}
		HashSet<String> ignoreSet = copyOptions.ignoreProperties != null ? CollectionUtil.newHashSet(copyOptions.ignoreProperties) : null;
		try {
			final PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(actualEditable);
			String propertyName;
			Class<?> propertyType;
			Object value;
			for (PropertyDescriptor property : propertyDescriptors) {
				propertyName = property.getName();
				if((null != ignoreSet && ignoreSet.contains(propertyName)) || false == valueProvider.containsKey(propertyName)){
					continue;//属性值被忽略或值提供者无此key时跳过
				}
				propertyType = property.getPropertyType();
				value = valueProvider.value(propertyName, propertyType);
				if (null == value && copyOptions.ignoreNullValue) {
					continue;//当允许跳过空时，跳过
				}

				try {
					//当类型不匹配的时候，执行默认转换
					if(false == propertyType.isInstance(value)){
						value = Convert.convert(propertyType, value);
						if (null == value && copyOptions.ignoreNullValue) {
							continue;//当允许跳过空时，跳过
						}
					}
//					property.getWriteMethod().invoke(bean, value);
					Method method = ClassUtil.setAccessible(property.getWriteMethod());
					if(null != method){
						method.invoke(bean, value);
					}
				} catch (Exception e) {
					if(copyOptions.ignoreError){
						continue;//忽略注入失败
					}else{
						throw new UtilException(e, "Inject [{}] error!", property.getName());
					}
				}
			}
		} catch (Exception e) {
			throw new UtilException(e);
		}
		return bean;
	}
	
	/**
	 * 对象转Map，不进行驼峰转下划线，不忽略值为空的字段
	 * 
	 * @param bean bean对象
	 * @return Map
	 */
	public static <T> Map<String, Object> beanToMap(T bean) {
		return beanToMap(bean, false, false);
	}

	/**
	 * 对象转Map
	 * 
	 * @param bean bean对象
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue 是否忽略值为空的字段
	 * @return Map
	 */
	public static <T> Map<String, Object> beanToMap(T bean, boolean isToUnderlineCase, boolean ignoreNullValue) {

		if (bean == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			final PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(bean.getClass());
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (false == key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(bean);
					if (false == ignoreNullValue || null != value) {
						map.put(isToUnderlineCase ? StrUtil.toUnderlineCase(key) : key, value);
					}
				}
			}
		} catch (Exception e) {
			throw new UtilException(e);
		}
		return map;
	}

	/**
	 * 复制Bean对象属性
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 */
	public static void copyProperties(Object source, Object target) {
		copyProperties(source, target, CopyOptions.create());
	}
	
	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @param ignoreProperties 不拷贝的的属性列表
	 */
	public static void copyProperties(Object source, Object target, String... ignoreProperties) {
		copyProperties(source, target, CopyOptions.create().setIgnoreProperties(ignoreProperties));
	}
	
	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @param copyOptions 拷贝选项，见 {@link CopyOptions}
	 */
	public static void copyProperties(final Object source, Object target, CopyOptions copyOptions) {
		if(null == copyOptions){
			copyOptions = new CopyOptions();
		}
		final boolean ignoreError = copyOptions.ignoreError;
		
		final Map<String, PropertyDescriptor> sourcePdMap;
		try {
			sourcePdMap = getFieldNamePropertyDescriptorMap(source.getClass());
		} catch (IntrospectionException e) {
			throw new UtilException(e);
		}
		
		fillBean(target, new ValueProvider<String>(){
			@Override
			public Object value(String key, Class<?> valueType) {
				PropertyDescriptor sourcePd = sourcePdMap.get(key);
				Method readMethod = sourcePd.getReadMethod();
				if (readMethod != null && ClassUtil.isAssignable(valueType, readMethod.getReturnType())) {
					try {
						return ClassUtil.setAccessible(readMethod).invoke(source);
					} catch (Exception e) {
						if(false == ignoreError){
							throw new UtilException(e, "Inject [{}] error!", key);
						}
					}
				}
				return null;
			}

			@Override
			public boolean containsKey(String key) {
				return sourcePdMap.containsKey(key);
			}
			
		}, copyOptions);
	}

	/**
	 * 值提供者，用于提供Bean注入时参数对应值得抽象接口<br>
	 * 继承或匿名实例化此接口<br>
	 * 在Bean注入过程中，Bean获得字段名，通过外部方式根据这个字段名查找相应的字段值，然后注入Bean<br>
	 * 
	 * @author Looly
	 * @param <T> KEY类型，一般情况下为 {@link String}
	 *
	 */
	public static interface ValueProvider<T>{
		/**
		 * 获取值<br>
		 * 返回值一般需要匹配被注入类型，如果不匹配会调用默认转换 {@link Convert#convert(Class, Object)}实现转换
		 * 
		 * @param key Bean对象中参数名
		 * @param valueType 被注入的值得类型
		 * @return 对应参数名的值
		 */
		public Object value(T key, Class<?> valueType);
		
		/**
		 * 是否包含指定KEY，如果不包含则忽略注入
		 * @param key Bean对象中参数名
		 * @return 是否包含指定KEY
		 */
		public boolean containsKey(T key);
	}
	
	/**
	 * 属性拷贝选项<br>
	 * 包括：<br>
	 * 1、限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类<br>
	 * 2、是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null<br>
	 * 3、忽略的属性列表，设置一个属性列表，不拷贝这些属性值<br>
	 * 
	 * @author Looly
	 */
	public static class CopyOptions {
		/** 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类 */
		private Class<?> editable;
		/** 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null */
		private boolean ignoreNullValue;
		/** 忽略的属性列表，设置一个属性列表，不拷贝这些属性值 */
		private String[] ignoreProperties;
		/** 是否忽略字段注入错误 */
		private boolean ignoreError;
		
		/**
		 * 创建拷贝选项
		 * @return 拷贝选项
		 */
		public static CopyOptions create(){
			return new CopyOptions();
		}
		
		/**
		 * 创建拷贝选项
		 * @param editable 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性
		 * @param ignoreNullValue 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
		 * @param ignoreProperties 忽略的属性列表，设置一个属性列表，不拷贝这些属性值
		 * @return 拷贝选项
		 */
		public static CopyOptions create(Class<?> editable, boolean ignoreNullValue, String... ignoreProperties){
			return new CopyOptions(editable, ignoreNullValue, ignoreProperties);
		}
		
		/**
		 * 构造拷贝选项
		 */
		public CopyOptions() {
		}
		
		/**
		 * 构造拷贝选项
		 * @param editable 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性
		 * @param ignoreNullValue 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
		 * @param ignoreProperties 忽略的属性列表，设置一个属性列表，不拷贝这些属性值
		 */
		public CopyOptions(Class<?> editable, boolean ignoreNullValue, String... ignoreProperties) {
			this.editable = editable;
			this.ignoreNullValue = ignoreNullValue;
			this.ignoreProperties = ignoreProperties;
		}

		/**
		 * 设置限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性
		 * @param editable 限制的类或接口
		 * @return CopyOptions
		 */
		public CopyOptions setEditable(Class<?> editable){
			this.editable = editable;
			return this;
		}
		
		/**
		 * 设置是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
		 * @param ignoreNullVall 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
		 * @return CopyOptions
		 */
		public CopyOptions setIgnoreNullValue(boolean ignoreNullVall){
			this.ignoreNullValue = ignoreNullVall;
			return this;
		}
		
		/**
		 * 设置忽略的属性列表，设置一个属性列表，不拷贝这些属性值
		 * @param ignoreProperties 忽略的属性列表，设置一个属性列表，不拷贝这些属性值
		 * @return CopyOptions
		 */
		public CopyOptions setIgnoreProperties(String... ignoreProperties){
			this.ignoreProperties = ignoreProperties;
			return this;
		}
		
		/**
		 * 设置是否忽略字段的注入错误
		 * @param ignoreError 是否忽略
		 * @return CopyOptions
		 */
		public CopyOptions setIgnoreError(boolean ignoreError) {
			this.ignoreError = ignoreError;
			return this;
		}
	}
}
