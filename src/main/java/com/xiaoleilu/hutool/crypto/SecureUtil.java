package com.xiaoleilu.hutool.crypto;

import java.util.UUID;

import com.xiaoleilu.hutool.crypto.Digester.DigestAlgorithm;
import com.xiaoleilu.hutool.crypto.SymmetricCriptor.SymmetricAlgorithm;

/**
 * 安全相关工具类<br>
 * 加密分为三种：<br>
 * 1、对称加密（symmetric）<br>
 * 2、非对称加密（asymmetric）<br>
 * 3、摘要加密（digest）<br>
 * 
 * @author xiaoleilu
 *
 */
public class SecureUtil {

	// ------------------------------------------------------------------- 对称加密算法

	/**
	 * AES加密
	 * 
	 * @return {@link SymmetricCriptor}
	 */
	public static SymmetricCriptor aes() {
		return new SymmetricCriptor(SymmetricAlgorithm.AES);
	}

	/**
	 * AES加密
	 * 
	 * @param key 密钥
	 * @return {@link SymmetricCriptor}
	 */
	public static SymmetricCriptor aes(byte[] key) {
		return new SymmetricCriptor(SymmetricAlgorithm.AES, key);
	}

	// ------------------------------------------------------------------- 摘要算法
	/**
	 * MD5加密
	 * 
	 * @return {@link Digester}
	 */
	public static Digester md5() {
		return new Digester(DigestAlgorithm.MD5);
	}

	/**
	 * SHA1加密
	 * 
	 * @return {@link Digester}
	 */
	public static Digester sha1() {
		return new Digester(DigestAlgorithm.SHA1);
	}

	// ------------------------------------------------------------------- UUID
	/**
	 * @return 简化的UUID，去掉了横线
	 */
	public static String simpleUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
