package com.xiaoleilu.hutool.crypto.symmetric;

/**
 * 对称算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyGenerator
 * 
 * @author Looly
 *
 */
public enum SymmetricAlgorithm {
	AES("AES"), 
	ARCFOUR("ARCFOUR"), 
	Blowfish("Blowfish"), 
	DES("DES"), 
	DESede("DESede"), 
	RC2("RC2"),

	PBEWithMD5AndDES("PBEWithMD5AndDES"), 
	PBEWithSHA1AndDESede("PBEWithSHA1AndDESede"), 
	PBEWithSHA1AndRC2_40("PBEWithSHA1AndRC2_40");

	private String value;

	private SymmetricAlgorithm(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}