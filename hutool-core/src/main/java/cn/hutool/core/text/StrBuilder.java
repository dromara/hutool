package cn.hutool.core.text;

import java.io.Serializable;
import java.util.Arrays;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 可复用的字符串生成器，非线程安全
 *
 * @author Looly
 * @since 4.0.0
 */
public class StrBuilder implements CharSequence, Appendable, Serializable {
	private static final long serialVersionUID = 6341229705927508451L;

	/** 默认容量 */
	public static final int DEFAULT_CAPACITY = 16;

	/** 存放的字符数组 */
	private char[] value;
	/** 当前指针位置，或者叫做已经加入的字符数，此位置总在最后一个字符之后 */
	private int position;
	
	/**
	 * 创建字符串构建器
	 * @return {@link StrBuilder}
	 */
	public static StrBuilder create() {
		return new StrBuilder();
	}
	
	/**
	 * 创建字符串构建器
	 * @param initialCapacity 初始容量
	 * @return {@link StrBuilder}
	 */
	public static StrBuilder create(int initialCapacity) {
		return new StrBuilder(initialCapacity);
	}
	
	/**
	 * 创建字符串构建器
	 * @param strs 初始字符串
	 * @return {@link StrBuilder}
	 * @since 4.0.1
	 */
	public static StrBuilder create(CharSequence... strs) {
		return new StrBuilder(strs);
	}

	// ------------------------------------------------------------------------------------ Constructor start
	/**
	 * 构造
	 */
	public StrBuilder() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 初始容量
	 */
	public StrBuilder(int initialCapacity) {
		value = new char[initialCapacity];
	}
	
	/**
	 * 构造
	 *
	 * @param strs 初始字符串
	 * @since 4.0.1
	 */
	public StrBuilder(CharSequence... strs) {
		this(ArrayUtil.isEmpty(strs) ? DEFAULT_CAPACITY : (totalLength(strs) + DEFAULT_CAPACITY));
		for(int i = 0; i < strs.length; i++) {
			append(strs[i]);
		}
	}
	// ------------------------------------------------------------------------------------ Constructor end

	// ------------------------------------------------------------------------------------ Append
	/**
	 * 追加对象，对象会被转换为字符串
	 * 
	 * @param obj 对象
	 * @return this
	 */
	public StrBuilder append(Object obj) {
		return insert(this.position, obj);
	}

	/**
	 * 追加一个字符
	 *
	 * @param c 字符
	 * @return this
	 */
	@Override
	public StrBuilder append(char c) {
		return insert(this.position, c);
	}

	/**
	 * 追加一个字符数组
	 * 
	 * @param src 字符数组
	 * @return this
	 */
	public StrBuilder append(char[] src) {
		if (ArrayUtil.isEmpty(src)) {
			return this;
		}
		return append(src, 0, src.length);
	}

	/**
	 * 追加一个字符数组
	 * 
	 * @param src 字符数组
	 * @param srcPos 开始位置（包括）
	 * @param length 长度
	 * @return this
	 */
	public StrBuilder append(char[] src, int srcPos, int length) {
		return insert(this.position, src, srcPos, length);
	}

	@Override
	public StrBuilder append(CharSequence csq) {
		return insert(this.position, csq);
	}

	@Override
	public StrBuilder append(CharSequence csq, int start, int end) {
		return insert(this.position, csq, start, end);
	}

	// ------------------------------------------------------------------------------------ Insert
	/**
	 * 追加对象，对象会被转换为字符串
	 * 
	 * @param obj 对象
	 * @return this
	 */
	public StrBuilder insert(int index, Object obj) {
		if (obj instanceof CharSequence) {
			return insert(index, (CharSequence) obj);
		}
		return insert(index, Convert.toStr(obj));
	}

	/**
	 * 插入指定字符
	 * 
	 * @param index 位置
	 * @param c 字符
	 * @return this
	 */
	public StrBuilder insert(int index, char c) {
		moveDataAfterIndex(index, 1);
		value[index] = c;
		this.position = Math.max(this.position, index) + 1;
		return this;
	}

	/**
	 * 指定位置插入数据<br>
	 * 如果插入位置为当前位置，则定义为追加<br>
	 * 如果插入位置大于当前位置，则中间部分补充空格
	 * 
	 * @param index 插入位置
	 * @param src 源数组
	 * @return this
	 */
	public StrBuilder insert(int index, char[] src) {
		if (ArrayUtil.isEmpty(src)) {
			return this;
		}
		return insert(index, src, 0, src.length);
	}

