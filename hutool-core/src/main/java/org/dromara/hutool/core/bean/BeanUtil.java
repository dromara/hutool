/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.bean.copier.BeanCopier;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.core.bean.path.BeanPath;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.convert.impl.RecordConverter;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.map.CaseInsensitiveMap;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.reflect.ModifierUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.beans.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

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
	 * 创建动态Bean
	 *
	 * @param bean 普通Bean或Map
	 * @return {@link DynaBean}
	 * @since 3.0.7
	 */
	public static DynaBean createDynaBean(final Object bean) {
		return new DynaBean(bean);
	}

	/**
	 * 查找类型转换器 {@link PropertyEditor}
	 *
	 * @param type 需要转换的目标类型
	 * @return {@link PropertyEditor}
	 */
	public static PropertyEditor findEditor(final Class<?> type) {
		return PropertyEditorManager.findEditor(type);
	}

	/**
	 * 获取{@link BeanDesc} Bean描述信息
	 *
	 * @param clazz Bean类
	 * @return {@link BeanDesc}
	 * @since 3.1.2
	 */
	public static BeanDesc getBeanDesc(final Class<?> clazz) {
		return BeanDescCache.INSTANCE.getBeanDesc(clazz, () -> new BeanDesc(clazz));
	}

	/**
	 * 遍历Bean的属性
	 *
	 * @param clazz  Bean类
	 * @param action 每个元素的处理类
	 * @since 5.4.2
	 */
	public static void descForEach(final Class<?> clazz, final Consumer<? super PropDesc> action) {
		getBeanDesc(clazz).getProps().forEach(action);
	}

	// region ----- getPropertyDescriptor
	/**
	 * 获得Bean字段描述数组
	 *
	 * @param clazz Bean类
	 * @return 字段描述数组
	 * @throws BeanException 获取属性异常
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(final Class<?> clazz) throws BeanException {
		final BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(clazz);
		} catch (final IntrospectionException e) {
			throw new BeanException(e);
		}
		return ArrayUtil.filter(beanInfo.getPropertyDescriptors(), t -> {
			// 过滤掉getClass方法
			return !"class".equals(t.getName());
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
	public static Map<String, PropertyDescriptor> getPropertyDescriptorMap(final Class<?> clazz, final boolean ignoreCase) throws BeanException {
		return BeanInfoCache.INSTANCE.getPropertyDescriptorMap(clazz, ignoreCase, () -> internalGetPropertyDescriptorMap(clazz, ignoreCase));
	}

	/**
	 * 获得字段名和字段描述Map。内部使用，直接获取Bean类的PropertyDescriptor
	 *
	 * @param clazz      Bean类
	 * @param ignoreCase 是否忽略大小写
	 * @return 字段名和字段描述Map
	 * @throws BeanException 获取属性异常
	 */
	private static Map<String, PropertyDescriptor> internalGetPropertyDescriptorMap(final Class<?> clazz, final boolean ignoreCase) throws BeanException {
		final PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);
		final Map<String, PropertyDescriptor> map = ignoreCase ? new CaseInsensitiveMap<>(propertyDescriptors.length, 1f)
			: new HashMap<>(propertyDescriptors.length, 1);

		for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
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
	public static PropertyDescriptor getPropertyDescriptor(final Class<?> clazz, final String fieldName) throws BeanException {
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
	public static PropertyDescriptor getPropertyDescriptor(final Class<?> clazz, final String fieldName, final boolean ignoreCase) throws BeanException {
		final Map<String, PropertyDescriptor> map = getPropertyDescriptorMap(clazz, ignoreCase);
		return (null == map) ? null : map.get(fieldName);
	}
	// endregion

	/**
	 * 获得字段值，通过反射直接获得字段值，并不调用getXXX方法<br>
	 * 对象同样支持Map类型，fieldNameOrIndex即为key
	 *
	 * <ul>
	 *     <li>Map: fieldNameOrIndex需为key，获取对应value</li>
	 *     <li>Collection: fieldNameOrIndex当为数字，返回index对应值，非数字遍历集合返回子bean对应name值</li>
	 *     <li>Array: fieldNameOrIndex当为数字，返回index对应值，非数字遍历数组返回子bean对应name值</li>
	 * </ul>
	 *
	 * @param bean             Bean对象
	 * @param fieldNameOrIndex 字段名或序号，序号支持负数
	 * @return 字段值
	 */
	public static Object getFieldValue(final Object bean, final String fieldNameOrIndex) {
		if (null == bean || null == fieldNameOrIndex) {
			return null;
		}

		if (bean instanceof Map) {
			return ((Map<?, ?>) bean).get(fieldNameOrIndex);
		} else if (bean instanceof Collection) {
			try {
				return CollUtil.get((Collection<?>) bean, Integer.parseInt(fieldNameOrIndex));
			} catch (final NumberFormatException e) {
				// 非数字，see pr#254@Gitee
				return CollUtil.map((Collection<?>) bean, (beanEle) -> getFieldValue(beanEle, fieldNameOrIndex), false);
			}
		} else if (ArrayUtil.isArray(bean)) {
			try {
				return ArrayUtil.get(bean, Integer.parseInt(fieldNameOrIndex));
			} catch (final NumberFormatException e) {
				// 非数字，see pr#254@Gitee
				return ArrayUtil.map(bean, Object.class, (beanEle) -> getFieldValue(beanEle, fieldNameOrIndex));
			}
		} else {// 普通Bean对象
			return FieldUtil.getFieldValue(bean, fieldNameOrIndex);
		}
	}

	/**
	 * 设置字段值，通过反射设置字段值，并不调用setXXX方法<br>
	 * 对象同样支持Map类型，fieldNameOrIndex即为key，支持：
	 * <ul>
	 *     <li>Map</li>
	 *     <li>List</li>
	 *     <li>Bean</li>
	 * </ul>
	 *
	 * @param bean             Bean
	 * @param fieldNameOrIndex 字段名或序号，序号支持负数
	 * @param value            值
	 * @return bean，当为数组时，返回一个新的数组
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Object setFieldValue(final Object bean, final String fieldNameOrIndex, final Object value) {
		if (bean instanceof Map) {
			((Map) bean).put(fieldNameOrIndex, value);
		} else if (bean instanceof List) {
			ListUtil.setOrPadding((List) bean, Convert.toInt(fieldNameOrIndex), value);
		} else if (ArrayUtil.isArray(bean)) {
			// issue#3008，追加产生新数组，此处返回新数组
			return ArrayUtil.setOrPadding(bean, Convert.toInt(fieldNameOrIndex), value);
		} else {
			// 普通Bean对象
			FieldUtil.setFieldValue(bean, fieldNameOrIndex, value);
		}
		return bean;
	}

	/**
	 * 获取Bean中的属性值
	 *
	 * @param <T>        属性值类型
	 * @param bean       Bean对象，支持Map、List、Collection、Array
	 * @param expression 表达式，例如：person.friend[5].name
	 * @return Bean属性值，bean为{@code null}或者express为空，返回{@code null}
	 * @see BeanPath#getValue(Object)
	 * @since 3.0.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(final Object bean, final String expression) {
		if (null == bean || StrUtil.isBlank(expression)) {
			return null;
		}
		return (T) BeanPath.of(expression).getValue(bean);
	}

	/**
	 * 设置Bean中的属性值
	 *
	 * @param bean       Bean对象，支持Map、List、Collection、Array
	 * @param expression 表达式，例如：person.friend[5].name
	 * @param value      属性值
	 * @see BeanPath#setValue(Object, Object)
	 * @since 4.0.6
	 */
	public static void setProperty(final Object bean, final String expression, final Object value) {
		BeanPath.of(expression).setValue(bean, value);
	}

	// region ----- toBean
	/**
	 * 对象或Map转Bean
	 *
	 * @param <T>    转换的Bean类型
	 * @param source Bean对象或Map
	 * @param clazz  目标的Bean类型
	 * @return Bean对象
	 * @since 4.1.20
	 */
	public static <T> T toBean(final Object source, final Class<T> clazz) {
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
	public static <T> T toBean(final Object source, final Class<T> clazz, final CopyOptions options) {
		return toBean(source, () -> ConstructorUtil.newInstanceIfPossible(clazz), options);
	}

	/**
	 * 对象或Map转Bean
	 *
	 * @param <T>            转换的Bean类型
	 * @param source         Bean对象或Map或{@link ValueProvider}
	 * @param targetSupplier 目标的Bean创建器
	 * @param options        属性拷贝选项
	 * @return Bean对象
	 * @since 5.8.0
	 */
	public static <T> T toBean(final Object source, final Supplier<T> targetSupplier, final CopyOptions options) {
		if (null == source || null == targetSupplier) {
			return null;
		}
		final T target = targetSupplier.get();
		copyProperties(source, target, options);
		return target;
	}
	// endregion

	// region ----- fillBean
	/**
	 * 填充Bean的核心方法
	 *
	 * @param <T>           Bean类型
	 * @param bean          Bean
	 * @param valueProvider 值提供者
	 * @param copyOptions   拷贝选项，见 {@link CopyOptions}
	 * @return Bean
	 */
	public static <T> T fillBean(final T bean, final ValueProvider<String> valueProvider, final CopyOptions copyOptions) {
		if (null == valueProvider) {
			return bean;
		}

		return BeanCopier.of(valueProvider, bean, copyOptions).copy();
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
	public static <T> T fillBeanWithMap(final Map<?, ?> map, final T bean, final CopyOptions copyOptions) {
		if (MapUtil.isEmpty(map)) {
			return bean;
		}
		return copyProperties(map, bean, copyOptions);
	}
	// endregion

	// region ----- beanToMap
	/**
	 * 将bean的部分属性转换成map<br>
	 * 可选拷贝哪些属性值，默认是不忽略值为{@code null}的值的。
	 *
	 * @param bean       bean
	 * @param properties 需要拷贝的属性值，{@code null}或空表示拷贝所有值
	 * @return Map
	 * @since 5.8.0
	 */
	public static Map<String, Object> beanToMap(final Object bean, final String... properties) {
		int mapSize = 16;
		UnaryOperator<MutableEntry<String, Object>> editor = null;
		if (ArrayUtil.isNotEmpty(properties)) {
			mapSize = properties.length;
			final Set<String> propertiesSet = SetUtil.of(properties);
			editor = entry -> {
				final String key = entry.getKey();
				entry.setKey(propertiesSet.contains(key) ? key : null);
				return entry;
			};
		}

		// 指明了要复制的属性 所以不忽略null值
		return beanToMap(bean, new LinkedHashMap<>(mapSize, 1), false, editor);
	}

	/**
	 * 对象转Map
	 *
	 * @param bean              bean对象
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue   是否忽略值为空的字段
	 * @return Map
	 */
	public static Map<String, Object> beanToMap(final Object bean, final boolean isToUnderlineCase, final boolean ignoreNullValue) {
		if (null == bean) {
			return null;
		}
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
	public static Map<String, Object> beanToMap(final Object bean, final Map<String, Object> targetMap, final boolean isToUnderlineCase, final boolean ignoreNullValue) {
		if (null == bean) {
			return null;
		}

		return beanToMap(bean, targetMap, ignoreNullValue, entry -> {
			final String key = entry.getKey();
			entry.setKey(isToUnderlineCase ? StrUtil.toUnderlineCase(key) : key);
			return entry;
		});
	}

	/**
	 * 对象转Map<br>
	 * 通过实现{@link UnaryOperator} 可以自定义字段值，如果这个Editor返回null则忽略这个字段，以便实现：
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
	 * @param keyEditor       属性字段（Map的key）编辑器，用于筛选、编辑key，如果这个Editor返回null则忽略这个字段
	 * @return Map
	 * @since 4.0.5
	 */
	public static Map<String, Object> beanToMap(final Object bean, final Map<String, Object> targetMap,
												final boolean ignoreNullValue, final UnaryOperator<MutableEntry<String, Object>> keyEditor) {
		if (null == bean) {
			return null;
		}

		return BeanCopier.of(bean, targetMap,
			CopyOptions.of()
				.setIgnoreNullValue(ignoreNullValue)
				.setFieldEditor(keyEditor)
		).copy();
	}

	/**
	 * 对象转Map<br>
	 * 通过自定义{@link CopyOptions} 完成抓换选项，以便实现：
	 *
	 * <pre>
	 * 1. 字段筛选，可以去除不需要的字段
	 * 2. 字段变换，例如实现驼峰转下划线
	 * 3. 自定义字段前缀或后缀等等
	 * 4. 字段值处理
	 * ...
	 * </pre>
	 *
	 * @param bean        bean对象
	 * @param targetMap   目标的Map
	 * @param copyOptions 拷贝选项
	 * @return Map
	 * @since 5.7.15
	 */
	public static Map<String, Object> beanToMap(final Object bean, final Map<String, Object> targetMap, final CopyOptions copyOptions) {
		if (null == bean) {
			return null;
		}

		return BeanCopier.of(bean, targetMap, copyOptions).copy();
	}
	// endregion

	// region ----- copyProperties

	/**
	 * 按照Bean对象属性创建对应的Class对象，并忽略某些属性
	 *
	 * @param <T>              对象类型
	 * @param source           源Bean对象
	 * @param tClass           目标Class
	 * @param ignoreProperties 不拷贝的的属性列表
	 * @return 目标对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copyProperties(final Object source, final Class<T> tClass, final String... ignoreProperties) {
		if (null == source) {
			return null;
		}
		if (RecordUtil.isRecord(tClass)) {
			// issue#I7EO3U
			// 转换record时，ignoreProperties无效
			return (T) RecordConverter.INSTANCE.convert(tClass, source);
		}
		final T target = ConstructorUtil.newInstanceIfPossible(tClass);
		return copyProperties(source, target, CopyOptions.of().setIgnoreProperties(ignoreProperties));
	}

	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 *
	 * @param <T>              目标类型
	 * @param source           源Bean对象
	 * @param target           目标Bean对象
	 * @param ignoreProperties 不拷贝的的属性列表
	 * @return 目标对象
	 */
	public static <T> T copyProperties(final Object source, final T target, final String... ignoreProperties) {
		return copyProperties(source, target, CopyOptions.of().setIgnoreProperties(ignoreProperties));
	}

	/**
	 * 复制Bean对象属性<br>
	 *
	 * @param <T>        目标类型
	 * @param source     源Bean对象
	 * @param target     目标Bean对象
	 * @param ignoreCase 是否忽略大小写
	 * @return 目标对象
	 */
	public static <T> T copyProperties(final Object source, final T target, final boolean ignoreCase) {
		return BeanCopier.of(source, target, CopyOptions.of().setIgnoreCase(ignoreCase)).copy();
	}

	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 *
	 * @param <T>         目标类型
	 * @param source      源Bean对象
	 * @param target      目标Bean对象
	 * @param copyOptions 拷贝选项，见 {@link CopyOptions}
	 * @return 目标对象
	 */
	public static <T> T copyProperties(final Object source, final T target, final CopyOptions copyOptions) {
		if (null == source || null == target) {
			return null;
		}
		return BeanCopier.of(source, target, ObjUtil.defaultIfNull(copyOptions, CopyOptions::of)).copy();
	}

	/**
	 * 复制集合中的Bean属性<br>
	 * 此方法遍历集合中每个Bean，复制其属性后加入一个新的{@link List}中。
	 *
	 * @param collection 原Bean集合
	 * @param targetType 目标Bean类型
	 * @param <T>        Bean类型
	 * @return 复制后的List
	 * @since 5.6.6
	 */
	public static <T> List<T> copyToList(final Collection<?> collection, final Class<T> targetType) {
		return copyToList(collection, targetType, CopyOptions.of());
	}

	/**
	 * 复制集合中的Bean属性<br>
	 * 此方法遍历集合中每个Bean，复制其属性后加入一个新的{@link List}中。
	 *
	 * @param collection  原Bean集合
	 * @param targetType  目标Bean类型
	 * @param copyOptions 拷贝选项
	 * @param <T>         Bean类型
	 * @return 复制后的List
	 * @since 5.6.4
	 */
	public static <T> List<T> copyToList(final Collection<?> collection, final Class<T> targetType, final CopyOptions copyOptions) {
		if (null == collection) {
			return null;
		}
		if (collection.isEmpty()) {
			return new ArrayList<>(0);
		}

		// issue#3091
		if (ClassUtil.isBasicType(targetType) || String.class == targetType) {
			return Convert.toList(targetType, collection);
		}

		return collection.stream().map((source) -> {

			final T target = ConstructorUtil.newInstanceIfPossible(targetType);
			copyProperties(source, target, copyOptions);
			return target;
		}).collect(Collectors.toList());
	}
	// endregion

	/**
	 * 给定的Bean的类名是否匹配指定类名字符串<br>
	 * 如果isSimple为{@code true}，则只匹配类名而忽略包名，例如：org.dromara.hutool.TestEntity只匹配TestEntity<br>
	 * 如果isSimple为{@code false}，则匹配包括包名的全类名，例如：org.dromara.hutool.TestEntity匹配org.dromara.hutool.TestEntity
	 *
	 * @param bean          Bean
	 * @param beanClassName Bean的类名
	 * @param isSimple      是否只匹配类名而忽略包名，true表示忽略包名
	 * @return 是否匹配
	 * @since 4.0.6
	 */
	public static boolean isMatchName(final Object bean, final String beanClassName, final boolean isSimple) {
		if (null == bean || StrUtil.isBlank(beanClassName)) {
			return false;
		}
		return ClassUtil.getClassName(bean, isSimple).equals(isSimple ? StrUtil.upperFirst(beanClassName) : beanClassName);
	}

	/**
	 * 编辑Bean的字段，static字段不会处理<br>
	 * 例如需要对指定的字段做判空操作、null转""操作等等。
	 *
	 * @param bean   bean
	 * @param editor 编辑器函数
	 * @param <T>    被编辑的Bean类型
	 * @return bean
	 * @since 5.6.4
	 */
	public static <T> T edit(final T bean, final UnaryOperator<Field> editor) {
		if (bean == null) {
			return null;
		}

		final Field[] fields = FieldUtil.getFields(bean.getClass());
		for (final Field field : fields) {
			if (ModifierUtil.isStatic(field)) {
				continue;
			}
			editor.apply(field);
		}
		return bean;
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
	public static <T> T trimStrFields(final T bean, final String... ignoreFields) {
		return edit(bean, (field) -> {
			if (ignoreFields != null && ArrayUtil.containsIgnoreCase(ignoreFields, field.getName())) {
				// 不处理忽略的Fields
				return field;
			}
			if (String.class.equals(field.getType())) {
				// 只有String的Field才处理
				final String val = (String) FieldUtil.getFieldValue(bean, field);
				if (null != val) {
					final String trimVal = StrUtil.trim(val);
					if (!val.equals(trimVal)) {
						// Field Value不为null，且首尾有空格才处理
						FieldUtil.setFieldValue(bean, field, trimVal);
					}
				}
			}
			return field;
		});
	}

	/**
	 * 判断Bean是否为空对象，空对象表示本身为{@code null}或者所有属性都为{@code null}<br>
	 * 此方法不判断static属性
	 *
	 * @param bean             Bean对象
	 * @param ignoreFieldNames 忽略检查的字段名
	 * @return 是否为空，{@code true} - 空 / {@code false} - 非空
	 * @since 4.1.10
	 */
	public static boolean isEmpty(final Object bean, final String... ignoreFieldNames) {
		// 不含有非空字段
		return !isNotEmpty(bean, ignoreFieldNames);
	}

	/**
	 * 判断Bean是否为非空对象，非空对象表示本身不为{@code null}或者含有非{@code null}属性的对象
	 *
	 * @param bean             Bean对象
	 * @param ignoreFieldNames 忽略检查的字段名
	 * @return 是否为非空，{@code true} - 非空 / {@code false} - 空
	 * @since 5.0.7
	 */
	public static boolean isNotEmpty(final Object bean, final String... ignoreFieldNames) {
		if (null == bean) {
			return false;
		}

		// 相当于 hasNoneNullField
		return checkBean(bean, field ->
			(!ArrayUtil.contains(ignoreFieldNames, field.getName()))
				&& null != FieldUtil.getFieldValue(bean, field)
		);
	}

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
	public static boolean isReadableBean(final Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return hasGetter(clazz) || hasPublicField(clazz);
	}

	/**
	 * 判断是否为可写Bean对象，判定方法是：
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
	public static boolean isWritableBean(final Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return hasSetter(clazz) || hasPublicField(clazz);
	}

	// region ----- hasXXX
	/**
	 * 判断是否有Setter方法<br>
	 * 判定方法是否存在只有一个参数的setXXX方法
	 *
	 * @param clazz 待测试类
	 * @return 是否为Bean对象
	 * @since 4.2.2
	 */
	public static boolean hasSetter(final Class<?> clazz) {
		if (ClassUtil.isNormalClass(clazz)) {
			for (final Method method : clazz.getMethods()) {
				if (method.getParameterCount() == 1 && method.getName().startsWith("set")) {
					// 检测包含标准的setXXX方法即视为标准的JavaBean
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否为Bean对象<br>
	 * 判定方法是否存在只有无参数的getXXX方法或者isXXX方法
	 *
	 * @param clazz 待测试类
	 * @return 是否为Bean对象
	 * @since 4.2.2
	 */
	public static boolean hasGetter(final Class<?> clazz) {
		if (ClassUtil.isNormalClass(clazz)) {
			for (final Method method : clazz.getMethods()) {
				if (method.getParameterCount() == 0) {
					final String name = method.getName();
					if (name.startsWith("get") || name.startsWith("is")) {
						if (!"getClass".equals(name)) {
							return true;
						}
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
	public static boolean hasPublicField(final Class<?> clazz) {
		if (ClassUtil.isNormalClass(clazz)) {
			for (final Field field : clazz.getFields()) {
				if (ModifierUtil.isPublic(field) && !ModifierUtil.isStatic(field)) {
					//非static的public字段
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断Bean是否包含值为{@code null}的属性<br>
	 * 对象本身为{@code null}也返回true
	 *
	 * @param bean             Bean对象
	 * @param ignoreFieldNames 忽略检查的字段名
	 * @return 是否包含值为{@code null}的属性，{@code true} - 包含 / {@code false} - 不包含
	 * @since 4.1.10
	 */
	public static boolean hasNullField(final Object bean, final String... ignoreFieldNames) {
		return checkBean(bean, field ->
			(!ArrayUtil.contains(ignoreFieldNames, field.getName()))
				&& null == FieldUtil.getFieldValue(bean, field)
		);
	}

	/**
	 * 判断Bean是否包含值为{@code null}的属性，或当字段为{@link CharSequence}时，是否为isEmpty（null或""）<br>
	 * 对象本身为{@code null}也返回true
	 *
	 * @param bean             Bean对象
	 * @param ignoreFieldNames 忽略检查的字段名
	 * @return 是否包含值为{@code null}的属性，{@code true} - 包含 / {@code false} - 不包含
	 * @since 4.1.10
	 */
	public static boolean hasEmptyField(final Object bean, final String... ignoreFieldNames) {
		return checkBean(bean, field ->
			(!ArrayUtil.contains(ignoreFieldNames, field.getName()))
				&& StrUtil.isEmptyIfStr(FieldUtil.getFieldValue(bean, field))
		);
	}
	// endregion

	/**
	 * 检查Bean<br>
	 * 遍历Bean的字段并断言检查字段，当某个字段：
	 * 断言为{@code true} 时，返回{@code true}并不再检查后续字段；<br>
	 * 断言为{@code false}时，继续检查后续字段
	 *
	 * @param bean      Bean
	 * @param predicate 断言
	 * @return 是否触发断言为真
	 */
	public static boolean checkBean(final Object bean, final Predicate<Field> predicate) {
		if (null == bean) {
			return true;
		}
		for (final Field field : FieldUtil.getFields(bean.getClass())) {
			if (ModifierUtil.isStatic(field)) {
				continue;
			}
			if (predicate.test(field)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取Getter或Setter方法名对应的字段名称，规则如下：
	 * <ul>
	 *     <li>getXxxx获取为xxxx，如getName得到name。</li>
	 *     <li>setXxxx获取为xxxx，如setName得到name。</li>
	 *     <li>isXxxx获取为xxxx，如isName得到name。</li>
	 *     <li>其它不满足规则的方法名抛出{@link IllegalArgumentException}</li>
	 * </ul>
	 *
	 * @param getterOrSetterName Getter或Setter方法名
	 * @return 字段名称
	 * @throws IllegalArgumentException 非Getter或Setter方法
	 * @since 5.7.23
	 */
	public static String getFieldName(final String getterOrSetterName) {
		if (getterOrSetterName.startsWith("get") || getterOrSetterName.startsWith("set")) {
			return StrUtil.removePreAndLowerFirst(getterOrSetterName, 3);
		} else if (getterOrSetterName.startsWith("is")) {
			return StrUtil.removePreAndLowerFirst(getterOrSetterName, 2);
		} else {
			throw new IllegalArgumentException("Invalid Getter or Setter name: " + getterOrSetterName);
		}
	}
}
