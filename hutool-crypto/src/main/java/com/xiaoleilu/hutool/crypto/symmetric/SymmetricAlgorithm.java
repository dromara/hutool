package com.xiaoleilu.hutool.crypto.symmetric;

/**
 * 对称算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyGenerator
 * 
 * @author Looly
 *
 */
public enum SymmetricAlgorithm {
	/** 默认的AES加密方式：AES/CBC/PKCS5Padding */
	AES("AES"), 
	ARCFOUR("ARCFOUR"), 
	Blowfish("Blowfish"), 
	/** 默认的DES加密方式：DES/ECB/PKCS5Padding */
	DES("DES"), 
	/** 3DES算法 */
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