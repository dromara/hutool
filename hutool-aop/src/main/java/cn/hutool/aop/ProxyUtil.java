package cn.hutool.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.aop.proxy.ProxyFactory;
import cn.hutool.core.util.ClassUtil;

/**
 * 代理工具类
 * @author Looly
 *
 */
public final class ProxyUtil {

	/**
	 * 使用切面代理对象
	 *
	 * @param <T> 切面对象类型
	 * @param target 目标对象
	 * @param aspectClass 切面对象类
	 * @return 代理对象
	 */
	public static <T> T proxy(T target, Class<? extends Aspect> aspectClass){
		return ProxyFactory.createProxy(target, aspectClass);
	}

	/**
	 * 使用切面代理对象
	 *
	 * @param <T> 被代理对象类型
	 * @param target 被代理对象
	 * @param aspect 切面对象
	 * @return 代理对象
	 */
	public static <T> T proxy(T target, Aspect aspect){
		return ProxyFactory.createProxy(target, aspect);
	}

	/**
	 * 创建动态代理对象<br>
	 * 动态代理对象的创建原理是：<br>
	 * 假设创建的代理对象名为 $Proxy0<br>
	 * 1、根据传入的interfaces动态生成一个类，实现interfaces中的接口<br>
	 * 2、通过传入的classloder将刚生成的类加载到jvm中。即将$Proxy0类load<br>
	 * 3、调用$Proxy0的$Proxy0(InvocationHandler)构造函数 创建$Proxy0的对象，并且用interfaces参数遍历其所有接口的方法，这些实现方法的实现本质上是通过反射调用被代理对象的方法<br>
	 * 4、将$Proxy0的实例返回给客户端。 <br>
	 * 5、当调用代理类的相应方法时，相当于调用 {@link InvocationHandler#invoke(Object, java.lang.reflect.Method, Object[])} 方法
	 *
	 *
	 * @param <T> 被代理对象类型
	 * @param classloader 被代理类对应的ClassLoader
	 * @param invocationHandler {@link InvocationHandler} ，被代理类通过实现此接口提供动态代理功能
	 * @param interfaces 代理类中需要实现的被代理类的接口方法
	 * @return 代理类
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newProxyInstance(ClassLoader classloader, InvocationHandler invocationHandler, Class<?>... interfaces) {
		return (T) Proxy.newProxyInstance(classloader, interfaces, invocationHandler);
	}

	/**
	 * 创建动态代理对象
	 *
	 * @param <T> 被代理对象类型
	 * @param invocationHandler {@link InvocationHandler} ，被代理类通过实现此接口提供动态代理功能
	 * @param interfaces 代理类中需要实现的被代理类的接口方法
	 * @return 代理类
	 */
	public static <T> T newProxyInstance(InvocationHandler invocationHandler, Class<?>... interfaces) {
		return newProxyInstance(ClassUtil.getClassLoader(), invocationHandler, interfaces);
	}
}
