package cn.hutool.core.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 断言<br>
 * 断言某些对象或值是否符合规定，否则抛出异常。经常用于做变量检查
 *
 * @author Looly
 */
public class Assert {

	/**
	 * 断言是否为真，如果为 {@code false} 抛出给定的异常<br>
	 *
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0, IllegalArgumentException::new);
	 * </pre>
	 *
	 * @param <X>        异常类型
	 * @param expression 布尔值
	 * @param supplier   指定断言不通过时抛出的异常
	 * @throws X if expression is {@code false}
	 */
	public static <X extends Throwable> void isTrue(boolean expression, Supplier<? extends X> supplier) throws X {
		if (false == expression) {
			throw supplier.get();
		}
	}

	/**
	 * 断言是否为真，如果为 {@code false} 抛出 {@code IllegalArgumentException} 异常<br>
	 *
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0, "The value must be greater than zero");
	 * </pre>
	 *
	 * @param expression       布尔值
	 * @param errorMsgTemplate 错误抛出异常附带的消息模板，变量用{}代替
	 * @param params           参数列表
	 * @throws IllegalArgumentException if expression is {@code false}
	 */
	public static void isTrue(boolean expression, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		isTrue(expression, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 断言是否为真，如果为 {@code false} 抛出 {@code IllegalArgumentException} 异常<br>
	 *
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0, "The value must be greater than zero");
	 * </pre>
	 *
	 * @param expression 布尔值
	 * @throws IllegalArgumentException if expression is {@code false}
	 */
	public static void isTrue(boolean expression) throws IllegalArgumentException {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	/**
	 * 断言是否为假，如果为 {@code true} 抛出指定类型异常<br>
	 * 并使用指定的函数获取错误信息返回
	 * <pre class="code">
	 *  Assert.isFalse(i &gt; 0, ()-&gt;{
	 *      // to query relation message
	 *      return new IllegalArgumentException("relation message to return");
	 *  });
	 * </pre>
	 *
	 * @param <X>           异常类型
	 * @param expression    布尔值
	 * @param errorSupplier 指定断言不通过时抛出的异常
	 * @throws X if expression is {@code false}
	 * @since 5.4.5
	 */
	public static <X extends Throwable> void isFalse(boolean expression, Supplier<X> errorSupplier) throws X {
		if (expression) {
			throw errorSupplier.get();
		}
	}

	/**
	 * 断言是否为假，如果为 {@code true} 抛出 {@code IllegalArgumentException} 异常<br>
	 *
	 * <pre class="code">
	 * Assert.isFalse(i &lt; 0, "The value must be greater than zero");
	 * </pre>
	 *
	 * @param expression       布尔值
	 * @param errorMsgTemplate 错误抛出异常附带的消息模板，变量用{}代替
	 * @param params           参数列表
	 * @throws IllegalArgumentException if expression is {@code false}
	 */
	public static void isFalse(boolean expression, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		isFalse(expression, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 断言是否为假，如果为 {@code true} 抛出 {@code IllegalArgumentException} 异常<br>
	 *
	 * <pre class="code">
	 * Assert.isFalse(i &lt; 0);
	 * </pre>
	 *
	 * @param expression 布尔值
	 * @throws IllegalArgumentException if expression is {@code false}
	 */
	public static void isFalse(boolean expression) throws IllegalArgumentException {
		isFalse(expression, "[Assertion failed] - this expression must be false");
	}

	/**
	 * 断言对象是否为{@code null} ，如果不为{@code null} 抛出指定类型异常
	 * 并使用指定的函数获取错误信息返回
	 * <pre class="code">
	 * Assert.isNull(value, ()-&gt;{
	 *      // to query relation message
	 *      return new IllegalArgumentException("relation message to return");
	 *  });
	 * </pre>
	 *
	 * @param <X>           异常类型
	 * @param object        被检查的对象
	 * @param errorSupplier 错误抛出异常附带的消息生产接口
	 * @throws X if the object is not {@code null}
	 * @since 5.4.5
	 */
	public static <X extends Throwable> void isNull(Object object, Supplier<X> errorSupplier) throws X {
		if (null != object) {
			throw errorSupplier.get();
		}
	}

	/**
	 * 断言对象是否为{@code null} ，如果不为{@code null} 抛出{@link IllegalArgumentException} 异常
	 *
	 * <pre class="code">
	 * Assert.isNull(value, "The value must be null");
	 * </pre>
	 *
	 * @param object           被检查的对象
	 * @param errorMsgTemplate 消息模板，变量使用{}表示
	 * @param params           参数列表
	 * @throws IllegalArgumentException if the object is not {@code null}
	 */
	public static void isNull(Object object, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		isNull(object, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 断言对象是否为{@code null} ，如果不为{@code null} 抛出{@link IllegalArgumentException} 异常
	 *
	 * <pre class="code">
	 * Assert.isNull(value);
	 * </pre>
	 *
	 * @param object 被检查对象
	 * @throws IllegalArgumentException if the object is not {@code null}
	 */
	public static void isNull(Object object) throws IllegalArgumentException {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	// ----------------------------------------------------------------------------------------------------------- Check not null

	/**
	 * 断言对象是否不为{@code null} ，如果为{@code null} 抛出指定类型异常
	 * 并使用指定的函数获取错误信息返回
	 * <pre class="code">
	 * Assert.notNull(clazz, ()-&gt;{
	 *      // to query relation message
	 *      return new IllegalArgumentException("relation message to return");
	 *  });
	 * </pre>
	 *
	 * @param <T>           被检查对象泛型类型
	 * @param <X>           异常类型
	 * @param object        被检查对象
	 * @param errorSupplier 错误抛出异常附带的消息生产接口
	 * @return 被检查后的对象
	 * @throws X if the object is {@code null}
	 * @since 5.4.5
	 */
	public static <T, X extends Throwable> T notNull(T object, Supplier<X> errorSupplier) throws X {
		if (null == object) {
			throw errorSupplier.get();
		}
		return object;
	}

	/**
	 * 断言对象是否不为{@code null} ，如果为{@code null} 抛出{@link IllegalArgumentException} 异常 Assert that an object is not {@code null} .
	 *
	 * <pre class="code">
	 * Assert.notNull(clazz, "The class must not be null");
	 * </pre>
	 *
	 * @param <T>              被检查对象泛型类型
	 * @param object           被检查对象
	 * @param errorMsgTemplate 错误消息模板，变量使用{}表示
	 * @param params           参数
	 * @return 被检查后的对象
	 * @throws IllegalArgumentException if the object is {@code null}
	 */
	public static <T> T notNull(T object, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		return notNull(object, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 断言对象是否不为{@code null} ，如果为{@code null} 抛出{@link IllegalArgumentException} 异常
	 *
	 * <pre class="code">
	 * Assert.notNull(clazz);
	 * </pre>
	 *
	 * @param <T>    被检查对象类型
	 * @param object 被检查对象
	 * @return 非空对象
	 * @throws IllegalArgumentException if the object is {@code null}
	 */
	public static <T> T notNull(T object) throws IllegalArgumentException {
		return notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	// ----------------------------------------------------------------------------------------------------------- Check empty

	/**
	 * 检查给定字符串是否为空，为空抛出自定义异常，并使用指定的函数获取错误信息返回。
	 * <pre class="code">
	 * Assert.notEmpty(name, ()-&gt;{
	 *      // to query relation message
	 *      return new IllegalArgumentException("relation message to return");
	 *  });
	 * </pre>
	 *
	 * @param <X>           异常类型
	 * @param <T>           字符串类型
	 * @param text          被检查字符串
	 * @param errorSupplier 错误抛出异常附带的消息生产接口
	 * @return 非空字符串
	 * @throws X 被检查字符串为空抛出此异常
	 * @see StrUtil#isNotEmpty(CharSequence)
	 * @since 5.4.5
	 */
	public static <T extends CharSequence, X extends Throwable> T notEmpty(T text, Supplier<X> errorSupplier) throws X {
		if (StrUtil.isEmpty(text)) {
			throw errorSupplier.get();
		}
		return text;
	}

	/**
	 * 检查给定字符串是否为空，为空抛出 {@link IllegalArgumentException}
	 *
	 * <pre class="code">
	 * Assert.notEmpty(name, "Name must not be empty");
	 * </pre>
	 *
	 * @param <T>              字符串类型
	 * @param text             被检查字符串
	 * @param errorMsgTemplate 错误消息模板，变量使用{}表示
	 * @param params           参数
	 * @return 非空字符串
	 * @throws IllegalArgumentException 被检查字符串为空
	 * @see StrUtil#isNotEmpty(CharSequence)
	 */
	public static <T extends CharSequence> T notEmpty(T text, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		return notEmpty(text, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 检查给定字符串是否为空，为空抛出 {@link IllegalArgumentException}
	 *
	 * <pre class="code">
	 * Assert.notEmpty(name);
	 * </pre>
	 *
	 * @param <T>  字符串类型
	 * @param text 被检查字符串
	 * @return 被检查的字符串
	 * @throws IllegalArgumentException 被检查字符串为空
	 * @see StrUtil#isNotEmpty(CharSequence)
	 */
	public static <T extends CharSequence> T notEmpty(T text) throws IllegalArgumentException {
		return notEmpty(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	/**
	 * 检查给定字符串是否为空白（null、空串或只包含空白符），为空抛出自定义异常。
	 * 并使用指定的函数获取错误信息返回
	 * <pre class="code">
	 * Assert.notBlank(name, ()-&gt;{
	 *      // to query relation message
	 *      return new IllegalArgumentException("relation message to return");
	 *  });
	 * </pre>
	 *
	 * @param <X>              异常类型
	 * @param <T>              字符串类型
	 * @param text             被检查字符串
	 * @param errorMsgSupplier 错误抛出异常附带的消息生产接口
	 * @return 非空字符串
	 * @throws X 被检查字符串为空白
	 * @see StrUtil#isNotBlank(CharSequence)
	 */
	public static <T extends CharSequence, X extends Throwable> T notBlank(T text, Supplier<X> errorMsgSupplier) throws X {
		if (StrUtil.isBlank(text)) {
			throw errorMsgSupplier.get();
		}
		return text;
	}

	/**
	 * 检查给定字符串是否为空白（null、空串或只包含空白符），为空抛出 {@link IllegalArgumentException}
	 *
	 * <pre class="code">
	 * Assert.notBlank(name, "Name must not be blank");
	 * </pre>
	 *
	 * @param <T>              字符串类型
	 * @param text             被检查字符串
	 * @param errorMsgTemplate 错误消息模板，变量使用{}表示
	 * @param params           参数
	 * @return 非空字符串
	 * @throws IllegalArgumentException 被检查字符串为空白
	 * @see StrUtil#isNotBlank(CharSequence)
	 */
	public static <T extends CharSequence> T notBlank(T text, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		return notBlank(text, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 检查给定字符串是否为空白（null、空串或只包含空白符），为空抛出 {@link IllegalArgumentException}
	 *
	 * <pre class="code">
	 * Assert.notBlank(name, "Name must not be blank");
	 * </pre>
	 *
	 * @param <T>  字符串类型
	 * @param text 被检查字符串
	 * @return 非空字符串
	 * @throws IllegalArgumentException 被检查字符串为空白
	 * @see StrUtil#isNotBlank(CharSequence)
	 */
	public static <T extends CharSequence> T notBlank(T text) throws IllegalArgumentException {
		return notBlank(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	/**
	 * 断言给定字符串是否不被另一个字符串包含（即是否为子串）
	 * 并使用指定的函数获取错误信息返回
	 * <pre class="code">
	 * Assert.notContain(name, "rod", ()-&gt;{
	 *      // to query relation message
	 *      return new IllegalArgumentException("relation message to return ");
	 *  });
	 * </pre>
	 *
	 * @param <T>           字符串类型
	 * @param <X>           异常类型
	 * @param textToSearch  被搜索的字符串
	 * @param substring     被检查的子串
	 * @param errorSupplier 错误抛出异常附带的消息生产接口
	 * @return 被检查的子串
	 * @throws X 非子串抛出异常
	 * @see StrUtil#contains(CharSequence, CharSequence)
	 * @since 5.4.5
	 */
	public static <T extends CharSequence, X extends Throwable> T notContain(CharSequence textToSearch, T substring, Supplier<X> errorSupplier) throws X {
		if (StrUtil.contains(textToSearch, substring)) {
			throw errorSupplier.get();
		}
		return substring;
	}

	/**
	 * 断言给定字符串是否不被另一个字符串包含（即是否为子串）
	 *
	 * <pre class="code">
	 * Assert.notContain(name, "rod", "Name must not contain 'rod'");
	 * </pre>
	 *
	 * @param textToSearch     被搜索的字符串
	 * @param substring        被检查的子串
	 * @param errorMsgTemplate 异常时的消息模板
	 * @param params           参数列表
	 * @return 被检查的子串
	 * @throws IllegalArgumentException 非子串抛出异常
	 */
	public static String notContain(String textToSearch, String substring, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		return notContain(textToSearch, substring, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 断言给定字符串是否不被另一个字符串包含（即是否为子串）
	 *
	 * <pre class="code">
	 * Assert.notContain(name, "rod", "Name must not contain 'rod'");
	 * </pre>
	 *
	 * @param textToSearch 被搜索的字符串
	 * @param substring    被检查的子串
	 * @return 被检查的子串
	 * @throws IllegalArgumentException 非子串抛出异常
	 */
	public static String notContain(String textToSearch, String substring) throws IllegalArgumentException {
		return notContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [{}]", substring);
	}

	/**
	 * 断言给定数组是否包含元素，数组必须不为 {@code null} 且至少包含一个元素
	 * 并使用指定的函数获取错误信息返回
	 *
	 * <pre class="code">
	 * Assert.notEmpty(array, ()-&gt;{
	 *      // to query relation message
	 *      return new IllegalArgumentException("relation message to return");
	 *  });
	 * </pre>
	 *
	 * @param <T>           数组元素类型
	 * @param <X>           异常类型
	 * @param array         被检查的数组
	 * @param errorSupplier 错误抛出异常附带的消息生产接口
	 * @return 被检查的数组
	 * @throws X if the object array is {@code null} or has no elements
	 * @see ArrayUtil#isNotEmpty(Object[])
	 * @since 5.4.5
	 */
	public static <T, X extends Throwable> T[] notEmpty(T[] array, Supplier<X> errorSupplier) throws X {
		if (ArrayUtil.isEmpty(array)) {
			throw errorSupplier.get();
		}
		return array;
	}

	/**
	 * 断言给定数组是否包含元素，数组必须不为 {@code null} 且至少包含一个元素
	 *
	 * <pre class="code">
	 * Assert.notEmpty(array, "The array must have elements");
	 * </pre>
	 *
	 * @param <T>              数组元素类型
	 * @param array            被检查的数组
	 * @param errorMsgTemplate 异常时的消息模板
	 * @param params           参数列表
	 * @return 被检查的数组
	 * @throws IllegalArgumentException if the object array is {@code null} or has no elements
	 */
	public static <T> T[] notEmpty(T[] array, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		return notEmpty(array, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 断言给定数组是否包含元素，数组必须不为 {@code null} 且至少包含一个元素
	 *
	 * <pre class="code">
	 * Assert.notEmpty(array, "The array must have elements");
	 * </pre>
	 *
	 * @param <T>   数组元素类型
	 * @param array 被检查的数组
	 * @return 被检查的数组
	 * @throws IllegalArgumentException if the object array is {@code null} or has no elements
	 */
	public static <T> T[] notEmpty(T[] array) throws IllegalArgumentException {
		return notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	/**
	 * 断言给定数组是否不包含{@code null}元素，如果数组为空或 {@code null}将被认为不包含
	 * 并使用指定的函数获取错误信息返回
	 * <pre class="code">
	 * Assert.noNullElements(array, ()-&gt;{
	 *      // to query relation message
	 *      return new IllegalArgumentException("relation message to return ");
	 *  });
	 * </pre>
	 *
	 * @param <T>           数组元素类型
	 * @param <X>           异常类型
	 * @param array         被检查的数组
	 * @param errorSupplier 错误抛出异常附带的消息生产接口
	 * @return 被检查的数组
	 * @throws X if the object array contains a {@code null} element
	 * @see ArrayUtil#hasNull(Object[])
	 * @since 5.4.5
	 */
	public static <T, X extends Throwable> T[] noNullElements(T[] array, Supplier<X> errorSupplier) throws X {
		if (ArrayUtil.hasNull(array)) {
			throw errorSupplier.get();
		}
		return array;
	}

	/**
	 * 断言给定数组是否不包含{@code null}元素，如果数组为空或 {@code null}将被认为不包含
	 *
	 * <pre class="code">
	 * Assert.noNullElements(array, "The array must have non-null elements");
	 * </pre>
	 *
	 * @param <T>              数组元素类型
	 * @param array            被检查的数组
	 * @param errorMsgTemplate 异常时的消息模板
	 * @param params           参数列表
	 * @return 被检查的数组
	 * @throws IllegalArgumentException if the object array contains a {@code null} element
	 */
	public static <T> T[] noNullElements(T[] array, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		return noNullElements(array, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 断言给定数组是否不包含{@code null}元素，如果数组为空或 {@code null}将被认为不包含
	 *
	 * <pre class="code">
	 * Assert.noNullElements(array);
	 * </pre>
	 *
	 * @param <T>   数组元素类型
	 * @param array 被检查的数组
	 * @return 被检查的数组
	 * @throws IllegalArgumentException if the object array contains a {@code null} element
	 */
	public static <T> T[] noNullElements(T[] array) throws IllegalArgumentException {
		return noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
	}

	/**
	 * 断言给定集合非空
	 * 并使用指定的函数获取错误信息返回
	 * <pre class="code">
	 * Assert.notEmpty(collection, ()-&gt;{
	 *      // to query relation message
	 *      return new IllegalArgumentException("relation message to return");
	 *  });
	 * </pre>
	 *
	 * @param <E>           集合元素类型
	 * @param <T>           集合类型
	 * @param <X>           异常类型
	 * @param collection    被检查的集合
	 * @param errorSupplier 错误抛出异常附带的消息生产接口
	 * @return 非空集合
	 * @throws X if the collection is {@code null} or has no elements
	 * @see CollUtil#isNotEmpty(Iterable)
	 * @since 5.4.5
	 */
	public static <E, T extends Iterable<E>, X extends Throwable> T notEmpty(T collection, Supplier<X> errorSupplier) throws X {
		if (CollUtil.isEmpty(collection)) {
			throw errorSupplier.get();
		}
		return collection;
	}

	/**
	 * 断言给定集合非空
	 *
	 * <pre class="code">
	 * Assert.notEmpty(collection, "Collection must have elements");
	 * </pre>
	 *
	 * @param <E>              集合元素类型
	 * @param <T>              集合类型
	 * @param collection       被检查的集合
	 * @param errorMsgTemplate 异常时的消息模板
	 * @param params           参数列表
	 * @return 非空集合
	 * @throws IllegalArgumentException if the collection is {@code null} or has no elements
	 */
	public static <E, T extends Iterable<E>> T notEmpty(T collection, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		return notEmpty(collection, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 断言给定集合非空
	 *
	 * <pre class="code">
	 * Assert.notEmpty(collection);
	 * </pre>
	 *
	 * @param <E>        集合元素类型
	 * @param <T>        集合类型
	 * @param collection 被检查的集合
	 * @return 被检查集合
	 * @throws IllegalArgumentException if the collection is {@code null} or has no elements
	 */
	public static <E, T extends Iterable<E>> T notEmpty(T collection) throws IllegalArgumentException {
		return notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	/**
	 * 断言给定Map非空
	 * 并使用指定的函数获取错误信息返回
	 * <pre class="code">
	 * Assert.notEmpty(map, ()-&gt;{
	 *      // to query relation message
	 *      return new IllegalArgumentException("relation message to return");
	 *  });
	 * </pre>
	 *
	 * @param <K>           Key类型
	 * @param <V>           Value类型
	 * @param <T>           Map类型
	 * @param <X>           异常类型
	 * @param map           被检查的Map
	 * @param errorSupplier 错误抛出异常附带的消息生产接口
	 * @return 被检查的Map
	 * @throws X if the map is {@code null} or has no entries
	 * @see MapUtil#isNotEmpty(Map)
	 * @since 5.4.5
	 */
	public static <K, V, T extends Map<K, V>, X extends Throwable> T notEmpty(T map, Supplier<X> errorSupplier) throws X {
		if (MapUtil.isEmpty(map)) {
			throw errorSupplier.get();
		}
		return map;
	}

	/**
	 * 断言给定Map非空
	 *
	 * <pre class="code">
	 * Assert.notEmpty(map, "Map must have entries");
	 * </pre>
	 *
	 * @param <K>              Key类型
	 * @param <V>              Value类型
	 * @param <T>              Map类型
	 * @param map              被检查的Map
	 * @param errorMsgTemplate 异常时的消息模板
	 * @param params           参数列表
	 * @return 被检查的Map
	 * @throws IllegalArgumentException if the map is {@code null} or has no entries
	 */
	public static <K, V, T extends Map<K, V>> T notEmpty(T map, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		return notEmpty(map, () -> new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params)));
	}

	/**
	 * 断言给定Map非空
	 *
	 * <pre class="code">
	 * Assert.notEmpty(map, "Map must have entries");
	 * </pre>
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param <T> Map类型
	 * @param map 被检查的Map
	 * @return 被检查的Map
	 * @throws IllegalArgumentException if the map is {@code null} or has no entries
	 */
	public static <K, V, T extends Map<K, V>> T notEmpty(T map) throws IllegalArgumentException {
		return notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}

	/**
	 * 断言给定对象是否是给定类的实例
	 *
	 * <pre class="code">
	 * Assert.instanceOf(Foo.class, foo);
	 * </pre>
	 *
	 * @param <T>  被检查对象泛型类型
	 * @param type 被检查对象匹配的类型
	 * @param obj  被检查对象
	 * @return 被检查的对象
	 * @throws IllegalArgumentException if the object is not an instance of clazz
	 * @see Class#isInstance(Object)
	 */
	public static <T> T isInstanceOf(Class<?> type, T obj) {
		return isInstanceOf(type, obj, "Object [{}] is not instanceof [{}]", obj, type);
	}

	/**
	 * 断言给定对象是否是给定类的实例
	 *
	 * <pre class="code">
	 * Assert.instanceOf(Foo.class, foo);
	 * </pre>
	 *
	 * @param <T>              被检查对象泛型类型
	 * @param type             被检查对象匹配的类型
	 * @param obj              被检查对象
	 * @param errorMsgTemplate 异常时的消息模板
	 * @param params           参数列表
	 * @return 被检查对象
	 * @throws IllegalArgumentException if the object is not an instance of clazz
	 * @see Class#isInstance(Object)
	 */
	public static <T> T isInstanceOf(Class<?> type, T obj, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		notNull(type, "Type to check against must not be null");
		if (false == type.isInstance(obj)) {
			throw new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
		}
		return obj;
	}

	/**
	 * 断言 {@code superType.isAssignableFrom(subType)} 是否为 {@code true}.
	 *
	 * <pre class="code">
	 * Assert.isAssignable(Number.class, myClass);
	 * </pre>
	 *
	 * @param superType 需要检查的父类或接口
	 * @param subType   需要检查的子类
	 * @throws IllegalArgumentException 如果子类非继承父类，抛出此异常
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType) throws IllegalArgumentException {
		isAssignable(superType, subType, "{} is not assignable to {})", subType, superType);
	}

	/**
	 * 断言 {@code superType.isAssignableFrom(subType)} 是否为 {@code true}.
	 *
	 * <pre class="code">
	 * Assert.isAssignable(Number.class, myClass);
	 * </pre>
	 *
	 * @param superType        需要检查的父类或接口
	 * @param subType          需要检查的子类
	 * @param errorMsgTemplate 异常时的消息模板
	 * @param params           参数列表
	 * @throws IllegalArgumentException 如果子类非继承父类，抛出此异常
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		notNull(superType, "Type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throw new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
		}
	}

	/**
	 * 检查boolean表达式，当检查结果为false时抛出 {@code IllegalStateException}。
	 * 并使用指定的函数获取错误信息返回
	 * <pre class="code">
	 * Assert.state(id == null, ()-&gt;{
	 *      // to query relation message
	 *      return "relation message to return ";
	 *  });
	 * </pre>
	 *
	 * @param expression       boolean 表达式
	 * @param errorMsgSupplier 错误抛出异常附带的消息生产接口
	 * @throws IllegalStateException 表达式为 {@code false} 抛出此异常
	 */
	public static void state(boolean expression, Supplier<String> errorMsgSupplier) throws IllegalStateException {
		if (false == expression) {
			throw new IllegalStateException(errorMsgSupplier.get());
		}
	}

	/**
	 * 检查boolean表达式，当检查结果为false时抛出 {@code IllegalStateException}。
	 *
	 * <pre class="code">
	 * Assert.state(id == null, "The id property must not already be initialized");
	 * </pre>
	 *
	 * @param expression       boolean 表达式
	 * @param errorMsgTemplate 异常时的消息模板
	 * @param params           参数列表
	 * @throws IllegalStateException 表达式为 {@code false} 抛出此异常
	 */
	public static void state(boolean expression, String errorMsgTemplate, Object... params) throws IllegalStateException {
		if (false == expression) {
			throw new IllegalStateException(StrUtil.format(errorMsgTemplate, params));
		}
	}

	/**
	 * 检查boolean表达式，当检查结果为false时抛出 {@code IllegalStateException}。
	 *
	 * <pre class="code">
	 * Assert.state(id == null);
	 * </pre>
	 *
	 * @param expression boolean 表达式
	 * @throws IllegalStateException 表达式为 {@code false} 抛出此异常
	 */
	public static void state(boolean expression) throws IllegalStateException {
		state(expression, "[Assertion failed] - this state invariant must be true");
	}

	/**
	 * 检查下标（数组、集合、字符串）是否符合要求，下标必须满足：
	 *
	 * <pre>
	 * 0 &le; index &lt; size
	 * </pre>
	 *
	 * @param index 下标
	 * @param size  长度
	 * @return 检查后的下标
	 * @throws IllegalArgumentException  如果size &lt; 0 抛出此异常
	 * @throws IndexOutOfBoundsException 如果index &lt; 0或者 index &ge; size 抛出此异常
	 * @since 4.1.9
	 */
	public static int checkIndex(int index, int size) throws IllegalArgumentException, IndexOutOfBoundsException {
		return checkIndex(index, size, "[Assertion failed]");
	}

	/**
	 * 检查下标（数组、集合、字符串）是否符合要求，下标必须满足：
	 *
	 * <pre>
	 * 0 &le; index &lt; size
	 * </pre>
	 *
	 * @param index            下标
	 * @param size             长度
	 * @param errorMsgTemplate 异常时的消息模板
	 * @param params           参数列表
	 * @return 检查后的下标
	 * @throws IllegalArgumentException  如果size &lt; 0 抛出此异常
	 * @throws IndexOutOfBoundsException 如果index &lt; 0或者 index &ge; size 抛出此异常
	 * @since 4.1.9
	 */
	public static int checkIndex(int index, int size, String errorMsgTemplate, Object... params) throws IllegalArgumentException, IndexOutOfBoundsException {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException(badIndexMsg(index, size, errorMsgTemplate, params));
		}
		return index;
	}

	/**
	 * 检查值是否在指定范围内
	 *
	 * @param value 值
	 * @param min   最小值（包含）
	 * @param max   最大值（包含）
	 * @return 检查后的长度值
	 * @since 4.1.10
	 */
	public static int checkBetween(int value, int min, int max) {
		if (value < min || value > max) {
			throw new IllegalArgumentException(StrUtil.format("Length must be between {} and {}.", min, max));
		}
		return value;
	}

	/**
	 * 检查值是否在指定范围内
	 *
	 * @param value 值
	 * @param min   最小值（包含）
	 * @param max   最大值（包含）
	 * @return 检查后的长度值
	 * @since 4.1.10
	 */
	public static long checkBetween(long value, long min, long max) {
		if (value < min || value > max) {
			throw new IllegalArgumentException(StrUtil.format("Length must be between {} and {}.", min, max));
		}
		return value;
	}

	/**
	 * 检查值是否在指定范围内
	 *
	 * @param value 值
	 * @param min   最小值（包含）
	 * @param max   最大值（包含）
	 * @return 检查后的长度值
	 * @since 4.1.10
	 */
	public static double checkBetween(double value, double min, double max) {
		if (value < min || value > max) {
			throw new IllegalArgumentException(StrUtil.format("Length must be between {} and {}.", min, max));
		}
		return value;
	}

	/**
	 * 检查值是否在指定范围内
	 *
	 * @param value 值
	 * @param min   最小值（包含）
	 * @param max   最大值（包含）
	 * @return 检查后的长度值
	 * @since 4.1.10
	 */
	public static Number checkBetween(Number value, Number min, Number max) {
		notNull(value);
		notNull(min);
		notNull(max);
		double valueDouble = value.doubleValue();
		double minDouble = min.doubleValue();
		double maxDouble = max.doubleValue();
		if (valueDouble < minDouble || valueDouble > maxDouble) {
			throw new IllegalArgumentException(StrUtil.format("Length must be between {} and {}.", min, max));
		}
		return value;
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 错误的下标时显示的消息
	 *
	 * @param index  下标
	 * @param size   长度
	 * @param desc   异常时的消息模板
	 * @param params 参数列表
	 * @return 消息
	 */
	private static String badIndexMsg(int index, int size, String desc, Object... params) {
		if (index < 0) {
			return StrUtil.format("{} ({}) must not be negative", StrUtil.format(desc, params), index);
		} else if (size < 0) {
			throw new IllegalArgumentException("negative size: " + size);
		} else { // index >= size
			return StrUtil.format("{} ({}) must be less than size ({})", StrUtil.format(desc, params), index, size);
		}
	}
	// -------------------------------------------------------------------------------------------------------------------------------------------- Private method end
}
