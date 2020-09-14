package cn.hutool.crypto;

import javax.crypto.Cipher;

/**
 * Cipher模式的枚举封装
 *
 * @author looly
 * @since 5.4.3
 */
public enum CipherMode {
	/**
	 * 加密模式
	 */
	encrypt(Cipher.ENCRYPT_MODE),
	/**
	 * 解密模式
	 */
	decrypt(Cipher.DECRYPT_MODE),
	/**
	 * 包装模式
	 */
	wrap(Cipher.WRAP_MODE),
	/**
	 * 拆包模式
	 */
	unwrap(Cipher.UNWRAP_MODE);


	/**
	 * 构造
	 *
	 * @param value 见{@link Cipher}
	 */
	CipherMode(int value) {
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
