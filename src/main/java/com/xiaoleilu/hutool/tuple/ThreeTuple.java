/**
 * 
 */
package com.xiaoleilu.hutool.tuple;

import org.apache.commons.lang.ObjectUtils;

/**
 * @author from Internet
 *
 */
public class ThreeTuple<A, B, C> extends TwoTuple<A, B> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5734749947104667495L;
	private final C third;

	public C getThird() {
		return third;
	}

	public ThreeTuple(A a, B b, C c) {
		super(a, b);
		this.third = c;
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(this);
	}

	@Override
	public String toString() {
		return super.toString() + ObjectUtils.toString(getThird());
	}

}
