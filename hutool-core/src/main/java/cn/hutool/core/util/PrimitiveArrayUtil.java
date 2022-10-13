package cn.hutool.core.util;

import cn.hutool.core.math.NumberUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

/**
 * 原始类型数组工具类
 *
 * @author looly
 * @since 5.5.2
 */
public class PrimitiveArrayUtil {
	/**
	 * 数组中元素未找到的下标，值为-1
	 */
	public static final int INDEX_NOT_FOUND = -1;

	// ---------------------------------------------------------------------- isEmpty

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final long[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final int[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final short[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final char[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final byte[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final double[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final float[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(final boolean[] array) {
		return array == null || array.length == 0;
	}

	// ---------------------------------------------------------------------- isNotEmpty

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final long[] array) {
		return false == isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final int[] array) {
		return false == isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final short[] array) {
		return false == isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final char[] array) {
		return false == isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final byte[] array) {
		return false == isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final double[] array) {
		return false == isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final float[] array) {
		return false == isEmpty(array);
	}

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(final boolean[] array) {
		return false == isEmpty(array);
	}

	// ---------------------------------------------------------------------- resize

	/**
	 * 生成一个新的重新设置大小的数组<br>
	 * 调整大小后拷贝原数组到新数组下。扩大则占位前N个位置，其它位置补充0，缩小则截断
	 *
	 * @param bytes   原数组
	 * @param newSize 新的数组大小
	 * @return 调整后的新数组
	 * @since 4.6.7
	 */
	public static byte[] resize(final byte[] bytes, final int newSize) {
		if (newSize < 0) {
			return bytes;
		}
		final byte[] newArray = new byte[newSize];
		if (newSize > 0 && isNotEmpty(bytes)) {
			System.arraycopy(bytes, 0, newArray, 0, Math.min(bytes.length, newSize));
		}
		return newArray;
	}

	// ---------------------------------------------------------------------- addAll

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static byte[] addAll(final byte[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final byte[] array : arrays) {
			if (null != array) {
				length += array.length;
			}
		}

		final byte[] result = new byte[length];
		length = 0;
		for (final byte[] array : arrays) {
			if (null != array) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static int[] addAll(final int[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final int[] array : arrays) {
			if (null != array) {
				length += array.length;
			}
		}

		final int[] result = new int[length];
		length = 0;
		for (final int[] array : arrays) {
			if (null != array) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static long[] addAll(final long[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final long[] array : arrays) {
			if (null != array) {
				length += array.length;
			}
		}

		final long[] result = new long[length];
		length = 0;
		for (final long[] array : arrays) {
			if (null != array) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static double[] addAll(final double[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final double[] array : arrays) {
			if (null != array) {
				length += array.length;
			}
		}

		final double[] result = new double[length];
		length = 0;
		for (final double[] array : arrays) {
			if (null != array) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static float[] addAll(final float[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final float[] array : arrays) {
			if (null != array) {
				length += array.length;
			}
		}

		final float[] result = new float[length];
		length = 0;
		for (final float[] array : arrays) {
			if (null != array) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static char[] addAll(final char[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final char[] array : arrays) {
			if (null != array) {
				length += array.length;
			}
		}

		final char[] result = new char[length];
		length = 0;
		for (final char[] array : arrays) {
			if (null != array) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static boolean[] addAll(final boolean[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final boolean[] array : arrays) {
			if (null != array) {
				length += array.length;
			}
		}

		final boolean[] result = new boolean[length];
		length = 0;
		for (final boolean[] array : arrays) {
			if (null != array) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static short[] addAll(final short[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (final short[] array : arrays) {
			if (null != array) {
				length += array.length;
			}
		}

		final short[] result = new short[length];
		length = 0;
		for (final short[] array : arrays) {
			if (null != array) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	// ---------------------------------------------------------------------- split

	/**
	 * 拆分byte数组为几个等份（最后一份按照剩余长度分配空间）
	 *
	 * @param array 数组
	 * @param len   每个小节的长度
	 * @return 拆分后的数组
	 */
	public static byte[][] split(final byte[] array, final int len) {
		int amount = array.length / len;
		final int remainder = array.length % len;
		if (remainder != 0) {
			++amount;
		}
		final byte[][] arrays = new byte[amount][];
		byte[] arr;
		for (int i = 0; i < amount; i++) {
			if (i == amount - 1 && remainder != 0) {
				// 有剩余，按照实际长度创建
				arr = new byte[remainder];
				System.arraycopy(array, i * len, arr, 0, remainder);
			} else {
				arr = new byte[len];
				System.arraycopy(array, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}

	// ---------------------------------------------------------------------- indexOf、LastIndexOf、contains

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(final long[] array, final long value) {
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
	public static int lastIndexOf(final long[] array, final long value) {
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
	public static boolean contains(final long[] array, final long value) {
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
	public static int indexOf(final int[] array, final int value) {
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
	public static int lastIndexOf(final int[] array, final int value) {
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
	public static boolean contains(final int[] array, final int value) {
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
	public static int indexOf(final short[] array, final short value) {
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
	public static int lastIndexOf(final short[] array, final short value) {
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
	public static boolean contains(final short[] array, final short value) {
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
	public static int indexOf(final char[] array, final char value) {
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
	public static int lastIndexOf(final char[] array, final char value) {
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
	public static boolean contains(final char[] array, final char value) {
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
	public static int indexOf(final byte[] array, final byte value) {
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
	public static int lastIndexOf(final byte[] array, final byte value) {
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
	public static boolean contains(final byte[] array, final byte value) {
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
	public static int indexOf(final double[] array, final double value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (NumberUtil.equals(value, array[i])) {
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
	public static int lastIndexOf(final double[] array, final double value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (NumberUtil.equals(value, array[i])) {
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
	public static boolean contains(final double[] array, final double value) {
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
	public static int indexOf(final float[] array, final float value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (NumberUtil.equals(value, array[i])) {
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
	public static int lastIndexOf(final float[] array, final float value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (NumberUtil.equals(value, array[i])) {
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
	public static boolean contains(final float[] array, final float value) {
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
	public static int indexOf(final boolean[] array, final boolean value) {
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
	public static int lastIndexOf(final boolean[] array, final boolean value) {
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
	public static boolean contains(final boolean[] array, final boolean value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	// ------------------------------------------------------------------- Wrap and unwrap

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Integer[] wrap(final int... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Integer[0];
		}

		final Integer[] array = new Integer[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组，null转为0
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static int[] unWrap(final Integer... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new int[0];
		}

		final int[] array = new int[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], 0);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Long[] wrap(final long... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Long[0];
		}

		final Long[] array = new Long[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static long[] unWrap(final Long... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new long[0];
		}

		final long[] array = new long[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], 0L);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Character[] wrap(final char... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Character[0];
		}

		final Character[] array = new Character[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static char[] unWrap(final Character... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new char[0];
		}

		final char[] array = new char[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], Character.MIN_VALUE);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Byte[] wrap(final byte... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Byte[0];
		}

		final Byte[] array = new Byte[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static byte[] unWrap(final Byte... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new byte[0];
		}

		final byte[] array = new byte[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], (byte) 0);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Short[] wrap(final short... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Short[0];
		}

		final Short[] array = new Short[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static short[] unWrap(final Short... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new short[0];
		}

		final short[] array = new short[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], (short) 0);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Float[] wrap(final float... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Float[0];
		}

		final Float[] array = new Float[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static float[] unWrap(final Float... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new float[0];
		}

		final float[] array = new float[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], 0F);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Double[] wrap(final double... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Double[0];
		}

		final Double[] array = new Double[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static double[] unWrap(final Double... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new double[0];
		}

		final double[] array = new double[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], 0D);
		}
		return array;
	}

	/**
	 * 将原始类型数组包装为包装类型
	 *
	 * @param values 原始类型数组
	 * @return 包装类型数组
	 */
	public static Boolean[] wrap(final boolean... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new Boolean[0];
		}

		final Boolean[] array = new Boolean[length];
		for (int i = 0; i < length; i++) {
			array[i] = values[i];
		}
		return array;
	}

	/**
	 * 包装类数组转为原始类型数组<br>
	 * {@code null} 按照 {@code false} 对待
	 *
	 * @param values 包装类型数组
	 * @return 原始类型数组
	 */
	public static boolean[] unWrap(final Boolean... values) {
		if (null == values) {
			return null;
		}
		final int length = values.length;
		if (0 == length) {
			return new boolean[0];
		}

		final boolean[] array = new boolean[length];
		for (int i = 0; i < length; i++) {
			array[i] = ObjUtil.defaultIfNull(values[i], false);
		}
		return array;
	}

	// ------------------------------------------------------------------- sub

	/**
	 * 获取子数组
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static byte[] sub(final byte[] array, int start, int end) {
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return new byte[0];
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return new byte[0];
			}
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static int[] sub(final int[] array, int start, int end) {
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return new int[0];
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return new int[0];
			}
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static long[] sub(final long[] array, int start, int end) {
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return new long[0];
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return new long[0];
			}
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static short[] sub(final short[] array, int start, int end) {
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return new short[0];
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return new short[0];
			}
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static char[] sub(final char[] array, int start, int end) {
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return new char[0];
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return new char[0];
			}
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static double[] sub(final double[] array, int start, int end) {
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return new double[0];
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return new double[0];
			}
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static float[] sub(final float[] array, int start, int end) {
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return new float[0];
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return new float[0];
			}
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @since 4.5.2
	 */
	public static boolean[] sub(final boolean[] array, int start, int end) {
		final int length = Array.getLength(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return new boolean[0];
		}
		if (start > end) {
			final int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return new boolean[0];
			}
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	// ------------------------------------------------------------------- remove

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
	public static long[] remove(final long[] array, final int index) throws IllegalArgumentException {
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
	public static int[] remove(final int[] array, final int index) throws IllegalArgumentException {
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
	public static short[] remove(final short[] array, final int index) throws IllegalArgumentException {
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
	public static char[] remove(final char[] array, final int index) throws IllegalArgumentException {
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
	public static byte[] remove(final byte[] array, final int index) throws IllegalArgumentException {
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
	public static double[] remove(final double[] array, final int index) throws IllegalArgumentException {
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
	public static float[] remove(final float[] array, final int index) throws IllegalArgumentException {
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
	public static boolean[] remove(final boolean[] array, final int index) throws IllegalArgumentException {
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
	@SuppressWarnings("SuspiciousSystemArraycopy")
	public static Object remove(final Object array, final int index) throws IllegalArgumentException {
		if (null == array) {
			return null;
		}
		final int length = Array.getLength(array);
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

	// ---------------------------------------------------------------------- removeEle

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 *
	 * @param array   数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static long[] removeEle(final long[] array, final long element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 *
	 * @param array   数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static int[] removeEle(final int[] array, final int element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 *
	 * @param array   数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static short[] removeEle(final short[] array, final short element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 *
	 * @param array   数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static char[] removeEle(final char[] array, final char element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 *
	 * @param array   数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static byte[] removeEle(final byte[] array, final byte element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 *
	 * @param array   数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static double[] removeEle(final double[] array, final double element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 *
	 * @param array   数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static float[] removeEle(final float[] array, final float element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 移除数组中指定的元素<br>
	 * 只会移除匹配到的第一个元素 copy from commons-lang
	 *
	 * @param array   数组对象，可以是对象数组，也可以原始类型数组
	 * @param element 要移除的元素
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 * @since 3.0.8
	 */
	public static boolean[] removeEle(final boolean[] array, final boolean element) throws IllegalArgumentException {
		return remove(array, indexOf(array, element));
	}

	// ---------------------------------------------------------------------- reverse

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static long[] reverse(final long[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
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
	 * @param array               数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static int[] reverse(final int[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
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
	 * @param array               数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static short[] reverse(final short[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
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
	 * @param array               数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static char[] reverse(final char[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
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
	 * @param array               数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static byte[] reverse(final byte[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
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
	 * @param array               数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static double[] reverse(final double[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
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
	 * @param array               数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static float[] reverse(final float[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
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
	 * @param array               数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 * @since 3.0.9
	 */
	public static boolean[] reverse(final boolean[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
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
	 * @param numberArray 数字数组
	 * @return 最小值
	 * @since 3.0.9
	 */
	public static long min(final long... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		long min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static int min(final int... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		int min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static short min(final short... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		short min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static char min(final char... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		char min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static byte min(final byte... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		byte min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static double min(final double... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		double min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static float min(final float... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		float min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最大值
	 *
	 * @param numberArray 数字数组
	 * @return 最大值
	 * @since 3.0.9
	 */
	public static long max(final long... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		long max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static int max(final int... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		int max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static short max(final short... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		short max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static char max(final char... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		char max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static byte max(final byte... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		byte max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static double max(final double... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		double max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
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
	public static float max(final float... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		float max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	// ---------------------------------------------------------------------- shuffle

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static int[] shuffle(final int[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static int[] shuffle(final int[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static long[] shuffle(final long[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static long[] shuffle(final long[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static double[] shuffle(final double[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static double[] shuffle(final double[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static float[] shuffle(final float[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static float[] shuffle(final float[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean[] shuffle(final boolean[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean[] shuffle(final boolean[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static byte[] shuffle(final byte[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static byte[] shuffle(final byte[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static char[] shuffle(final char[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static char[] shuffle(final char[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static short[] shuffle(final short[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static short[] shuffle(final short[] array, final Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}

	// ---------------------------------------------------------------------- shuffle

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static int[] swap(final int[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final int tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static long[] swap(final long[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final long tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static double[] swap(final double[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final double tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static float[] swap(final float[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final float tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static boolean[] swap(final boolean[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final boolean tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static byte[] swap(final byte[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final byte tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static char[] swap(final char[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final char tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 * @since 4.0.7
	 */
	public static short[] swap(final short[] array, final int index1, final int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		final short tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 检查数组是否升序，即array[i] &lt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final byte[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即array[i] &gt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final byte[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即array[i] &lt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final short[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即array[i] &gt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final short[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即array[i] &lt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final char[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即array[i] &gt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final char[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即array[i] &lt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final int[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即array[i] &gt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final int[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即array[i] &lt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final long[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即array[i] &gt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final long[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即array[i] &lt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final double[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即array[i] &gt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final double[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否升序，即array[i] &lt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedASC(final float[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即array[i] &gt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 * @author FengBaoheng
	 * @since 5.5.2
	 */
	public static boolean isSortedDESC(final float[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}
}
