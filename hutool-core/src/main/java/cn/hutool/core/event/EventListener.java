package cn.hutool.core.event;

import java.lang.annotation.*;

/**
 * 监听器注解，被注解标注的方法会通过动态代理成为{@link Listener}的实现类<br>
 * 仅可标注在方法上
 *
 * @author Create by liuwenhao on 2022/7/23 13:03
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventListener {


	/**
	 * 需要监听的事件类型。<br>
	 * 如果是参数化类型则代表类型的RawType部分
	 *
	 * @return 类型
	 */
	Class<?> value();

	/**
	 * 参数化类型中对应的Arguments部分。<br>
	 * 如果针对一个无法转化成参数化类型的value，设置了arguments，则会抛出异常<br>
	 * 如果参数化类型参数的数量不一致，则会抛出异常
	 *
	 * @return 泛型列表
	 */
	Class<?>[] arguments() default {};

	/**
	 * 是否异步执行。默认否
	 *
	 * @return 是否异步执行
	 */
	boolean isAsync() default false;

	/**
	 * 执行传播模式，默认{@link EdgeSpreadPattern}
	 *
	 * @return 传播模式类型
	 */
	Class<?> spread() default EdgeSpreadPattern.class;

	/**
	 * 监听器优先度。order越大，优先度越低，默认最高优先级
	 *
	 * @return 优先级
	 */
	int order() default Integer.MIN_VALUE;

	/**
	 * 异常处理，选取注解标注方法所在类中的对应方法名的方法作为异常处理方法，不考虑访问权限。<br>
	 * 异常处理方法的入参应该为{@link Throwable}类型，不限制返回值，如果存在多个重载方法，则选取入参为{@link Throwable}类型的，
	 * 如果不存在符合条件的方法则使用默认异常处理机制<br>
	 * 方法示例：
	 * <pre>
	 *     public String throwHandler(Throwable throwable){
	 * 		log.error(throwable.getMessage);
	 * 		return "异常处理完成";
	 *     }
	 * </pre>
	 *
	 * @return 方法名
	 */
	String throwHandler() default "";

}
