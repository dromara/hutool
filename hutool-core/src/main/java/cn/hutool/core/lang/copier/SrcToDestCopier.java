package cn.hutool.core.lang.copier;

import java.io.Serializable;

import cn.hutool.core.lang.Filter;

/**
 * 复制器抽象类<br>
 * 抽象复制器抽象了一个对象复制到另一个对象，通过实现{@link #copy()}方法实现复制逻辑。<br>
 *
 * @author Looly
 *
 * @param <T> 拷贝的对象
 * @param <C> 本类的类型。用于set方法返回本对象，方便流式编程
 * @since 3.0.9
 */
public abstract class SrcToDestCopier<T, C extends SrcToDestCopier<T, C>> implements Copier<T>, Serializable{
	private static final long serialVersionUID = 1L;

	/** 源 */
	protected T src;
	/** 目标 */
	protected T dest;
	/** 拷贝过滤器，可以过滤掉不需要拷贝的源 */
	protected Filter<T> copyFilter;

	//-------------------------------------------------------------------------------------------------------- Getters and Setters start
	/**
	 * 获取源
	 * @return 源
	 */
	public T getSrc() {
		return src;
	}
	/**
	 * 设置源
	 *
	 * @param src 源
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public C setSrc(T src) {
		this.src = src;
		return (C)this;
	}

	/**
	 * 获得目标
	 *
	 * @return 目标
	 */
	public T getDest() {
		return dest;
	}
	/**
	 * 设置目标
	 *
	 * @param dest 目标
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public C setDest(T dest) {
		this.dest = dest;
		return (C)this;
	}

	/**
	 * 获得过滤器
	 * @return 过滤器
	 */
	public Filter<T> getCopyFilter() {
		return copyFilter;
	}
	/**
	 * 设置过滤器
	 *
	 * @param copyFilter 过滤器
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public C setCopyFilter(Filter<T> copyFilter) {
		this.copyFilter = copyFilter;
		return (C)this;
	}
	//-------------------------------------------------------------------------------------------------------- Getters and Setters end
}
