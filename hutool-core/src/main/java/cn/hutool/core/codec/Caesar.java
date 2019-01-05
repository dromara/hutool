package cn.hutool.core.codec;

/**
 * 凯撒密码实现<br>
 * 算法来自：https://github.com/zhaorenjie110/SymmetricEncryptionAndDecryption
 * 
 * @author looly
 *
 */
public class Caesar {

	// 26个字母表
	public static String table = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";

	/**
	 * 传入明文，加密得到密文
	 *
	 * @param message 加密的消息
	 * @param offset 偏移量
	 * @return 加密后的内容
	 */
	public static String encode(String message, int offset) {
		final int len = message.length();
		final char[] plain = message.toCharArray();
		char c;
		for (int i = 0; i < len; i++) {
			c = message.charAt(i);
			if (false == Character.isLetter(c)) {
				continue;
			}
			plain[i] = encodeChar(c, offset);
		}
		return new String(plain);
	}

	/**
	 * 传入明文解密到密文
	 *
	 * @param ciphertext 密文
	 * @return 解密后的内容
	 */
	public static String decode(String ciphertext, int offset) {
		final int len = ciphertext.length();
		final char[] plain = ciphertext.toCharArray();
		char c;
		for (int i = 0; i < len; i++) {
			c = ciphertext.charAt(i);
			if (false == Character.isLetter(c)) {
				continue;
			}
			plain[i] = decodeChar(c, offset);
		}
		return new String(plain);
	}

	// ----------------------------------------------------------------------------------------- Private method start
	/**
	 * 加密轮盘
	 * 
	 * @param c 被加密字符
	 * @param offset 偏移量
	 * @return 加密后的字符
	 */
	private static char encodeChar(char c, int offset) {
		int position = (table.indexOf(c) + offset) % 52;
		return table.charAt(position);

	}

	/**
	 * 解密轮盘
	 * 
	 * @param c 字符
	 * @return 解密后的字符
	 */
	private static char decodeChar(char c, int offset) {
		int position = (table.indexOf(c) - offset) % 52;
		if (position < 0) {
			position += 52;
		}
		return table.charAt(position);
	}
	// ----------------------------------------------------------------------------------------- Private method end
}
