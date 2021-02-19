package cn.hutool.crypto.symmetric;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;

/**
 * DES加密算法实现<br>
 * DES全称为Data Encryption Standard，即数据加密标准，是一种使用密钥加密的块算法<br>
 * Java中默认实现为：DES/CBC/PKCS5Padding
 * 
 * @author Looly
 * @since 3.0.8
 */
public class DES extends SymmetricCrypto {
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------- Constrctor start
	/**
	 * 构造，默认DES/CBC/PKCS5Padding，使用随机密钥
	 */
	public DES() {
		super(SymmetricAlgorithm.DES);
	}

	/**
	 * 构造，使用默认的DES/CBC/PKCS5Padding
	 * 
	 * @param key 密钥
	 */
	public DES(byte[] key) {
		super(SymmetricAlgorithm.DES, key);
	}

	/**
	 * 构造，使用随机密钥
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public DES(Mode mode, Padding padding) {
		this(mode.name(), padding.name());
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度：8的倍数
	 */
	public DES(Mode mode, Padding padding, byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度：8的倍数
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public DES(Mode mode, Padding padding, byte[] key, byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度：8的倍数
	 * @since 3.3.0
	 */
	public DES(Mode mode, Padding padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度：8的倍数
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public DES(Mode mode, Padding padding, SecretKey key, IvParameterSpec iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 */
	public DES(String mode, String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度：8的倍数
	 */
	public DES(String mode, String padding, byte[] key) {
		this(mode, padding, SecureUtil.generateKey("DES", key), null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度：8的倍数
	 * @param iv 加盐
	 */
	public DES(String mode, String padding, byte[] key, byte[] iv) {
		this(mode, padding, SecureUtil.generateKey("DES", key), null == iv ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度：8的倍数
	 */
	public DES(String mode, String padding, SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 * 
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度：8的倍数
	 * @param iv 加盐
	 */
	public DES(String mode, String padding, SecretKey key, IvParameterSpec iv) {
		super(StrUtil.format("DES/{}/{}", mode, padding), key, iv);
	}
	// ------------------------------------------------------------------------- Constrctor end
}
