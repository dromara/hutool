/**
 * 
 */
package com.xiaoleilu.hutool.tuple;

import java.io.Serializable;

import org.apache.commons.lang.ObjectUtils;

/**
 * java 元组，用于多对象返回。
 * @author from Internet
 *
 */
public class TwoTuple<A, B> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6940274490652561610L;
	/**
	 * 第一个对象
	 */
	private final A first;
	/**
	 * 第二个对象
	 */
	private final B second;

	public TwoTuple(A a, B b) {
		this.first = a;
		this.second = b;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(this);
	}

	@Override
	public String toString() {
		return ObjectUtils.toString(getFirst()) + ObjectUtils.toString(getSecond());
	}

}