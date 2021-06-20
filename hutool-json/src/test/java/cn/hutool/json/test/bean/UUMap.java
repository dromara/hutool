package cn.hutool.json.test.bean;

import java.io.Serializable;

public class UUMap<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private T result;

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
