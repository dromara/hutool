package cn.hutool.core.text;

import cn.hutool.core.collection.ArrayIter;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.function.Function;

/**
 * 字符串连接器（拼接器），通过给定的字符串和多个元素，拼接为一个字符串
 *
 * @author looly
 * @since 5.7.2
 */
public class StrJoiner implements Appendable {

	private Appendable appendable;
	private CharSequence delimiter;
	private CharSequence prefix;
	private CharSequence suffix;
	// 前缀和后缀是否包装每个元素，true表示包装每个元素，false包装整个字符串
	private boolean wrapElement;
	// null元素处理逻辑
	private NullMode nullMode = NullMode.NULL_STRING;
	// 当结果为空时默认返回的拼接结果
	private String emptyResult = StrUtil.EMPTY;

	// appendable中是否包含内容，用于判断增加内容时，是否首先加入分隔符
	private boolean hasContent;

	/**
	 * 使用指定分隔符创建{@link StrJoiner}
	 *
	 * @param delimiter 分隔符
	 * @return {@link StrJoiner}
	 */
	public static StrJoiner of(CharSequence delimiter) {
		return new StrJoiner(delimiter);
	}

	/**
	 * 使用指定分隔符创建{@link StrJoiner}
	 *
	 * @param delimiter 分隔符
	 * @param prefix    前缀
	 * @param suffix    后缀
	 * @return {@link StrJoiner}
	 */
	public static StrJoiner of(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		return new StrJoiner(delimiter, prefix, suffix);
	}

	/**
	 * 构造
	 *
	 * @param delimiter 分隔符，{@code null}表示无连接符，直接拼接
	 */
	public StrJoiner(CharSequence delimiter) {
		this(null, delimiter);
	}

	/**
	 * 构造
	 *
	 * @param appendable 字符串追加器，拼接的字符串都将加入到此，{@code null}使用默认{@link StringBuilder}
	 * @param delimiter  分隔符，{@code null}表示无连接符，直接拼接
	 */
	public StrJoiner(Appendable appendable, CharSequence delimiter) {
		this(appendable, delimiter, null, null);
	}

