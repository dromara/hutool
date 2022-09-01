package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.iter.IterUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.SerializeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.math.NumberUtil;
import cn.hutool.core.reflect.ClassUtil;
import cn.hutool.core.reflect.MethodUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 对象工具类，包括判空、克隆、序列化等操作
 *
 * @author Looly
 */
public class ObjUtil {

	/**
	 * <p>比较两个对象是否相等，满足下述任意条件即返回{@code true}：
	 * <ul>
	 *     <li>若两对象皆为{@link BigDecimal}，且满足{@code 0 == obj1.compareTo(obj2)}</li>
	 *     <li>{@code obj1 == null && obj2 == null}</li>
	 *     <li>{@code obj1.equals(obj2)}</li>
	 * </ul>
	 *
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否相等
	 * @since 5.4.3
	 */
	public static boolean equals(final Object obj1, final Object obj2) {
		if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
			return NumberUtil.equals((BigDecimal) obj1, (BigDecimal) obj2);
		}
		return Objects.equals(obj1, obj2);
	}

	/**
	 * 比较两个对象是否不相等
	 *
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否不等
	 * @since 3.0.7
	 * @see #equals(Object, Object)
	 */
	public static boolean notEquals(final Object obj1, final Object obj2) {
		return false == equals(obj1, obj2);
	}

	/**
	 * <p>计算对象长度，支持类型包括：
	 * <ul>
	 *     <li>{@code null}：默认返回{@code 0}；</li>
	 *     <li>数组：返回数组长度；</li>
	 *     <li>{@link CharSequence}：返回{@link CharSequence#length()}；</li>
	 *     <li>{@link Collection}：返回{@link Collection#size()}；</li>
	 *     <li>{@link Iterator}或{@link Iterable}：可迭代的元素数量；</li>
	 *     <li>{@link Enumeration}：返回可迭代的元素数量；</li>
	 * </ul>
	 *
	 * @param obj 被计算长度的对象
	 * @return 长度
	 */
	public static int length(final Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length();
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).size();
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).size();
		}

		int count;
		if (obj instanceof Iterator || obj instanceof Iterable) {
			final Iterator<?> iter = (obj instanceof Iterator) ? (Iterator<?>) obj : ((Iterable<?>) obj).iterator();
			count = 0;
			while (iter.hasNext()) {
				count++;
				iter.next();
			}
			return count;
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj);
		}
		if (obj instanceof Enumeration) {
			final Enumeration<?> enumeration = (Enumeration<?>) obj;
			count = 0;
			while (enumeration.hasMoreElements()) {
				count++;
				enumeration.nextElement();
			}
			return count;
		}
		return -1;
	}

	/**
	 * <p>检查{@code obj}中是否包含{@code element}，若{@code obj}为{@code null}，则直接返回{@code false}。<br>
	 * 支持类型包括：
	 * <ul>
	 *     <li>{@code null}：默认返回{@code false}；</li>
	 *     <li>{@link String}：等同{@link String#contains(CharSequence)}；</li>
	 *     <li>{@link Collection}：等同{@link Collection#contains(Object)}；</li>
	 *     <li>{@link Map}：等同{@link Map#containsValue(Object)}；</li>
	 *     <li>
	 *         {@link Iterator}、{@link Iterable}、{@link Enumeration}或数组：
	 *         等同于遍历后对其元素调用{@link #equals(Object, Object)}方法；
	 *     </li>
	 * </ul>
	 *
	 * @param obj     对象
	 * @param element 元素
	 * @return 是否包含
	 */
	public static boolean contains(final Object obj, final Object element) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			if (element == null) {
				return false;
			}
			return ((String) obj).contains(element.toString());
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).contains(element);
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).containsValue(element);
		}

		if (obj instanceof Iterator || obj instanceof Iterable) {
			final Iterator<?> iter = obj instanceof Iterator ? (Iterator<?>) obj : ((Iterable<?>) obj).iterator();
			while (iter.hasNext()) {
				final Object o = iter.next();
				if (equals(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj instanceof Enumeration) {
			final Enumeration<?> enumeration = (Enumeration<?>) obj;
			while (enumeration.hasMoreElements()) {
				final Object o = enumeration.nextElement();
				if (equals(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (ArrayUtil.isArray(obj)) {
			final int len = Array.getLength(obj);
			for (int i = 0; i < len; i++) {
				final Object o = Array.get(obj, i);
				if (equals(o, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检查对象是否为{@code null}
	 *
	 * @param obj 对象
	 * @return 是否为null
	 */
	public static boolean isNull(final Object obj) {
		return null == obj;
	}

	/**
	 * 检查对象是否不为{@code null}
	 *
	 * @param obj 对象
	 * @return 是否为null
	 */
	public static boolean isNotNull(final Object obj) {
		return null != obj;
	}

	/**
	 * 判断指定对象是否为空，支持类型包括：
	 * <ul>
	 *     <li>{@code null}：默认返回{@code true}；</li>
	 *     <li>数组：等同于{@link ArrayUtil#isEmpty(Object)}；</li>
	 *     <li>{@link CharSequence}：等同于{@link CharSequenceUtil#isEmpty(CharSequence)}；</li>
	 *     <li>{@link Collection}：等同于{@link CollUtil#isEmpty(Collection)}；</li>
	 *     <li>{@link Map}：等同于{@link MapUtil#isEmpty(Map)}；</li>
	 *     <li>
	 *         {@link Iterator}或{@link Iterable}：等同于{@link IterUtil#isEmpty(Iterator)}、
	 *         {@link IterUtil#isEmpty(Iterable)}；
	 *     </li>
	 * </ul>
	 *
	 * @param obj 被判断的对象
	 * @return 是否为空，如果类型不支持，返回false
	 * @since 4.5.7
	 * @see StrUtil#isEmpty(CharSequence)
	 * @see MapUtil#isEmpty(Map)
	 * @see IterUtil#isEmpty(Iterable)
	 * @see IterUtil#isEmpty(Iterator)
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(final Object obj) {
		if (null == obj) {
			return true;
		}

		if (obj instanceof CharSequence) {
			return StrUtil.isEmpty((CharSequence) obj);
		} else if(obj instanceof Collection){
			return CollUtil.isEmpty((Collection)obj);
		}else if (obj instanceof Map) {
			return MapUtil.isEmpty((Map) obj);
		} else if (obj instanceof Iterable) {
			return IterUtil.isEmpty((Iterable) obj);
		} else if (obj instanceof Iterator) {
			return IterUtil.isEmpty((Iterator) obj);
		} else if (ArrayUtil.isArray(obj)) {
			return ArrayUtil.isEmpty(obj);
		}

		return false;
	}

	/**
	 * 判断指定对象是否为非空
	 *
	 * @param obj 被判断的对象
	 * @return 是否为空，如果类型不支持，返回true
	 * @since 4.5.7
	 * @see #isEmpty(Object)
	 */
	public static boolean isNotEmpty(final Object obj) {
		return false == isEmpty(obj);
	}

	/**
	 * <p>如果给定对象为{@code null}返回默认值
	 * <pre>{@code
	 * ObjectUtil.defaultIfNull(null, null);      // = null
	 * ObjectUtil.defaultIfNull(null, "");        // = ""
	 * ObjectUtil.defaultIfNull(null, "zz");      // = "zz"
	 * ObjectUtil.defaultIfNull("abc", *);        // = "abc"
	 * ObjectUtil.defaultIfNull(Boolean.TRUE, *); // = Boolean.TRUE
	 * }</pre>
	 *
	 * @param <T>          对象类型
	 * @param object       被检查对象，可能为{@code null}
	 * @param defaultValue 被检查对象为{@code null}返回的默认值，可以为{@code null}
	 * @return 被检查对象为{@code null}返回默认值，否则返回原值
	 * @since 3.0.7
	 */
	public static <T> T defaultIfNull(final T object, final T defaultValue) {
		return isNull(object) ? defaultValue : object;
	}

	/**
	 * 如果给定对象为{@code null} 返回默认值, 如果不为null 返回自定义handle处理后的返回值
	 *
	 * @param <T>          被检查对象类型
	 * @param source       Object 类型对象
	 * @param defaultSupplier 默认为空的处理逻辑
	 * @return 处理后的返回值
	 * @since 5.4.6
	 */
	public static <T> T defaultIfNull(final T source, final Supplier<? extends T> defaultSupplier) {
		if (isNotNull(source)) {
			return source;
		}
		return defaultSupplier.get();
	}

	/**
	 * 如果给定对象为{@code null} 返回默认值, 如果不为null 返回自定义handle处理后的返回值
	 *
	 * @param <R>          返回值类型
	 * @param <T>          被检查对象类型
	 * @param source       Object 类型对象
	 * @param handler      非空时自定义的处理方法
	 * @param defaultSupplier 默认为空的处理逻辑
	 * @return 处理后的返回值
	 * @since 6.0.0
	 */
	public static <T, R> R defaultIfNull(final T source, final Function<? super T, ? extends R> handler, final Supplier<? extends R> defaultSupplier) {
		if (isNotNull(source)) {
			return handler.apply(source);
		}
		return defaultSupplier.get();
	}

	/**
	 * 如果给定对象为{@code null}返回默认值, 如果不为null返回自定义handle处理后的返回值
	 *
	 * @param <R>          返回值类型
	 * @param <T>          被检查对象类型
	 * @param source       Object 类型对象
	 * @param handler      非空时自定义的处理方法
	 * @param defaultValue 默认为空的返回值
	 * @return 处理后的返回值
	 * @since 6.0.0
	 */
	public static <T, R> R defaultIfNull(
		final T source, final Function<? super T, ? extends R> handler, final R defaultValue) {
		return isNull(source) ? defaultValue : handler.apply(source);
	}

	/**
	 * <p>克隆对象
	 * <ol>
	 *     <li>如果对象是数组，则等同于{@link ArrayUtil#clone(Object)}；</li>
	 *     <li>如果对象实现了{@link Cloneable}接口，调用其{@link Cloneable#clone()}方法；</li>
	 *     <li>如果对象实现了{@link Serializable}接口，执行深度克隆；</li>
	 *     <li>不符合上述任意情况则返回{@code null}；</li>
	 * </ol>
	 *
	 * @param <T> 对象类型
	 * @param obj 被克隆对象
	 * @return 克隆后的对象
	 * @see ArrayUtil#clone(Object)
	 * @see Object#clone()
	 * @see #cloneByStream(Object)
	 */
	public static <T> T clone(final T obj) {
		T result = ArrayUtil.clone(obj);
		if (null == result) {
			if (obj instanceof Cloneable) {
				result = MethodUtil.invoke(obj, "clone");
			} else {
				result = cloneByStream(obj);
			}
		}
		return result;
	}

	/**
	 * 返回克隆后的对象，如果克隆失败，返回原对象
	 *
	 * @param <T> 对象类型
	 * @param obj 对象
	 * @return 克隆后或原对象
	 * @see #clone(Object)
	 */
	public static <T> T cloneIfPossible(final T obj) {
		T clone = null;
		try {
			clone = clone(obj);
		} catch (final Exception e) {
			// pass
		}
		return clone == null ? obj : clone;
	}

	/**
	 * 序列化后拷贝流的方式克隆<br>
	 * 若对象未实现{@link Serializable}接口则默认返回{@code null}
	 *
	 * @param <T> 对象类型
	 * @param obj 被克隆对象
	 * @return 克隆后的对象
	 * @throws UtilException IO异常和ClassNotFoundException封装
	 * @see SerializeUtil#clone(Object)
	 */
	public static <T> T cloneByStream(final T obj) {
		return SerializeUtil.clone(obj);
	}

	/**
	 * 是否为基本类型，包括包装类型和非包装类型
	 *
	 * @param object 被检查对象，{@code null}返回{@code false}
	 * @return 是否为基本类型
	 * @see ClassUtil#isBasicType(Class)
	 */
	public static boolean isBasicType(final Object object) {
		if (null == object) {
			return false;
		}
		return ClassUtil.isBasicType(object.getClass());
	}

	/**
	 * 检查是否为有效的数字，若对象不为{@link Number}，则直接返回{@code true}，否则：
	 * <ul>
	 *     <li>若对象类型为{@link Double}，则检查{@link Double#isInfinite()}或{@link Double#isNaN()}；</li>
	 *     <li>若对象类型为{@link Float}，则检查{@link Float#isInfinite()}或{@link Float#isNaN()}；</li>
	 * </ul>
	 *
	 * @param obj 被检查类型
	 * @return 检查结果，非数字类型和Null将返回true
	 * @see NumberUtil#isValidNumber(Number)
	 */
	public static boolean isValidIfNumber(final Object obj) {
		if (obj instanceof Number) {
			return NumberUtil.isValidNumber((Number) obj);
		}
		return true;
	}

	/**
	 * {@code null}安全的对象比较，{@code null}对象排在末尾
	 *
	 * @param <T> 被比较对象类型
	 * @param c1  对象1，可以为{@code null}
	 * @param c2  对象2，可以为{@code null}
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 * @see java.util.Comparator#compare(Object, Object)
	 * @since 3.0.7
	 */
	public static <T extends Comparable<? super T>> int compare(final T c1, final T c2) {
		return CompareUtil.compare(c1, c2);
	}

	/**
	 * {@code null}安全的对象比较
	 *
	 * @param <T>         被比较对象类型
	 * @param c1          对象1，可以为{@code null}
	 * @param c2          对象2，可以为{@code null}
	 * @param nullGreater 当被比较对象为null时是否排在前面
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 * @see java.util.Comparator#compare(Object, Object)
	 * @since 3.0.7
	 */
	public static <T extends Comparable<? super T>> int compare(final T c1, final T c2, final boolean nullGreater) {
		return CompareUtil.compare(c1, c2, nullGreater);
	}

	/**
	 * 获得给定类的第一个泛型参数
	 *
	 * @param obj 被检查的对象
	 * @return {@link Class}
	 * @since 3.0.8
	 */
	public static Class<?> getTypeArgument(final Object obj) {
		return getTypeArgument(obj, 0);
	}

	/**
	 * 获得给定类指定下标的泛型参数
	 *
	 * @param obj   被检查的对象
	 * @param index 泛型类型的索引号，即第几个泛型类型
	 * @return {@link Class}
	 * @since 3.0.8
	 */
	public static Class<?> getTypeArgument(final Object obj, final int index) {
		return ClassUtil.getTypeArgument(obj.getClass(), index);
	}

	/**
	 * <p>将对象转为字符串
	 * <ul>
	 *     <li>若对象为{@code null}，则返回“null”；</li>
	 *     <li>若对象为{@link Map}，则返回{@link Map#toString()}；</li>
	 *     <li>若对象为其他类型，则调用{@link Convert#toStr(Object)}进行转换；</li>
	 * </ul>
	 *
	 * @param obj Bean对象
	 * @return 转换后的字符串
	 * @since 3.2.0
	 * @see Convert#toStr(Object)
	 */
	public static String toString(final Object obj) {
		if (null == obj) {
			return StrUtil.NULL;
		}
		if (obj instanceof Map) {
			return obj.toString();
		}
		return Convert.toStr(obj);
	}
}
