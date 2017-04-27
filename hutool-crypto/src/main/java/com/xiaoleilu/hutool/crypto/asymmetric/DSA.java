package com.xiaoleilu.hutool.crypto.asymmetric;

/**
 * DSA加密算法
 * 
 * @author Looly
 *
 */
public class DSA extends AsymmetricCriptor {

	private static final AsymmetricAlgorithm ALGORITHM_DSA = AsymmetricAlgorithm.DSA;

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造，创建新的私钥公钥对
	 */
	public DSA() {
		super(ALGORITHM_DSA);
	}
	
	/**
	 * 构造
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKeyBase64 私钥Base64
	 * @param publicKeyBase64 公钥Base64
	 */
	public DSA(String privateKeyBase64, String publicKeyBase64) {
		super(ALGORITHM_DSA, privateKeyBase64, publicKeyBase64);
	}

	/**
	 * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 * 
	 * @param privateKey 私钥
	 * @param publicKey 公钥
	 */
	public DSA(byte[] privateKey, byte[] publicKey) {
		super(ALGORITHM_DSA, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end
}
