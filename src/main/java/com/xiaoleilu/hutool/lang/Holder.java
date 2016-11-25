package com.xiaoleilu.hutool.lang;

import java.io.Serializable;

/**
 * 为不可变的对象引用提供一个可变的包装，在java中支持引用传递。
 * @author Looly
 *
 * @param <T> 所持有值类型
 */
public final class Holder<T> implements Serializable{
	private static final long serialVersionUID = 861411261825135385L;
	
	public T value;
	
	/**
	 * 新建Holder类，持有指定值，当值为空时抛出空指针异常
	 * @param value 值，不能为空
	 * @return Holder
	 */
	public static <T> Holder<T> of(T value) throws NullPointerException{
		if(null == value){
			throw new NullPointerException("Holder can not hold a null value!");
		}
		return new Holder<>(value);
	}
	
	//--------------------------------------------------------------------------- Constructor start
	public Holder() {
	}
	
	public Holder(T value) {
		this.value = value;
	}
	//--------------------------------------------------------------------------- Constructor end
}
