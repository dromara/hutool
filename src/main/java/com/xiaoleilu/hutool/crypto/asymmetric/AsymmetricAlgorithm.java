package com.xiaoleilu.hutool.crypto.asymmetric;

/**
 * 非对称算法类型<br>
 * 
 * @author Looly
 *
 */
public enum AsymmetricAlgorithm {
	RSA("RSA"), DSA("DSA");

	private String value;

	private AsymmetricAlgorithm(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
