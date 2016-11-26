package com.xiaoleilu.hutool.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.xiaoleilu.hutool.util.ClassUtil;

/**
 * 切面抽象
 * @author Looly
 *
 */
public abstract class Aspect implements InvocationHandler{
	
	private Object target;
	
	public Aspect(Object target) {
		this.target = target;
	}
	
	public Object getTarget(){
		return this.target;
	}
	
	/**
	 * 目标方法执行前的操作
	 * @param target 目标对象
	 * @param method 目标方法
	 * @param args 参数
	 * @return 是否继续执行接下来的操作
	 */
	public abstract boolean before(Object target, Method method, Object[] args);
	
	/**
	 * 目标方法执行后的操作
	 * @param target 目标对象
	 * @param method 目标方法
	 * @param args 参数
	 * @return 是否允许返回值（接下来的操作）
	 */
	public abstract boolean after(Object target, Method method, Object[] args);
	
	/**
	 * 目标方法抛出异常时的操作
	 * @param target 目标对象
	 * @param method 目标方法
	 * @param args 参数
	 * @param e 异常
	 * @return 是否允许抛出异常
	 */
	public abstract boolean afterException(Object target, Method method, Object[] args, Throwable e);
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		if(before(target, method, args)){
			try {
				result = ClassUtil.invoke(target, method, args);
			}catch (InvocationTargetException e) {
				afterException(args, method, args, e.getTargetException());
			}catch (Exception e) {
				throw e;//其它异常属于代理的异常，直接抛出
			}
		}
		if(after(target, method, args)){
			return result;
		}
		return null;
	}
	
}
