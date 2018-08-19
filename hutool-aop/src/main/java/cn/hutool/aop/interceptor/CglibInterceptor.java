package cn.hutool.aop.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.core.exceptions.UtilException;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibInterceptor implements MethodInterceptor{
	private Object target;
	private Aspect aspect;
	
	/**
	 * 构造
	 * 
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 */
	public CglibInterceptor(Object target, Aspect aspect) {
		this.target = target;
		this.aspect = aspect;
	}
	
	public Object getTarget(){
		return this.target;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object result = null;
		if(aspect.before(target, method, args)){
			try {
//				result = ReflectUtil.invoke(target, method, args);
				result = proxy.invokeSuper(obj, args);
			}catch (UtilException e) {
				final Throwable cause = e.getCause();
				if(e.getCause() instanceof InvocationTargetException) {
					aspect.afterException(target, method, args, ((InvocationTargetException)cause).getTargetException());
				}else {
					throw e;//其它异常属于代理的异常，直接抛出
				}
			}
		}
		if(aspect.after(target, method, args)){
			return result;
		}
		return null;
	}
}
