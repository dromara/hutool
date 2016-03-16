package com.xiaoleilu.hutool.lang;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 不可变数组类型，用于多值返回
 * @author Looly
 *
 */
public class Tuple extends CloneSupport<Tuple> implements Serializable{
	private static final long serialVersionUID = -7689304393482182157L;
	
	private Object[] members;
	
	public Tuple(Object... members) {
		this.members = members;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(int index){
		return (T) members[index];
	}
	
	@Override
	public String toString() {
		return Arrays.toString(members);
	}
}
