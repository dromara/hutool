package cn.hutool.json.test.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class UUMap<T> implements Serializable{
	private static final long serialVersionUID = 1L;

	private T result;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
