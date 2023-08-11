package cn.hutool.extra.redisson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

/**
 * Author: miracle
 * Date: 2023/8/10 16:47
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {

	LockKey key();

	TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

	long waitTime() default 3000;

	String errorCode() default "redisson_lock_error";

}