	/**
	 * 构造
	 *
	 * @param delimiter 分隔符，{@code null}表示无连接符，直接拼接
	 * @param prefix    前缀
	 * @param suffix    后缀
	 */
	public StrJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		this(null, delimiter, prefix, suffix);
	}

	/**
	 * 构造
	 *
	 * @param appendable 字符串追加器，拼接的字符串都将加入到此，{@code null}使用默认{@link StringBuilder}
	 * @param delimiter  分隔符，{@code null}表示无连接符，直接拼接
	 * @param prefix     前缀
	 * @param suffix     后缀
	 */
	public StrJoiner(Appendable appendable, CharSequence delimiter,
					 CharSequence prefix, CharSequence suffix) {
		if (null != appendable) {
			this.appendable = appendable;
			checkHasContent(appendable);
		}

		this.delimiter = delimiter;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	/**
	 * 设置分隔符
	 *
	 * @param delimiter 分隔符
	 * @return this
	 */
	public StrJoiner setDelimiter(CharSequence delimiter) {
		this.delimiter = delimiter;
		return this;
	}

	/**
	 * 设置前缀
	 *
	 * @param prefix 前缀
	 * @return this
	 */
	public StrJoiner setPrefix(CharSequence prefix) {
		this.prefix = prefix;
		return this;
	}

	/**
	 * 设置后缀
	 *
	 * @param suffix 后缀
	 * @return this
	 */
	public StrJoiner setSuffix(CharSequence suffix) {
		this.suffix = suffix;
		return this;
	}

	/**
	 * 设置前缀和后缀是否包装每个元素
	 *
	 * @param wrapElement true表示包装每个元素，false包装整个字符串
	 * @return this
	 */
	public StrJoiner setWrapElement(boolean wrapElement) {
		this.wrapElement = wrapElement;
		return this;
	}

	/**
	 * 设置{@code null}元素处理逻辑
	 *
	 * @param nullMode 逻辑枚举，可选忽略、转换为""或转换为null字符串
	 * @return this
	 */
	public StrJoiner setNullMode(NullMode nullMode) {
		this.nullMode = nullMode;
		return this;
	}

	/**
	 * 设置当没有任何元素加入时，默认返回的字符串，默认""
	 *
	 * @param emptyResult 默认字符串
	 * @return this
	 */
	public StrJoiner setEmptyResult(String emptyResult) {
		this.emptyResult = emptyResult;
		return this;
	}

	/**
	 * 追加对象到拼接器中
	 *
	 * @param <T> 元素类型
	 * @param obj 对象，支持数组、集合等
	 * @return this
	 */
	public <T> StrJoiner append(Object obj) {
		if (null == obj) {
			append((CharSequence) null);
		} else if (ArrayUtil.isArray(obj)) {
			append(new ArrayIter<>(obj));
		} else if (obj instanceof Iterator) {
			append((Iterator<?>) obj);
		} else if (obj instanceof Iterable) {
			append(((Iterable<?>) obj).iterator());
		} else {
			append(String.valueOf(obj));
		}
		return this;
	}

	/**
	 * 追加数组中的元素到拼接器中
	 *
	 * @param <T>   元素类型
	 * @param array 元素数组
	 * @return this
	 */
	public <T> StrJoiner append(T[] array) {
		if(null == array){
			return this;
		}
		return append(new ArrayIter<>(array));
	}

	/**
	 * 追加{@link Iterator}中的元素到拼接器中
	 *
	 * @param <T>      元素类型
	 * @param iterator 元素列表
	 * @return this
	 */
	public <T> StrJoiner append(Iterator<T> iterator) {
		if(null == iterator){
			return this;
		}
		return append(iterator, (t) -> StrJoiner.of(this.delimiter).append(t).toString());
	}

	/**
	 * 追加数组中的元素到拼接器中
	 *
	 * @param <T>       元素类型
	 * @param array     元素数组
	 * @param toStrFunc 元素对象转换为字符串的函数
	 * @return this
	 */
	public <T> StrJoiner append(T[] array, Function<T, ? extends CharSequence> toStrFunc) {
		return append((Iterator<T>) new ArrayIter<>(array), toStrFunc);
	}

	/**
	 * 追加{@link Iterator}中的元素到拼接器中
	 *
	 * @param <T>       元素类型
	 * @param iterable  元素列表
	 * @param toStrFunc 元素对象转换为字符串的函数
	 * @return this
	 */
	public <T> StrJoiner append(Iterable<T> iterable, Function<T, ? extends CharSequence> toStrFunc) {
		return append(IterUtil.getIter(iterable), toStrFunc);
	}

	/**
	 * 追加{@link Iterator}中的元素到拼接器中
	 *
	 * @param <T>       元素类型
	 * @param iterator  元素列表
	 * @param toStrFunc 元素对象转换为字符串的函数
	 * @return this
	 */
	public <T> StrJoiner append(Iterator<T> iterator, Function<T, ? extends CharSequence> toStrFunc) {
		if (null != iterator) {
			while (iterator.hasNext()) {
				append(toStrFunc.apply(iterator.next()));
			}
		}
		return this;
	}

	@Override
	public StrJoiner append(CharSequence csq) {
		if (null == csq) {
			switch (this.nullMode) {
				case IGNORE:
					return this;
				case TO_EMPTY:
					csq = StrUtil.EMPTY;
					break;
				case NULL_STRING:
					csq = StrUtil.NULL;
			}
		}
		try {
			final Appendable appendable = prepare();
			if (wrapElement && StrUtil.isNotEmpty(this.prefix)) {
				appendable.append(prefix);
			}
			appendable.append(csq);
			if (wrapElement && StrUtil.isNotEmpty(this.suffix)) {
				appendable.append(suffix);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public StrJoiner append(CharSequence csq, int startInclude, int endExclude) {
		return append(StrUtil.sub(csq, startInclude, endExclude));
	}

	@Override
	public StrJoiner append(char c) {
		return append(String.valueOf(c));
	}

	@Override
	public String toString() {
		if(null == this.appendable){
			return emptyResult;
		}
		if (false == wrapElement && StrUtil.isNotEmpty(this.suffix)) {
			try {
				this.appendable.append(this.suffix);
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return this.appendable.toString();
	}

	/**
	 * {@code null}处理的模式
	 */
	public enum NullMode {
		/**
		 * 忽略{@code null}，即null元素不加入拼接的字符串
		 */
		IGNORE,
		/**
		 * {@code null}转为""
		 */
		TO_EMPTY,
		/**
		 * {@code null}转为null字符串
		 */
		NULL_STRING
	}

	/**
	 * 准备连接器，如果连接器非空，追加元素，否则初始化前缀
	 *
	 * @return {@link Appendable}
	 * @throws IOException IO异常
	 */
	private Appendable prepare() throws IOException {
		if (hasContent) {
			this.appendable.append(delimiter);
		} else {
			if (null == this.appendable) {
				this.appendable = new StringBuilder();
			}
			if (false == wrapElement && StrUtil.isNotEmpty(this.prefix)) {
				this.appendable.append(this.prefix);
			}
			this.hasContent = true;
		}
		return this.appendable;
	}

	/**
	 * 检查用户传入的{@link Appendable} 是否已经存在内容，而且不能以分隔符结尾
	 *
	 * @param appendable {@link Appendable}
	 */
	private void checkHasContent(Appendable appendable) {
		if (appendable instanceof CharSequence) {
			final CharSequence charSequence = (CharSequence) appendable;
			if (charSequence.length() > 0 && StrUtil.endWith(charSequence, delimiter)) {
				this.hasContent = true;
			}
		} else {
			final String initStr = appendable.toString();
			if (StrUtil.isNotEmpty(initStr) && false == StrUtil.endWith(initStr, delimiter)) {
				this.hasContent = true;
			}
		}
	}
}
