package cn.hutool.crypto.asymmetric;

import javax.crypto.Cipher;

/**
 * 密钥类型
 * 
 * @author Looly
 *
 */
public enum KeyType {
	/**
	 * 公钥
	 */
	PublicKey(Cipher.PUBLIC_KEY),
	/**
	 * 私钥
	 */
	PrivateKey(Cipher.PRIVATE_KEY),
	/**
	 * 密钥
	 */
	SecretKey(Cipher.SECRET_KEY);


	/**
	 * 构造
	 *
	 * @param value 见{@link Cipher}
	 */
	KeyType(int value) {
		this.value = value;
	}

	private final int value;

	/**
	 * 获取枚举值对应的int表示
	 *
	 * @return 枚举值对应的int表示
	 */
	public int getValue() {
		return this.value;
	}
}