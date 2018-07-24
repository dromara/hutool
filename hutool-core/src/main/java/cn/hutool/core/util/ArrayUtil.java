package cn.hutool.core.util;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;

/**
 * 数组工具类
 * 
 * @author Looly
 *
 */
public class ArrayUtil {

	/** 数组中元素未找到的下标，值为-1 */
	public static final int INDEX_NOT_FOUND = -1;

	// ---------------------------------------------------------------------- isEmpty
	/**
	 * 数组是否为空
	 * 
	 * @param <T> 数组元素类型
	 * @param array 数组
	 * @return 是否为空
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean isEmpty(final T... array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空<br>
	 * 此方法会匹配单一对象，如果此对象为{@code null}则返回true<br>
	 * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回false<br>
	 * 如果此对象为数组对象，数组长度大于0情况下返回false，否则返回true
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object array) {
		if (null == array) {
			return true;
		} else if (isArray(array)) {
			return 0 == Array.getLength(array);
		}
		throw new UtilException("Object to provide is not a Array !");
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final long... array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final int... array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final short... array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final char... array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final byte... array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final double... array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final float... array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final boolean... array) {
		return array == null || array.length == 0;
	}

	// ---------------------------------------------------------------------- isNotEmpty
	/**
	 * 数组是否为非空
	 * 
	 * @param <T> 数组元素类型
	 * @param array 数组
	 * @return 是否为非空
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean isNotEmpty(final T... array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空<br>
	 * 此方法会匹配单一对象，如果此对象为{@code null}则返回false<br>
	 * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回true<br>
	 * 如果此对象为数组对象，数组长度大于0情况下返回true，否则返回false
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final Object array) {
		return false == isEmpty((Object) array);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final long... array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final int... array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final short... array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final char... array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final byte... array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final double... array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final float... array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空
	 * 
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final boolean... array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 是否包含{@code null}元素
	 * 
	 * @param <T> 数组元素类型
	 * @param array 被检查的数组
	 * @return 是否包含{@code null}元素
	 * @since 3.0.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean hasNull(T... array) {
		if (isNotEmpty(array)) {
			for (T element : array) {
				if (null == element) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 返回数组中第一个非空元素
	 * 
	 * @param <T> 数组元素类型
	 * @param array 数组
	 * @return 非空元素，如果不存在非空元素或数组为空，返回{@code null}
	 * @since 3.0.7
	 */
	@SuppressWarnings("unchecked")
	public static <T> T firstNonNull(T... array) {
		if (isNotEmpty(array)) {
			for (final T val : array) {
				if (null != val) {
					return val;
				}
			}
		}
		return null;
	}

	/**
	 * 新建一个空数组
	 * 
	 * @param <T> 数组元素类型
	 * @param componentType 元素类型
	 * @param newSize 大小
	 * @return 空数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<?> componentType, int newSize) {
		return (T[]) Array.newInstance(componentType, newSize);
	}

	/**
	 * 新建一个空数组
	 * 
	 * @param <T> 数组元素类型
	 * @param newSize 大小
	 * @return 空数组
	 * @since 3.3.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(int newSize) {
		return (T[]) new Object[newSize];
	}

	/**
	 * 获取数组对象的元素类型
	 * 
	 * @param array 数组对象
	 * @return 元素类型
	 * @since 3.2.2
	 */
	public static Class<?> getComponentType(Object array) {
		return null == array ? null : array.getClass().getComponentType();
	}

	/**
	 * 获取数组对象的元素类型
	 * 
	 * @param arrayClass 数组类
	 * @return 元素类型
	 * @since 3.2.2
	 */
	public static Class<?> getComponentType(Class<?> arrayClass) {
		return null == arrayClass ? null : arrayClass.getComponentType();
	}

	/**
	 * 根据数组元素类型，获取数组的类型<br>
	 * 方法是通过创建一个空数组从而获取其类型
	 * 
	 * @param componentType 数组元素类型
	 * @return 数组类型
	 * @since 3.2.2
	 */
	public static Class<?> getArrayType(Class<?> componentType) {
		return newArray(componentType, 0).getClass();
	}

	/**
	 * 强转数组类型<br>
	 * 强制转换的前提是数组元素类型可被强制转换<br>
	 * 强制转换后会生成一个新数组
	 * 
	 * @param type 数组类型或数组元素类型
	 * @param arrayObj 原数组
	 * @return 转换后的数组类型
	 * @throws NullPointerException 提供参数为空
	 * @throws IllegalArgumentException 参数arrayObj不是数组
	 * @since 3.0.6
	 */
	public static Object[] cast(Class<?> type, Object arrayObj) throws NullPointerException, IllegalArgumentException {
		if (null == arrayObj) {
			throw new NullPointerException("Argument [arrayObj] is null !");
		}
		if (false == arrayObj.getClass().isArray()) {
			throw new IllegalArgumentException("Argument [arrayObj] is not array !");
		}
		if (null == type) {
			return (Object[]) arrayObj;
		}

		final Class<?> componentType = type.isArray() ? type.getComponentType() : type;
		final Object[] array = (Object[]) arrayObj;
		final Object[] result = ArrayUtil.newArray(componentType, array.length);
		System.arraycopy(array, 0, result, 0, array.length);
		return result;
	}
	
