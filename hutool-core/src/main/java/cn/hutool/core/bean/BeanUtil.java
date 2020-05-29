package cn.hutool.core.bean;

import cn.hutool.core.bean.BeanDesc.PropDesc;
import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean工具类
 *
 * <p>
 * 把一个拥有对属性进行set和get方法的类，我们就可以称之为JavaBean。
 * </p>
 *
 * @author Looly
 * @since 3.1.2
 */
public class BeanUtil {

	/**
	 * 判断是否为可读的Bean对象，判定方法是：
	 *
	 * <pre>
	 *     1、是否存在只有无参数的getXXX方法或者isXXX方法
	 *     2、是否存在public类型的字段
	 * </pre>
	 *
	 * @param clazz 待测试类
	 * @return 是否为可读的Bean对象
	 * @see #hasGetter(Class) 
	 * @see #hasPublicField(Class)
	 */
	public static boolean isReadableBean(Class<?> clazz) {
		return hasGetter(clazz) || hasPublicField(clazz);
	}

	/**
	 * 判断是否为Bean对象，判定方法是：
	 *
	 * <pre>
	 *     1、是否存在只有一个参数的setXXX方法
	 *     2、是否存在public类型的字段
	 * </pre>
	 *
	 * @param clazz 待测试类
	 * @return 是否为Bean对象
	 * @see #hasSetter(Class)
	 * @see #hasPublicField(Class)
	 */
	public static boolean isBean(Class<?> clazz) {
		return hasSetter(clazz) || hasPublicField(clazz);
	}

