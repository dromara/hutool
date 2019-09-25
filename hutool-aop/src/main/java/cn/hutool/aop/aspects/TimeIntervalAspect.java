package cn.hutool.aop.aspects;

import java.lang.reflect.Method;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;

/**
 * 通过日志打印方法的执行时间的切面
 * @author Looly
 *
 */
public class TimeIntervalAspect extends SimpleAspect {
    private static final long serialVersionUID = 1L;

    private TimeInterval      interval         = new TimeInterval();

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        interval.start();
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        Console.log("Method [{}.{}] execute spend [{}]ms return value [{}]", target.getClass().getName(), method.getName(), interval.intervalMs(), StrUtil.toString(returnVal));
        return true;
    }
}
