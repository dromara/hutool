package cn.hutool.core.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 字符串链式处理工具类 <br/>
 * 使用可以参考对应测试类 StringStreamUtilTest
 *
 * @author van
 * @since 5.8.22
 */
public class StrFunctionUtil {
	/**
	 * 需要被处理的数据
	 */
	private CharSequence value;

	private StrFunctionUtil() {
	}

	private StrFunctionUtil(CharSequence value) {
		this.value = value;
	}

	/**
	 * 字符串构造转为链式
	 *
	 * @param str 来源字符串
	 * @return 字符串链式处理工具类
	 */
	public static StrFunctionUtil of(CharSequence str) {
		return new StrFunctionUtil(str);
	}

	/**
	 * 当value不为Null/空的时候 {@link cn.hutool.core.util.StrUtil#isBlankIfStr}，执行consumer，字符串会当作入参传入
	 *
	 * @param consumer 满足条件时会调用的方法
	 * @return StrFunctionUtil 用于继续做链式处理
	 */
	public StrFunctionUtil then(Consumer<String> consumer) {
		if (null != value && value.length() != 0) {
			consumer.accept(value.toString());
		}
		return this;
	}

	/**
	 * 当value不为Null/空的时候，执行supplier无参方法
	 *
	 * @param supplier 满足条件时会调用的方法
	 * @return StrFunctionUtil 用于继续做链式处理
	 */
	public StrFunctionUtil then(Supplier<CharSequence> supplier) {
		if (null != value && value.length() != 0) {
			supplier.get();
		}
		return this;
	}

	/**
	 * 当value不为Null/空的时候，执行function，字符串会当作入参传入，并将function返回的字符串赋值给value
	 *
	 * @param function 满足条件时会调用的方法
	 * @return StrFunctionUtil 用于继续做链式处理
	 */
	public StrFunctionUtil apply(Function<CharSequence, CharSequence> function) {
		if (null != value && value.length() != 0) {
			value = function.apply(value);
		}
		return this;
	}

	/**
	 * 当value不为Null/空的时候，执行function，字符串会当作入参传入，并将function返回的字符串赋值给value
	 * 与 {@link #apply(Function)} 的区别是可以允许function的入参和出参为String
	 *
	 * @param function 满足条件时会调用的方法
	 * @return StrFunctionUtil 用于继续做链式处理
	 */
	public StrFunctionUtil applyStr(Function<String, String> function) {
		if (null != value && value.length() != 0) {
			value = function.apply(value.toString());
		}
		return this;
	}

	/**
	 * 当value为Null/空的时候 {@link cn.hutool.core.util.StrUtil#isBlankIfStr}，执行supplier无参方法
	 *
	 * @param supplier 满足条件时会调用的方法
	 * @return StrFunctionUtil 用于继续做链式处理
	 */
	public StrFunctionUtil ifBlankThen(Supplier<CharSequence> supplier) {
		if (null == value || value.length() == 0) {
			supplier.get();
		}
		return this;
	}

	/**
	 * 当value为Null的时候，执行supplier
	 *
	 * @param supplier 满足条件时会调用的方法
	 * @return StrFunctionUtil 用于继续做链式处理
	 */
	public StrFunctionUtil ifNullThen(Supplier<CharSequence> supplier) {
		if (null == value) {
			supplier.get();
		}
		return this;
	}

	/**
	 * 当value为Null/空的时候 {@link cn.hutool.core.util.StrUtil#isBlankIfStr}，向上抛出指定异常
	 *
	 * @param runtimeException 满足条件时会抛出的异常
	 * @return StrFunctionUtil 用于不满足条件时继续做链式处理
	 */
	public StrFunctionUtil blankThrow(RuntimeException runtimeException) {
		if (null == value || value.length() == 0) {
			throw runtimeException;
		}
		return this;
	}

	/**
	 * 当value为Null的时候，向上抛出指定异常
	 *
	 * @param runtimeException 满足条件时会抛出的异常
	 * @return StrFunctionUtil 用于不满足条件时继续做链式处理
	 */
	public StrFunctionUtil nullThrow(RuntimeException runtimeException) {
		if (null == value) {
			throw runtimeException;
		}
		return this;
	}

	/**
	 * 当value为Null/空的时候 {@link cn.hutool.core.util.StrUtil#isBlankIfStr}，返回指定数据，否则返回 value 数据
	 *a
	 * @param object object
	 * @return 根据value是否为blank返回str/value数据
	 */
	public Object blankReturn(Object object) {
		if (null == value || value.length() == 0) {
			return object;
		}
		return value.toString();
	}

	/**
	 * 当value为Null的时候，返回指定数据，否则返回 value 数据
	 *
	 * @param object object
	 * @return 根据value是否为null返回str/value数据
	 */
	public Object nullReturn(Object object) {
		if (null == value) {
			return object;
		}
		return value.toString();
	}

	/**
	 * 当value为Null/空的时候 {@link cn.hutool.core.util.StrUtil#isBlankIfStr}，返回指定字符串，否则返回 value 数据
	 *
	 * @param str str
	 * @return 根据value是否为blank返回str/value数据
	 */
	public CharSequence blankReturn(CharSequence str) {
		if (null == value || value.length() == 0) {
			return str;
		}
		return value;
	}

	/**
	 * 当value为Null的时候，返回指定字符串，否则返回 value 数据
	 *
	 * @param str str
	 * @return 根据value是否为null返回str/value数据
	 */
	public CharSequence nullReturn(CharSequence str) {
		if (null == value || value.length() == 0) {
			return str;
		}
		return value;
	}

	/**
	 * 当value为Null/空的时候 {@link cn.hutool.core.util.StrUtil#isBlankIfStr}，返回指定字符串，否则返回 value 数据
	 *
	 * @param str str
	 * @return 根据value是否为blank返回str/value数据
	 */
	public String blankReturn(String str) {
		if (null == value || value.length() == 0) {
			return str;
		}
		return value.toString();
	}

	/**
	 * 当value为Null的时候，返回指定字符串，否则返回 value 数据
	 *
	 * @param str str
	 * @return 根据value是否为null返回str/value数据
	 */
	public String nullReturn(String str) {
		if (null == value || value.length() == 0) {
			return str;
		}
		return value.toString();
	}

	/**
	 * 如果value不为null/空，将value直接经过函数调用处理后返回
	 *
	 * @param function 满足条件时会调用的方法
	 * @return 将要返回的字符串
	 */
	public String applyReturn(Function<CharSequence, String> function) {
		if (null != value && value.length() != 0) {
			function.apply(value);
		}
		return String.valueOf(value);
	}

	public CharSequence getValue() {
		return value;
	}

	public void setValue(CharSequence value) {
		this.value = value;
	}
}