	/**
	 * 将新元素添加到已有数组中<br>
	 * 添加新元素会生成一个新的数组，不影响原数组
	 * 
	 * @param <T> 数组元素类型
	 * @param buffer 已有数组
	 * @param newElements 新元素
	 * @return 新数组
	 */
	@SafeVarargs
	public static <T> T[] append(T[] buffer, T... newElements) {
		if(isEmpty(buffer)) {
			return newElements;
		}
		return insert(buffer, buffer.length, newElements);
	}
	
	/**
	 * 将新元素添加到已有数组中<br>
	 * 添加新元素会生成一个新的数组，不影响原数组
	 * 
	 * @param <T> 数组元素类型
	 * @param array 已有数组
	 * @param newElements 新元素
	 * @return 新数组
	 */
	@SafeVarargs
	public static <T> Object append(Object array, T... newElements) {
		if(isEmpty(array)) {
			return newElements;
		}
		return insert(array, length(array), newElements);
	}
	
	/**
	 * 将元素值设置为数组的某个位置，当给定的index大于数组长度，则追加
	 * 
	 * @param <T> 数组元素类型
	 * @param buffer 已有数组
	 * @param index 位置，大于长度追加，否则替换
	 * @param value 新值
	 * @return 新数组或原有数组
	 * @since 4.1.2
	 */
	public static <T> T[] setOrAppend(T[] buffer, int index, T value) {
		if(index < buffer.length) {
			Array.set(buffer, index, value);
			return buffer;
		}else {
			return append(buffer, value);
		}
	}
	
	/**
	 * 将元素值设置为数组的某个位置，当给定的index大于数组长度，则追加
	 * 
	 * @param array 已有数组
	 * @param index 位置，大于长度追加，否则替换
	 * @param value 新值
	 * @return 新数组或原有数组
	 * @since 4.1.2
	 */
	public static Object setOrAppend(Object array, int index, Object value) {
		if(index < length(array)) {
			Array.set(array, index, value);
			return array;
		}else {
			return append(array, value);
		}
	}

	/**
	 * 将新元素插入到到已有数组中的某个位置<br>
	 * 添加新元素会生成一个新的数组，不影响原数组<br>
	 * 如果插入位置为为负数，从原数组从后向前计数，若大于原数组长度，则空白处用null填充
	 * 
	 * @param <T> 数组元素类型
	 * @param buffer 已有数组
	 * @param index 插入位置，此位置为对应此位置元素之前的空档
	 * @param newElements 新元素
	 * @return 新数组
	 * @since 4.0.8
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] insert(T[] buffer, int index, T... newElements) {
		return (T[]) insert((Object)buffer, index, newElements);
	}
	
	/**
	 * 将新元素插入到到已有数组中的某个位置<br>
	 * 添加新元素会生成一个新的数组，不影响原数组<br>
	 * 如果插入位置为为负数，从原数组从后向前计数，若大于原数组长度，则空白处用null填充
	 * 
	 * @param <T> 数组元素类型
	 * @param array 已有数组
	 * @param index 插入位置，此位置为对应此位置元素之前的空档
	 * @param newElements 新元素
	 * @return 新数组
	 * @since 4.0.8
	 */
	@SuppressWarnings("unchecked")
	public static <T> Object insert(Object array, int index, T... newElements) {
		if (isEmpty(newElements)) {
			return array;
		}
		if(isEmpty(array)) {
			return newElements;
		}
		
		final int len = length(array);
		if (index < 0) {
			index = (index % len) + len;
		}
		
		final T[] result = newArray(array.getClass().getComponentType(), Math.max(len, index) + newElements.length);
		System.arraycopy(array, 0, result, 0, Math.min(len, index));
		System.arraycopy(newElements, 0, result, index, newElements.length);
		if (index < len) {
			System.arraycopy(array, index, result, index + newElements.length, len - index);
		}
		return result;
	}

	/**
	 * 生成一个新的重新设置大小的数组<br>
	 * 调整大小后拷贝原数组到新数组下。扩大则占位前N个位置，缩小则截断
	 * 
	 * @param <T> 数组元素类型
	 * @param buffer 原数组
	 * @param newSize 新的数组大小
	 * @param componentType 数组元素类型
	 * @return 调整后的新数组
	 */
	public static <T> T[] resize(T[] buffer, int newSize, Class<?> componentType) {
		T[] newArray = newArray(componentType, newSize);
		if (isNotEmpty(buffer)) {
			System.arraycopy(buffer, 0, newArray, 0, Math.min(buffer.length, newSize));
		}
		return newArray;
	}

