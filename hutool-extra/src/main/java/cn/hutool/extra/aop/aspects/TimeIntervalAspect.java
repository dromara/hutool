package cn.hutool.extra.aop.aspects;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Console;

import java.lang.reflect.Method;

/**
 * 通过日志打印方法的执行时间的切面
 *
 * @author Looly
 */
public class TimeIntervalAspect extends SimpleAspect {
	private static final long serialVersionUID = 1L;

	private final StopWatch interval = new StopWatch();

	@Override
	public boolean before(final Object target, final Method method, final Object[] args) {
		interval.start();
		return true;
	}

	@Override
	public boolean after(final Object target, final Method method, final Object[] args, final Object returnVal) {
		interval.stop();
		Console.log("Method [{}.{}] execute spend [{}]ms return value [{}]",
				target.getClass().getName(), //
				method.getName(), //
				interval.getLastTaskTimeMillis(), //
				returnVal);
		return true;
	}
}
