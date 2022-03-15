package cn.hutool.db;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;

/**
 * 支持lambda的Entity
 *
 * @author VampireAchao
 */
public class LambdaEntity<T> extends ActiveEntity {

	public LambdaEntity(T entity) {
		super(parse(entity));
	}

	@SuppressWarnings("unchecked")
	public <R> R get(Func1<T, R> field) {
		return (R) super.get(LambdaUtil.getFieldName(field));
	}

	@SuppressWarnings("unchecked")
	public LambdaEntity<T> set(Func1<T, ?> field, Object value) {
		return (LambdaEntity<T>) super.set(LambdaUtil.getFieldName(field), value);
	}

	@SuppressWarnings("unchecked")
	public LambdaEntity<T> setIgnoreNull(Func1<T, ?> field, Object value) {
		if (null != field && null != value) {
			return (LambdaEntity<T>) set(LambdaUtil.getFieldName(field), value);
		}
		return this;
	}

}
