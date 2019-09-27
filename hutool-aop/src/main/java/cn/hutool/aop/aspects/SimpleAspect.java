package cn.hutool.aop.aspects;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 简单切面类，不做任何操作<br>
 * 可以继承此类实现自己需要的方法即可
 * 
 * @author Looly
 * @author ted.L
 *
 */
public abstract class SimpleAspect implements Aspect, Serializable{
	private static final long serialVersionUID = 1L;

    /**
     * @see Aspect#before(Object, Method, Object[])
     * @return 是否继续执行接下来的操作 默认值true
     */
	@Override
	public boolean before(Object target, Method method, Object[] args) {
		//继承此类后实现此方法
		return true;
	}

    /**
     * @see Aspect#after(Object, Method, Object[], Object)
     * @return 是否允许返回值（接下来的操作） 默认值true
     */
	@Override
	public boolean after(Object target, Method method, Object[] args, Object returnVal) {
		//继承此类后实现此方法
		return true;
	}

    /**
     * @see Aspect#afterException(Object, Method, Object[], Throwable)
     * @return 是否允许抛出异常 默认值true
     */
	@Override
	public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
		//继承此类后实现此方法
		return true;
	}

}
