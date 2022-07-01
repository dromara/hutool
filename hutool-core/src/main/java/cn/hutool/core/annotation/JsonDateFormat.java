package cn.hutool.core.annotation;

import java.lang.annotation.*;

/**
 * bean时间格式化注解
 *
 *
 * @author duhanmin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface JsonDateFormat {

	/**
	 * 时间格式化
	 *
	 * @return 时间格式化
	 */
	String value();
}
