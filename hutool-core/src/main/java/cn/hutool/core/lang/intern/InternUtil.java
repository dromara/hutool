package cn.hutool.core.lang.intern;

/**
 * 规范化对象生成工具
 *
 * @author looly
 * @since 5.4.3
 */
public class InternUtil {

	/**
	 * 创建WeakHshMap实现的字符串规范化器
	 *
	 * @param <T> 规范对象的类型
	 * @return {@link Interner}
	 */
	public static <T> Interner<T> createWeakInterner(){
		return new WeakInterner<>();
	}

	/**
	 * 创建JDK默认实现的字符串规范化器
	 *
	 * @return {@link Interner}
	 * @see String#intern()
	 */
	public static Interner<String> createJdkInterner(){
		return new JdkStringInterner();
	}

	/**
	 * 创建字符串规范化器
	 *
	 * @param isWeak 是否创建使用WeakHashMap实现的Interner
	 * @return {@link Interner}
	 */
	public static Interner<String> createStringInterner(boolean isWeak){
		return isWeak ? createWeakInterner() : createJdkInterner();
	}
}
