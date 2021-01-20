package cn.hutool.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 别名注解，使用此注解的字段、方法、参数等会有一个别名，用于Bean拷贝、Bean转Map等
 *
 * @author Looly
 * @since 5.1.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Alias {

	/**
	 * 别名值，即使用此注解要替换成的别名名称
	 *
	 * @return 别名值
	 */
	String value();
}
