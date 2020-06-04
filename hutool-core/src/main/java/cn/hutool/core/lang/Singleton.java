package cn.hutool.core.lang;

import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.HashMap;

/**
 * 单例类<br>
 * 提供单例对象的统一管理，当调用get方法时，如果对象池中存在此对象，返回此对象，否则创建新对象返回<br>
 *
 * @author loolly
 */
public final class Singleton {

	private static final SimpleCache<String, Object> POOL = new SimpleCache<>(new HashMap<>());

	private Singleton() {
	}

	/**
	 * 获得指定类的单例对象<br>
	 * 对象存在于池中返回，否则创建，每次调用此方法获得的对象为同一个对象<br>
	 * 注意：单例针对的是类和参数，也就是说只有类、参数一致才会返回同一个对象
	 *
	 * @param <T>    单例对象类型
	 * @param clazz  类
	 * @param params 构造方法参数
	 * @return 单例对象
	 */
	public static <T> T get(Class<T> clazz, Object... params) {
		Assert.notNull(clazz, "Class must be not null !");
		final String key = buildKey(clazz.getName(), params);
		return get(key, () -> ReflectUtil.newInstance(clazz, params));
	}

	/**
	 * 获得指定类的单例对象<br>
	 * 对象存在于池中返回，否则创建，每次调用此方法获得的对象为同一个对象<br>
	 * 注意：单例针对的是类和参数，也就是说只有类、参数一致才会返回同一个对象
	 *
	 * @param <T>      单例对象类型
	 * @param key      自定义键
	 * @param supplier 单例对象的创建函数
	 * @return 单例对象
	 * @since 5.3.3
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String key, Func0<T> supplier) {
		return (T) POOL.get(key, supplier::call);
	}

	/**
	 * 获得指定类的单例对象<br>
	 * 对象存在于池中返回，否则创建，每次调用此方法获得的对象为同一个对象<br>
	 *
	 * @param <T>       单例对象类型
	 * @param className 类名
	 * @param params    构造参数
	 * @return 单例对象
	 */
	public static <T> T get(String className, Object... params) {
		Assert.notBlank(className, "Class name must be not blank !");
		final Class<T> clazz = ClassUtil.loadClass(className);
		return get(clazz, params);
	}

	/**
	 * 将已有对象放入单例中，其Class做为键
	 *
	 * @param obj 对象
	 * @since 4.0.7
	 */
	public static void put(Object obj) {
		Assert.notNull(obj, "Bean object must be not null !");
		put(obj.getClass().getName(), obj);
	}

	/**
	 * 将已有对象放入单例中，其Class做为键
	 *
	 * @param key 键
	 * @param obj 对象
	 * @since 5.3.3
	 */
	public static void put(String key, Object obj) {
		POOL.put(key, obj);
	}

	/**
	 * 移除指定Singleton对象
	 *
	 * @param clazz 类
	 */
	public static void remove(Class<?> clazz) {
		if (null != clazz) {
			remove(clazz.getName());
		}
	}

	/**
	 * 移除指定Singleton对象
	 *
	 * @param key 键
	 */
	public static void remove(String key) {
		POOL.remove(key);
	}

	/**
	 * 清除所有Singleton对象
	 */
	public static void destroy() {
		POOL.clear();
	}

	// ------------------------------------------------------------------------------------------- Private method start

	/**
	 * 构建key
	 *
	 * @param className 类名
	 * @param params    参数列表
	 * @return key
	 */
	private static String buildKey(String className, Object... params) {
		if (ArrayUtil.isEmpty(params)) {
			return className;
		}
		return StrUtil.format("{}#{}", className, ArrayUtil.join(params, "_"));
	}
	// ------------------------------------------------------------------------------------------- Private method end
}
