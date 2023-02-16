package cn.hutool.core.bean.annotations;

import cn.hutool.core.annotation.MirrorFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被该注解标识的字段不可为空，请在包装类型上使用该注解
 * @author 晓龙
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldNotNull {

	/**
	 * 字段为空时的异常信息
	 */
	@MirrorFor(attribute = "value")
	String message() default "";

	@MirrorFor(attribute = "message")
	String value() default "";


}
