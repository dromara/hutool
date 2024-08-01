/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

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
 * @param <T> 方法对象类型
 * @param <R> 返回值类型
 */
public class LambdaTreeNodeConfig<T, R> extends TreeNodeConfig {
	private static final long serialVersionUID = 1L;

	private SerFunction<T, R> idKeyFun;
	private SerFunction<T, R> parentIdKeyFun;
	private SerFunction<T, Comparable<?>> weightKeyFun;
	private SerFunction<T, CharSequence> nameKeyFun;
	private SerFunction<T, List<T>> childrenKeyFun;

	/**
	 * 获取ID方法
	 * @return ID方法
	 */
	public SerFunction<T, R> getIdKeyFun() {
		return idKeyFun;
	}

	/**
	 * 设置ID方法
	 * @param idKeyFun ID方法
	 * @return this
	 */
	public LambdaTreeNodeConfig<T, R> setIdKeyFun(final SerFunction<T, R> idKeyFun) {
		this.idKeyFun = idKeyFun;
		return this;
	}

	/**
	 * 获取父ID方法
	 * @return 父ID方法
	 */
	public SerFunction<T, R> getParentIdKeyFun() {
		return parentIdKeyFun;
	}

	/**
	 * 设置父ID方法
	 * @param parentIdKeyFun 父ID方法
	 * @return this
	 */
	public LambdaTreeNodeConfig<T, R> setParentIdKeyFun(final SerFunction<T, R> parentIdKeyFun) {
		this.parentIdKeyFun = parentIdKeyFun;
		return this;
	}

	/**
	 * 设置权重方法
	 * @return 权重方法
	 */
	public SerFunction<T, Comparable<?>> getWeightKeyFun() {
		return weightKeyFun;
	}

	/**
	 * 设置权重方法
	 * @param weightKeyFun 权重方法
	 * @return this
	 */
	public LambdaTreeNodeConfig<T, R> setWeightKeyFun(final SerFunction<T, Comparable<?>> weightKeyFun) {
		this.weightKeyFun = weightKeyFun;
		return this;
	}

	/**
	 * 获取节点名称方法
	 * @return 节点名称方法
	 */
	public SerFunction<T, CharSequence> getNameKeyFun() {
		return nameKeyFun;
	}

	/**
	 * 设置节点名称方法
	 * @param nameKeyFun 节点名称方法
	 * @return this
	 */
	public LambdaTreeNodeConfig<T, R> setNameKeyFun(final SerFunction<T, CharSequence> nameKeyFun) {
		this.nameKeyFun = nameKeyFun;
		return this;
	}

	/**
	 * 获取子节点名称方法
	 * @return 子节点名称方法
	 */
	public SerFunction<T, List<T>> getChildrenKeyFun() {
		return childrenKeyFun;
	}

	/**
	 * 设置子节点名称方法
	 * @param childrenKeyFun 子节点名称方法
	 * @return this
	 */
	public LambdaTreeNodeConfig<T, R> setChildrenKeyFun(final SerFunction<T, List<T>> childrenKeyFun) {
		this.childrenKeyFun = childrenKeyFun;
		return this;
	}

	@Override
	public String getIdKey() {
		final SerFunction<?, ?> serFunction = getIdKeyFun();
		if (Objects.isNull(serFunction)) {
			return super.getIdKey();
		}
		return LambdaUtil.getFieldName(serFunction);
	}

	@Override
	public String getParentIdKey() {
		final SerFunction<?, ?> serFunction = getParentIdKeyFun();
		if (Objects.isNull(serFunction)) {
			return super.getParentIdKey();
		}
		return LambdaUtil.getFieldName(serFunction);
	}

	@Override
	public String getWeightKey() {
		final SerFunction<?, ?> serFunction = getWeightKeyFun();
		if (Objects.isNull(serFunction)) {
			return super.getWeightKey();
		}
		return LambdaUtil.getFieldName(serFunction);
	}

	@Override
	public String getNameKey() {
		final SerFunction<?, ?> serFunction = getNameKeyFun();
		if (Objects.isNull(serFunction)) {
			return super.getNameKey();
		}
		return LambdaUtil.getFieldName(serFunction);
	}

	@Override
	public String getChildrenKey() {
		final SerFunction<?, ?> serFunction = getChildrenKeyFun();
		if (Objects.isNull(serFunction)) {
			return super.getChildrenKey();
		}
		return LambdaUtil.getFieldName(serFunction);
	}
}
