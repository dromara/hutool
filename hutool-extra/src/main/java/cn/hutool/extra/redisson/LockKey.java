package cn.hutool.extra.redisson;

/**
 * Author: miracle
 * Date: 2023/8/10 16:47
 */
public @interface LockKey {

	String prefix();

	String delimiter() default "::";

	/**
	 * 支持：SPEL表达式
	 */
	String[] keys() default "";

	/**
	 * true则表示获取写锁，否则获取读锁
	 */
	boolean isWrite() default false;

}