	/**
	 * 判断是否有Setter方法<br>
	 * 判定方法是是否存在只有一个参数的setXXX方法
	 *
	 * @param clazz 待测试类
	 * @return 是否为Bean对象
	 * @since 4.2.2
	 */
	public static boolean hasSetter(Class<?> clazz) {
		if (ClassUtil.isNormalClass(clazz)) {
			final Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
					// 检测包含标准的setXXX方法即视为标准的JavaBean
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否为Bean对象<br>
	 * 判定方法是是否存在只有一个参数的setXXX方法
	 *
	 * @param clazz 待测试类
	 * @return 是否为Bean对象
	 * @since 4.2.2
	 */
	public static boolean hasGetter(Class<?> clazz) {
		if (ClassUtil.isNormalClass(clazz)) {
			for (Method method : clazz.getMethods()) {
				if (method.getParameterTypes().length == 0) {
					if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 指定类中是否有public类型字段(static字段除外)
	 *
	 * @param clazz 待测试类
	 * @return 是否有public类型字段
	 * @since 5.1.0
	 */
	public static boolean hasPublicField(Class<?> clazz) {
		if (ClassUtil.isNormalClass(clazz)) {
			for (Field field : clazz.getFields()) {
				if (ModifierUtil.isPublic(field) && false == ModifierUtil.isStatic(field)) {
					//非static的public字段
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 创建动态Bean
	 *
	 * @param bean 普通Bean或Map
	 * @return {@link DynaBean}
	 * @since 3.0.7
	 */
	public static DynaBean createDynaBean(Object bean) {
		return new DynaBean(bean);
	}

	/**
	 * 查找类型转换器 {@link PropertyEditor}
	 *
	 * @param type 需要转换的目标类型
	 * @return {@link PropertyEditor}
	 */
	public static PropertyEditor findEditor(Class<?> type) {
		return PropertyEditorManager.findEditor(type);
	}

	/**
	 * 获取{@link BeanDesc} Bean描述信息
	 *
	 * @param clazz Bean类
	 * @return {@link BeanDesc}
	 * @since 3.1.2
	 */
	public static BeanDesc getBeanDesc(Class<?> clazz) {
		BeanDesc beanDesc = BeanDescCache.INSTANCE.getBeanDesc(clazz);
		if (null == beanDesc) {
			beanDesc = new BeanDesc(clazz);
			BeanDescCache.INSTANCE.putBeanDesc(clazz, beanDesc);
		}
		return beanDesc;
	}

	// --------------------------------------------------------------------------------------------------------- PropertyDescriptor

	/**
	 * 获得Bean字段描述数组
	 *
	 * @param clazz Bean类
	 * @return 字段描述数组
	 * @throws BeanException 获取属性异常
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws BeanException {
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			throw new BeanException(e);
		}
		return ArrayUtil.filter(beanInfo.getPropertyDescriptors(), (Filter<PropertyDescriptor>) t -> {
			// 过滤掉getClass方法
			return false == "class".equals(t.getName());
		});
	}

	/**
	 * 获得字段名和字段描述Map，获得的结果会缓存在 {@link BeanInfoCache}中
	 *
	 * @param clazz      Bean类
	 * @param ignoreCase 是否忽略大小写
	 * @return 字段名和字段描述Map
	 * @throws BeanException 获取属性异常
	 */
	public static Map<String, PropertyDescriptor> getPropertyDescriptorMap(Class<?> clazz, boolean ignoreCase) throws BeanException {
		Map<String, PropertyDescriptor> map = BeanInfoCache.INSTANCE.getPropertyDescriptorMap(clazz, ignoreCase);
		if (null == map) {
			map = internalGetPropertyDescriptorMap(clazz, ignoreCase);
			BeanInfoCache.INSTANCE.putPropertyDescriptorMap(clazz, map, ignoreCase);
		}
		return map;
	}

	/**
	 * 获得字段名和字段描述Map。内部使用，直接获取Bean类的PropertyDescriptor
	 *
	 * @param clazz      Bean类
	 * @param ignoreCase 是否忽略大小写
	 * @return 字段名和字段描述Map
	 * @throws BeanException 获取属性异常
	 */
	private static Map<String, PropertyDescriptor> internalGetPropertyDescriptorMap(Class<?> clazz, boolean ignoreCase) throws BeanException {
		final PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);
		final Map<String, PropertyDescriptor> map = ignoreCase ? new CaseInsensitiveMap<>(propertyDescriptors.length, 1)
				: new HashMap<>((int) (propertyDescriptors.length), 1);

		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			map.put(propertyDescriptor.getName(), propertyDescriptor);
		}
		return map;
	}

	/**
	 * 获得Bean类属性描述，大小写敏感
	 *
	 * @param clazz     Bean类
	 * @param fieldName 字段名
	 * @return PropertyDescriptor
	 * @throws BeanException 获取属性异常
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, final String fieldName) throws BeanException {
		return getPropertyDescriptor(clazz, fieldName, false);
	}

	/**
	 * 获得Bean类属性描述
	 *
	 * @param clazz      Bean类
	 * @param fieldName  字段名
	 * @param ignoreCase 是否忽略大小写
	 * @return PropertyDescriptor
	 * @throws BeanException 获取属性异常
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, final String fieldName, boolean ignoreCase) throws BeanException {
		final Map<String, PropertyDescriptor> map = getPropertyDescriptorMap(clazz, ignoreCase);
		return (null == map) ? null : map.get(fieldName);
	}

	/**
	 * 获得字段值，通过反射直接获得字段值，并不调用getXXX方法<br>
	 * 对象同样支持Map类型，fieldNameOrIndex即为key
	 *
	 * @param bean             Bean对象
	 * @param fieldNameOrIndex 字段名或序号，序号支持负数
	 * @return 字段值
	 */
	public static Object getFieldValue(Object bean, String fieldNameOrIndex) {
		if (null == bean || null == fieldNameOrIndex) {
			return null;
		}

		if (bean instanceof Map) {
			return ((Map<?, ?>) bean).get(fieldNameOrIndex);
		} else if (bean instanceof Collection) {
			return CollUtil.get((Collection<?>) bean, Integer.parseInt(fieldNameOrIndex));
		} else if (ArrayUtil.isArray(bean)) {
			return ArrayUtil.get(bean, Integer.parseInt(fieldNameOrIndex));
		} else {// 普通Bean对象
			return ReflectUtil.getFieldValue(bean, fieldNameOrIndex);
		}
	}

	/**
	 * 设置字段值，，通过反射设置字段值，并不调用setXXX方法<br>
	 * 对象同样支持Map类型，fieldNameOrIndex即为key
	 *
	 * @param bean             Bean
	 * @param fieldNameOrIndex 字段名或序号，序号支持负数
	 * @param value            值
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void setFieldValue(Object bean, String fieldNameOrIndex, Object value) {
		if (bean instanceof Map) {
			((Map) bean).put(fieldNameOrIndex, value);
		} else if (bean instanceof List) {
			CollUtil.setOrAppend((List) bean, Convert.toInt(fieldNameOrIndex), value);
		} else if (ArrayUtil.isArray(bean)) {
			ArrayUtil.setOrAppend(bean, Convert.toInt(fieldNameOrIndex), value);
		} else {
			// 普通Bean对象
			ReflectUtil.setFieldValue(bean, fieldNameOrIndex, value);
		}
	}

	/**
	 * 解析Bean中的属性值
	 *
	 * @param <T>        属性值类型
	 * @param bean       Bean对象，支持Map、List、Collection、Array
	 * @param expression 表达式，例如：person.friend[5].name
	 * @return Bean属性值
	 * @see BeanPath#get(Object)
	 * @since 3.0.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object bean, String expression) {
		return (T) BeanPath.create(expression).get(bean);
	}

	/**
	 * 解析Bean中的属性值
	 *
	 * @param bean       Bean对象，支持Map、List、Collection、Array
	 * @param expression 表达式，例如：person.friend[5].name
	 * @param value      属性值
	 * @see BeanPath#get(Object)
	 * @since 4.0.6
	 */
	public static void setProperty(Object bean, String expression, Object value) {
		BeanPath.create(expression).set(bean, value);
	}

	// --------------------------------------------------------------------------------------------- mapToBean

	/**
	 * Map转换为Bean对象
	 *
	 * @param <T>           Bean类型
	 * @param map           {@link Map}
	 * @param beanClass     Bean Class
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError) {
		return fillBeanWithMap(map, ReflectUtil.newInstance(beanClass), isIgnoreError);
	}

	/**
	 * Map转换为Bean对象<br>
	 * 忽略大小写
	 *
	 * @param <T>           Bean类型
	 * @param map           Map
	 * @param beanClass     Bean Class
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T mapToBeanIgnoreCase(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError) {
		return fillBeanWithMapIgnoreCase(map, ReflectUtil.newInstance(beanClass), isIgnoreError);
	}

	/**
	 * Map转换为Bean对象
	 *
	 * @param <T>         Bean类型
	 * @param map         {@link Map}
	 * @param beanClass   Bean Class
	 * @param copyOptions 转Bean选项
	 * @return Bean
	 */
	public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass, CopyOptions copyOptions) {
		return fillBeanWithMap(map, ReflectUtil.newInstance(beanClass), copyOptions);
	}

	// --------------------------------------------------------------------------------------------- fillBeanWithMap

	/**
	 * 使用Map填充Bean对象
	 *
	 * @param <T>           Bean类型
	 * @param map           Map
	 * @param bean          Bean
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, boolean isIgnoreError) {
		return fillBeanWithMap(map, bean, false, isIgnoreError);
	}

	/**
	 * 使用Map填充Bean对象，可配置将下划线转换为驼峰
	 *
	 * @param <T>           Bean类型
	 * @param map           Map
	 * @param bean          Bean
	 * @param isToCamelCase 是否将下划线模式转换为驼峰模式
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, boolean isToCamelCase, boolean isIgnoreError) {
		return fillBeanWithMap(map, bean, isToCamelCase, CopyOptions.create().setIgnoreError(isIgnoreError));
	}

	/**
	 * 使用Map填充Bean对象，忽略大小写
	 *
	 * @param <T>           Bean类型
	 * @param map           Map
	 * @param bean          Bean
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBeanWithMapIgnoreCase(Map<?, ?> map, T bean, boolean isIgnoreError) {
		return fillBeanWithMap(map, bean, CopyOptions.create().setIgnoreCase(true).setIgnoreError(isIgnoreError));
	}

	/**
	 * 使用Map填充Bean对象
	 *
	 * @param <T>         Bean类型
	 * @param map         Map
	 * @param bean        Bean
	 * @param copyOptions 属性复制选项 {@link CopyOptions}
	 * @return Bean
	 */
	public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, CopyOptions copyOptions) {
		return fillBeanWithMap(map, bean, false, copyOptions);
	}

	/**
	 * 使用Map填充Bean对象
	 *
	 * @param <T>           Bean类型
	 * @param map           Map
	 * @param bean          Bean
	 * @param isToCamelCase 是否将Map中的下划线风格key转换为驼峰风格
	 * @param copyOptions   属性复制选项 {@link CopyOptions}
	 * @return Bean
	 * @since 3.3.1
	 */
	public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, boolean isToCamelCase, CopyOptions copyOptions) {
		if (MapUtil.isEmpty(map)) {
			return bean;
		}
		if (isToCamelCase) {
			map = MapUtil.toCamelCaseMap(map);
		}
		return BeanCopier.create(map, bean, copyOptions).copy();
	}

	// --------------------------------------------------------------------------------------------- fillBean

	/**
	 * 对象或Map转Bean
	 *
	 * @param <T>    转换的Bean类型
	 * @param source Bean对象或Map
	 * @param clazz  目标的Bean类型
	 * @return Bean对象
	 * @since 4.1.20
	 */
	public static <T> T toBean(Object source, Class<T> clazz) {
		return toBean(source, clazz, null);
	}

	/**
	 * 对象或Map转Bean
	 *
	 * @param <T>     转换的Bean类型
	 * @param source  Bean对象或Map
	 * @param clazz   目标的Bean类型
	 * @param options 属性拷贝选项
	 * @return Bean对象
	 * @since 5.2.4
	 */
	public static <T> T toBean(Object source, Class<T> clazz, CopyOptions options) {
		final T target = ReflectUtil.newInstanceIfPossible(clazz);
		copyProperties(source, target, options);
		return target;
	}

	/**
	 * ServletRequest 参数转Bean
	 *
	 * @param <T>           Bean类型
	 * @param beanClass     Bean Class
	 * @param valueProvider 值提供者
	 * @param copyOptions   拷贝选项，见 {@link CopyOptions}
	 * @return Bean
	 */
	public static <T> T toBean(Class<T> beanClass, ValueProvider<String> valueProvider, CopyOptions copyOptions) {
		return fillBean(ReflectUtil.newInstance(beanClass), valueProvider, copyOptions);
	}

	/**
	 * 填充Bean的核心方法
	 *
	 * @param <T>           Bean类型
	 * @param bean          Bean
	 * @param valueProvider 值提供者
	 * @param copyOptions   拷贝选项，见 {@link CopyOptions}
	 * @return Bean
	 */
	public static <T> T fillBean(T bean, ValueProvider<String> valueProvider, CopyOptions copyOptions) {
		if (null == valueProvider) {
			return bean;
		}

		return BeanCopier.create(valueProvider, bean, copyOptions).copy();
	}

	// --------------------------------------------------------------------------------------------- beanToMap

	/**
	 * 对象转Map，不进行驼峰转下划线，不忽略值为空的字段
	 *
	 * @param bean bean对象
	 * @return Map
	 */
	public static Map<String, Object> beanToMap(Object bean) {
		return beanToMap(bean, false, false);
	}

	/**
	 * 对象转Map
	 *
	 * @param bean              bean对象
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue   是否忽略值为空的字段
	 * @return Map
	 */
	public static Map<String, Object> beanToMap(Object bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
		return beanToMap(bean, new LinkedHashMap<>(), isToUnderlineCase, ignoreNullValue);
	}

	/**
	 * 对象转Map
	 *
	 * @param bean              bean对象
	 * @param targetMap         目标的Map
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue   是否忽略值为空的字段
	 * @return Map
	 * @since 3.2.3
	 */
	public static Map<String, Object> beanToMap(Object bean, Map<String, Object> targetMap, final boolean isToUnderlineCase, boolean ignoreNullValue) {
		if (bean == null) {
			return null;
		}

		return beanToMap(bean, targetMap, ignoreNullValue, key -> isToUnderlineCase ? StrUtil.toUnderlineCase(key) : key);
	}

	/**
	 * 对象转Map<br>
	 * 通过实现{@link Editor} 可以自定义字段值，如果这个Editor返回null则忽略这个字段，以便实现：
	 *
	 * <pre>
	 * 1. 字段筛选，可以去除不需要的字段
	 * 2. 字段变换，例如实现驼峰转下划线
	 * 3. 自定义字段前缀或后缀等等
	 * </pre>
	 *
	 * @param bean            bean对象
	 * @param targetMap       目标的Map
	 * @param ignoreNullValue 是否忽略值为空的字段
	 * @param keyEditor       属性字段（Map的key）编辑器，用于筛选、编辑key
	 * @return Map
	 * @since 4.0.5
	 */
	public static Map<String, Object> beanToMap(Object bean, Map<String, Object> targetMap, boolean ignoreNullValue, Editor<String> keyEditor) {
		if (bean == null) {
			return null;
		}

		final Collection<PropDesc> props = BeanUtil.getBeanDesc(bean.getClass()).getProps();

		String key;
		Method getter;
		Object value;
		for (PropDesc prop : props) {
			key = prop.getFieldName();
			// 过滤class属性
			// 得到property对应的getter方法
			getter = prop.getGetter();
			if (null != getter) {
				// 只读取有getter方法的属性
				try {
					value = getter.invoke(bean);
				} catch (Exception ignore) {
					continue;
				}
				if (false == ignoreNullValue || (null != value && false == value.equals(bean))) {
					key = keyEditor.edit(key);
					if (null != key) {
						targetMap.put(key, value);
					}
				}
			}
		}
		return targetMap;
	}

	// --------------------------------------------------------------------------------------------- copyProperties

	/**
	 * 创建对应的Class对象并复制Bean对象属性
	 *
	 * @param <T>    对象类型
	 * @param source 源Bean对象
	 * @param tClass 目标Class
	 * @return 目标对象
	 */
	public static <T> T copyProperties(Object source, Class<T> tClass) {
		T target = ReflectUtil.newInstanceIfPossible(tClass);
		copyProperties(source, target, CopyOptions.create());
		return target;
	}

	/**
	 * 复制Bean对象属性
	 *
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 */
	public static void copyProperties(Object source, Object target) {
		copyProperties(source, target, CopyOptions.create());
	}

	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 *
	 * @param source           源Bean对象
	 * @param target           目标Bean对象
	 * @param ignoreProperties 不拷贝的的属性列表
	 */
	public static void copyProperties(Object source, Object target, String... ignoreProperties) {
		copyProperties(source, target, CopyOptions.create().setIgnoreProperties(ignoreProperties));
	}

	/**
	 * 复制Bean对象属性<br>
	 *
	 * @param source     源Bean对象
	 * @param target     目标Bean对象
	 * @param ignoreCase 是否忽略大小写
	 */
	public static void copyProperties(Object source, Object target, boolean ignoreCase) {
		BeanCopier.create(source, target, CopyOptions.create().setIgnoreCase(ignoreCase)).copy();
	}

	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 *
	 * @param source      源Bean对象
	 * @param target      目标Bean对象
	 * @param copyOptions 拷贝选项，见 {@link CopyOptions}
	 */
	public static void copyProperties(Object source, Object target, CopyOptions copyOptions) {
		if (null == copyOptions) {
			copyOptions = new CopyOptions();
		}
		BeanCopier.create(source, target, copyOptions).copy();
	}

	/**
	 * 给定的Bean的类名是否匹配指定类名字符串<br>
	 * 如果isSimple为{@code false}，则只匹配类名而忽略包名，例如：cn.hutool.TestEntity只匹配TestEntity<br>
	 * 如果isSimple为{@code true}，则匹配包括包名的全类名，例如：cn.hutool.TestEntity匹配cn.hutool.TestEntity
	 *
	 * @param bean          Bean
	 * @param beanClassName Bean的类名
	 * @param isSimple      是否只匹配类名而忽略包名，true表示忽略包名
	 * @return 是否匹配
	 * @since 4.0.6
	 */
	public static boolean isMatchName(Object bean, String beanClassName, boolean isSimple) {
		return ClassUtil.getClassName(bean, isSimple).equals(isSimple ? StrUtil.upperFirst(beanClassName) : beanClassName);
	}

	/**
	 * 把Bean里面的String属性做trim操作。此方法直接对传入的Bean做修改。
	 * <p>
	 * 通常bean直接用来绑定页面的input，用户的输入可能首尾存在空格，通常保存数据库前需要把首尾空格去掉
	 *
	 * @param <T>          Bean类型
	 * @param bean         Bean对象
	 * @param ignoreFields 不需要trim的Field名称列表（不区分大小写）
	 * @return 处理后的Bean对象
	 */
	public static <T> T trimStrFields(T bean, String... ignoreFields) {
		if (bean == null) {
			return null;
		}

		final Field[] fields = ReflectUtil.getFields(bean.getClass());
		for (Field field : fields) {
			if (ignoreFields != null && ArrayUtil.containsIgnoreCase(ignoreFields, field.getName())) {
				// 不处理忽略的Fields
				continue;
			}
			if (String.class.equals(field.getType())) {
				// 只有String的Field才处理
				final String val = (String) ReflectUtil.getFieldValue(bean, field);
				if (null != val) {
					final String trimVal = StrUtil.trim(val);
					if (false == val.equals(trimVal)) {
						// Field Value不为null，且首尾有空格才处理
						ReflectUtil.setFieldValue(bean, field, trimVal);
					}
				}
			}
		}

		return bean;
	}

	/**
	 * 判断Bean是否为非空对象，非空对象表示本身不为<code>null</code>或者含有非<code>null</code>属性的对象
	 *
	 * @param bean             Bean对象
	 * @param ignoreFiledNames 忽略检查的字段名
	 * @return 是否为空，<code>true</code> - 空 / <code>false</code> - 非空
	 * @since 5.0.7
	 */
	public static boolean isNotEmpty(Object bean, String... ignoreFiledNames) {
		return false == isEmpty(bean, ignoreFiledNames);
	}

	/**
	 * 判断Bean是否为空对象，空对象表示本身为<code>null</code>或者所有属性都为<code>null</code>
	 *
	 * @param bean             Bean对象
	 * @param ignoreFiledNames 忽略检查的字段名
	 * @return 是否为空，<code>true</code> - 空 / <code>false</code> - 非空
	 * @since 4.1.10
	 */
	public static boolean isEmpty(Object bean, String... ignoreFiledNames) {
		if (null != bean) {
			for (Field field : ReflectUtil.getFields(bean.getClass())) {
				if ((false == ArrayUtil.contains(ignoreFiledNames, field.getName()))
						&& null != ReflectUtil.getFieldValue(bean, field)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 判断Bean是否包含值为<code>null</code>的属性<br>
	 * 对象本身为<code>null</code>也返回true
	 *
	 * @param bean             Bean对象
	 * @param ignoreFiledNames 忽略检查的字段名
	 * @return 是否包含值为<code>null</code>的属性，<code>true</code> - 包含 / <code>false</code> - 不包含
	 * @since 4.1.10
	 */
	public static boolean hasNullField(Object bean, String... ignoreFiledNames) {
		if (null == bean) {
			return true;
		}
		for (Field field : ReflectUtil.getFields(bean.getClass())) {
			if ((false == ArrayUtil.contains(ignoreFiledNames, field.getName()))//
					&& null == ReflectUtil.getFieldValue(bean, field)) {
				return true;
			}
		}
		return false;
	}
}
