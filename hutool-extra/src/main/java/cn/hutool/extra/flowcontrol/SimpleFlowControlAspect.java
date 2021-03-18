package cn.hutool.extra.flowcontrol;

import cn.hutool.core.annotation.AnnotationUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流注解切面
 * 自动装配 spring提供类似spi机制的扩展 META-INF/spring.factories
 * @author WangChen
 **/
@Aspect
public class SimpleFlowControlAspect {

    private static final Map<Method, FlowControlNode> METHOD_FLOW_CONTROL_NODE_MAP = new ConcurrentHashMap<>();

    @Before("@annotation(cn.hutool.extra.flowcontrol.SimpleFlowControl)")
    public void resolveFlowControl(JoinPoint joinPoint){

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        final Method method = methodSignature.getMethod();

        METHOD_FLOW_CONTROL_NODE_MAP.computeIfAbsent(method, key ->
                FlowControlNode.create(AnnotationUtil.getAnnotation(method, SimpleFlowControl.class), method)).tryToPass();

    }
}
