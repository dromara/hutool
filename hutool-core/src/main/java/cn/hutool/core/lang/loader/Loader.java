package cn.hutool.core.lang.loader;

/**
 * 对象加载抽象接口<br>
 * 通过实现此接口自定义实现对象的加载方式，例如懒加载机制、多线程加载等
 *
 * @author looly
 *
 * @param <T> 对象类型
 */
@FunctionalInterface
public interface Loader<T> {

	/**
	 * 获取一个准备好的对象<br>
	 * 通过准备逻辑准备好被加载的对象，然后返回。在准备完毕之前此方法应该被阻塞
	 *
	 * @return 加载完毕的对象
	 */
	T get();
}
