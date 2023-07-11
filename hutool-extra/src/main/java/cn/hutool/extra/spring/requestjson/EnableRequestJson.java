package cn.hutool.extra.spring.requestjson;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启对应@RequestJson和@RequestJsonParam注解
 * Created by useheart on 2023/7/11
 * @author useheart
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RequestJsonAutoConfiguration.class)
public @interface EnableRequestJson {
}
