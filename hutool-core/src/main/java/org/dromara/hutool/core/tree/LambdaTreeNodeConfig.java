package org.dromara.hutool.core.tree;

import org.dromara.hutool.core.func.LambdaUtil;
import org.dromara.hutool.core.func.SerFunction;

import java.util.List;
import java.util.Objects;

/**
 * 树配置属性相关（使用Lambda语法）
 * 避免对字段名称硬编码
 *
 * @author Earlman
 */
public class LambdaTreeNodeConfig<R, T> extends TreeNodeConfig {
	private SerFunction<R, T> idKeyFun;

	private SerFunction<R, T> parentIdKeyFun;

	private SerFunction<R, Comparable<?>> weightKeyFun;

	private SerFunction<R, CharSequence> nameKeyFun;

	private SerFunction<R, List<R>> childrenKeyFun;

	public SerFunction<R, T> getIdKeyFun() {
		return idKeyFun;
	}

	public void setIdKeyFun(SerFunction<R, T> idKeyFun) {
		this.idKeyFun = idKeyFun;
	}

	public SerFunction<R, T> getParentIdKeyFun() {
		return parentIdKeyFun;
	}

	public void setParentIdKeyFun(SerFunction<R, T> parentIdKeyFun) {
		this.parentIdKeyFun = parentIdKeyFun;
	}

	public SerFunction<R, Comparable<?>> getWeightKeyFun() {
		return weightKeyFun;
	}

	public void setWeightKeyFun(SerFunction<R, Comparable<?>> weightKeyFun) {
		this.weightKeyFun = weightKeyFun;
	}

	public SerFunction<R, CharSequence> getNameKeyFun() {
		return nameKeyFun;
	}

	public void setNameKeyFun(SerFunction<R, CharSequence> nameKeyFun) {
		this.nameKeyFun = nameKeyFun;
	}

	public SerFunction<R, List<R>> getChildrenKeyFun() {
		return childrenKeyFun;
	}

	public void setChildrenKeyFun(SerFunction<R, List<R>> childrenKeyFun) {
		this.childrenKeyFun = childrenKeyFun;
	}

	@Override
	public String getIdKey() {
		SerFunction<?, ?> serFunction = getIdKeyFun();
		if (Objects.isNull(serFunction)) {
			return super.getIdKey();
		}
		return LambdaUtil.getFieldName(serFunction);
	}

	@Override
	public String getParentIdKey() {
		SerFunction<?, ?> serFunction = getParentIdKeyFun();
		if (Objects.isNull(serFunction)) {
			return super.getParentIdKey();
		}
		return LambdaUtil.getFieldName(serFunction);
	}

	@Override
	public String getWeightKey() {
		SerFunction<?, ?> serFunction = getWeightKeyFun();
		if (Objects.isNull(serFunction)) {
			return super.getWeightKey();
		}
		return LambdaUtil.getFieldName(serFunction);
	}

	@Override
	public String getNameKey() {
		SerFunction<?, ?> serFunction = getNameKeyFun();
		if (Objects.isNull(serFunction)) {
			return super.getNameKey();
		}
		return LambdaUtil.getFieldName(serFunction);
	}

	@Override
	public String getChildrenKey() {
		SerFunction<?, ?> serFunction = getChildrenKeyFun();
		if (Objects.isNull(serFunction)) {
			return super.getChildrenKey();
		}
		return LambdaUtil.getFieldName(serFunction);
	}
}
