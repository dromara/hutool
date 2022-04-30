package cn.hutool.extra.aop.aspects;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 简单切面类，不做任何操作<br>
 * 可以继承此类实现自己需要的方法即可
 *
 * @author Looly, ted.L
 */
public class SimpleAspect implements Aspect, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean before(final Object target, final Method method, final Object[] args) {
		//继承此类后实现此方法
		return true;
	}

	@Override
	public boolean after(final Object target, final Method method, final Object[] args, final Object returnVal) {
		//继承此类后实现此方法
		return true;
	}

	@Override
	public boolean afterException(final Object target, final Method method, final Object[] args, final Throwable e) {
		//继承此类后实现此方法
		return true;
	}

}