	/**
	 * 指定位置插入数据<br>
	 * 如果插入位置为当前位置，则定义为追加<br>
	 * 如果插入位置大于当前位置，则中间部分补充空格
	 * 
	 * @param index 插入位置
	 * @param src 源数组
	 * @param srcPos 位置
	 * @param length 长度
	 * @return this
	 */
	public StrBuilder insert(int index, char[] src, int srcPos, int length) {
		if (ArrayUtil.isEmpty(src) || srcPos > src.length || length <= 0) {
			return this;
		}
		if (index < 0) {
			index = 0;
		}
		if (srcPos < 0) {
			srcPos = 0;
		} else if (srcPos + length > src.length) {
			// 长度越界，只截取最大长度
			length = src.length - srcPos;
		}

		moveDataAfterIndex(index, length);
		// 插入数据
		System.arraycopy(src, srcPos, value, index, length);
		this.position = Math.max(this.position, index) + length;
		return this;
	}

	/**
	 * 指定位置插入字符串的某个部分<br>
	 * 如果插入位置为当前位置，则定义为追加<br>
	 * 如果插入位置大于当前位置，则中间部分补充空格
	 * 
	 * @param index 位置
	 * @param csq 字符串
	 * @return this
	 */
	public StrBuilder insert(int index, CharSequence csq) {
		if (null == csq) {
			csq = "null";
		}
		int len = csq.length();
		moveDataAfterIndex(index, csq.length());
		if (csq instanceof String) {
			((String) csq).getChars(0, len, this.value, index);
		} else if (csq instanceof StringBuilder) {
			((StringBuilder) csq).getChars(0, len, this.value, index);
		} else if (csq instanceof StringBuffer) {
			((StringBuffer) csq).getChars(0, len, this.value, index);
		} else if (csq instanceof StrBuilder) {
			((StrBuilder) csq).getChars(0, len, this.value, index);
		} else {
			for (int i = 0, j = this.position; i < len; i++, j++) {
				this.value[j] = csq.charAt(i);
			}
		}
		this.position = Math.max(this.position, index) + len;
		return this;
	}

	/**
	 * 指定位置插入字符串的某个部分<br>
	 * 如果插入位置为当前位置，则定义为追加<br>
	 * 如果插入位置大于当前位置，则中间部分补充空格
	 * 
	 * @param index 位置
	 * @param csq 字符串
	 * @param start 字符串开始位置（包括）
	 * @param end 字符串结束位置（不包括）
	 * @return this
	 */
	public StrBuilder insert(int index, CharSequence csq, int start, int end) {
		if (csq == null) {
			csq = "null";
		}
		final int csqLen = csq.length();
		if (start > csqLen) {
			return this;
		}
		if (start < 0) {
			start = 0;
		}
		if (end > csqLen) {
			end = csqLen;
		}
		if (start >= end) {
			return this;
		}
		if (index < 0) {
			index = 0;
		}

		final int length = end - start;
		moveDataAfterIndex(index, length);
		for (int i = start, j = this.position; i < end; i++, j++) {
			value[j] = csq.charAt(i);
		}
		this.position = Math.max(this.position, index) + length;
		return this;
	}

