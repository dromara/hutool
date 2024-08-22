/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.crypto.symmetric;

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
	public static String encrypt(final CharSequence data, final CharSequence cipherKey) {
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
	public static String decrypt(final CharSequence data, final CharSequence cipherKey) {
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
