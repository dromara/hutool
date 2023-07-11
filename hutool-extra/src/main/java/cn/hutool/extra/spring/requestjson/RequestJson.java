package cn.hutool.extra.spring.requestjson;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 将json转换为JsonNode类
 * Created by useheart on 2023/7/11
 * @author useheart
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJson {
	@AliasFor("name")
	String value() default "";

	@AliasFor("value")
	String name() default "";

	boolean required() default true;
}
