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
	/**
	 * appendable中是否包含内容
	 */
	private boolean hasContent;

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
	 * @param delimiter  分隔符，{@code null}表示无连接符，直接拼接
	 * @param prefix 前缀
	 * @param suffix 后缀
	 */
	public StrJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		this(null, delimiter, prefix, suffix);
	}

	/**
	 * 构造
	 *
	 * @param appendable 字符串追加器，拼接的字符串都将加入到此，{@code null}使用默认{@link StringBuilder}
	 * @param delimiter  分隔符，{@code null}表示无连接符，直接拼接
	 * @param prefix 前缀
	 * @param suffix 后缀
	 */
	public StrJoiner(Appendable appendable, CharSequence delimiter,
					 CharSequence prefix, CharSequence suffix) {
		if (null != appendable) {
			this.appendable = appendable;
			final String initStr = appendable.toString();
			if (StrUtil.isNotEmpty(initStr) && false == StrUtil.endWith(initStr, delimiter)) {
				// 用户传入的Appendable中已经存在内容，且末尾不是分隔符
				this.hasContent = true;
			}
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
	 * 追加{@link Iterator}中的元素到拼接器中
	 *
	 * @param <T>       元素类型
	 * @param iterable  元素列表
	 * @return this
	 */
	public <T> StrJoiner append(Iterable<T> iterable) {
		return append(IterUtil.getIter(iterable));
	}

	/**
	 * 追加{@link Iterator}中的元素到拼接器中
	 *
	 * @param <T>       元素类型
	 * @param iterator  元素列表
	 * @return this
	 */
	public <T> StrJoiner append(Iterator<T> iterator) {
		return append(iterator, (t)->{
			if(ArrayUtil.isArray(t)){
				return new StrJoiner(this.delimiter).append((Iterator<?>) new ArrayIter<>(t)).toString();
			} else if(t instanceof Iterator){
				return new StrJoiner(this.delimiter).append((Iterator<?>)t).toString();
			} else if(t instanceof Iterable){
				return new StrJoiner(this.delimiter).append((Iterable<?>)t).toString();
			}
			return String.valueOf(t);
		});
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
		try {
			prepare().append(csq);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public StrJoiner append(CharSequence csq, int start, int end) {
		try {
			prepare().append(csq, start, end);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public StrJoiner append(char c) {
		try {
			prepare().append(c);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public String toString() {
		if (StrUtil.isNotEmpty(this.suffix)) {
			try {
				appendable.append(this.suffix);
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return appendable.toString();
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
			this.appendable = new StringBuilder();
			if (StrUtil.isNotEmpty(this.prefix)) {
				this.appendable.append(this.prefix);
			}
			this.hasContent = true;
		}
		return this.appendable;
	}
}