	/**
	 * 生成一个新的重新设置大小的数组<br>
	 * 新数组的类型为原数组的类型，调整大小后拷贝原数组到新数组下。扩大则占位前N个位置，缩小则截断
	 * 
	 * @param <T> 数组元素类型
	 * @param buffer 原数组
	 * @param newSize 新的数组大小
	 * @return 调整后的新数组
	 */
	public static <T> T[] resize(T[] buffer, int newSize) {
		return resize(buffer, newSize, buffer.getClass().getComponentType());
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 * 
	 * @param <T> 数组元素类型
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 */
	@SafeVarargs
	public static <T> T[] addAll(T[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		int length = 0;
		for (T[] array : arrays) {
			if (array == null) {
				continue;
			}
			length += array.length;
		}
		T[] result = newArray(arrays.getClass().getComponentType().getComponentType(), length);

		length = 0;
		for (T[] array : arrays) {
			if (array == null) {
				continue;
			}
			System.arraycopy(array, 0, result, length, array.length);
			length += array.length;
		}
		return result;
	}

	/**
	 * 包装 {@link System#arraycopy(Object, int, Object, int, int)}<br>
	 * 数组复制
	 * 
	 * @param src 源数组
	 * @param srcPos 源数组开始位置
	 * @param dest 目标数组
	 * @param destPos 目标数组开始位置
	 * @param length 拷贝数组长度
	 * @return 目标数组
	 * @since 3.0.6
	 */
	public static Object copy(Object src, int srcPos, Object dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
		return dest;
	}

	/**
	 * 包装 {@link System#arraycopy(Object, int, Object, int, int)}<br>
	 * 数组复制，缘数组和目标数组都是从位置0开始复制
	 * 
	 * @param src 源数组
	 * @param dest 目标数组
	 * @param length 拷贝数组长度
	 * @return 目标数组
	 * @since 3.0.6
	 */
	public static Object copy(Object src, Object dest, int length) {
		System.arraycopy(src, 0, dest, 0, length);
		return dest;
	}

	/**
	 * 克隆数组
	 * 
	 * @param <T> 数组元素类型
	 * @param array 被克隆的数组
	 * @return 新数组
	 */
	public static <T> T[] clone(T[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * 克隆数组，如果非数组返回<code>null</code>
	 * 
	 * @param <T> 数组元素类型
	 * @param obj 数组对象
	 * @return 克隆后的数组对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(final T obj) {
		if (null == obj) {
			return null;
		}
		if (isArray(obj)) {
			final Object result;
			final Class<?> componentType = obj.getClass().getComponentType();
			if (componentType.isPrimitive()) {// 原始类型
				int length = Array.getLength(obj);
				result = Array.newInstance(componentType, length);
				while (length-- > 0) {
					Array.set(result, length, Array.get(obj, length));
				}
			} else {
				result = ((Object[]) obj).clone();
			}
			return (T) result;
		}
		return null;
	}

	/**
	 * 生成一个从0开始的数字列表<br>
	 * 
	 * @param excludedEnd 结束的数字（不包含）
	 * @return 数字列表
	 */
	public static int[] range(int excludedEnd) {
		return range(0, excludedEnd, 1);
	}

	/**
	 * 生成一个数字列表<br>
	 * 自动判定正序反序
	 * 
	 * @param includedStart 开始的数字（包含）
	 * @param excludedEnd 结束的数字（不包含）
	 * @return 数字列表
	 */
	public static int[] range(int includedStart, int excludedEnd) {
		return range(includedStart, excludedEnd, 1);
	}

	/**
	 * 生成一个数字列表<br>
	 * 自动判定正序反序
	 * 
	 * @param includedStart 开始的数字（包含）
	 * @param excludedEnd 结束的数字（不包含）
	 * @param step 步进
	 * @return 数字列表
	 */
	public static int[] range(int includedStart, int excludedEnd, int step) {
		if (includedStart > excludedEnd) {
			int tmp = includedStart;
			includedStart = excludedEnd;
			excludedEnd = tmp;
		}

		if (step <= 0) {
			step = 1;
		}

		int deviation = excludedEnd - includedStart;
		int length = deviation / step;
		if (deviation % step != 0) {
			length += 1;
		}
		int[] range = new int[length];
		for (int i = 0; i < length; i++) {
			range[i] = includedStart;
			includedStart += step;
		}
		return range;
	}

	/**
	 * 拆分byte数组为几个等份（最后一份可能小于len）
	 * 
	 * @param array 数组
	 * @param len 每个小节的长度
	 * @return 拆分后的数组
	 */
	public static byte[][] split(byte[] array, int len) {
		int x = array.length / len;
		int y = array.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(array, i * len, arr, 0, y);
			} else {
				System.arraycopy(array, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}

	/**
	 * 过滤<br>
	 * 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
	 * 
	 * <pre>
	 * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
	 * 2、修改元素对象，返回集合中为修改后的对象
	 * </pre>
	 * 
	 * @param <T> 数组元素类型
	 * @param array 数组
	 * @param editor 编辑器接口
	 * @return 过滤后的数组
	 */
	public static <T> T[] filter(T[] array, Editor<T> editor) {
		ArrayList<T> list = new ArrayList<T>(array.length);
		T modified;
		for (T t : array) {
			modified = editor.edit(t);
			if (null != modified) {
				list.add(modified);
			}
		}
		return list.toArray(Arrays.copyOf(array, list.size()));
	}

	/**
	 * 过滤<br>
	 * 过滤过程通过传入的Filter实现来过滤返回需要的元素内容，这个Editor实现可以实现以下功能：
	 * 
	 * <pre>
	 * 1、过滤出需要的对象，{@link Filter#accept(Object)}方法返回true的对象将被加入结果集合中
	 * </pre>
	 * 
	 * @param <T> 数组元素类型
	 * @param array 数组
	 * @param filter 过滤器接口，用于定义过滤规则
	 * @return 过滤后的数组
	 * @since 3.2.1
	 */
	public static <T> T[] filter(T[] array, Filter<T> filter) {
		ArrayList<T> list = new ArrayList<T>(array.length);
		boolean isAccept;
		for (T t : array) {
			isAccept = filter.accept(t);
			if (isAccept) {
				list.add(t);
			}
		}
		return list.toArray(Arrays.copyOf(array, list.size()));
	}

	/**
	 * 去除{@code null} 元素
	 * 
	 * @param array 数组
	 * @return 处理后的数组
	 * @since 3.2.2
	 */
	public static <T> T[] removeNull(T[] array) {
		return filter(array, new Editor<T>() {
			@Override
			public T edit(T t) {
				// 返回null便不加入集合
				return t;
			}
		});
	}

	/**
	 * 去除{@code null}或者"" 元素
	 * 
	 * @param array 数组
	 * @return 处理后的数组
	 * @since 3.2.2
	 */
	public static <T extends CharSequence> T[] removeEmpty(T[] array) {
		return filter(array, new Filter<T>() {
			@Override
			public boolean accept(T t) {
				return false == StrUtil.isEmpty(t);
			}
		});
	}

	/**
	 * 去除{@code null}或者""或者空白字符串 元素
	 * 
	 * @param array 数组
	 * @return 处理后的数组
	 * @since 3.2.2
	 */
	public static <T extends CharSequence> T[] removeBlank(T[] array) {
		return filter(array, new Filter<T>() {
			@Override
			public boolean accept(T t) {
				return false == StrUtil.isBlank(t);
			}
		});
	}

	/**
	 * 数组元素中的null转换为""
	 * 
	 * @param array 数组
	 * @return 新数组
	 * @since 3.2.1
	 */
	public static String[] nullToEmpty(String[] array) {
		return filter(array, new Editor<String>() {
			@Override
			public String edit(String t) {
				return null == t ? StrUtil.EMPTY : t;
			}
		});
	}

	/**
	 * 映射键值（参考Python的zip()函数）<br>
	 * 例如：<br>
	 * keys = [a,b,c,d]<br>
	 * values = [1,2,3,4]<br>
	 * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
	 * 如果两个数组长度不同，则只对应最短部分
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param keys 键列表
	 * @param values 值列表
	 * @param isOrder 是否有序
	 * @return Map
	 * @since 3.0.4
	 */
	public static <K, V> Map<K, V> zip(K[] keys, V[] values, boolean isOrder) {
		if (isEmpty(keys) || isEmpty(values)) {
			return null;
		}

		final int size = Math.min(keys.length, values.length);
		final Map<K, V> map = CollectionUtil.newHashMap(size, isOrder);
		for (int i = 0; i < size; i++) {
			map.put(keys[i], values[i]);
		}

		return map;
	}

	/**
	 * 映射键值（参考Python的zip()函数），返回Map无序<br>
	 * 例如：<br>
	 * keys = [a,b,c,d]<br>
	 * values = [1,2,3,4]<br>
	 * 则得到的Map是 {a=1, b=2, c=3, d=4}<br>
	 * 如果两个数组长度不同，则只对应最短部分
	 * 
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param keys 键列表
	 * @param values 值列表
	 * @return Map
	 */
	public static <K, V> Map<K, V> zip(K[] keys, V[] values) {
		return zip(keys, values, false);
	}

	// ------------------------------------------------------------------- indexOf and lastIndexOf and contains
	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param <T> 数组类型
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static <T> int indexOf(T[] array, Object value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (ObjectUtil.equal(value, array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，忽略大小写，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.1.2
	 */
	public static int indexOfIgnoreCase(CharSequence[] array, CharSequence value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (StrUtil.equalsIgnoreCase(array[i], value)) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param <T> 数组类型
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static <T> int lastIndexOf(T[] array, Object value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (ObjectUtil.equal(value, array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 * 
	 * @param <T> 数组元素类型
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 */
	public static <T> boolean contains(T[] array, T value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素，忽略大小写
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.1.2
	 */
	public static boolean containsIgnoreCase(CharSequence[] array, CharSequence value) {
		return indexOfIgnoreCase(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(long[] array, long value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(long[] array, long value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(long[] array, long value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(int[] array, int value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(int[] array, int value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(int[] array, int value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(short[] array, short value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(short[] array, short value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(short[] array, short value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(char[] array, char value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(char[] array, char value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(char[] array, char value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(byte[] array, byte value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(byte[] array, byte value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(byte[] array, byte value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(double[] array, double value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(double[] array, double value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(double[] array, double value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(float[] array, float value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(float[] array, float value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(float[] array, float value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(boolean[] array, boolean value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int lastIndexOf(boolean[] array, boolean value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 * 
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(boolean[] array, boolean value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	// ------------------------------------------------------------------- Wrap and unwrap
	/**
	 * 将原始类型数组包装为包装类型
	 * 
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Integer[] wrap(int... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Integer[0];
		}

		final Integer[] array = new Integer[length];
		for (int i = 0; i < length; i++) {
			array[i] = Integer.valueOf(values[i]);
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 * 
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static int[] unWrap(Integer... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new int[0];
		}

		final int[] array = new int[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i].intValue();
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 * 
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Long[] wrap(long... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Long[0];
		}

		final Long[] array = new Long[length];
		for (int i = 0; i < length; i++) {
			array[i] = Long.valueOf(values[i]);
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 * 
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static long[] unWrap(Long... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new long[0];
		}

		final long[] array = new long[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i].longValue();
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 * 
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Character[] wrap(char... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Character[0];
		}

		final Character[] array = new Character[length];
		for (int i = 0; i < length; i++) {
			array[i] = Character.valueOf(values[i]);
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 * 
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static char[] unWrap(Character... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new char[0];
		}

		char[] array = new char[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i].charValue();
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 * 
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Byte[] wrap(byte... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Byte[0];
		}

		final Byte[] array = new Byte[length];
		for (int i = 0; i < length; i++) {
			array[i] = Byte.valueOf(values[i]);
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 * 
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static byte[] unWrap(Byte... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new byte[0];
		}

		final byte[] array = new byte[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i].byteValue();
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 * 
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Short[] wrap(short... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Short[0];
		}

		final Short[] array = new Short[length];
		for (int i = 0; i < length; i++) {
			array[i] = Short.valueOf(values[i]);
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 * 
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static short[] unWrap(Short... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new short[0];
		}

		final short[] array = new short[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i].shortValue();
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 * 
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Float[] wrap(float... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Float[0];
		}

		final Float[] array = new Float[length];
		for (int i = 0; i < length; i++) {
			array[i] = Float.valueOf(values[i]);
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 * 
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static float[] unWrap(Float... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new float[0];
		}

		final float[] array = new float[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i].floatValue();
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 * 
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Double[] wrap(double... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Double[0];
		}

		final Double[] array = new Double[length];
		for (int i = 0; i < length; i++) {
			array[i] = Double.valueOf(values[i]);
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 * 
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static double[] unWrap(Double... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new double[0];
		}

		final double[] array = new double[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i].doubleValue();
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 * 
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Boolean[] wrap(boolean... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Boolean[0];
		}

		final Boolean[] array = new Boolean[length];
		for (int i = 0; i < length; i++) {
			array[i] = Boolean.valueOf(values[i]);
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 * 
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static boolean[] unWrap(Boolean... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new boolean[0];
		}

		final boolean[] array = new boolean[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i].booleanValue();
		}
		return array;
	}

	/**
	 * 包装数组对象
	 * 
	 * @param obj 对象，可以是对象数组或者基本类型数组
	 * @return 包装类型数组或对象数组
	 * @throws UtilException 对象为非数组
	 */
	public static Object[] wrap(Object obj) {
		if (null == obj) {
			return null;
		}
		if (isArray(obj)) {
			try {
				return (Object[]) obj;
			} catch (Exception e) {
				final String className = obj.getClass().getComponentType().getName();
				switch (className) {
				case "long":
					return wrap((long[]) obj);
				case "int":
					return wrap((int[]) obj);
				case "short":
					return wrap((short[]) obj);
				case "char":
					return wrap((char[]) obj);
				case "byte":
					return wrap((byte[]) obj);
				case "boolean":
					return wrap((boolean[]) obj);
				case "float":
					return wrap((float[]) obj);
				case "double":
					return wrap((double[]) obj);
				default:
					throw new UtilException(e);
				}
			}
		}
		throw new UtilException(StrUtil.format("[{}] is not Array!", obj.getClass()));
	}

	/**
	 * 对象是否为数组对象
	 * 
	 * @param obj 对象
	 * @return 是否为数组对象，如果为{@code null} 返回false
	 */
	public static boolean isArray(Object obj) {
		if (null == obj) {
			// throw new NullPointerException("Object check for isArray is null");
			return false;
		}
		return obj.getClass().isArray();
	}

	/**
	 * 获取数组对象中指定index的值，支持负数，例如-1表示倒数第一个值
	 * 
	 * @param <T> 数组元素类型
	 * @param array 数组对象
	 * @param index 下标，支持负数
	 * @return 值
	 * @since 4.0.6
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Object array, int index) {
		if (index < 0) {
			index += Array.getLength(array);
		}
		return (T) Array.get(array, index);
	}

	/**
	 * 获取数组中指定多个下标元素值，组成新数组
	 * 
	 * @param <T> 数组元素类型
	 * @param array 数组
	 * @param indexes 下标列表
	 * @return 结果
	 */
	public static <T> T[] getAny(Object array, int... indexes) {
		final T[] result = newArray(indexes.length);
		for (int i : indexes) {
			result[i] = get(array, i);
		}
		return result;
	}

	/**
	 * 获取子数组
	 * 
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end 结束位置（不包括）
	 * @return 新的数组
	 * @since 4.0.6
	 */
	public static Object[] sub(Object array, int start, int end) {
		return sub(array, start, end, 1);
	}

	/**
	 * 获取子数组
	 * 
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end 结束位置（不包括）
	 * @param step 步进
	 * @return 新的数组
	 * @since 4.0.6
	 */
	public static Object[] sub(Object array, int start, int end, int step) {
		int length = length(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return new Object[0];
		}
		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return new Object[0];
			}
			end = length;
		}

		if (step <= 1) {
			step = 1;
		}

		final ArrayList<Object> list = new ArrayList<>();
		for (int i = start; i < end; i += step) {
			list.add(get(array, i));
		}

		return list.toArray();
	}

	/**
	 * 数组或集合转String
	 * 
	 * @param obj 集合或数组对象
	 * @return 数组字符串，与集合转字符串格式相同
	 */
	public static String toString(Object obj) {
		if (null == obj) {
			return null;
		}
		if (ArrayUtil.isArray(obj)) {
			try {
				return Arrays.deepToString((Object[]) obj);
			} catch (Exception e) {
				final String className = obj.getClass().getComponentType().getName();
				switch (className) {
				case "long":
					return Arrays.toString((long[]) obj);
				case "int":
					return Arrays.toString((int[]) obj);
				case "short":
					return Arrays.toString((short[]) obj);
				case "char":
					return Arrays.toString((char[]) obj);
				case "byte":
					return Arrays.toString((byte[]) obj);
				case "boolean":
					return Arrays.toString((boolean[]) obj);
				case "float":
					return Arrays.toString((float[]) obj);
				case "double":
					return Arrays.toString((double[]) obj);
				default:
					throw new UtilException(e);
				}
			}
		}
		return obj.toString();
	}

	/**
	 * 获取数组长度<br>
	 * 如果参数为{@code null}，返回0
	 * 
	 * <pre>
	 * ArrayUtil.length(null)            = 0
	 * ArrayUtil.length([])              = 0
	 * ArrayUtil.length([null])          = 1
	 * ArrayUtil.length([true, false])   = 2
	 * ArrayUtil.length([1, 2, 3])       = 3
	 * ArrayUtil.length(["a", "b", "c"]) = 3
	 * </pre>
	 * 
	 * @param array 数组对象
	 * @return 数组长度
	 * @throws IllegalArgumentException 如果参数不为数组，抛出此异常
	 * @since 3.0.8
	 * @see Array#getLength(Object)
	 */
	public static int length(Object array) throws IllegalArgumentException {
		if (null == array) {
			return 0;
		}
		return Array.getLength(array);
	}
	
	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param <T> 被处理的集合
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(T[] array, CharSequence conjunction) {
		return join(array, conjunction, null, null);
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param <T> 被处理的集合
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @param prefix 每个元素添加的前缀，null表示不添加
	 * @param suffix 每个元素添加的后缀，null表示不添加
	 * @return 连接后的字符串
	 * @since 4.0.10
	 */
	public static <T> String join(T[] array, CharSequence conjunction, String prefix, String suffix) {
		if (null == array) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (T item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			if (ArrayUtil.isArray(item)) {
				sb.append(join(ArrayUtil.wrap(item), conjunction, prefix, suffix));
			} else if (item instanceof Iterable<?>) {
				sb.append(IterUtil.join((Iterable<?>) item, conjunction, prefix, suffix));
			} else if (item instanceof Iterator<?>) {
				sb.append(IterUtil.join((Iterator<?>) item, conjunction, prefix, suffix));
			} else {
				sb.append(StrUtil.wrap(StrUtil.toString(item), prefix, suffix));
			}
		}
		return sb.toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static String join(long[] array, CharSequence conjunction) {
		if (null == array) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (long item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static String join(int[] array, CharSequence conjunction) {
		if (null == array) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (int item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static String join(short[] array, CharSequence conjunction) {
		if (null == array) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (short item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static String join(char[] array, CharSequence conjunction) {
		if (null == array) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (char item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static String join(byte[] array, CharSequence conjunction) {
		if (null == array) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (byte item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static String join(boolean[] array, CharSequence conjunction) {
		if (null == array) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (boolean item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static String join(float[] array, CharSequence conjunction) {
		if (null == array) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (float item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static String join(double[] array, CharSequence conjunction) {
		if (null == array) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (double item : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(conjunction);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 * 
	 * @param array 数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static String join(Object array, CharSequence conjunction) {
		if (isArray(array)) {
			final Class<?> componentType = array.getClass().getComponentType();
			if (componentType.isPrimitive()) {
				final String componentTypeName = componentType.getName();
				switch (componentTypeName) {
				case "long":
					return join((long[]) array, conjunction);
				case "int":
					return join((int[]) array, conjunction);
				case "short":
					return join((short[]) array, conjunction);
				case "char":
					return join((char[]) array, conjunction);
				case "byte":
					return join((byte[]) array, conjunction);
				case "boolean":
					return join((boolean[]) array, conjunction);
				case "float":
					return join((float[]) array, conjunction);
				case "double":
					return join((double[]) array, conjunction);
				default:
					throw new UtilException("Unknown primitive type: [{}]", componentTypeName);
				}
			} else {
				return join((Object[]) array, conjunction);
			}
		}
		throw new UtilException(StrUtil.format("[{}] is not a Array!", array.getClass()));
	}

	/**
	 * {@link ByteBuffer} 转byte数组
	 * 
	 * @param bytebuffer {@link ByteBuffer}
	 * @return byte数组
	 * @since 3.0.1
	 */
	public static byte[] toArray(ByteBuffer bytebuffer) {
		if (false == bytebuffer.hasArray()) {
			int oldPosition = bytebuffer.position();
			bytebuffer.position(0);
			int size = bytebuffer.limit();
			byte[] buffers = new byte[size];
			bytebuffer.get(buffers);
			bytebuffer.position(oldPosition);
			return buffers;
		} else {
			return Arrays.copyOfRange(bytebuffer.array(), bytebuffer.position(), bytebuffer.limit());
		}
	}

	/**
	 * 将集合转为数组
	 * 
	 * @param <T> 数组元素类型
	 * @param iterator {@link Iterator}
	 * @param componentType 集合元素类型
	 * @return 数组
	 * @since 3.0.9
	 */
	public static <T> T[] toArray(Iterator<T> iterator, Class<T> componentType) {
		return toArray(CollectionUtil.newArrayList(iterator), componentType);
	}

	/**
	 * 将集合转为数组
	 * 
	 * @param <T> 数组元素类型
	 * @param iterable {@link Iterable}
	 * @param componentType 集合元素类型
	 * @return 数组
	 * @since 3.0.9
	 */
	public static <T> T[] toArray(Iterable<T> iterable, Class<T> componentType) {
		return toArray(CollectionUtil.toCollection(iterable), componentType);
	}

	/**
	 * 将集合转为数组
	 * 
	 * @param <T> 数组元素类型
	 * @param collection 集合
	 * @param componentType 集合元素类型
	 * @return 数组
	 * @since 3.0.9
	 */
	public static <T> T[] toArray(Collection<T> collection, Class<T> componentType) {
		final T[] array = newArray(componentType, collection.size());
		return collection.toArray(array);
	}

	// ---------------------------------------------------------------------- remove
	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 * 
	 * @param <T> 数组元素类型
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] remove(T[] array, int index) throws IllegalArgumentException {
		return (T[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static long[] remove(long[] array, int index) throws IllegalArgumentException {
		return (long[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static int[] remove(int[] array, int index) throws IllegalArgumentException {
		return (int[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static short[] remove(short[] array, int index) throws IllegalArgumentException {
		return (short[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static char[] remove(char[] array, int index) throws IllegalArgumentException {
		return (char[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static byte[] remove(byte[] array, int index) throws IllegalArgumentException {
		return (byte[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static double[] remove(double[] array, int index) throws IllegalArgumentException {
		return (double[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static float[] remove(float[] array, int index) throws IllegalArgumentException {
		return (float[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static boolean[] remove(boolean[] array, int index) throws IllegalArgumentException {
		return (boolean[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static Object remove(Object array, int index) throws IllegalArgumentException {
		if (null == array) {
			return array;
		}
		int length = length(array);
		if (index < 0 || index >= length) {
			return array;
		}

		final Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
		System.arraycopy(array, 0, result, 0, index);
		if (index < length - 1) {
			// 后半部分
			System.arraycopy(array, index + 1, result, index, length - index - 1);
		}

		return result;
	}

	// ---------------------------------------------------------------------- remove
	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 * 
	 * @param <T> 数组元素类型
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static <T> T[] removeEle(T[] array, T element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static long[] removeEle(long[] array, long element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static int[] removeEle(int[] array, int element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static short[] removeEle(short[] array, short element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static char[] removeEle(char[] array, char element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static byte[] removeEle(byte[] array, byte element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static double[] removeEle(double[] array, double element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static float[] removeEle(float[] array, float element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 * 
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static boolean[] removeEle(boolean[] array, boolean element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	// ------------------------------------------------------------------------------------------------------------ Reverse array

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param <T> 数组元素类型
	 * @param array 数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive 结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static <T> T[] reverse(final T[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		T tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param <T> 数组元素类型
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static <T> T[] reverse(final T[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive 结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static long[] reverse(final long[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		long tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static long[] reverse(final long[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive 结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static int[] reverse(final int[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		int tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static int[] reverse(final int[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive 结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static short[] reverse(final short[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		short tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static short[] reverse(final short[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive 结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static char[] reverse(final char[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		char tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static char[] reverse(final char[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive 结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static byte[] reverse(final byte[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		byte tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static byte[] reverse(final byte[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive 结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static double[] reverse(final double[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		double tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static double[] reverse(final double[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive 结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static float[] reverse(final float[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		float tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static float[] reverse(final float[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive 结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static boolean[] reverse(final boolean[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		boolean tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 * 
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static boolean[] reverse(final boolean[] array) {
		return reverse(array, 0, array.length);
	}

	// ------------------------------------------------------------------------------------------------------------ min and max
	/**
	 * 取最小值
	 * 
	 * @param <T> 元素类型
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static <T extends Comparable<? super T>> T min(T[] numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		T min = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (ObjectUtil.compare(min, numberArray[i]) > 0) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 * 
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static long min(long... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		long min = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 * 
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static int min(int... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		int min = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 * 
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static short min(short... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		short min = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 * 
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static char min(char... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		char min = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 * 
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static byte min(byte... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		byte min = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 * 
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static double min(double... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		double min = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 * 
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static float min(float... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		float min = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最大值
	 * 
	 * @param <T> 元素类型
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static <T extends Comparable<? super T>> T max(T[] numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		T max = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (ObjectUtil.compare(max, numberArray[i]) < 0) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 * 
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static long max(long... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		long max = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 * 
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static int max(int... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		int max = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 * 
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static short max(short... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		short max = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 * 
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static char max(char... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		char max = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 * 
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static byte max(byte... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		byte max = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 * 
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static double max(double... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		double max = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 * 
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static float max(float... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		float max = numberArray[0];
		for (int i = 0; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 交换数组中连个位置的值
	 * 
	 * @param array 数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static int[] swap(int[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		int tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中连个位置的值
	 * 
	 * @param array 数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static long[] swap(long[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		long tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中连个位置的值
	 * 
	 * @param array 数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static double[] swap(double[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		double tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中连个位置的值
	 * 
	 * @param array 数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static float[] swap(float[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		float tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中连个位置的值
	 * 
	 * @param array 数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static boolean[] swap(boolean[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		boolean tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中连个位置的值
	 * 
	 * @param array 数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static byte[] swap(byte[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		byte tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中连个位置的值
	 * 
	 * @param array 数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static char[] swap(char[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		char tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中连个位置的值
	 * 
	 * @param array 数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static short[] swap(short[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		short tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中连个位置的值
	 * 
	 * @param <T> 元素类型
	 * @param array 数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static <T> T[] swap(T[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Array must not empty !");
		}
		T tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中连个位置的值
	 * 
	 * @param array 数组对象
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static Object swap(Object array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Array must not empty !");
		}
		Object tmp = get(array, index1);
		Array.set(array, index1, Array.get(array, index2));
		Array.set(array, index2, tmp);
		return array;
	}
}
