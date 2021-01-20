package cn.hutool.crypto.symmetric;

/**
 * 维吉尼亚密码实现。<br>
 * 人们在恺撒移位密码的基础上扩展出多表密码，称为维吉尼亚密码。<br>
 * 算法实现来自：https://github.com/zhaorenjie110/SymmetricEncryptionAndDecryption
 * 
 * @author looly,zhaorenjie110
 * @since 4.4.1
 */
public class Vigenere {

	/**
	 * 加密
	 * 
	 * @param data 数据
	 * @param cipherKey 密钥
	 * @return 密文
	 */
	public static String encrypt(CharSequence data, CharSequence cipherKey) {
		final int dataLen = data.length();
		final int cipherKeyLen = cipherKey.length();

		final char[] cipherArray = new char[dataLen];
		for (int i = 0; i < dataLen / cipherKeyLen + 1; i++) {
			for (int t = 0; t < cipherKeyLen; t++) {
				if (t + i * cipherKeyLen < dataLen) {
					final char dataChar = data.charAt(t + i * cipherKeyLen);
					final char cipherKeyChar = cipherKey.charAt(t);
					cipherArray[t + i * cipherKeyLen] = (char) ((dataChar + cipherKeyChar - 64) % 95 + 32);
				}
			}
		}

		return String.valueOf(cipherArray);
	}

	/**
	 * 解密
	 * 
	 * @param data 密文
	 * @param cipherKey 密钥
	 * @return 明文
	 */
	public static String decrypt(CharSequence data, CharSequence cipherKey) {
		final int dataLen = data.length();
		final int cipherKeyLen = cipherKey.length();

		final char[] clearArray = new char[dataLen];
		for (int i = 0; i < dataLen; i++) {
			for (int t = 0; t < cipherKeyLen; t++) {
				if (t + i * cipherKeyLen < dataLen) {
					final char dataChar = data.charAt(t + i * cipherKeyLen);
					final char cipherKeyChar = cipherKey.charAt(t);
					if (dataChar - cipherKeyChar >= 0) {
						clearArray[t + i * cipherKeyLen] = (char) ((dataChar - cipherKeyChar) % 95 + 32);
					} else {
						clearArray[t + i * cipherKeyLen] = (char) ((dataChar - cipherKeyChar + 95) % 95 + 32);
					}
				}
			}
		}
		return String.valueOf(clearArray);
	}
}
