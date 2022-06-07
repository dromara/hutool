package cn.hutool.core.bean.copier;

import cn.hutool.core.lang.copier.Copier;
import cn.hutool.core.util.ObjUtil;

/**
 * 抽象的对象拷贝封装，提供来源对象、目标对象持有
 *
 * @param <S> 来源对象类型
 * @param <T> 目标对象类型
 * @author looly
 * @since 5.8.0
 */
public abstract class AbsCopier<S, T> implements Copier<T> {

	protected final S source;
	protected final T target;
	/**
	 * 拷贝选项
	 */
	protected final CopyOptions copyOptions;

	public AbsCopier(final S source, final T target, final CopyOptions copyOptions) {
		this.source = source;
		this.target = target;
		this.copyOptions = ObjUtil.defaultIfNull(copyOptions, CopyOptions::of);
	}
}