	// ------------------------------------------------------------------------------------ Others
	/**
	 * 将指定段的字符列表写出到目标字符数组中
	 * 
	 * @param srcBegin 起始位置（包括）
	 * @param srcEnd 结束位置（不包括）
	 * @param dst 目标数组
	 * @param dstBegin 目标起始位置（包括）
	 * @return this
	 */
	public StrBuilder getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
		if (srcBegin < 0) {
			srcBegin = 0;
		}
		if (srcEnd < 0) {
			srcEnd = 0;
		} else if (srcEnd > this.position) {
			srcEnd = this.position;
		}
		if (srcBegin > srcEnd) {
			throw new StringIndexOutOfBoundsException("srcBegin > srcEnd");
		}
		System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
		return this;
	}

	/**
	 * 是否有内容
	 * 
	 * @return 是否有内容
	 */
	public boolean hasContent() {
		return position > 0;
	}

	/**
	 * 是否为空
	 * 
	 * @return 是否为空
	 */
	public boolean isEmpty() {
		return position == 0;
	}
	
	/**
	 * 删除全部字符，位置归零
	 * 
	 * @return this
	 */
	public StrBuilder clear() {
		return reset();
	}

	/**
	 * 删除全部字符，位置归零
	 * 
	 * @return this
	 */
	public StrBuilder reset() {
		this.position = 0;
		return this;
	}

	/**
	 * 删除到指定位置<br>
	 * 如果新位置小于等于0，则删除全部
	 * 
	 * @param newPosition 新的位置，不包括这个位置
	 * @return this
	 */
	public StrBuilder delTo(int newPosition) {
		if (newPosition < 0) {
			this.reset();
		} else if (newPosition < this.position) {
			this.position = newPosition;
		}
		return this;
	}

	/**
	 * 删除指定长度的字符
	 * 
	 * @param start 开始位置（包括）
	 * @param end 结束位置（不包括）
	 * @return this
	 */
	public StrBuilder del(int start, int end) {
		if (start < 0) {
			start = 0;
		}
		if (end > this.position) {
			end = this.position;
		}
		if (start > end) {
			throw new StringIndexOutOfBoundsException("Start is greater than End.");
		}
		if (end == this.position) {
			this.position = start;
		}

		int len = end - start;
		if (len > 0) {
			System.arraycopy(value, start + len, value, start, this.position - end);
			this.position -= len;
		}
		return this;
	}

	/**
	 * 生成字符串
	 * 
	 * @param isReset 是否重置，重置后相当于空的构建器
	 * @return 生成的字符串
	 */
	public String toString(boolean isReset) {
		if (position > 0) {
			final String s = new String(value, 0, position);
			if (isReset) {
				reset();
			}
			return s;
		}
		return StrUtil.EMPTY;
	}

	/**
	 * 重置并返回生成的字符串
	 * 
	 * @return 字符串
	 */
	public String toStringAndReset() {
		return toString(true);
	}

	/**
	 * 生成字符串
	 */
	@Override
	public String toString() {
		return toString(false);
	}

	@Override
	public int length() {
		return this.position;
	}

	@Override
	public char charAt(int index) {
		if ((index < 0) || (index > this.position)) {
			throw new StringIndexOutOfBoundsException(index);
		}
		return this.value[index];
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return subString(start, end);
	}

	/**
	 * 返回自定段的字符串
	 * 
	 * @param start 开始位置（包括）
	 * @return this
	 */
	public String subString(int start) {
		return subString(start, this.position);
	}

	/**
	 * 返回自定段的字符串
	 * 
	 * @param start 开始位置（包括）
	 * @param end 结束位置（不包括）
	 * @return this
	 */
	public String subString(int start, int end) {
		return new String(this.value, start, end - start);
	}

	// ------------------------------------------------------------------------------------ Private method start
	/**
	 * 指定位置之后的数据后移指定长度
	 * 
	 * @param index 位置
	 * @param length 位移长度
	 */
	private void moveDataAfterIndex(int index, int length) {
		ensureCapacity(Math.max(this.position, index) + length);
		if (index < this.position) {
			// 插入位置在已有数据范围内，后移插入位置之后的数据
			System.arraycopy(this.value, index, this.value, index + length, this.position - index);
		} else if (index > this.position) {
			// 插入位置超出范围，则当前位置到index清除为空格
			Arrays.fill(this.value, this.position, index, StrUtil.C_SPACE);
		}
		// 不位移
	}

	/**
	 * 确认容量是否够用，不够用则扩展容量
	 * 
	 * @param minimumCapacity 最小容量
	 */
	private void ensureCapacity(int minimumCapacity) {
		if (minimumCapacity > value.length) {
			expandCapacity(minimumCapacity);
		}
	}

	/**
	 * 扩展容量<br>
	 * 首先对容量进行二倍扩展，如果小于最小容量，则扩展为最小容量
	 * 
	 * @param minimumCapacity 需要扩展的最小容量
	 */
	private void expandCapacity(int minimumCapacity) {
		int newCapacity = value.length * 2 + 2;
		if (newCapacity < minimumCapacity) {
			newCapacity = minimumCapacity;
		}
		if (newCapacity < 0) {
			if (minimumCapacity < 0) {
				// overflow
				throw new OutOfMemoryError("Capacity is too long and max than Integer.MAX");
			}
			newCapacity = Integer.MAX_VALUE;
		}
		value = Arrays.copyOf(value, newCapacity);
	}
	
	/**
	 * 给定字符串数组的总长度<br>
	 * null字符长度定义为0
	 * 
	 * @param strs 字符串数组
	 * @return 总长度
	 * @since 4.0.1
	 */
	private static int totalLength(CharSequence... strs) {
		int totalLength = 0;
		for(int i = 0 ; i < strs.length; i++) {
			totalLength += (null == strs[i] ? 4 : strs[i].length());
		}
		return totalLength;
	}
	// ------------------------------------------------------------------------------------ Private method end
}
