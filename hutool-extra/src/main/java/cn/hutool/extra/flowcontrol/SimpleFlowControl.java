package cn.hutool.extra.flowcontrol;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解</br>
 *
 * @see cn.hutool.extra.flowcontrol.SimpleFlowControlAspect
 * @see FlowControlNode
 * @author WangChen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface SimpleFlowControl {

    int DEFAULT_REQUEST = 0;
    int DEFAULT_PERIOD = 1;

    /**
     * 时间间隔内最大通过的请求数
     * 默认为0 不拦截请求
     * @see FlowControlNode#tryToPass()
     */
    int limit() default DEFAULT_REQUEST;

    /**
     * 时间间隔
     * 默认为1秒
     */
    int period() default DEFAULT_PERIOD;

    /**
     * 时间单位
     * 默认为秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
