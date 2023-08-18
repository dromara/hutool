package cn.hutool.extra.redisson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: miracle
 * Date: 2023/8/10 16:47
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
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
	boolean isWrite() default true;

}
